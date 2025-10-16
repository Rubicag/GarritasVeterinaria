/**
 * GARRITAS VETERINARIA - MAIN JAVASCRIPT
 * Sistema de gestión veterinaria
 */

class VeterinariaApp {
    constructor() {
        this.API_BASE = '/api';
        this.currentTheme = localStorage.getItem('theme') || 'light';
        this.notifications = [];
        this.init();
    }

    // Inicialización
    init() {
        this.setupTheme();
        this.setupEventListeners();
        this.setupNotificationSystem();
        this.initializeAnimations();
        console.log('🐾 Garritas Veterinaria App initialized');
    }

    // ========================
    // SISTEMA DE TEMAS
    // ========================
    setupTheme() {
        document.documentElement.setAttribute('data-theme', this.currentTheme);
        this.updateThemeToggle();
    }

    toggleTheme() {
        this.currentTheme = this.currentTheme === 'light' ? 'dark' : 'light';
        document.documentElement.setAttribute('data-theme', this.currentTheme);
        localStorage.setItem('theme', this.currentTheme);
        this.updateThemeToggle();
        this.showNotification(`Tema ${this.currentTheme === 'light' ? 'claro' : 'oscuro'} activado`, 'info');
    }

    updateThemeToggle() {
        const toggles = document.querySelectorAll('.theme-toggle');
        toggles.forEach(toggle => {
            const icon = toggle.querySelector('i');
            if (icon) {
                icon.className = this.currentTheme === 'light' ? 'bi bi-moon-fill' : 'bi bi-sun-fill';
            }
        });
    }

    // ========================
    // SISTEMA DE NOTIFICACIONES
    // ========================
    setupNotificationSystem() {
        // Crear contenedor de notificaciones si no existe
        if (!document.getElementById('notification-container')) {
            const container = document.createElement('div');
            container.id = 'notification-container';
            container.style.cssText = `
                position: fixed;
                top: 20px;
                right: 20px;
                z-index: 2000;
                max-width: 400px;
            `;
            document.body.appendChild(container);
        }
    }

    showNotification(message, type = 'info', duration = 4000) {
        const notification = document.createElement('div');
        const id = 'notification-' + Date.now();
        
        const icons = {
            success: 'bi-check-circle-fill',
            error: 'bi-x-circle-fill',
            warning: 'bi-exclamation-triangle-fill',
            info: 'bi-info-circle-fill'
        };

        const colors = {
            success: 'linear-gradient(135deg, #28a745, #20c997)',
            error: 'linear-gradient(135deg, #dc3545, #e83e8c)',
            warning: 'linear-gradient(135deg, #ffc107, #fd7e14)',
            info: 'linear-gradient(135deg, #17a2b8, #007bff)'
        };

        notification.id = id;
        notification.className = 'notification-toast';
        notification.style.background = colors[type];
        notification.innerHTML = `
            <div class="d-flex align-items-center">
                <i class="bi ${icons[type]} me-2"></i>
                <div class="flex-grow-1">${message}</div>
                <button class="btn btn-sm ms-2" onclick="veterinariaApp.closeNotification('${id}')" 
                        style="background: rgba(255,255,255,0.2); border: none; color: white;">
                    <i class="bi bi-x"></i>
                </button>
            </div>
        `;

        document.getElementById('notification-container').appendChild(notification);

        // Mostrar con animación
        setTimeout(() => notification.classList.add('show'), 100);

        // Auto-cerrar
        if (duration > 0) {
            setTimeout(() => this.closeNotification(id), duration);
        }

        this.notifications.push(id);
    }

    closeNotification(id) {
        const notification = document.getElementById(id);
        if (notification) {
            notification.classList.remove('show');
            setTimeout(() => {
                notification.remove();
                this.notifications = this.notifications.filter(n => n !== id);
            }, 300);
        }
    }

