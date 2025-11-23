package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase de conexión a MySQL con zona horaria configurada
 */
public class Conexion {
    
    // URL de conexión con zona horaria de Perú (UTC-5)
    private static String URL = "jdbc:mysql://localhost:3306/bd_sistema_ventas1?"
           + "useUnicode=true&"
           + "useJDBCCompliantTimezoneShift=true&"
           + "useLegacyDatetimeCode=false&"
           + "serverTimezone=America/Lima&"  // <-- ZONA HORARIA DE PERÚ
           + "useSSL=false&"
           + "allowPublicKeyRetrieval=true";
    
    private static final String USER = "root";
    private static final String PASSWORD = "admin"; // Cambia por tu contraseña
    
    //conexion local
    public static Connection conectar() {
        Connection cn = null;
        try {
            // Establecer la conexión
            cn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a MySQL");
            
            return cn;
        } catch (SQLException e) {
            System.out.println("Error en la conexion local " + e);
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            // Obtener conexión
            conexion = conectar();
            
            if (conexion != null) {
                // Crear statement
                stmt = conexion.createStatement();
                
                // Ejecutar una consulta de prueba
                String query = "SELECT VERSION() as version, DATABASE() as db, NOW() as hora_actual";
                rs = stmt.executeQuery(query);
                
                // Mostrar resultado
                if (rs.next()) {
                    System.out.println("Conexion exitosa");
                    System.out.println("🔹 Versión MySQL: " + rs.getString("version"));
                    System.out.println("🔹 Base de datos: " + rs.getString("db"));
                    System.out.println("🔹 Hora del servidor: " + rs.getString("hora_actual"));
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Error al ejecutar la consulta");
            e.printStackTrace();
        } finally {
            // Cerrar recursos
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexion != null) conexion.close();
                System.out.println("Conexión cerrada");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}