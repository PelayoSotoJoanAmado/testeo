package vista;

import controlador.Ctrl_HistorialMovimientos;
import controlador.Ctrl_HistorialMovimientos.MovimientoProducto;
import conexion.Conexion;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.sql.*;

public class InterHistorialMovimientos extends javax.swing.JInternalFrame {
    
    private Ctrl_HistorialMovimientos historialCtrl;
    private DefaultListModel<String> modeloLista;

    public InterHistorialMovimientos() {
        initComponents();
        this.setSize(new Dimension(700, 500));
        this.setTitle("Historial de Movimientos (Pila)");
        
        historialCtrl = new Ctrl_HistorialMovimientos();
        modeloLista = new DefaultListModel<>();
        cargarHistorial();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_historial = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jButton_ver_ultimo = new javax.swing.JButton();
        jButton_actualizar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel_cantidad = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_detalle = new javax.swing.JTextArea();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Historial de Movimientos de Inventario (Pila LIFO)");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Últimos Movimientos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList_historial.setFont(new java.awt.Font("Monospaced", 0, 11));
        jList_historial.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_historialMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList_historial);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 640, 180));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 660, 210));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton_ver_ultimo.setBackground(new java.awt.Color(0, 153, 255));
        jButton_ver_ultimo.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_ver_ultimo.setText("Ver Último");
        jButton_ver_ultimo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ver_ultimoActionPerformed(evt);
            }
        });
        jPanel2.add(jButton_ver_ultimo, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 130, 30));

        jButton_actualizar.setBackground(new java.awt.Color(255, 153, 0));
        jButton_actualizar.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_actualizar.setText("Actualizar");
        jButton_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_actualizarActionPerformed(evt);
            }
        });
        jPanel2.add(jButton_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 130, 30));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11));
        jLabel2.setText("Movimientos en pila:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, -1, -1));

        jLabel_cantidad.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel_cantidad.setForeground(new java.awt.Color(0, 102, 204));
        jLabel_cantidad.setText("0");
        jPanel2.add(jLabel_cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 130, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, 150, 140));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Detalle del Movimiento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea_detalle.setEditable(false);
        jTextArea_detalle.setColumns(20);
        jTextArea_detalle.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea_detalle.setRows(5);
        jScrollPane2.setViewportView(jTextArea_detalle);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 490, 110));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 270, 500, 140));

        jLabel_wallpaper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondo3.jpg")));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 690, 470));

        pack();
    }// </editor-fold>

    private void jButton_ver_ultimoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Connection cn = Conexion.conectar();
            String sql = "SELECT " +
                        "h.idMovimiento, h.idProducto, p.nombre, h.tipoMovimiento, " +
                        "h.cantidadAnterior, h.cantidadNueva, h.cantidadMovida, " +
                        "h.fechaMovimiento, h.idUsuario, h.observaciones " +
                        "FROM tb_historial_movimientos h " +
                        "INNER JOIN tb_producto p ON h.idProducto = p.idProducto " +
                        "ORDER BY h.fechaMovimiento DESC LIMIT 1";
            
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            if (rs.next()) {
                MovimientoProducto ultimo = new MovimientoProducto(
                    rs.getInt("idProducto"),
                    rs.getString("nombre"),
                    rs.getString("tipoMovimiento"),
                    rs.getInt("cantidadAnterior"),
                    rs.getInt("cantidadNueva"),
                    rs.getInt("cantidadMovida"),
                    rs.getInt("idUsuario"),
                    rs.getString("observaciones")
                );
                ultimo.setIdMovimiento(rs.getInt("idMovimiento"));
                ultimo.setFechaMovimiento(rs.getTimestamp("fechaMovimiento"));
                
                mostrarDetalleMovimiento(ultimo);
            } else {
                JOptionPane.showMessageDialog(this, "No hay movimientos en el historial", 
                                             "Pila Vacía", JOptionPane.INFORMATION_MESSAGE);
            }
            
            cn.close();
            
        } catch (SQLException e) {
            System.out.println("Error al obtener último movimiento: " + e);
            JOptionPane.showMessageDialog(this, "Error al obtener el último movimiento", 
                                         "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jButton_actualizarActionPerformed(java.awt.event.ActionEvent evt) {
        cargarHistorial();
        JOptionPane.showMessageDialog(this, "Historial actualizado");
    }

    private void jList_historialMouseClicked(java.awt.event.MouseEvent evt) {
        int index = jList_historial.getSelectedIndex();
        if (index >= 0) {
            // Obtener el movimiento correspondiente DESDE LA BASE DE DATOS
            try {
                Connection cn = conexion.Conexion.conectar();
                String sql = "SELECT " +
                            "h.idMovimiento, h.idProducto, p.nombre, h.tipoMovimiento, " +
                            "h.cantidadAnterior, h.cantidadNueva, h.cantidadMovida, " +
                            "h.fechaMovimiento, h.idUsuario, h.observaciones " +
                            "FROM tb_historial_movimientos h " +
                            "INNER JOIN tb_producto p ON h.idProducto = p.idProducto " +
                            "ORDER BY h.fechaMovimiento DESC LIMIT 50";
                
                Statement st = cn.createStatement();
                ResultSet rs = st.executeQuery(sql);
                
                // Avanzar hasta el índice seleccionado
                int contador = 0;
                MovimientoProducto movimientoSeleccionado = null;
                
                while (rs.next() && contador <= index) {
                    if (contador == index) {
                        movimientoSeleccionado = new MovimientoProducto(
                            rs.getInt("idProducto"),
                            rs.getString("nombre"),
                            rs.getString("tipoMovimiento"),
                            rs.getInt("cantidadAnterior"),
                            rs.getInt("cantidadNueva"),
                            rs.getInt("cantidadMovida"),
                            rs.getInt("idUsuario"),
                            rs.getString("observaciones")
                        );
                        movimientoSeleccionado.setIdMovimiento(rs.getInt("idMovimiento"));
                        movimientoSeleccionado.setFechaMovimiento(rs.getTimestamp("fechaMovimiento"));
                    }
                    contador++;
                }
                
                cn.close();
                
                if (movimientoSeleccionado != null) {
                    mostrarDetalleMovimiento(movimientoSeleccionado);
                }
                
            } catch (java.sql.SQLException e) {
                System.out.println("Error al obtener detalle del movimiento: " + e);
            }
        }
    }

    // Variables declaration
    private javax.swing.JButton jButton_actualizar;
    private javax.swing.JButton jButton_ver_ultimo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel_cantidad;
    private javax.swing.JLabel jLabel_wallpaper;
    private javax.swing.JList<String> jList_historial;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea_detalle;
    // End of variables declaration

    private void cargarHistorial() {
        modeloLista.clear();
        
        // Obtener movimientos DIRECTAMENTE de la base de datos
        // No usar la pila en memoria para evitar duplicaciones
        try {
            Connection cn = conexion.Conexion.conectar();
            String sql = "SELECT " +
                        "h.idMovimiento, h.idProducto, p.nombre, h.tipoMovimiento, " +
                        "h.cantidadAnterior, h.cantidadNueva, h.cantidadMovida, " +
                        "h.fechaMovimiento, h.idUsuario, h.observaciones " +
                        "FROM tb_historial_movimientos h " +
                        "INNER JOIN tb_producto p ON h.idProducto = p.idProducto " +
                        "ORDER BY h.fechaMovimiento DESC LIMIT 50";
            
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
            int contador = 0;
            while (rs.next()) {
                String tipoMovimiento = rs.getString("tipoMovimiento");
                String icono = "";
                switch (tipoMovimiento) {
                    case "ENTRADA":
                        icono = "↑";
                        break;
                    case "SALIDA":
                        icono = "↓";
                        break;
                    case "AJUSTE":
                        icono = "⚙";
                        break;
                }
                
                int cantidadMovida = rs.getInt("cantidadMovida");
                int signo = tipoMovimiento.equals("SALIDA") ? -1 : 1;
                
                String linea = String.format("%s [%s] %s: %d → %d (%+d)",
                    icono,
                    tipoMovimiento,
                    rs.getString("nombre"),
                    rs.getInt("cantidadAnterior"),
                    rs.getInt("cantidadNueva"),
                    cantidadMovida * signo
                );
                modeloLista.addElement(linea);
                contador++;
            }
            
            cn.close();
            
            if (contador == 0) {
                modeloLista.addElement("No hay movimientos registrados");
            }
            
            // Actualizar contador
            jLabel_cantidad.setText(String.valueOf(contador));
            
        } catch (java.sql.SQLException e) {
            System.out.println("Error al cargar historial: " + e);
            modeloLista.addElement("Error al cargar historial");
        }
        
        jList_historial.setModel(modeloLista);
    }
    
    private void mostrarDetalleMovimiento(MovimientoProducto mov) {
        StringBuilder detalle = new StringBuilder();
        detalle.append("DETALLE DEL MOVIMIENTO\n");
        detalle.append("═══════════════════════════════\n\n");
        detalle.append("Tipo: ").append(mov.getTipoMovimiento()).append("\n");
        detalle.append("Producto: ").append(mov.getNombreProducto()).append("\n");
        detalle.append("Cantidad anterior: ").append(mov.getCantidadAnterior()).append(" unidades\n");
        detalle.append("Cantidad nueva: ").append(mov.getCantidadNueva()).append(" unidades\n");
        detalle.append("Cantidad movida: ").append(mov.getCantidadMovida()).append(" unidades\n");
        detalle.append("Fecha: ").append(mov.getFechaMovimiento()).append("\n");
        
        if (mov.getObservaciones() != null && !mov.getObservaciones().isEmpty()) {
            detalle.append("Observaciones: ").append(mov.getObservaciones()).append("\n");
        }
        
        jTextArea_detalle.setText(detalle.toString());
    }
}