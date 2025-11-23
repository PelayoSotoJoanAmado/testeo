package vista;

import conexion.Conexion;
import controlador.Ctrl_Producto;
import controlador.Ctrl_StockManager;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import modelo.Producto;

public class InterActualizarStock extends javax.swing.JInternalFrame {

    //variables
    int idProducto = 0;
    int cantidad = 0;
    private Ctrl_StockManager stockManager;

    public InterActualizarStock() {
        initComponents();
        setTitle("Actualizar Stock de los Productos");
        setSize(new Dimension(500, 400));

        this.stockManager = new Ctrl_StockManager();
        this.CargarComboProductos();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txt_cantidad_actual = new javax.swing.JTextField();
        txt_cantidad_nueva = new javax.swing.JTextField();
        jComboBox_producto = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        jButton_ver_info = new javax.swing.JButton();
        jPanel_info = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_info = new javax.swing.JTextArea();
        jLabel_wallpaper = new javax.swing.JLabel();

        setClosable(true);
        setIconifiable(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Actualizar Stock de Productos");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, -1, -1));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Producto:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 110, -1));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Stock Actual:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 110, -1));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 14));
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText("Cantidad a Agregar:");
        getContentPane().add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 130, -1));

        txt_cantidad_actual.setFont(new java.awt.Font("Tahoma", 1, 14));
        txt_cantidad_actual.setEnabled(false);
        getContentPane().add(txt_cantidad_actual, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, 170, -1));

        txt_cantidad_nueva.setFont(new java.awt.Font("Tahoma", 0, 14));
        txt_cantidad_nueva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_cantidad_nuevaKeyReleased(evt);
            }
        });
        getContentPane().add(txt_cantidad_nueva, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 170, -1));

        jComboBox_producto.setFont(new java.awt.Font("Tahoma", 0, 14));
        jComboBox_producto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione producto:", "Item 2", "Item 3", "Item 4" }));
        jComboBox_producto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_productoActionPerformed(evt);
            }
        });
        getContentPane().add(jComboBox_producto, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 170, -1));

        jButton1.setBackground(new java.awt.Color(0, 255, 0));
        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14));
        jButton1.setText("Actualizar Stock");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 180, 170, 30));

        jButton_ver_info.setBackground(new java.awt.Color(0, 153, 255));
        jButton_ver_info.setFont(new java.awt.Font("Tahoma", 1, 12));
        jButton_ver_info.setText("Ver Info");
        jButton_ver_info.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ver_infoActionPerformed(evt);
            }
        });
        getContentPane().add(jButton_ver_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 100, 90, -1));

        jPanel_info.setBackground(new java.awt.Color(255, 255, 255));
        jPanel_info.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Información de Stock", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12)));
        jPanel_info.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextArea_info.setEditable(false);
        jTextArea_info.setColumns(20);
        jTextArea_info.setFont(new java.awt.Font("Monospaced", 0, 11));
        jTextArea_info.setRows(5);
        jScrollPane1.setViewportView(jTextArea_info);

        jPanel_info.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 440, 100));

        getContentPane().add(jPanel_info, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 460, 130));

        jLabel_wallpaper.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/fondo3.jpg")));
        getContentPane().add(jLabel_wallpaper, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        pack();
    }// </editor-fold>

    private void jComboBox_productoActionPerformed(java.awt.event.ActionEvent evt) {
        this.MostrarStock();
        this.actualizarInfoStock();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        //validamos seleccion del producto
        if (!jComboBox_producto.getSelectedItem().equals("Seleccione producto:")) {
            //Validamos campos vacios
            if (!txt_cantidad_nueva.getText().isEmpty()) {
                //validamos que el usuario no ingrese otros caracteres no numericos
                boolean validacion = validar(txt_cantidad_nueva.getText().trim());
                if (validacion == true) {
                    //validar que la cantidad sea mayor cero (0)
                    if (Integer.parseInt(txt_cantidad_nueva.getText()) > 0) {

                        Producto producto = new Producto();
                        Ctrl_Producto controlProducto = new Ctrl_Producto();
                        int stockActual = Integer.parseInt(txt_cantidad_actual.getText().trim());
                        int cantidadAAgregar = Integer.parseInt(txt_cantidad_nueva.getText().trim());
                        int stockNuevo = stockActual + cantidadAAgregar;

                        // *** NUEVA VALIDACIÓN DE LÍMITE DE STOCK ***
                        if (!stockManager.verificarLimiteStock(idProducto, cantidadAAgregar)) {
                            JOptionPane.showMessageDialog(null, 
                                "⚠️ ERROR: Se excede el límite máximo de inventario\n\n" + 
                                stockManager.obtenerInfoStock(idProducto),
                                "Límite de Stock Excedido",
                                JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        producto.setCantidad(stockNuevo);
                        if (controlProducto.actualizarStock(producto, idProducto)) {
                            // Verificar MANUALMENTE si necesita notificación
                            boolean necesitaNotificacion = stockManager.verificarStockBajo(idProducto);
                            
                            String mensaje = "✅ Stock Actualizado Correctamente\n" +
                                "Cantidad anterior: " + stockActual + "\n" +
                                "Cantidad agregada: +" + cantidadAAgregar + "\n" +
                                "Nueva cantidad: " + stockNuevo;
                            
                            if (necesitaNotificacion) {
                                mensaje += "\n\n⚠️ ALERTA: Stock bajo detectado\n" +
                                          "Se ha generado una notificación de reabastecimiento";
                            }
                            
                            JOptionPane.showMessageDialog(null, mensaje);
                            
                            jComboBox_producto.setSelectedItem("Seleccione producto:");
                            txt_cantidad_actual.setText("");
                            txt_cantidad_nueva.setText("");
                            jTextArea_info.setText("");
                            this.CargarComboProductos();
                        } else {
                            JOptionPane.showMessageDialog(null, "Error al Actualizar Stock");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "La cantidad no puede ser cero ni negativa");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "En la cantidad no se admiten caracteres no numericos");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ingrese una cantidad para agregar al stock del producto");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un producto");
        }
    }

    private void jButton_ver_infoActionPerformed(java.awt.event.ActionEvent evt) {
        if (idProducto > 0) {
            String info = stockManager.obtenerInfoStock(idProducto);
            jTextArea_info.setText(info);
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto primero");
        }
    }

    private void txt_cantidad_nuevaKeyReleased(java.awt.event.KeyEvent evt) {
        // Calcular preview del nuevo stock en tiempo real
        if (!txt_cantidad_actual.getText().isEmpty() && !txt_cantidad_nueva.getText().isEmpty()) {
            try {
                int actual = Integer.parseInt(txt_cantidad_actual.getText());
                int agregar = Integer.parseInt(txt_cantidad_nueva.getText());
                int nuevo = actual + agregar;
                
                // Mostrar preview en el área de información
                StringBuilder preview = new StringBuilder();
                preview.append("VISTA PREVIA:\n");
                preview.append("════════════════════════════\n");
                preview.append("Stock actual: ").append(actual).append("\n");
                preview.append("Cantidad a agregar: +").append(agregar).append("\n");
                preview.append("Nuevo stock: ").append(nuevo).append("\n\n");
                
                // Verificar si excede el límite
                if (!stockManager.verificarLimiteStock(idProducto, agregar)) {
                    preview.append("⚠️ ADVERTENCIA: Excede el límite máximo!\n");
                } else {
                    preview.append("✓ Cantidad válida\n");
                }
                
                jTextArea_info.setText(preview.toString());
            } catch (NumberFormatException e) {
                // Ignorar si no es un número válido
            }
        }
    }

    // Variables declaration
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton_ver_info;
    private javax.swing.JComboBox<String> jComboBox_producto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel_wallpaper;
    private javax.swing.JPanel jPanel_info;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea_info;
    private javax.swing.JTextField txt_cantidad_actual;
    private javax.swing.JTextField txt_cantidad_nueva;
    // End of variables declaration

    //Metodo para caragar los productos en el jComboBox
    private void CargarComboProductos() {
        Connection cn = Conexion.conectar();
        String sql = "select * from tb_producto";
        Statement st;
        try {
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            jComboBox_producto.removeAllItems();
            jComboBox_producto.addItem("Seleccione producto:");
            while (rs.next()) {
                jComboBox_producto.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar los productos en: " + e);
        }
    }

    //metodo para mostrar stock del producto seleccionado
    private void MostrarStock() {
        try {
            Connection cn = Conexion.conectar();
            String sql = "select * from tb_producto where nombre = '" + this.jComboBox_producto.getSelectedItem() + "'";
            Statement st;
            st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                idProducto = rs.getInt("idProducto");
                cantidad = rs.getInt("cantidad");
                txt_cantidad_actual.setText(String.valueOf(cantidad));
            } else {
                txt_cantidad_actual.setText("");
                idProducto = 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener stock del producto en: " + e);
        }
    }

    //metodo de validacion de caracteres no numericos
    private boolean validar(String valor) {
        int num;
        try {
            num = Integer.parseInt(valor);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    //metodo para actualizar información de stock en el área de texto
    private void actualizarInfoStock() {
        if (idProducto > 0) {
            String info = stockManager.obtenerInfoStock(idProducto);
            jTextArea_info.setText(info);
        }
    }
}