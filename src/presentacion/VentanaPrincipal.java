package presentacion;

import entidades.RegistroParqueo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import logica.ParqueoService;

public class VentanaPrincipal extends JFrame {

    private static final Color COLOR_FONDO = new Color(239, 244, 248);
    private static final Color COLOR_TARJETA = Color.WHITE;
    private static final Color COLOR_BORDE = new Color(210, 220, 228);
    private static final Color COLOR_PRIMARIO = new Color(18, 62, 92);
    private static final Color COLOR_SECUNDARIO = new Color(73, 118, 146);
    private static final Color COLOR_EXITO = new Color(46, 125, 90);
    private static final Color COLOR_ADVERTENCIA = new Color(196, 118, 37);
    private static final Color COLOR_PELIGRO = new Color(170, 63, 63);
    private static final Color COLOR_TEXTO = new Color(45, 57, 68);
    private static final Color COLOR_SUBTEXTO = new Color(96, 108, 120);
    private final JTextField txtPlaca;
    private final JTextField txtMinutosSalida;
    private final JComboBox<String> cmbTipoVehiculo;
    private final JLabel lblEstado;
    private final JLabel lblSeleccionActivo;
    private final JLabel lblSeleccionHistorial;
    private final JLabel lblMontoSalida;
    private final JLabel lblResumenActivos;
    private final JLabel lblResumenHistorial;
    private final JTable tblVehiculosActivos;
    private final JTable tblHistorial;
    private final DefaultTableModel modeloActivos;
    private final DefaultTableModel modeloHistorial;
    private ParqueoService parqueoService;
    private int idRegistroSeleccionado;
    private int idHistorialSeleccionado;

