package controlador;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import modelo.Producto;

public class Ctrl_Producto {
    
    private Ctrl_StockManager stockManager;
    private Ctrl_HistorialMovimientos historialMovimientos;
    
    public Ctrl_Producto() {
        this.stockManager = new Ctrl_StockManager();
        this.historialMovimientos = new Ctrl_HistorialMovimientos();
    }
    
    /**
     * Guarda un nuevo producto
     */
    public boolean guardar(Producto objeto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                "insert into tb_producto values(?,?,?,?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
            consulta.setInt(1, 0);//id
            consulta.setString(2, objeto.getNombre());
            consulta.setInt(3, objeto.getCantidad());
            consulta.setDouble(4, objeto.getPrecio());
            consulta.setString(5, objeto.getDescripcion());
            consulta.setInt(6, objeto.getPorcentajeIva());
            consulta.setInt(7, objeto.getIdCategoria());
            consulta.setInt(8, objeto.getEstado());
            
            // Valores predeterminados para stock_minimo y stock_maximo
            consulta.setInt(9, 5);  // stock_minimo por defecto
            consulta.setInt(10, 100); // stock_maximo por defecto

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
                
                // Obtener el ID generado
                ResultSet rs = consulta.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    
                    // Registrar movimiento inicial
                    historialMovimientos.registrarMovimiento(
                        idGenerado, 
                        "ENTRADA", 
                        0, 
                        objeto.getCantidad(), 
                        1, 
                        "Producto creado - Stock inicial"
                    );
                }
            }

            cn.close();

        } catch (SQLException e) {
            System.out.println("Error al guardar producto: " + e);
        }

        return respuesta;
    }

    /**
     * Verifica si el producto ya existe
     */
    public boolean existeProducto(String producto) {
        boolean respuesta = false;
        String sql = "select nombre from tb_producto where nombre = '" + producto + "';";
        Statement st;

        try {
            Connection cn = Conexion.conectar();
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                respuesta = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar producto: " + e);
        }
        return respuesta;
    }
    
    /**
     * Actualiza un producto
     */
    public boolean actualizar(Producto objeto, int idProducto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            // Obtener cantidad anterior para registrar el movimiento
            int cantidadAnterior = 0;
            String sqlAnterior = "SELECT cantidad FROM tb_producto WHERE idProducto = ?";
            PreparedStatement pstAnterior = cn.prepareStatement(sqlAnterior);
            pstAnterior.setInt(1, idProducto);
            ResultSet rs = pstAnterior.executeQuery();
            if (rs.next()) {
                cantidadAnterior = rs.getInt("cantidad");
            }
            
            PreparedStatement consulta = cn.prepareStatement(
                "update tb_producto set nombre=?, cantidad = ?, precio = ?, " +
                "descripcion= ?, porcentajeIva = ?, idCategoria = ?, estado = ? " +
                "where idProducto = ?");
            consulta.setString(1, objeto.getNombre());
            consulta.setInt(2, objeto.getCantidad());
            consulta.setDouble(3, objeto.getPrecio());
            consulta.setString(4, objeto.getDescripcion());
            consulta.setInt(5, objeto.getPorcentajeIva());
            consulta.setInt(6, objeto.getIdCategoria());
            consulta.setInt(7, objeto.getEstado());
            consulta.setInt(8, idProducto);
           
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
                
                // Registrar movimiento si la cantidad cambió
                if (cantidadAnterior != objeto.getCantidad()) {
                    String tipoMovimiento = objeto.getCantidad() > cantidadAnterior ? "ENTRADA" : 
                                           objeto.getCantidad() < cantidadAnterior ? "SALIDA" : "AJUSTE";
                    historialMovimientos.registrarMovimiento(
                        idProducto, 
                        tipoMovimiento, 
                        cantidadAnterior, 
                        objeto.getCantidad(), 
                        1, 
                        "Actualización desde gestión de productos"
                    );
                    
                    // Verificar stock bajo
                    if (stockManager.verificarStockBajo(idProducto)) {
                        stockManager.agregarNotificacionReabastecimiento(idProducto);
                        System.out.println("⚠️ Notificación generada por actualización en gestión");
                    }
                }
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar producto: " + e);
        }
        return respuesta;
    }
    
    /**
     * Elimina un producto
     */
    public boolean eliminar(int idProducto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            PreparedStatement consulta = cn.prepareStatement(
                    "delete from tb_producto where idProducto ='" + idProducto + "'");
            consulta.executeUpdate();
           
            if (consulta.executeUpdate() > 0) {
                respuesta = true;
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e);
        }
        return respuesta;
    }
    
    /**
     * Actualiza el stock de un producto con validaciones
     */
    public boolean actualizarStock(Producto object, int idProducto) {
        boolean respuesta = false;
        Connection cn = Conexion.conectar();
        try {
            // Obtener cantidad anterior
            int cantidadAnterior = 0;
            String sqlAnterior = "SELECT cantidad FROM tb_producto WHERE idProducto = ?";
            PreparedStatement pstAnterior = cn.prepareStatement(sqlAnterior);
            pstAnterior.setInt(1, idProducto);
            ResultSet rs = pstAnterior.executeQuery();
            if (rs.next()) {
                cantidadAnterior = rs.getInt("cantidad");
            }
            
            int cantidadAAgregar = object.getCantidad() - cantidadAnterior;
            
            // Verificar límite de stock antes de actualizar
            if (cantidadAAgregar > 0) {
                if (!stockManager.verificarLimiteStock(idProducto, cantidadAAgregar)) {
                    System.out.println("ERROR: Se excede el límite de stock máximo");
                    cn.close();
                    return false;
                }
            }
            
            PreparedStatement consulta = cn.prepareStatement(
                "update tb_producto set cantidad=? where idProducto = ?");
            consulta.setInt(1, object.getCantidad());
            consulta.setInt(2, idProducto);

            if (consulta.executeUpdate() > 0) {
                respuesta = true;
                
                // Registrar movimiento en el historial
                String tipoMovimiento = cantidadAAgregar > 0 ? "ENTRADA" : 
                                       cantidadAAgregar < 0 ? "SALIDA" : "AJUSTE";
                historialMovimientos.registrarMovimiento(
                    idProducto, 
                    tipoMovimiento, 
                    cantidadAnterior, 
                    object.getCantidad(), 
                    1, 
                    "Actualización de stock"
                );
                
                System.out.println("✓ Stock actualizado. Verificando nivel mínimo...");
                
                // Verificar si necesita reabastecimiento DESPUÉS de actualizar
                if (stockManager.verificarStockBajo(idProducto)) {
                    System.out.println("⚠️ Stock bajo detectado. Generando notificación...");
                    stockManager.agregarNotificacionReabastecimiento(idProducto);
                    System.out.println("✓ Notificación generada");
                } else {
                    System.out.println("✓ Stock en nivel adecuado");
                }
            }
            cn.close();
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock del producto: " + e);
        }
        return respuesta;
    }
    
    /**
     * Obtiene información del límite de stock
     */
    public String obtenerInfoStock(int idProducto) {
        return stockManager.obtenerInfoStock(idProducto);
    }
    
    /**
     * Obtiene el gestor de stock
     */
    public Ctrl_StockManager getStockManager() {
        return stockManager;
    }
    
    /**
     * Obtiene el controlador de historial
     */
    public Ctrl_HistorialMovimientos getHistorialMovimientos() {
        return historialMovimientos;
    }
}