    // ========================
    // API UTILITIES
    // ========================
    async apiRequest(endpoint, method = 'GET', data = null) {
        const config = {
            method,
            headers: {
                'Content-Type': 'application/json',
            }
        };

        if (data && method !== 'GET') {
            config.body = JSON.stringify(data);
        }

        try {
            const response = await fetch(`${this.API_BASE}${endpoint}`, config);
            
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            
            return await response.json();
        } catch (error) {
            console.error('API Request failed:', error);
            this.showNotification(`Error de conexión: ${error.message}`, 'error');
            throw error;
        }
    }

    // ========================
    // DASHBOARD FUNCTIONS
    // ========================
    async loadDashboardData() {
        this.showLoading('dashboard-stats');
        
        try {
            const data = await this.apiRequest('/reportes/dashboard');
            this.updateDashboardStats(data);
            this.showNotification('Dashboard actualizado', 'success', 2000);
        } catch (error) {
            this.showNotification('Error al cargar datos del dashboard', 'error');
        } finally {
            this.hideLoading('dashboard-stats');
        }
    }

    updateDashboardStats(data) {
        // Actualizar contadores con animación
        this.animateCounter('totalUsuarios', data.totalUsuarios || 0);
        this.animateCounter('totalMascotas', data.totalMascotas || 0);
        this.animateCounter('totalProductos', data.totalProductos || 0);
        this.animateCounter('totalServicios', data.totalServicios || 0);
        this.animateCounter('citasFuturas', data.citasFuturas || 0);
        this.animateCounter('productosConBajoStock', data.productosConBajoStock || 0);
        
        // Formatear valor del inventario
        const valorElement = document.getElementById('valorTotalInventario');
        if (valorElement && data.valorTotalInventario) {
            this.animateCounter('valorTotalInventario', data.valorTotalInventario, 'S/. ');
        }
    }

    animateCounter(elementId, targetValue, prefix = '') {
        const element = document.getElementById(elementId);
        if (!element) return;

        const startValue = parseInt(element.textContent.replace(/[^\d]/g, '')) || 0;
        const increment = (targetValue - startValue) / 50;
        let currentValue = startValue;

        const timer = setInterval(() => {
            currentValue += increment;
            if ((increment > 0 && currentValue >= targetValue) || 
                (increment < 0 && currentValue <= targetValue)) {
                element.textContent = prefix + targetValue.toLocaleString();
                clearInterval(timer);
            } else {
                element.textContent = prefix + Math.floor(currentValue).toLocaleString();
            }
        }, 20);
    }

    // ========================
    // CITAS MANAGEMENT
    // ========================
    async loadCitas(filters = {}) {
        this.showLoading('citas-table');
        
        try {
            let endpoint = '/citas';
            const params = new URLSearchParams();
            
            if (filters.estado) params.append('estado', filters.estado);
            if (filters.fecha) params.append('fecha', filters.fecha);
            if (filters.veterinario) params.append('veterinario', filters.veterinario);
            
            if (params.toString()) {
                endpoint += '?' + params.toString();
            }
            
            const citas = await this.apiRequest(endpoint);
            this.renderCitasTable(citas);
            this.updateCitasStats(citas);
            
        } catch (error) {
            this.showNotification('Error al cargar citas', 'error');
        } finally {
            this.hideLoading('citas-table');
        }
    }

