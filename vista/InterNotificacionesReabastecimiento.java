package vista;

import controlador.Ctrl_StockManager;
import modelo.NotificacionReabastecimiento;
import java.awt.Dimension;
import javax.swing.*;
import java.awt.*;

public class InterNotificacionesReabastecimiento extends javax.swing.JInternalFrame {
    
    private Ctrl_StockManager stockManager;
    private DefaultListModel<String> modeloLista;

    public InterNotificacionesReabastecimiento() {
        initComponents();
        this.setSize(new Dimension(600, 500));
        this.setTitle("Notificaciones de Reabastecimiento");
        
        stockManager = new Ctrl_StockManager();
        modeloLista = new DefaultListModel<>();
        cargarNotificaciones();
        actualizarResumen();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_notificaciones = new javax.swing.JList<>();
        jPanel2 = new javax.swing.JPanel();
        jButton_ver_siguiente = new javax.swing.JButton();
        jButton_procesar = new javax.swing.JButton();
        jButton_actualizar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea_resumen = new javax.swing.JTextArea();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Cola de Notificaciones de Reabastecimiento");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, -1, -1));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Notificaciones Pendientes (Cola FIFO)", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jList_notificaciones.setFont(new java.awt.Font("Monospaced", 0, 11));
        jScrollPane1.setViewportView(jList_notificaciones);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 540, 150));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 560, 180));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton_ver_siguiente.setBackground(new java.awt.Color(0, 153, 255));
        jButton_ver_siguiente.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_ver_siguiente.setText("Ver Siguiente");
        jButton_ver_siguiente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ver_siguienteActionPerformed(evt);
            }
        });
        jPanel2.add(jButton_ver_siguiente, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 150, 30));

        jButton_procesar.setBackground(new java.awt.Color(51, 204, 0));
        jButton_procesar.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_procesar.setText("Procesar");
        jButton_procesar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_procesarActionPerformed(evt);
            }
        });
        jPanel2.add(jButton_procesar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 150, 30));

        jButton_actualizar.setBackground(new java.awt.Color(255, 153, 0));
        jButton_actualizar.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_actualizar.setText("Actualizar Lista");
        jButton_actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_actualizarActionPerformed(evt);
            }
        });
        jPanel2.add(jButton_actualizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 90, 150, 30));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 170, 130));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Resumen de Inventario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea_resumen.setEditable(false);
        jTextArea_resumen.setColumns(20);
        jTextArea_resumen.setFont(new java.awt.Font("Monospaced", 0, 12));
        jTextArea_resumen.setRows(5);
        jScrollPane2.setViewportView(jTextArea_resumen);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 360, 100));

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 240, 380, 130));

        jLabel_wallpaper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondo3.jpg")));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 590, 470));

        pack();
    }// </editor-fold>

    private void jButton_ver_siguienteActionPerformed(java.awt.event.ActionEvent evt) {
        NotificacionReabastecimiento siguiente = stockManager.verSiguienteNotificacion();
        if (siguiente != null) {
            String mensaje = String.format(
                "Siguiente Notificación:\n\n" +
                "Producto: %s\n" +
                "Stock Actual: %d unidades\n" +
                "Stock Mínimo: %d unidades\n" +
                "Cantidad Sugerida: %d unidades\n" +
                "Prioridad: %s\n\n" +
                "¿Desea procesar esta notificación?",
                siguiente.getNombreProducto(),
                siguiente.getCantidadActual(),
                siguiente.getStockMinimo(),
                siguiente.getCantidadSugerida(),
                siguiente.getPrioridad() == 2 ? "URGENTE" : "Normal"
            );
            
            JOptionPane.showMessageDialog(this, mensaje, "Siguiente en Cola", 
                                         JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No hay notificaciones pendientes", 
                                         "Cola Vacía", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void jButton_procesarActionPerformed(java.awt.event.ActionEvent evt) {
        NotificacionReabastecimiento notificacion = stockManager.obtenerSiguienteNotificacion();
        if (notificacion != null) {
            int opcion = JOptionPane.showConfirmDialog(this, 
                String.format("¿Procesar reabastecimiento de %s?\nCantidad sugerida: %d unidades", 
                    notificacion.getNombreProducto(), notificacion.getCantidadSugerida()),
                "Confirmar Procesamiento", 
                JOptionPane.YES_NO_OPTION);
            
            if (opcion == JOptionPane.YES_OPTION) {
                stockManager.procesarNotificacion(notificacion.getIdNotificacion());
                JOptionPane.showMessageDialog(this, "Notificación procesada correctamente");
                cargarNotificaciones();
                actualizarResumen();
            } else {
                // Si no se procesa, volver a agregar a la cola
                stockManager.agregarNotificacionReabastecimiento(notificacion.getIdProducto());
            }
        } else {
            JOptionPane.showMessageDialog(this, "No hay notificaciones para procesar");
        }
    }

    private void jButton_actualizarActionPerformed(java.awt.event.ActionEvent evt) {
        cargarNotificaciones();
        actualizarResumen();
        JOptionPane.showMessageDialog(this, "Lista actualizada");
    }

    // Variables declaration
    private javax.swing.JButton jButton_actualizar;
    private javax.swing.JButton jButton_procesar;
    private javax.swing.JButton jButton_ver_siguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel_wallpaper;
    private javax.swing.JList<String> jList_notificaciones;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea_resumen;
    // End of variables declaration

    private void cargarNotificaciones() {
        modeloLista.clear();
        
        // Crear una nueva instancia para obtener datos frescos
        Ctrl_StockManager tempManager = new Ctrl_StockManager();
        
        // Recargar el manager principal
        stockManager = new Ctrl_StockManager();
        
        int cantidadTotal = tempManager.obtenerCantidadNotificaciones();
        
        if (cantidadTotal == 0) {
            modeloLista.addElement("No hay notificaciones pendientes");
        } else {
            // Crear lista temporal
            java.util.List<NotificacionReabastecimiento> tempList = new java.util.ArrayList<>();
            
            while (tempManager.obtenerCantidadNotificaciones() > 0) {
                NotificacionReabastecimiento notif = tempManager.obtenerSiguienteNotificacion();
                if (notif != null) {
                    tempList.add(notif);
                }
            }
            
            // Mostrar en la lista
            for (int i = 0; i < tempList.size(); i++) {
                NotificacionReabastecimiento notif = tempList.get(i);
                String prioridad = notif.getPrioridad() == 2 ? "[URGENTE]" : "[Normal]";
                String linea = String.format("%d. %s %s - Stock: %d/%d - Sugerido: +%d",
                    i + 1,
                    prioridad,
                    notif.getNombreProducto(),
                    notif.getCantidadActual(),
                    notif.getStockMinimo(),
                    notif.getCantidadSugerida()
                );
                modeloLista.addElement(linea);
            }
        }
        
        jList_notificaciones.setModel(modeloLista);
    }
    
    private void actualizarResumen() {
        String resumen = stockManager.obtenerResumenInventario();
        jTextArea_resumen.setText(resumen);
    }
}