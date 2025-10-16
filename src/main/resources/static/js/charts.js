/**
 * GARRITAS VETERINARIA - CHARTS UTILITIES
 * Utilidades para gráficos y visualizaciones
 */

class ChartManager {
    constructor() {
        this.charts = new Map();
        this.defaultColors = {
            primary: '#28a745',
            secondary: '#6c757d',
            success: '#28a745',
            danger: '#dc3545',
            warning: '#ffc107',
            info: '#17a2b8',
            light: '#f8f9fa',
            dark: '#343a40'
        };
        
        this.gradients = {
            green: ['rgba(40, 167, 69, 0.8)', 'rgba(32, 201, 151, 0.8)'],
            blue: ['rgba(0, 123, 255, 0.8)', 'rgba(111, 66, 193, 0.8)'],
            orange: ['rgba(255, 193, 7, 0.8)', 'rgba(253, 126, 20, 0.8)'],
            red: ['rgba(220, 53, 69, 0.8)', 'rgba(232, 62, 140, 0.8)']
        };
    }

    // Crear gradiente lineal
    createGradient(ctx, colorArray, direction = 'vertical') {
        const gradient = direction === 'vertical' 
            ? ctx.createLinearGradient(0, 0, 0, 400)
            : ctx.createLinearGradient(0, 0, 400, 0);
        
        colorArray.forEach((color, index) => {
            gradient.addColorStop(index / (colorArray.length - 1), color);
        });
        
        return gradient;
    }

