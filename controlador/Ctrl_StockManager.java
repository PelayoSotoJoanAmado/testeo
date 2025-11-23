package controlador;

import conexion.Conexion;
import modelo.NotificacionReabastecimiento;
import modelo.Producto;
import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class Ctrl_StockManager {
    
    // Cola para manejar notificaciones de reabastecimiento (FIFO)
    private Queue<NotificacionReabastecimiento> colaReabastecimiento;
    
    public Ctrl_StockManager() {
        this.colaReabastecimiento = new LinkedList<>();
        cargarNotificacionesPendientes();
    }
    
    /**
     * Verifica si se puede agregar stock sin exceder el límite
     */
    public boolean verificarLimiteStock(int idProducto, int cantidadAAgregar) {
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT cantidad, stock_maximo FROM tb_producto WHERE idProducto = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idProducto);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                int cantidadActual = rs.getInt("cantidad");
                int stockMaximo = rs.getInt("stock_maximo");
                int nuevaCantidad = cantidadActual + cantidadAAgregar;
                
                cn.close();
                return nuevaCantidad <= stockMaximo;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al verificar límite de stock: " + e);
        }
        return false;
    }
    
    /**
     * Obtiene información detallada del stock
     */
    public String obtenerInfoStock(int idProducto) {
        StringBuilder info = new StringBuilder();
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT nombre, cantidad, stock_minimo, stock_maximo FROM tb_producto WHERE idProducto = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idProducto);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int stockMin = rs.getInt("stock_minimo");
                int stockMax = rs.getInt("stock_maximo");
                int espacioDisponible = stockMax - cantidad;
                
                info.append("Producto: ").append(nombre).append("\n");
                info.append("Stock actual: ").append(cantidad).append("\n");
                info.append("Stock mínimo: ").append(stockMin).append("\n");
                info.append("Stock máximo: ").append(stockMax).append("\n");
                info.append("Espacio disponible: ").append(espacioDisponible).append(" unidades");
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener info de stock: " + e);
        }
        return info.toString();
    }
    
    /**
     * Verifica si el producto necesita reabastecimiento
     */
    public boolean verificarStockBajo(int idProducto) {
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT cantidad, stock_minimo FROM tb_producto WHERE idProducto = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idProducto);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                int cantidad = rs.getInt("cantidad");
                int stockMinimo = rs.getInt("stock_minimo");
                cn.close();
                return cantidad <= stockMinimo;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al verificar stock bajo: " + e);
        }
        return false;
    }
    
    /**
     * Agrega una notificación a la cola de reabastecimiento
     */
    public void agregarNotificacionReabastecimiento(int idProducto) {
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT nombre, cantidad, stock_minimo, stock_maximo FROM tb_producto WHERE idProducto = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idProducto);
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int stockMin = rs.getInt("stock_minimo");
                int stockMax = rs.getInt("stock_maximo");
                
                // Calcular cantidad sugerida (60% del stock máximo)
                int cantidadSugerida = (int)(stockMax * 0.6) - cantidad;
                
                // Determinar prioridad (2: Urgente si está por debajo del 50% del mínimo)
                int prioridad = (cantidad < stockMin * 0.5) ? 2 : 1;
                
                NotificacionReabastecimiento notificacion = new NotificacionReabastecimiento(
                    idProducto, nombre, cantidad, cantidadSugerida, stockMin, prioridad
                );
                
                // Agregar a la cola
                colaReabastecimiento.offer(notificacion);
                
                // Guardar en base de datos
                guardarNotificacionBD(notificacion);
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al agregar notificación: " + e);
        }
    }
    
    /**
     * Guarda la notificación en la base de datos
     */
    private void guardarNotificacionBD(NotificacionReabastecimiento notif) {
        try {
            Connection cn = Conexion.conectar();
            String sql = "INSERT INTO tb_notificacion_reabastecimiento " +
                        "(idProducto, cantidadSugerida, fechaNotificacion, estado, prioridad) " +
                        "VALUES (?, ?, NOW(), ?, ?)";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, notif.getIdProducto());
            pst.setInt(2, notif.getCantidadSugerida());
            pst.setInt(3, notif.getEstado());
            pst.setInt(4, notif.getPrioridad());
            pst.executeUpdate();
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al guardar notificación en BD: " + e);
        }
    }
    
    /**
     * Obtiene la siguiente notificación de la cola (FIFO)
     */
    public NotificacionReabastecimiento obtenerSiguienteNotificacion() {
        return colaReabastecimiento.poll();
    }
    
    /**
     * Ver la siguiente notificación sin removerla
     */
    public NotificacionReabastecimiento verSiguienteNotificacion() {
        return colaReabastecimiento.peek();
    }
    
    /**
     * Obtiene el tamaño de la cola
     */
    public int obtenerCantidadNotificaciones() {
        return colaReabastecimiento.size();
    }
    
    /**
     * Carga notificaciones pendientes de la base de datos
     */
    private void cargarNotificacionesPendientes() {
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT n.idNotificacion, n.idProducto, p.nombre, p.cantidad, " +
                        "n.cantidadSugerida, p.stock_minimo, n.fechaNotificacion, n.prioridad " +
                        "FROM tb_notificacion_reabastecimiento n " +
                        "INNER JOIN tb_producto p ON n.idProducto = p.idProducto " +
                        "WHERE n.estado = 1 ORDER BY n.prioridad DESC, n.fechaNotificacion ASC";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            while (rs.next()) {
                NotificacionReabastecimiento notif = new NotificacionReabastecimiento();
                notif.setIdNotificacion(rs.getInt("idNotificacion"));
                notif.setIdProducto(rs.getInt("idProducto"));
                notif.setNombreProducto(rs.getString("nombre"));
                notif.setCantidadActual(rs.getInt("cantidad"));
                notif.setCantidadSugerida(rs.getInt("cantidadSugerida"));
                notif.setStockMinimo(rs.getInt("stock_minimo"));
                notif.setFechaNotificacion(rs.getTimestamp("fechaNotificacion"));
                notif.setPrioridad(rs.getInt("prioridad"));
                
                colaReabastecimiento.offer(notif);
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al cargar notificaciones pendientes: " + e);
        }
    }
    
    /**
     * Marca una notificación como procesada
     */
    public boolean procesarNotificacion(int idNotificacion) {
        try {
            Connection cn = Conexion.conectar();
            String sql = "UPDATE tb_notificacion_reabastecimiento SET estado = 0 WHERE idNotificacion = ?";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idNotificacion);
            int resultado = pst.executeUpdate();
            cn.close();
            return resultado > 0;
        } catch (SQLException e) {
            System.out.println("Error al procesar notificación: " + e);
            return false;
        }
    }
    
    /**
     * Obtiene información de capacidad del inventario
     */
    public String obtenerResumenInventario() {
        StringBuilder resumen = new StringBuilder();
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT " +
                        "COUNT(*) as total_productos, " +
                        "SUM(cantidad) as total_unidades, " +
                        "SUM(stock_maximo) as capacidad_total, " +
                        "SUM(stock_maximo - cantidad) as espacio_disponible " +
                        "FROM tb_producto WHERE estado = 1";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                int totalProductos = rs.getInt("total_productos");
                int totalUnidades = rs.getInt("total_unidades");
                int capacidadTotal = rs.getInt("capacidad_total");
                int espacioDisponible = rs.getInt("espacio_disponible");
                double porcentajeOcupacion = (totalUnidades * 100.0) / capacidadTotal;
                
                resumen.append("=== RESUMEN DE INVENTARIO ===\n");
                resumen.append("Total de productos: ").append(totalProductos).append("\n");
                resumen.append("Unidades en stock: ").append(totalUnidades).append("\n");
                resumen.append("Capacidad total: ").append(capacidadTotal).append("\n");
                resumen.append("Espacio disponible: ").append(espacioDisponible).append("\n");
                resumen.append("Ocupación: ").append(String.format("%.2f", porcentajeOcupacion)).append("%\n");
                resumen.append("Notificaciones pendientes: ").append(obtenerCantidadNotificaciones());
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener resumen de inventario: " + e);
        }
        return resumen.toString();
    }
}