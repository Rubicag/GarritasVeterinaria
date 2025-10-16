package com.mycompany.controller;

import com.mycompany.model.Producto;
import com.mycompany.service.ProductoService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    private final ProductoService productoService;

    public InventarioController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping("/resumen")
    public ResponseEntity<?> getResumenInventario() {
        try {
            List<Producto> productos = productoService.findAll();
            int totalProductos = productos.size();
            int productosConBajoStock = productoService.findProductosConBajoStock(5).size();
            int productosDisponibles = productoService.findDisponibles().size();
            
            double valorTotalInventario = productos.stream()
                    .mapToDouble(p -> (p.getPrecio() != null ? p.getPrecio() : 0.0) * 
                                     (p.getStock() != null ? p.getStock() : 0))
                    .sum();

            return ResponseEntity.ok(Map.of(
                "totalProductos", totalProductos,
                "productosConBajoStock", productosConBajoStock,
                "productosDisponibles", productosDisponibles,
                "valorTotalInventario", valorTotalInventario
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al obtener resumen del inventario"));
        }
    }

    @GetMapping("/bajo-stock")
    public ResponseEntity<List<Producto>> getProductosConBajoStock(@RequestParam(defaultValue = "5") Integer minimo) {
        try {
            List<Producto> productos = productoService.findProductosConBajoStock(minimo);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<List<Producto>> getProductosDisponibles() {
        try {
            List<Producto> productos = productoService.findDisponibles();
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarProductos(@RequestParam String nombre) {
        try {
            List<Producto> productos = productoService.findByNombre(nombre);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/por-precio")
    public ResponseEntity<List<Producto>> getProductosPorRangoPrecio(
            @RequestParam Double min, 
            @RequestParam Double max) {
        try {
            List<Producto> productos = productoService.findByPrecioBetween(min, max);
            return ResponseEntity.ok(productos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestBody Map<String, Integer> stockData) {
        try {
            Integer nuevoStock = stockData.get("stock");
            if (nuevoStock == null || nuevoStock < 0) {
                return ResponseEntity.badRequest().body(Map.of("error", "Stock debe ser un número positivo"));
            }

            boolean actualizado = productoService.actualizarStock(id, nuevoStock);
            if (actualizado) {
                Optional<Producto> producto = productoService.getById(id);
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Stock actualizado exitosamente",
                    "producto", producto.orElse(null)
                ));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al actualizar stock"));
        }
    }

    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@RequestBody Map<String, Object> entradaData) {
        try {
            Long productoId = Long.valueOf(entradaData.get("productoId").toString());
            Integer cantidad = Integer.valueOf(entradaData.get("cantidad").toString());

            Optional<Producto> productoOpt = productoService.getById(productoId);
            if (productoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Producto producto = productoOpt.get();
            int stockActual = producto.getStock() != null ? producto.getStock() : 0;
            boolean actualizado = productoService.actualizarStock(productoId, stockActual + cantidad);

            if (actualizado) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Entrada registrada exitosamente",
                    "stockAnterior", stockActual,
                    "stockActual", stockActual + cantidad
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error al registrar entrada"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }

    @PostMapping("/salida")
    public ResponseEntity<?> registrarSalida(@RequestBody Map<String, Object> salidaData) {
        try {
            Long productoId = Long.valueOf(salidaData.get("productoId").toString());
            Integer cantidad = Integer.valueOf(salidaData.get("cantidad").toString());

            Optional<Producto> productoOpt = productoService.getById(productoId);
            if (productoOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Producto producto = productoOpt.get();
            int stockActual = producto.getStock() != null ? producto.getStock() : 0;

            if (stockActual < cantidad) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Stock insuficiente", 
                    "stockDisponible", stockActual,
                    "cantidadSolicitada", cantidad
                ));
            }

            boolean actualizado = productoService.actualizarStock(productoId, stockActual - cantidad);

            if (actualizado) {
                return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Salida registrada exitosamente",
                    "stockAnterior", stockActual,
                    "stockActual", stockActual - cantidad
                ));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "Error al registrar salida"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error interno del servidor"));
        }
    }
}