    renderCitasTable(citas) {
        const tbody = document.querySelector('#citasTable tbody');
        if (!tbody) return;

        if (citas.length === 0) {
            tbody.innerHTML = `
                <tr>
                    <td colspan="8" class="text-center py-5">
                        <i class="bi bi-calendar-x display-1 text-muted mb-3"></i>
                        <h5 class="text-muted">No hay citas registradas</h5>
                        <p class="text-muted mb-3">Comienza creando tu primera cita médica</p>
                        <button class="btn btn-success" onclick="veterinariaApp.openNewCitaModal()">
                            <i class="bi bi-plus-circle me-1"></i>Crear Primera Cita
                        </button>
                    </td>
                </tr>
            `;
            return;
        }

        tbody.innerHTML = citas.map(cita => `
            <tr class="fade-in-up">
                <td class="ps-4 fw-bold">#${cita.id}</td>
                <td>
                    <div class="d-flex align-items-center">
                        <i class="bi bi-heart-fill text-danger me-2"></i>
                        <div>
                            <div class="fw-semibold">${cita.mascota?.nombre || 'No disponible'}</div>
                            <small class="text-muted">${cita.mascota?.especie || ''}</small>
                        </div>
                    </div>
                </td>
                <td>
                    <div>
                        <div class="fw-semibold">${cita.mascota?.propietario?.nombre || 'No disponible'}</div>
                        <small class="text-muted">${cita.mascota?.propietario?.telefono || ''}</small>
                    </div>
                </td>
                <td>
                    <div>
                        <div class="fw-semibold">${cita.servicio?.nombre || 'No disponible'}</div>
                        <small class="text-success fw-bold">S/. ${cita.servicio?.precio || '0.00'}</small>
                    </div>
                </td>
                <td>
                    <div>
                        <div class="fw-semibold">${this.formatDate(cita.fecha)}</div>
                        <small class="text-muted">${this.formatTime(cita.fecha)}</small>
                    </div>
                </td>
                <td>
                    <span class="status-badge ${this.getStatusClass(cita.estado)}">
                        ${cita.estado || 'Pendiente'}
                    </span>
                </td>
                <td>
                    <div>
                        <div class="fw-semibold">${cita.veterinario?.nombre || 'No asignado'}</div>
                        <small class="text-muted">Veterinario</small>
                    </div>
                </td>
                <td class="pe-4">
                    <div class="btn-group btn-group-sm">
                        <button class="btn btn-outline-success" onclick="veterinariaApp.confirmarCita(${cita.id})" 
                                title="Confirmar cita">
                            <i class="bi bi-check2"></i>
                        </button>
                        <button class="btn btn-outline-primary" onclick="veterinariaApp.editarCita(${cita.id})" 
                                title="Editar cita">
                            <i class="bi bi-pencil"></i>
                        </button>
                        <button class="btn btn-outline-danger" onclick="veterinariaApp.cancelarCita(${cita.id})" 
                                title="Cancelar cita">
                            <i class="bi bi-x-lg"></i>
                        </button>
                    </div>
                </td>
            </tr>
        `).join('');
    }

    async confirmarCita(citaId) {
        if (!confirm('¿Está seguro de confirmar esta cita?')) return;

        try {
            await this.apiRequest(`/citas/${citaId}/confirmar`, 'PUT');
            this.showNotification('Cita confirmada exitosamente', 'success');
            this.loadCitas();
        } catch (error) {
            this.showNotification('Error al confirmar la cita', 'error');
        }
    }

    async cancelarCita(citaId) {
        if (!confirm('¿Está seguro de cancelar esta cita?')) return;

        try {
            await this.apiRequest(`/citas/${citaId}/cancelar`, 'PUT');
            this.showNotification('Cita cancelada', 'warning');
            this.loadCitas();
        } catch (error) {
            this.showNotification('Error al cancelar la cita', 'error');
        }
    }

