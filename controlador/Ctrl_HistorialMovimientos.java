package controlador;

import conexion.Conexion;
import java.sql.*;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class Ctrl_HistorialMovimientos {
    
    // Pila para operaciones de deshacer (LIFO)
    private Stack<MovimientoProducto> pilaMovimientos;
    private static final int MAX_HISTORIAL = 50; // Límite de movimientos en memoria
    
    public Ctrl_HistorialMovimientos() {
        this.pilaMovimientos = new Stack<>();
        cargarUltimosMovimientos();
    }
    
    /**
     * Clase interna para representar un movimiento
     */
    public static class MovimientoProducto {
        private int idMovimiento;
        private int idProducto;
        private String nombreProducto;
        private String tipoMovimiento; // ENTRADA, SALIDA, AJUSTE
        private int cantidadAnterior;
        private int cantidadNueva;
        private int cantidadMovida;
        private Timestamp fechaMovimiento;
        private int idUsuario;
        private String observaciones;
        
        public MovimientoProducto(int idProducto, String nombreProducto, String tipoMovimiento,
                                 int cantidadAnterior, int cantidadNueva, int cantidadMovida,
                                 int idUsuario, String observaciones) {
            this.idProducto = idProducto;
            this.nombreProducto = nombreProducto;
            this.tipoMovimiento = tipoMovimiento;
            this.cantidadAnterior = cantidadAnterior;
            this.cantidadNueva = cantidadNueva;
            this.cantidadMovida = cantidadMovida;
            this.fechaMovimiento = new Timestamp(System.currentTimeMillis());
            this.idUsuario = idUsuario;
            this.observaciones = observaciones;
        }
        
        // Getters y Setters
        public int getIdMovimiento() { return idMovimiento; }
        public void setIdMovimiento(int idMovimiento) { this.idMovimiento = idMovimiento; }
        
        public int getIdProducto() { return idProducto; }
        public String getNombreProducto() { return nombreProducto; }
        public String getTipoMovimiento() { return tipoMovimiento; }
        public int getCantidadAnterior() { return cantidadAnterior; }
        public int getCantidadNueva() { return cantidadNueva; }
        public int getCantidadMovida() { return cantidadMovida; }
        
        public Timestamp getFechaMovimiento() { return fechaMovimiento; }
        public void setFechaMovimiento(Timestamp fechaMovimiento) { 
            this.fechaMovimiento = fechaMovimiento; 
        }
        
        public int getIdUsuario() { return idUsuario; }
        public String getObservaciones() { return observaciones; }
        
        @Override
        public String toString() {
            return String.format("[%s] %s: %s - De %d a %d unidades (Movimiento: %d)",
                    tipoMovimiento, nombreProducto, 
                    fechaMovimiento.toString(), 
                    cantidadAnterior, cantidadNueva, cantidadMovida);
        }
    }
    
    /**
     * Registra un movimiento en la pila y en la base de datos
     */
    public boolean registrarMovimiento(int idProducto, String tipoMovimiento, 
                                      int cantidadAnterior, int cantidadNueva, 
                                      int idUsuario, String observaciones) {
        try {
            // Obtener nombre del producto
            Connection cn = Conexion.conectar();
            String sqlNombre = "SELECT nombre FROM tb_producto WHERE idProducto = ?";
            PreparedStatement pstNombre = cn.prepareStatement(sqlNombre);
            pstNombre.setInt(1, idProducto);
            ResultSet rs = pstNombre.executeQuery();
            
            String nombreProducto = "";
            if (rs.next()) {
                nombreProducto = rs.getString("nombre");
            }
            
            int cantidadMovida = Math.abs(cantidadNueva - cantidadAnterior);
            
            // Crear objeto de movimiento
            MovimientoProducto movimiento = new MovimientoProducto(
                idProducto, nombreProducto, tipoMovimiento, 
                cantidadAnterior, cantidadNueva, cantidadMovida,
                idUsuario, observaciones
            );
            
            // Agregar a la pila (último en entrar, primero en salir)
            pilaMovimientos.push(movimiento);
            
            // Limitar tamaño de la pila en memoria
            if (pilaMovimientos.size() > MAX_HISTORIAL) {
                // Crear una nueva pila con los últimos MAX_HISTORIAL elementos
                Stack<MovimientoProducto> nuevaPila = new Stack<>();
                List<MovimientoProducto> temp = new ArrayList<>();
                
                while (!pilaMovimientos.isEmpty() && temp.size() < MAX_HISTORIAL) {
                    temp.add(0, pilaMovimientos.pop());
                }
                
                for (MovimientoProducto m : temp) {
                    nuevaPila.push(m);
                }
                
                pilaMovimientos = nuevaPila;
            }
            
            // Guardar en base de datos
            String sql = "INSERT INTO tb_historial_movimientos " +
                        "(idProducto, tipoMovimiento, cantidadAnterior, " +
                        "cantidadNueva, cantidadMovida, fechaMovimiento, idUsuario, observaciones) " +
                        "VALUES (?, ?, ?, ?, ?, NOW(), ?, ?)";
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idProducto);
            pst.setString(2, tipoMovimiento);
            pst.setInt(3, cantidadAnterior);
            pst.setInt(4, cantidadNueva);
            pst.setInt(5, cantidadMovida);
            pst.setInt(6, idUsuario);
            pst.setString(7, observaciones);
            
            int resultado = pst.executeUpdate();
            cn.close();
            
            return resultado > 0;
            
        } catch (SQLException e) {
            System.out.println("Error al registrar movimiento: " + e);
            return false;
        }
    }
    
    /**
     * Obtiene el último movimiento sin removerlo de la pila
     */
    public MovimientoProducto verUltimoMovimiento() {
        if (!pilaMovimientos.isEmpty()) {
            return pilaMovimientos.peek();
        }
        return null;
    }
    
    /**
     * Obtiene y remueve el último movimiento de la pila
     */
    public MovimientoProducto obtenerUltimoMovimiento() {
        if (!pilaMovimientos.isEmpty()) {
            return pilaMovimientos.pop();
        }
        return null;
    }
    
    /**
     * Verifica si hay movimientos en la pila
     */
    public boolean hayMovimientos() {
        return !pilaMovimientos.isEmpty();
    }
    
    /**
     * Obtiene el tamaño de la pila
     */
    public int obtenerCantidadMovimientos() {
        return pilaMovimientos.size();
    }
    
    /**
     * Carga los últimos movimientos desde la base de datos
     */
    private void cargarUltimosMovimientos() {
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT " +
                        "h.idMovimiento, h.idProducto, p.nombre, h.tipoMovimiento, " +
                        "h.cantidadAnterior, h.cantidadNueva, h.cantidadMovida, " +
                        "h.fechaMovimiento, h.idUsuario, h.observaciones " +
                        "FROM tb_historial_movimientos h " +
                        "INNER JOIN tb_producto p ON h.idProducto = p.idProducto " +
                        "ORDER BY h.fechaMovimiento DESC LIMIT " + MAX_HISTORIAL;
            
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            // Cargar en una lista temporal para invertir el orden
            List<MovimientoProducto> movimientosTemp = new ArrayList<>();
            
            while (rs.next()) {
                MovimientoProducto mov = new MovimientoProducto(
                    rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getString("tipoMovimiento"),
                    rs.getInt("cantidadAnterior"),
                    rs.getInt("cantidadNueva"),
                    rs.getInt("cantidadMovida"),
                    rs.getInt("idUsuario"),
                    rs.getString("observaciones")
                );
                mov.setIdMovimiento(rs.getInt("idMovimiento"));
                // Asignar la fecha REAL de la base de datos
                mov.setFechaMovimiento(rs.getTimestamp("fechaMovimiento"));
                movimientosTemp.add(0, mov); // Agregar al inicio para mantener orden
            }
            
            // Agregar a la pila en orden cronológico
            for (MovimientoProducto mov : movimientosTemp) {
                pilaMovimientos.push(mov);
            }
            
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al cargar historial de movimientos: " + e);
        }
    }
    
    /**
     * Obtiene los últimos N movimientos de un producto específico
     */
    public List<MovimientoProducto> obtenerMovimientosProducto(int idProducto, int limite) {
        List<MovimientoProducto> movimientos = new ArrayList<>();
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT " +
                        "h.idMovimiento, h.idProducto, p.nombre, h.tipoMovimiento, " +
                        "h.cantidadAnterior, h.cantidadNueva, h.cantidadMovida, " +
                        "h.fechaMovimiento, h.idUsuario, h.observaciones " +
                        "FROM tb_historial_movimientos h " +
                        "INNER JOIN tb_producto p ON h.idProducto = p.idProducto " +
                        "WHERE h.idProducto = ? " +
                        "ORDER BY h.fechaMovimiento DESC LIMIT ?";
            
            PreparedStatement pst = cn.prepareStatement(sql);
            pst.setInt(1, idProducto);
            pst.setInt(2, limite);
            ResultSet rs = pst.executeQuery();
            
            while (rs.next()) {
                MovimientoProducto mov = new MovimientoProducto(
                    rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getString("tipoMovimiento"),
                    rs.getInt("cantidadAnterior"),
                    rs.getInt("cantidadNueva"),
                    rs.getInt("cantidadMovida"),
                    rs.getInt("idUsuario"),
                    rs.getString("observaciones")
                );
                mov.setIdMovimiento(rs.getInt("idMovimiento"));
                // Asignar la fecha REAL de la base de datos
                mov.setFechaMovimiento(rs.getTimestamp("fechaMovimiento"));
                movimientos.add(mov);
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener movimientos del producto: " + e);
        }
        return movimientos;
    }
    
    /**
     * Limpia la pila de movimientos en memoria
     */
    public void limpiarPila() {
        pilaMovimientos.clear();
    }
}