    // Configuración común para todos los gráficos
    getDefaultOptions() {
        return {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: true,
                    position: 'bottom',
                    labels: {
                        padding: 20,
                        usePointStyle: true,
                        font: {
                            family: 'Inter',
                            size: 12
                        }
                    }
                },
                tooltip: {
                    backgroundColor: 'rgba(0,0,0,0.8)',
                    titleColor: 'white',
                    bodyColor: 'white',
                    borderColor: 'rgba(255,255,255,0.1)',
                    borderWidth: 1,
                    cornerRadius: 8,
                    displayColors: true,
                    titleFont: {
                        family: 'Inter',
                        size: 14,
                        weight: '600'
                    },
                    bodyFont: {
                        family: 'Inter',
                        size: 12
                    }
                }
            },
            animation: {
                duration: 800,
                easing: 'easeInOutQuart'
            }
        };
    }

    // Crear gráfico de líneas para ingresos/métricas temporales
    createTimeSeriesChart(canvasId, data, options = {}) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return null;

        const ctx = canvas.getContext('2d');
        const gradient = this.createGradient(ctx, ['rgba(40, 167, 69, 0.2)', 'rgba(40, 167, 69, 0.05)']);

        const defaultConfig = {
            type: 'line',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: data.label || 'Datos',
                    data: data.values || [],
                    borderColor: this.defaultColors.primary,
                    backgroundColor: gradient,
                    borderWidth: 3,
                    fill: true,
                    tension: 0.4,
                    pointBackgroundColor: this.defaultColors.primary,
                    pointBorderColor: '#ffffff',
                    pointBorderWidth: 2,
                    pointRadius: 6,
                    pointHoverRadius: 8
                }]
            },
            options: {
                ...this.getDefaultOptions(),
                scales: {
                    x: {
                        grid: {
                            display: false
                        },
                        ticks: {
                            font: {
                                family: 'Inter',
                                size: 11
                            }
                        }
                    },
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: 'rgba(0,0,0,0.1)',
                            drawBorder: false
                        },
                        ticks: {
                            font: {
                                family: 'Inter',
                                size: 11
                            },
                            callback: function(value) {
                                if (options.currency) {
                                    return 'S/. ' + value.toLocaleString();
                                }
                                return value.toLocaleString();
                            }
                        }
                    }
                },
                ...options
            }
        };

        const chart = new Chart(ctx, defaultConfig);
        this.charts.set(canvasId, chart);
        return chart;
    }

    // Crear gráfico de barras
    createBarChart(canvasId, data, options = {}) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return null;

        const ctx = canvas.getContext('2d');
        
        const colors = data.colors || [
            'rgba(40, 167, 69, 0.8)',
            'rgba(0, 123, 255, 0.8)',
            'rgba(255, 193, 7, 0.8)',
            'rgba(220, 53, 69, 0.8)',
            'rgba(111, 66, 193, 0.8)'
        ];

        const defaultConfig = {
            type: 'bar',
            data: {
                labels: data.labels || [],
                datasets: [{
                    label: data.label || 'Datos',
                    data: data.values || [],
                    backgroundColor: colors,
                    borderRadius: 8,
                    borderSkipped: false,
                    borderWidth: 0
                }]
            },
            options: {
                ...this.getDefaultOptions(),
                plugins: {
                    ...this.getDefaultOptions().plugins,
                    legend: {
                        display: false
                    }
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        },
                        ticks: {
                            font: {
                                family: 'Inter',
                                size: 11
                            }
                        }
                    },
                    y: {
                        beginAtZero: true,
                        grid: {
                            color: 'rgba(0,0,0,0.1)',
                            drawBorder: false
                        },
                        ticks: {
                            font: {
                                family: 'Inter',
                                size: 11
                            }
                        }
                    }
                },
                ...options
            }
        };

        const chart = new Chart(ctx, defaultConfig);
        this.charts.set(canvasId, chart);
        return chart;
    }

    // Crear gráfico de dona/pie
    createDoughnutChart(canvasId, data, options = {}) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return null;

        const ctx = canvas.getContext('2d');
        
        const colors = data.colors || [
            '#28a745', '#007bff', '#ffc107', '#dc3545', '#6c757d'
        ];

        const defaultConfig = {
            type: options.type || 'doughnut',
            data: {
                labels: data.labels || [],
                datasets: [{
                    data: data.values || [],
                    backgroundColor: colors,
                    borderWidth: 0,
                    cutout: options.type === 'pie' ? '0%' : '60%',
                    hoverOffset: 10
                }]
            },
            options: {
                ...this.getDefaultOptions(),
                plugins: {
                    ...this.getDefaultOptions().plugins,
                    legend: {
                        ...this.getDefaultOptions().plugins.legend,
                        position: options.legendPosition || 'bottom'
                    }
                },
                ...options
            }
        };

        const chart = new Chart(ctx, defaultConfig);
        this.charts.set(canvasId, chart);
        return chart;
    }

    // Actualizar datos de un gráfico existente
    updateChart(canvasId, newData, animate = true) {
        const chart = this.charts.get(canvasId);
        if (!chart) return false;

        if (newData.labels) {
            chart.data.labels = newData.labels;
        }

        if (newData.datasets) {
            chart.data.datasets = newData.datasets;
        } else if (newData.values) {
            chart.data.datasets[0].data = newData.values;
        }

        chart.update(animate ? 'active' : 'none');
        return true;
    }

    // Destruir un gráfico
    destroyChart(canvasId) {
        const chart = this.charts.get(canvasId);
        if (chart) {
            chart.destroy();
            this.charts.delete(canvasId);
            return true;
        }
        return false;
    }

    // Exportar gráfico como imagen
    exportChart(canvasId, filename = 'chart') {
        const chart = this.charts.get(canvasId);
        if (!chart) return false;

        const canvas = chart.canvas;
        const url = canvas.toDataURL('image/png');
        
        const link = document.createElement('a');
        link.download = `${filename}.png`;
        link.href = url;
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        
        return true;
    }

    // Redimensionar todos los gráficos
    resizeAllCharts() {
        this.charts.forEach(chart => {
            chart.resize();
        });
    }

    // Obtener estadísticas de un conjunto de datos
    getDataStats(data) {
        if (!Array.isArray(data) || data.length === 0) {
            return { min: 0, max: 0, avg: 0, sum: 0, count: 0 };
        }

        const numbers = data.filter(val => typeof val === 'number' && !isNaN(val));
        
        return {
            min: Math.min(...numbers),
            max: Math.max(...numbers),
            avg: numbers.reduce((a, b) => a + b, 0) / numbers.length,
            sum: numbers.reduce((a, b) => a + b, 0),
            count: numbers.length
        };
    }

    // Generar colores automáticamente
    generateColors(count, baseColor = '#28a745') {
        const colors = [];
        const baseHue = this.hexToHsl(baseColor).h;
        
        for (let i = 0; i < count; i++) {
            const hue = (baseHue + (i * 360 / count)) % 360;
            const color = this.hslToHex(hue, 70, 50);
            colors.push(color);
        }
        
        return colors;
    }

    // Utilidades de conversión de color
    hexToHsl(hex) {
        const r = parseInt(hex.slice(1, 3), 16) / 255;
        const g = parseInt(hex.slice(3, 5), 16) / 255;
        const b = parseInt(hex.slice(5, 7), 16) / 255;

        const max = Math.max(r, g, b);
        const min = Math.min(r, g, b);
        let h, s, l = (max + min) / 2;

        if (max === min) {
            h = s = 0;
        } else {
            const d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
            switch (max) {
                case r: h = (g - b) / d + (g < b ? 6 : 0); break;
                case g: h = (b - r) / d + 2; break;
                case b: h = (r - g) / d + 4; break;
            }
            h /= 6;
        }

        return { h: h * 360, s: s * 100, l: l * 100 };
    }

    hslToHex(h, s, l) {
        l /= 100;
        const a = s * Math.min(l, 1 - l) / 100;
        const f = n => {
            const k = (n + h / 30) % 12;
            const color = l - a * Math.max(Math.min(k - 3, 9 - k, 1), -1);
            return Math.round(255 * color).toString(16).padStart(2, '0');
        };
        return `#${f(0)}${f(8)}${f(4)}`;
    }

    // Crear gráfico combinado (línea + barras)
    createCombinedChart(canvasId, data, options = {}) {
        const canvas = document.getElementById(canvasId);
        if (!canvas) return null;

        const ctx = canvas.getContext('2d');

        const defaultConfig = {
            type: 'line',
            data: {
                labels: data.labels || [],
                datasets: [
                    {
                        type: 'bar',
                        label: data.barLabel || 'Barras',
                        data: data.barData || [],
                        backgroundColor: 'rgba(0, 123, 255, 0.6)',
                        borderRadius: 6,
                        yAxisID: 'y'
                    },
                    {
                        type: 'line',
                        label: data.lineLabel || 'Línea',
                        data: data.lineData || [],
                        borderColor: this.defaultColors.primary,
                        backgroundColor: 'rgba(40, 167, 69, 0.1)',
                        borderWidth: 3,
                        fill: false,
                        tension: 0.4,
                        yAxisID: 'y1'
                    }
                ]
            },
            options: {
                ...this.getDefaultOptions(),
                interaction: {
                    mode: 'index',
                    intersect: false,
                },
                scales: {
                    x: {
                        grid: {
                            display: false
                        }
                    },
                    y: {
                        type: 'linear',
                        display: true,
                        position: 'left',
                        grid: {
                            color: 'rgba(0,0,0,0.1)'
                        }
                    },
                    y1: {
                        type: 'linear',
                        display: true,
                        position: 'right',
                        grid: {
                            drawOnChartArea: false,
                        },
                    }
                },
                ...options
            }
        };

        const chart = new Chart(ctx, defaultConfig);
        this.charts.set(canvasId, chart);
        return chart;
    }
}

