package vista;

import conexion.Conexion;
import controlador.Ctrl_StockManager;
import java.awt.Dimension;
import java.sql.*;
import javax.swing.*;

public class InterDiagnostico extends javax.swing.JInternalFrame {
    
    private Ctrl_StockManager stockManager;

    public InterDiagnostico() {
        initComponents();
        this.setSize(new Dimension(600, 500));
        this.setTitle("Diagnóstico del Sistema");
        
        stockManager = new Ctrl_StockManager();
        ejecutarDiagnostico();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_diagnostico = new javax.swing.JTextArea();
        jButton_actualizar = new javax.swing.JButton();
        jButton_forzar_notificacion = new javax.swing.JButton();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Diagnóstico del Sistema de Inventario");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resultado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea_diagnostico.setEditable(false);
        jTextArea_diagnostico.setColumns(20);
        jTextArea_diagnostico.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea_diagnostico.setRows(5);
        jScrollPane1.setViewportView(jTextArea_diagnostico);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 540, 330));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 560, 360));

        jButton_actualizar.setBackground(new java.awt.Color(0, 153, 255));
        jButton_actualizar.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_actualizar.setText("Actualizar Diagnóstico");
        jButton_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_actualizarActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 250, 30));

        jButton_forzar_notificacion.setBackground(new java.awt.Color(255, 153, 0));
        jButton_forzar_notificacion.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_forzar_notificacion.setText("Forzar Notificaciones");
        jButton_forzar_notificacion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_forzar_notificacionActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_forzar_notificacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 420, 250, 30));

        jLabel_wallpaper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondo3.jpg")));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 580, 470));

        pack();
    }// </editor-fold>

    private void jButton_actualizarActionPerformed(java.awt.event.ActionEvent evt) {
        ejecutarDiagnostico();
    }

    private void jButton_forzar_notificacionActionPerformed(java.awt.event.ActionEvent evt) {
        StringBuilder resultado = new StringBuilder();
        resultado.append("FORZANDO GENERACIÓN DE NOTIFICACIONES...\n");
        resultado.append("════════════════════════════════════════\n\n");
        
        try {
            Connection cn = Conexion.conectar();
            
            // Buscar productos con stock bajo
            String sql = "SELECT idProducto, nombre, cantidad, stock_minimo " +
                        "FROM tb_producto " +
                        "WHERE cantidad <= stock_minimo";
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            int contador = 0;
            while (rs.next()) {
                int idProducto = rs.getInt("idProducto");
                String nombre = rs.getString("nombre");
                int cantidad = rs.getInt("cantidad");
                int stockMin = rs.getInt("stock_minimo");
                
                // Forzar creación de notificación
                stockManager.agregarNotificacionReabastecimiento(idProducto);
                
                resultado.append("✓ Notificación creada para: ").append(nombre)
                         .append(" (Stock: ").append(cantidad).append("/")
                         .append(stockMin).append(")\n");
                contador++;
            }
            
            if (contador == 0) {
                resultado.append("No hay productos con stock bajo.\n");
                resultado.append("Todos los productos están en nivel adecuado.\n");
            } else {
                resultado.append("\n").append(contador).append(" notificaciones generadas!\n");
            }
            
            cn.close();
            
        } catch (SQLException e) {
            resultado.append("\n❌ Error: ").append(e.getMessage());
        }
        
        jTextArea_diagnostico.setText(resultado.toString());
    }

    // Variables declaration
    private javax.swing.JButton jButton_actualizar;
    private javax.swing.JButton jButton_forzar_notificacion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel_wallpaper;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_diagnostico;
    // End of variables declaration

    private void ejecutarDiagnostico() {
        StringBuilder diag = new StringBuilder();
        diag.append("═══════════════════════════════════════════════════════\n");
        diag.append("          DIAGNÓSTICO DEL SISTEMA DE INVENTARIO        \n");
        diag.append("═══════════════════════════════════════════════════════\n\n");
        
        try {
            Connection cn = Conexion.conectar();
            
            // 1. Estado de productos
            diag.append("1. ESTADO DE PRODUCTOS:\n");
            diag.append("──────────────────────────────────────────────────────\n");
            String sql1 = "SELECT idProducto, nombre, cantidad, stock_minimo, stock_maximo " +
                         "FROM tb_producto ORDER BY cantidad ASC LIMIT 5";
            Statement st1 = cn.createStatement();
            ResultSet rs1 = st1.executeQuery(sql1);
            
            while (rs1.next()) {
                String nombre = rs1.getString("nombre");
                int cantidad = rs1.getInt("cantidad");
                int stockMin = rs1.getInt("stock_minimo");
                int stockMax = rs1.getInt("stock_maximo");
                
                String estado = "";
                if (cantidad <= stockMin) {
                    estado = "⚠️ BAJO";
                } else if (cantidad >= stockMax) {
                    estado = "⚠️ MÁXIMO";
                } else {
                    estado = "✓ OK";
                }
                
                diag.append(String.format("  • %s: %d (Min: %d, Max: %d) %s\n", 
                    nombre, cantidad, stockMin, stockMax, estado));
            }
            
            // 2. Productos con stock bajo
            diag.append("\n2. PRODUCTOS CON STOCK BAJO:\n");
            diag.append("──────────────────────────────────────────────────────\n");
            String sql2 = "SELECT COUNT(*) as total FROM tb_producto WHERE cantidad <= stock_minimo";
            Statement st2 = cn.createStatement();
            ResultSet rs2 = st2.executeQuery(sql2);
            if (rs2.next()) {
                int total = rs2.getInt("total");
                diag.append("  Total: ").append(total).append(" productos\n");
                
                if (total > 0) {
                    String sql2b = "SELECT nombre, cantidad, stock_minimo FROM tb_producto " +
                                  "WHERE cantidad <= stock_minimo";
                    ResultSet rs2b = st2.executeQuery(sql2b);
                    while (rs2b.next()) {
                        diag.append("  • ").append(rs2b.getString("nombre"))
                            .append(": ").append(rs2b.getInt("cantidad"))
                            .append("/").append(rs2b.getInt("stock_minimo")).append("\n");
                    }
                }
            }
            
            // 3. Notificaciones pendientes
            diag.append("\n3. NOTIFICACIONES PENDIENTES:\n");
            diag.append("──────────────────────────────────────────────────────\n");
            String sql3 = "SELECT COUNT(*) as total FROM tb_notificacion_reabastecimiento WHERE estado = 1";
            Statement st3 = cn.createStatement();
            ResultSet rs3 = st3.executeQuery(sql3);
            if (rs3.next()) {
                int total = rs3.getInt("total");
                diag.append("  Total: ").append(total).append(" notificaciones\n");
                
                if (total > 0) {
                    String sql3b = "SELECT p.nombre, n.cantidadSugerida, " +
                                  "CASE n.prioridad WHEN 2 THEN 'URGENTE' ELSE 'Normal' END as prioridad " +
                                  "FROM tb_notificacion_reabastecimiento n " +
                                  "INNER JOIN tb_producto p ON n.idProducto = p.idProducto " +
                                  "WHERE n.estado = 1 ORDER BY n.prioridad DESC";
                    ResultSet rs3b = st3.executeQuery(sql3b);
                    while (rs3b.next()) {
                        String prioridad = rs3b.getString("prioridad").equals("URGENTE") ? "🔴" : "🟡";
                        diag.append("  ").append(prioridad).append(" ")
                            .append(rs3b.getString("nombre"))
                            .append(" (Sugerido: +").append(rs3b.getInt("cantidadSugerida"))
                            .append(")\n");
                    }
                } else {
                    diag.append("  ✓ No hay notificaciones pendientes\n");
                }
            }
            
            // 4. Historial de movimientos
            diag.append("\n4. HISTORIAL DE MOVIMIENTOS:\n");
            diag.append("──────────────────────────────────────────────────────\n");
            String sql4 = "SELECT COUNT(*) as total FROM tb_historial_movimientos";
            Statement st4 = cn.createStatement();
            ResultSet rs4 = st4.executeQuery(sql4);
            if (rs4.next()) {
                int total = rs4.getInt("total");
                diag.append("  Total registrados: ").append(total).append(" movimientos\n");
                
                if (total > 0) {
                    String sql4b = "SELECT p.nombre, h.tipoMovimiento, h.cantidadMovida, h.fechaMovimiento " +
                                  "FROM tb_historial_movimientos h " +
                                  "INNER JOIN tb_producto p ON h.idProducto = p.idProducto " +
                                  "ORDER BY h.fechaMovimiento DESC LIMIT 3";
                    ResultSet rs4b = st4.executeQuery(sql4b);
                    diag.append("\n  Últimos 3 movimientos:\n");
                    while (rs4b.next()) {
                        String icono = rs4b.getString("tipoMovimiento").equals("ENTRADA") ? "↑" : "↓";
                        diag.append("  ").append(icono).append(" ")
                            .append(rs4b.getString("nombre"))
                            .append(" (").append(rs4b.getInt("cantidadMovida"))
                            .append(" unidades)\n");
                    }
                } else {
                    diag.append("  ⚠️ No hay movimientos registrados\n");
                }
            }
            
            // 5. Recomendaciones
            diag.append("\n5. RECOMENDACIONES:\n");
            diag.append("──────────────────────────────────────────────────────\n");
            
            String sql5 = "SELECT COUNT(*) as bajo FROM tb_producto WHERE cantidad <= stock_minimo";
            ResultSet rs5 = st3.executeQuery(sql5);
            if (rs5.next() && rs5.getInt("bajo") > 0) {
                diag.append("  ⚠️ Hay productos con stock bajo\n");
                diag.append("  → Haz clic en 'Forzar Notificaciones' para generarlas\n");
            } else {
                diag.append("  ✓ Todos los productos tienen stock adecuado\n");
            }
            
            cn.close();
            
        } catch (SQLException e) {
            diag.append("\n❌ ERROR: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
        }
        
        diag.append("\n═══════════════════════════════════════════════════════\n");
        diag.append("Diagnóstico completado: ").append(new java.util.Date()).append("\n");
        
        jTextArea_diagnostico.setText(diag.toString());
    }
}