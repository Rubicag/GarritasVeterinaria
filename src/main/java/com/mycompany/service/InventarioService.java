package com.mycompany.service;

import com.mycompany.model.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventarioService {

    @Autowired
    private ProductoService productoService;

    /**
     * Obtiene un resumen completo del inventario
     */
    public Map<String, Object> getResumenInventario() {
        List<Producto> productos = productoService.findAll();
        
        Map<String, Object> resumen = new HashMap<>();
        
        // Estadísticas generales
        resumen.put("totalProductos", productos.size());
        resumen.put("fechaActualizacion", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        // Valor total del inventario
        double valorTotal = productos.stream()
                .filter(p -> p.getPrecio() != null && p.getStock() != null)
                .mapToDouble(p -> p.getPrecio() * p.getStock())
                .sum();
        resumen.put("valorTotalInventario", valorTotal);
        
        // Productos con stock
        long productosConStock = productos.stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0)
                .count();
        resumen.put("productosConStock", productosConStock);
        
        // Productos sin stock
        long productosSinStock = productos.stream()
                .filter(p -> p.getStock() == null || p.getStock() == 0)
                .count();
        resumen.put("productosSinStock", productosSinStock);
        
        // Productos con bajo stock (menos de 5 unidades)
        List<Producto> bajoStock = productoService.findProductosConBajoStock(5);
        resumen.put("productosConBajoStock", bajoStock.size());
        resumen.put("detallesBajoStock", bajoStock);
        
        // Productos más valiosos (top 5)
        List<Map<String, Object>> topValiosos = productos.stream()
                .filter(p -> p.getPrecio() != null && p.getStock() != null)
                .sorted((p1, p2) -> Double.compare(
                        p2.getPrecio() * p2.getStock(),
                        p1.getPrecio() * p1.getStock()
                ))
                .limit(5)
                .map(p -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", p.getId());
                    item.put("nombre", p.getNombre());
                    item.put("stock", p.getStock());
                    item.put("precio", p.getPrecio());
                    item.put("valorTotal", p.getPrecio() * p.getStock());
                    return item;
                })
                .collect(Collectors.toList());
        resumen.put("top5MasValiosos", topValiosos);
        
        return resumen;
    }

    /**
     * Registra entrada de stock para un producto
     */
    public Map<String, Object> registrarEntrada(Long productoId, Integer cantidad, String motivo, String responsable) {
        try {
            Optional<Producto> productoOpt = productoService.getById(productoId);
            if (productoOpt.isEmpty()) {
                return Map.of(
                    "success", false,
                    "message", "Producto no encontrado"
                );
            }

            Producto producto = productoOpt.get();
            int stockAnterior = producto.getStock() != null ? producto.getStock() : 0;
            int nuevoStock = stockAnterior + cantidad;
            
            producto.setStock(nuevoStock);
            productoService.update(producto.getId(), producto);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("success", true);
            resultado.put("message", "Entrada registrada correctamente");
            resultado.put("producto", producto.getNombre());
            resultado.put("stockAnterior", stockAnterior);
            resultado.put("cantidadEntrada", cantidad);
            resultado.put("stockActual", nuevoStock);
            resultado.put("motivo", motivo);
            resultado.put("responsable", responsable);
            resultado.put("fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            return resultado;

        } catch (Exception e) {
            return Map.of(
                "success", false,
                "message", "Error al registrar entrada: " + e.getMessage()
            );
        }
    }

    /**
     * Registra salida de stock para un producto
     */
    public Map<String, Object> registrarSalida(Long productoId, Integer cantidad, String motivo, String responsable) {
        try {
            Optional<Producto> productoOpt = productoService.getById(productoId);
            if (productoOpt.isEmpty()) {
                return Map.of(
                    "success", false,
                    "message", "Producto no encontrado"
                );
            }

            Producto producto = productoOpt.get();
            int stockAnterior = producto.getStock() != null ? producto.getStock() : 0;
            
            if (stockAnterior < cantidad) {
                return Map.of(
                    "success", false,
                    "message", "Stock insuficiente. Stock actual: " + stockAnterior
                );
            }
            
            int nuevoStock = stockAnterior - cantidad;
            producto.setStock(nuevoStock);
            productoService.update(producto.getId(), producto);

            Map<String, Object> resultado = new HashMap<>();
            resultado.put("success", true);
            resultado.put("message", "Salida registrada correctamente");
            resultado.put("producto", producto.getNombre());
            resultado.put("stockAnterior", stockAnterior);
            resultado.put("cantidadSalida", cantidad);
            resultado.put("stockActual", nuevoStock);
            resultado.put("motivo", motivo);
            resultado.put("responsable", responsable);
            resultado.put("fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            // Alertar si queda poco stock
            if (nuevoStock <= 5) {
                resultado.put("alerta", "¡ATENCIÓN! Stock bajo: " + nuevoStock + " unidades restantes");
            }

            return resultado;

        } catch (Exception e) {
            return Map.of(
                "success", false,
                "message", "Error al registrar salida: " + e.getMessage()
            );
        }
    }

    /**
     * Obtiene alertas de inventario
     */
    public Map<String, Object> getAlertas() {
        List<Producto> productos = productoService.findAll();
        Map<String, Object> alertas = new HashMap<>();
        
        // Productos sin stock
        List<Producto> sinStock = productos.stream()
                .filter(p -> p.getStock() == null || p.getStock() == 0)
                .collect(Collectors.toList());
        
        // Productos con stock crítico (1-3 unidades)
        List<Producto> stockCritico = productos.stream()
                .filter(p -> p.getStock() != null && p.getStock() > 0 && p.getStock() <= 3)
                .collect(Collectors.toList());
        
        // Productos con stock bajo (4-10 unidades)
        List<Producto> stockBajo = productos.stream()
                .filter(p -> p.getStock() != null && p.getStock() > 3 && p.getStock() <= 10)
                .collect(Collectors.toList());

        alertas.put("sinStock", Map.of(
            "cantidad", sinStock.size(),
            "productos", sinStock,
            "prioridad", "ALTA"
        ));
        
        alertas.put("stockCritico", Map.of(
            "cantidad", stockCritico.size(),
            "productos", stockCritico,
            "prioridad", "ALTA"
        ));
        
        alertas.put("stockBajo", Map.of(
            "cantidad", stockBajo.size(),
            "productos", stockBajo,
            "prioridad", "MEDIA"
        ));
        
        alertas.put("fechaGeneracion", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        alertas.put("totalAlertas", sinStock.size() + stockCritico.size() + stockBajo.size());
        
        return alertas;
    }

    /**
     * Busca productos en el inventario
     */
    public List<Producto> buscarProductos(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return productoService.findAll();
        }
        
        return productoService.findByNombre(termino.trim());
    }

    /**
     * Ajusta el stock de un producto directamente
     */
    public Map<String, Object> ajustarStock(Long productoId, Integer nuevoStock, String motivo, String responsable) {
        try {
            Optional<Producto> productoOpt = productoService.getById(productoId);
            if (productoOpt.isEmpty()) {
                return Map.of(
                    "success", false,
                    "message", "Producto no encontrado"
                );
            }

            Producto producto = productoOpt.get();
            int stockAnterior = producto.getStock() != null ? producto.getStock() : 0;
            
            producto.setStock(nuevoStock);
            productoService.update(producto.getId(), producto);

            return Map.of(
                "success", true,
                "message", "Stock ajustado correctamente",
                "producto", producto.getNombre(),
                "stockAnterior", stockAnterior,
                "stockActual", nuevoStock,
                "diferencia", nuevoStock - stockAnterior,
                "motivo", motivo,
                "responsable", responsable,
                "fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            );

        } catch (Exception e) {
            return Map.of(
                "success", false,
                "message", "Error al ajustar stock: " + e.getMessage()
            );
        }
    }

    /**
     * Obtiene estadísticas de movimientos de inventario
     */
    public Map<String, Object> getEstadisticas() {
        List<Producto> productos = productoService.findAll();
        
        Map<String, Object> estadisticas = new HashMap<>();
        
        // Distribución por rangos de stock
        Map<String, Long> distribucionStock = new HashMap<>();
        distribucionStock.put("Sin stock (0)", productos.stream().filter(p -> p.getStock() == null || p.getStock() == 0).count());
        distribucionStock.put("Crítico (1-3)", productos.stream().filter(p -> p.getStock() != null && p.getStock() >= 1 && p.getStock() <= 3).count());
        distribucionStock.put("Bajo (4-10)", productos.stream().filter(p -> p.getStock() != null && p.getStock() >= 4 && p.getStock() <= 10).count());
        distribucionStock.put("Normal (11-50)", productos.stream().filter(p -> p.getStock() != null && p.getStock() >= 11 && p.getStock() <= 50).count());
        distribucionStock.put("Alto (50+)", productos.stream().filter(p -> p.getStock() != null && p.getStock() > 50).count());
        
        estadisticas.put("distribucionStock", distribucionStock);
        
        // Valor promedio por producto
        double valorPromedio = productos.stream()
                .filter(p -> p.getPrecio() != null)
                .mapToDouble(Producto::getPrecio)
                .average()
                .orElse(0.0);
        estadisticas.put("valorPromedioPorProducto", valorPromedio);
        
        // Stock promedio
        double stockPromedio = productos.stream()
                .filter(p -> p.getStock() != null)
                .mapToInt(Producto::getStock)
                .average()
                .orElse(0.0);
        estadisticas.put("stockPromedio", stockPromedio);
        
        estadisticas.put("fechaGeneracion", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        
        return estadisticas;
    }
}