    public VentanaPrincipal() {
        this.txtPlaca = new JTextField(16);
        this.txtMinutosSalida = new JTextField(16);
        this.cmbTipoVehiculo = new JComboBox<>(new String[]{
            "Seleccione un tipo", "Carro", "Moto"
        });
        this.lblEstado = new JLabel("Sistema listo para registrar ingresos, salidas y gestionar historial.");
        this.lblSeleccionActivo = new JLabel("Vehiculo activo seleccionado: ninguno");
        this.lblSeleccionHistorial = new JLabel("Registro historico seleccionado: ninguno");
        this.lblMontoSalida = new JLabel("Monto calculado en la ultima salida: no disponible");
        this.lblResumenActivos = new JLabel("Activos: 0");
        this.lblResumenHistorial = new JLabel("Historial: 0");
        this.modeloActivos = new DefaultTableModel(
                new Object[]{"ID", "Placa", "Tipo", "Fecha", "Hora", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.modeloHistorial = new DefaultTableModel(
                new Object[]{"ID", "Placa", "Tipo", "Entrada", "Salida", "Minutos", "Monto", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tblVehiculosActivos = new JTable(modeloActivos);
        this.tblHistorial = new JTable(modeloHistorial);
        this.idRegistroSeleccionado = -1;
        this.idHistorialSeleccionado = -1;

        inicializarServicio();
        inicializarVentana();
        cargarDatos();
    }

    private void inicializarServicio() {
        try {
            parqueoService = new ParqueoService();
        } catch (IOException ex) {
            mostrarMensaje("No fue posible iniciar el acceso a datos.", false);
        }
    }

    private void inicializarVentana() {
        setTitle("Parqueo Publico - Gestion de Ingresos y Salidas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 680);
        setMinimumSize(new Dimension(980, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(16, 16));
        getContentPane().setBackground(COLOR_FONDO);

        add(construirEncabezado(), BorderLayout.NORTH);
        add(construirContenidoCentral(), BorderLayout.CENTER);
        add(construirPie(), BorderLayout.SOUTH);
    }

    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARIO);
        panel.setBorder(BorderFactory.createEmptyBorder(22, 28, 22, 28));

        JLabel lblTitulo = new JLabel("Sistema de Parqueo Publico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Version 1.3 - Panel de control del parqueo");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(214, 231, 242));

        JLabel lblDescripcion = new JLabel("Registro de ingresos, salidas, cobros e historial desde una sola ventana.");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescripcion.setForeground(new Color(194, 215, 230));

        JPanel texto = new JPanel(new BorderLayout());
        texto.setOpaque(false);
        texto.add(lblTitulo, BorderLayout.NORTH);
        texto.add(lblSubtitulo, BorderLayout.CENTER);
        texto.add(lblDescripcion, BorderLayout.SOUTH);

        panel.add(texto, BorderLayout.WEST);
        return panel;
    }

    private JPanel construirContenidoCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(16, 16));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 16, 0, 16));
        panelCentral.add(construirPanelFormulario(), BorderLayout.WEST);
        panelCentral.add(construirPanelTablas(), BorderLayout.CENTER);
        return panelCentral;
    }

    private JPanel construirPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(COLOR_TARJETA);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(22, 22, 22, 22)
        ));
        panelFormulario.setPreferredSize(new Dimension(340, 520));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        JLabel lblSeccion = new JLabel("Panel de acciones");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSeccion.setForeground(COLOR_TEXTO);
        panelFormulario.add(lblSeccion, gbc);

        gbc.gridy++;
        JLabel lblPlaca = new JLabel("Placa");
        lblPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblPlaca.setForeground(COLOR_TEXTO);
        panelFormulario.add(lblPlaca, gbc);

        gbc.gridy++;
        txtPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPlaca.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(196, 206, 214)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelFormulario.add(txtPlaca, gbc);

        gbc.gridy++;
        JLabel lblTipo = new JLabel("Tipo de vehiculo");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTipo.setForeground(COLOR_TEXTO);
        panelFormulario.add(lblTipo, gbc);

        gbc.gridy++;
        cmbTipoVehiculo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cmbTipoVehiculo.setBackground(Color.WHITE);
        panelFormulario.add(cmbTipoVehiculo, gbc);

        gbc.gridy++;
        JLabel lblAyuda = new JLabel("<html>La entrada se registra automaticamente. Para la salida, selecciona un vehiculo e indica los minutos de permanencia.</html>");
        lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAyuda.setForeground(COLOR_SUBTEXTO);
        panelFormulario.add(lblAyuda, gbc);

        gbc.gridy++;
        JButton btnRegistrar = new JButton("Registrar ingreso");
        aplicarEstiloBoton(btnRegistrar, COLOR_EXITO);
        btnRegistrar.addActionListener(e -> registrarIngreso());
        panelFormulario.add(btnRegistrar, gbc);

        gbc.gridy++;
        JLabel lblMinutosSalida = new JLabel("Minutos de permanencia");
        lblMinutosSalida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMinutosSalida.setForeground(COLOR_TEXTO);
        panelFormulario.add(lblMinutosSalida, gbc);

        gbc.gridy++;
        txtMinutosSalida.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMinutosSalida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(196, 206, 214)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        panelFormulario.add(txtMinutosSalida, gbc);

        gbc.gridy++;
        JButton btnSalida = new JButton("Registrar salida");
        aplicarEstiloBoton(btnSalida, COLOR_ADVERTENCIA);
        btnSalida.addActionListener(e -> registrarSalida());
        panelFormulario.add(btnSalida, gbc);

        gbc.gridy++;
        JButton btnEliminarHistorial = new JButton("Eliminar historial");
        aplicarEstiloBoton(btnEliminarHistorial, COLOR_PELIGRO);
        btnEliminarHistorial.addActionListener(e -> eliminarRegistroHistorial());
        panelFormulario.add(btnEliminarHistorial, gbc);

        gbc.gridy++;
        lblSeleccionActivo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSeleccionActivo.setForeground(COLOR_SECUNDARIO);
        panelFormulario.add(lblSeleccionActivo, gbc);

        gbc.gridy++;
        lblSeleccionHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSeleccionHistorial.setForeground(COLOR_SECUNDARIO);
        panelFormulario.add(lblSeleccionHistorial, gbc);

        gbc.gridy++;
        lblMontoSalida.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblMontoSalida.setForeground(COLOR_ADVERTENCIA);
        lblMontoSalida.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(235, 214, 188)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        panelFormulario.add(lblMontoSalida, gbc);

        gbc.gridy++;
        gbc.weighty = 1.0;
        JPanel espacio = new JPanel();
        espacio.setOpaque(false);
        panelFormulario.add(espacio, gbc);

        return panelFormulario;
    }

    private JPanel construirPanelTablas() {
        JPanel panelTablas = new JPanel(new BorderLayout(10, 10));
        panelTablas.setBackground(COLOR_TARJETA);
        panelTablas.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDE),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setOpaque(false);

        JLabel lblTabla = new JLabel("Vista general del parqueo");
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTabla.setForeground(COLOR_TEXTO);

        JPanel resumen = new JPanel(new GridLayout(1, 2, 12, 0));
        resumen.setOpaque(false);
        resumen.add(crearTarjetaResumen(lblResumenActivos, new Color(230, 240, 247), COLOR_PRIMARIO));
        resumen.add(crearTarjetaResumen(lblResumenHistorial, new Color(247, 239, 228), COLOR_ADVERTENCIA));

        encabezado.add(lblTabla, BorderLayout.WEST);
        encabezado.add(resumen, BorderLayout.EAST);

        tblVehiculosActivos.setRowHeight(24);
        tblVehiculosActivos.getTableHeader().setReorderingAllowed(false);
        tblVehiculosActivos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblVehiculosActivos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblVehiculosActivos.setGridColor(new Color(229, 235, 239));
        tblVehiculosActivos.setSelectionBackground(new Color(218, 232, 242));
        tblVehiculosActivos.setSelectionForeground(COLOR_TEXTO);
        tblVehiculosActivos.getTableHeader().setBackground(new Color(230, 238, 244));
        tblVehiculosActivos.getTableHeader().setForeground(COLOR_TEXTO);
        tblVehiculosActivos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarVehiculoActivo();
            }
        });

        tblHistorial.setRowHeight(24);
        tblHistorial.getTableHeader().setReorderingAllowed(false);
        tblHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tblHistorial.setGridColor(new Color(229, 235, 239));
        tblHistorial.setSelectionBackground(new Color(243, 231, 214));
        tblHistorial.setSelectionForeground(COLOR_TEXTO);
        tblHistorial.getTableHeader().setBackground(new Color(230, 238, 244));
        tblHistorial.getTableHeader().setForeground(COLOR_TEXTO);
        tblHistorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seleccionarRegistroHistorial();
            }
        });

        JScrollPane scrollActivos = new JScrollPane(tblVehiculosActivos);
        scrollActivos.setBorder(BorderFactory.createLineBorder(new Color(214, 223, 230)));

        JScrollPane scrollHistorial = new JScrollPane(tblHistorial);
        scrollHistorial.setBorder(BorderFactory.createLineBorder(new Color(214, 223, 230)));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pestanas.addTab("Vehiculos activos", scrollActivos);
        pestanas.addTab("Historial", scrollHistorial);

        panelTablas.add(encabezado, BorderLayout.NORTH);
        panelTablas.add(pestanas, BorderLayout.CENTER);
        return panelTablas;
    }

    private JPanel construirPie() {
        JPanel panelPie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPie.setBackground(new Color(245, 248, 250));
        panelPie.setBorder(BorderFactory.createEmptyBorder(6, 16, 12, 16));

        lblEstado.setHorizontalAlignment(SwingConstants.LEFT);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEstado.setForeground(COLOR_EXITO);

        panelPie.add(lblEstado);
        return panelPie;
    }

    private void registrarIngreso() {
        if (parqueoService == null) {
            mostrarMensaje("El servicio no se encuentra disponible.", false);
            return;
        }

        String placa = txtPlaca.getText();
        String tipoSeleccionado = cmbTipoVehiculo.getSelectedIndex() <= 0
                ? ""
                : cmbTipoVehiculo.getSelectedItem().toString();

        try {
            parqueoService.registrarIngreso(placa, tipoSeleccionado);
            mostrarMensaje("Ingreso registrado correctamente para la placa "
                    + placa.trim().toUpperCase() + ".", true);
            limpiarFormulario();
            cargarDatos();
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), false);
        } catch (IOException ex) {
            mostrarMensaje("Ocurrio un problema al guardar la informacion.", false);
        }
    }

    private void registrarSalida() {
        if (parqueoService == null) {
            mostrarMensaje("El servicio no se encuentra disponible.", false);
            return;
        }

        try {
            int minutosPermanencia = Integer.parseInt(txtMinutosSalida.getText().trim());
            RegistroParqueo registroSalida = parqueoService.registrarSalida(idRegistroSeleccionado, minutosPermanencia);
            lblMontoSalida.setText("Tiempo: " + registroSalida.getMinutosTotales()
                    + " minutos. Monto: " + formatearColones(registroSalida.getMontoPagado()));
            mostrarMensaje("Salida registrada para la placa " + registroSalida.getVehiculo().getPlaca()
                    + ". Monto a pagar: " + formatearColones(registroSalida.getMontoPagado()) + ".", true);
            txtMinutosSalida.setText("");
            cargarDatos();
        } catch (NumberFormatException ex) {
            mostrarMensaje("Debe indicar los minutos de permanencia con un numero entero.", false);
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), false);
        } catch (IOException ex) {
            mostrarMensaje("Ocurrio un problema al procesar la salida.", false);
        }
    }

    private void eliminarRegistroHistorial() {
        if (parqueoService == null) {
            mostrarMensaje("El servicio no se encuentra disponible.", false);
            return;
        }

        try {
            parqueoService.eliminarRegistroHistorial(idHistorialSeleccionado);
            mostrarMensaje("El registro historico seleccionado fue eliminado correctamente.", true);
            cargarDatos();
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), false);
        } catch (IOException ex) {
            mostrarMensaje("Ocurrio un problema al eliminar el registro historico.", false);
        }
    }

    private void cargarDatos() {
        limpiarTablas();
        limpiarSeleccionActivo();
        limpiarSeleccionHistorial();

        if (parqueoService == null) {
            return;
        }

        try {
            List<RegistroParqueo> registrosActivos = parqueoService.obtenerRegistrosActivos();
            List<RegistroParqueo> historial = parqueoService.obtenerRegistrosHistorial();

            for (RegistroParqueo registro : registrosActivos) {
                modeloActivos.addRow(new Object[]{
                    registro.getIdRegistro(),
                    registro.getVehiculo().getPlaca(),
                    registro.getVehiculo().getTipo(),
                    registro.getFechaEntrada(),
                    registro.getHoraEntrada(),
                    registro.getEstado()
                });
            }

            for (RegistroParqueo registro : historial) {
                modeloHistorial.addRow(new Object[]{
                    registro.getIdRegistro(),
                    registro.getVehiculo().getPlaca(),
                    registro.getVehiculo().getTipo(),
                    registro.getFechaEntrada() + " " + registro.getHoraEntrada(),
                    registro.getFechaSalida() + " " + registro.getHoraSalida(),
                    registro.getMinutosTotales(),
                    formatearColones(registro.getMontoPagado()),
                    registro.getEstado()
                });
            }

            lblResumenActivos.setText("Activos: " + registrosActivos.size());
            lblResumenHistorial.setText("Historial: " + historial.size());
        } catch (IOException ex) {
            mostrarMensaje("No fue posible cargar la informacion del sistema.", false);
        }
    }

    private void limpiarTablas() {
        modeloActivos.setRowCount(0);
        modeloHistorial.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtPlaca.setText("");
        cmbTipoVehiculo.setSelectedIndex(0);
        txtPlaca.requestFocusInWindow();
    }

    private void seleccionarVehiculoActivo() {
        int filaSeleccionada = tblVehiculosActivos.getSelectedRow();

        if (filaSeleccionada >= 0) {
            idRegistroSeleccionado = Integer.parseInt(
                    modeloActivos.getValueAt(filaSeleccionada, 0).toString());
            String placa = modeloActivos.getValueAt(filaSeleccionada, 1).toString();
            lblSeleccionActivo.setText("Vehiculo activo seleccionado: " + placa
                    + " (ID " + idRegistroSeleccionado + ")");
        }
    }

    private void seleccionarRegistroHistorial() {
        int filaSeleccionada = tblHistorial.getSelectedRow();

        if (filaSeleccionada >= 0) {
            idHistorialSeleccionado = Integer.parseInt(
                    modeloHistorial.getValueAt(filaSeleccionada, 0).toString());
            String placa = modeloHistorial.getValueAt(filaSeleccionada, 1).toString();
            lblSeleccionHistorial.setText("Registro historico seleccionado: " + placa
                    + " (ID " + idHistorialSeleccionado + ")");
        }
    }

    private void limpiarSeleccionActivo() {
        idRegistroSeleccionado = -1;
        tblVehiculosActivos.clearSelection();
        lblSeleccionActivo.setText("Vehiculo activo seleccionado: ninguno");
    }

    private void limpiarSeleccionHistorial() {
        idHistorialSeleccionado = -1;
        tblHistorial.clearSelection();
        lblSeleccionHistorial.setText("Registro historico seleccionado: ninguno");
    }

    private void mostrarMensaje(String mensaje, boolean exito) {
        lblEstado.setText(mensaje);
        lblEstado.setForeground(exito ? COLOR_EXITO : COLOR_PELIGRO);
    }

    private String formatearColones(double monto) {
        return "CRC " + String.format("%.0f", monto);
    }

    private void aplicarEstiloBoton(JButton boton, Color colorFondo) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setBackground(colorFondo);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JPanel crearTarjetaResumen(JLabel etiqueta, Color fondo, Color colorTexto) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        panel.setBackground(fondo);
        panel.setBorder(BorderFactory.createLineBorder(new Color(214, 223, 230)));
        etiqueta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiqueta.setForeground(colorTexto);
        panel.add(etiqueta);
        return panel;
    }
}
