package modelo;

import java.util.Date;

public class NotificacionReabastecimiento {
    private int idNotificacion;
    private int idProducto;
    private String nombreProducto;
    private int cantidadActual;
    private int cantidadSugerida;
    private int stockMinimo;
    private Date fechaNotificacion;
    private int estado; // 1: Pendiente, 0: Procesada
    private int prioridad; // 1: Normal, 2: Urgente

    public NotificacionReabastecimiento() {
        this.fechaNotificacion = new Date();
        this.estado = 1;
        this.prioridad = 1;
    }

    public NotificacionReabastecimiento(int idProducto, String nombreProducto, 
                                       int cantidadActual, int cantidadSugerida, 
                                       int stockMinimo, int prioridad) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadActual = cantidadActual;
        this.cantidadSugerida = cantidadSugerida;
        this.stockMinimo = stockMinimo;
        this.fechaNotificacion = new Date();
        this.estado = 1;
        this.prioridad = prioridad;
    }

    // Getters y Setters
    public int getIdNotificacion() { return idNotificacion; }
    public void setIdNotificacion(int idNotificacion) { this.idNotificacion = idNotificacion; }

    public int getIdProducto() { return idProducto; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }

    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }

    public int getCantidadActual() { return cantidadActual; }
    public void setCantidadActual(int cantidadActual) { this.cantidadActual = cantidadActual; }

    public int getCantidadSugerida() { return cantidadSugerida; }
    public void setCantidadSugerida(int cantidadSugerida) { this.cantidadSugerida = cantidadSugerida; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public Date getFechaNotificacion() { return fechaNotificacion; }
    public void setFechaNotificacion(Date fechaNotificacion) { this.fechaNotificacion = fechaNotificacion; }

    public int getEstado() { return estado; }
    public void setEstado(int estado) { this.estado = estado; }

    public int getPrioridad() { return prioridad; }
    public void setPrioridad(int prioridad) { this.prioridad = prioridad; }

    @Override
    public String toString() {
        return "NotificacionReabastecimiento{" +
                "producto='" + nombreProducto + '\'' +
                ", cantidadActual=" + cantidadActual +
                ", cantidadSugerida=" + cantidadSugerida +
                ", prioridad=" + (prioridad == 2 ? "URGENTE" : "Normal") +
                '}';
    }
}