package com.mycompany.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un movimiento de inventario (entrada o salida).
 * Registra cambios en el stock de productos para auditoría y control.
 */
@Entity
@Table(name = "movimiento_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "stock_anterior", nullable = false)
    private Integer stockAnterior;

    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(length = 255)
    private String motivo;

    @Column(length = 100)
    private String usuario;

    /**
     * Enum para tipo de movimiento
     */
    public enum TipoMovimiento {
        Entrada,
        Salida
    }

    // Constructores
    public MovimientoInventario() {
        this.fecha = LocalDateTime.now();
    }

    public MovimientoInventario(Producto producto, TipoMovimiento tipo, Integer cantidad, 
                               Integer stockAnterior, Integer stockActual, String motivo, String usuario) {
        this.producto = producto;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.stockAnterior = stockAnterior;
        this.stockActual = stockActual;
        this.fecha = LocalDateTime.now();
        this.motivo = motivo;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoMovimiento tipo) {
        this.tipo = tipo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getStockAnterior() {
        return stockAnterior;
    }

    public void setStockAnterior(Integer stockAnterior) {
        this.stockAnterior = stockAnterior;
    }

    public Integer getStockActual() {
        return stockActual;
    }

    public void setStockActual(Integer stockActual) {
        this.stockActual = stockActual;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "MovimientoInventario{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", cantidad=" + cantidad +
                ", stockAnterior=" + stockAnterior +
                ", stockActual=" + stockActual +
                ", fecha=" + fecha +
                ", motivo='" + motivo + '\'' +
                ", usuario='" + usuario + '\'' +
                '}';
    }
}