// Instancia global del gestor de gráficos
window.chartManager = new ChartManager();

// Auto-redimensionar gráficos cuando cambie el tamaño de la ventana
window.addEventListener('resize', () => {
    window.chartManager.resizeAllCharts();
});

// Configuración global de Chart.js
Chart.defaults.font.family = 'Inter';
Chart.defaults.font.size = 12;
Chart.defaults.color = '#64748b';

// Utilidades adicionales para gráficos específicos de la veterinaria
window.VetChartUtils = {
    // Colores específicos para tipos de mascotas
    speciesColors: {
        'Perros': '#28a745',
        'Gatos': '#007bff', 
        'Aves': '#ffc107',
        'Conejos': '#dc3545',
        'Otros': '#6c757d'
    },

    // Colores para estados de citas
    appointmentStatusColors: {
        'Pendiente': '#ffc107',
        'Confirmada': '#28a745',
        'Atendida': '#17a2b8',
        'Cancelada': '#dc3545'
    },

    // Generar datos de ejemplo para desarrollo
    generateSampleData: {
        appointments: (days = 7) => {
            const data = { labels: [], values: [] };
            for (let i = days - 1; i >= 0; i--) {
                const date = new Date();
                date.setDate(date.getDate() - i);
                data.labels.push(date.toLocaleDateString('es-ES', { 
                    weekday: 'short', 
                    day: 'numeric' 
                }));
                data.values.push(Math.floor(Math.random() * 10) + 1);
            }
            return data;
        },

        income: (months = 6) => {
            const data = { labels: [], values: [] };
            for (let i = months - 1; i >= 0; i--) {
                const date = new Date();
                date.setMonth(date.getMonth() - i);
                data.labels.push(date.toLocaleDateString('es-ES', { 
                    month: 'short',
                    year: '2-digit'
                }));
                data.values.push(Math.floor(Math.random() * 15000) + 5000);
            }
            return data;
        },

        species: () => ({
            labels: ['Perros', 'Gatos', 'Aves', 'Conejos', 'Otros'],
            values: [45, 35, 12, 5, 3],
            colors: Object.values(window.VetChartUtils.speciesColors)
        }),

        services: () => ({
            labels: ['Consulta General', 'Vacunación', 'Cirugía', 'Emergencia', 'Otros'],
            values: [35, 25, 15, 15, 10],
            colors: ['#28a745', '#007bff', '#ffc107', '#dc3545', '#6c757d']
        })
    },

    // Formatear valores monetarios
    formatCurrency: (value) => {
        return 'S/. ' + value.toLocaleString('es-PE', {
            minimumFractionDigits: 2,
            maximumFractionDigits: 2
        });
    },

    // Formatear fechas para gráficos
    formatDate: (date, format = 'short') => {
        const options = {
            short: { month: 'short', day: 'numeric' },
            medium: { month: 'short', day: 'numeric', year: '2-digit' },
            long: { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' }
        };
        
        return new Date(date).toLocaleDateString('es-ES', options[format]);
    }
};

console.log('📊 Chart Manager inicializado correctamente');