    // ========================
    // UTILITIES
    // ========================
    formatDate(dateString) {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleDateString('es-ES', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    }

    formatTime(dateString) {
        if (!dateString) return 'N/A';
        const date = new Date(dateString);
        return date.toLocaleTimeString('es-ES', {
            hour: '2-digit',
            minute: '2-digit'
        });
    }

    getStatusClass(estado) {
        const statusMap = {
            'Pendiente': 'status-pendiente',
            'Confirmada': 'status-confirmada',
            'Atendida': 'status-atendida',
            'Cancelada': 'status-cancelada'
        };
        return statusMap[estado] || 'status-pendiente';
    }

    showLoading(containerId) {
        const container = document.getElementById(containerId);
        if (container) {
            const overlay = document.createElement('div');
            overlay.id = `loading-${containerId}`;
            overlay.className = 'loading-overlay';
            overlay.innerHTML = `
                <div class="d-flex justify-content-center align-items-center h-100">
                    <div class="loading-spinner me-2"></div>
                    <span>Cargando...</span>
                </div>
            `;
            overlay.style.cssText = `
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: rgba(255, 255, 255, 0.9);
                backdrop-filter: blur(2px);
                z-index: 100;
                display: flex;
                align-items: center;
                justify-content: center;
            `;
            
            if (container.style.position !== 'relative') {
                container.style.position = 'relative';
            }
            container.appendChild(overlay);
        }
    }

    hideLoading(containerId) {
        const overlay = document.getElementById(`loading-${containerId}`);
        if (overlay) {
            overlay.remove();
        }
    }

    // ========================
    // EVENT LISTENERS
    // ========================
    setupEventListeners() {
        // Theme toggle
        document.addEventListener('click', (e) => {
            if (e.target.closest('.theme-toggle')) {
                this.toggleTheme();
            }
        });

        // Search functionality
        const searchInputs = document.querySelectorAll('[data-search]');
        searchInputs.forEach(input => {
            input.addEventListener('input', this.debounce((e) => {
                this.handleSearch(e.target.value, e.target.dataset.search);
            }, 300));
        });

        // Filter functionality
        const filterSelects = document.querySelectorAll('[data-filter]');
        filterSelects.forEach(select => {
            select.addEventListener('change', (e) => {
                this.handleFilter(e.target.dataset.filter, e.target.value);
            });
        });

        // Auto-refresh
        document.addEventListener('visibilitychange', () => {
            if (!document.hidden && window.location.pathname === '/dashboard') {
                this.loadDashboardData();
            }
        });
    }

    debounce(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    }

    // ========================
    // ANIMATIONS
    // ========================
    initializeAnimations() {
        // Intersection Observer para animaciones
        const observerOptions = {
            threshold: 0.1,
            rootMargin: '0px 0px -50px 0px'
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach(entry => {
                if (entry.isIntersecting) {
                    entry.target.classList.add('fade-in-up');
                    observer.unobserve(entry.target);
                }
            });
        }, observerOptions);

        // Observar elementos animables
        document.querySelectorAll('.card, .stat-card, .table').forEach(el => {
            observer.observe(el);
        });
    }

    // ========================
    // EXPORT FUNCTIONS
    // ========================
    async exportData(type, format = 'csv') {
        try {
            const response = await fetch(`${this.API_BASE}/reportes/export/${format}?tipo=${type}`);
            
            if (!response.ok) {
                throw new Error('Error en la exportación');
            }

            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `${type}_${new Date().toISOString().split('T')[0]}.${format}`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);

            this.showNotification(`Datos de ${type} exportados exitosamente`, 'success');
        } catch (error) {
            this.showNotification('Error al exportar datos', 'error');
        }
    }
}

// Instancia global
const veterinariaApp = new VeterinariaApp();

// Funciones globales para compatibilidad
window.confirmarCita = (id) => veterinariaApp.confirmarCita(id);
window.cancelarCita = (id) => veterinariaApp.cancelarCita(id);
window.editarCita = (id) => veterinariaApp.editarCita(id);
window.exportarCitas = () => veterinariaApp.exportData('citas');
window.cargarCitasAPI = () => veterinariaApp.loadCitas();

// Auto-inicialización cuando el DOM esté listo
document.addEventListener('DOMContentLoaded', () => {
    // Cargar datos iniciales según la página
    const currentPage = window.location.pathname;
    
    if (currentPage === '/dashboard') {
        veterinariaApp.loadDashboardData();
    } else if (currentPage === '/citas') {
        veterinariaApp.loadCitas();
    }
});

// Service Worker para funcionalidad offline (opcional)
if ('serviceWorker' in navigator) {
    window.addEventListener('load', () => {
        navigator.serviceWorker.register('/sw.js')
            .then((registration) => {
                console.log('SW registered: ', registration);
            })
            .catch((registrationError) => {
                console.log('SW registration failed: ', registrationError);
            });
    });
}