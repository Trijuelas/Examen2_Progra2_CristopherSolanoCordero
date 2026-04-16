package presentacion;

import entidades.RegistroParqueo;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import logica.ParqueoService;

public class VentanaPrincipal extends JFrame {

    private final JTextField txtPlaca;
    private final JComboBox<String> cmbTipoVehiculo;
    private final JLabel lblEstado;
    private final JTable tblVehiculosActivos;
    private final DefaultTableModel modeloTabla;
    private ParqueoService parqueoService;

    public VentanaPrincipal() {
        this.txtPlaca = new JTextField(16);
        this.cmbTipoVehiculo = new JComboBox<>(new String[]{
            "Seleccione un tipo", "Carro", "Moto"
        });
        this.lblEstado = new JLabel("Sistema listo para registrar ingresos.");
        this.modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Placa", "Tipo", "Fecha", "Hora", "Estado"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        this.tblVehiculosActivos = new JTable(modeloTabla);

        inicializarServicio();
        inicializarVentana();
        cargarRegistrosActivos();
    }

    private void inicializarServicio() {
        try {
            parqueoService = new ParqueoService();
        } catch (IOException ex) {
            mostrarMensaje("No fue posible iniciar el acceso a datos.", false);
        }
    }

    private void inicializarVentana() {
        setTitle("Parqueo Publico - Registro de Ingresos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 560);
        setMinimumSize(new Dimension(820, 500));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(14, 14));
        getContentPane().setBackground(new Color(236, 242, 247));

        add(construirEncabezado(), BorderLayout.NORTH);
        add(construirContenidoCentral(), BorderLayout.CENTER);
        add(construirPie(), BorderLayout.SOUTH);
    }

    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 72, 106));
        panel.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel lblTitulo = new JLabel("Sistema de Parqueo Publico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Version 1.0 - Registro de ingreso de vehiculos");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(214, 231, 242));

        JPanel texto = new JPanel(new BorderLayout());
        texto.setOpaque(false);
        texto.add(lblTitulo, BorderLayout.NORTH);
        texto.add(lblSubtitulo, BorderLayout.SOUTH);

        panel.add(texto, BorderLayout.WEST);
        return panel;
    }

    private JPanel construirContenidoCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout(14, 14));
        panelCentral.setOpaque(false);
        panelCentral.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));

        panelCentral.add(construirPanelFormulario(), BorderLayout.WEST);
        panelCentral.add(construirPanelTabla(), BorderLayout.CENTER);

        return panelCentral;
    }

    private JPanel construirPanelFormulario() {
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(202, 213, 222)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        panelFormulario.setPreferredSize(new Dimension(300, 380));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.weightx = 1.0;

        JLabel lblSeccion = new JLabel("Registrar ingreso");
        lblSeccion.setFont(new Font("Segoe UI", Font.BOLD, 18));
        panelFormulario.add(lblSeccion, gbc);

        gbc.gridy++;
        JLabel lblPlaca = new JLabel("Placa");
        lblPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFormulario.add(lblPlaca, gbc);

        gbc.gridy++;
        txtPlaca.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFormulario.add(txtPlaca, gbc);

        gbc.gridy++;
        JLabel lblTipo = new JLabel("Tipo de vehiculo");
        lblTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFormulario.add(lblTipo, gbc);

        gbc.gridy++;
        cmbTipoVehiculo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panelFormulario.add(cmbTipoVehiculo, gbc);

        gbc.gridy++;
        JLabel lblAyuda = new JLabel("<html>La hora de entrada se registra automaticamente.</html>");
        lblAyuda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblAyuda.setForeground(new Color(88, 103, 117));
        panelFormulario.add(lblAyuda, gbc);

        gbc.gridy++;
        JButton btnRegistrar = new JButton("Registrar ingreso");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrar.setBackground(new Color(47, 127, 92));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.addActionListener(e -> registrarIngreso());
        panelFormulario.add(btnRegistrar, gbc);

        gbc.weighty = 1.0;
        JPanel espacio = new JPanel();
        espacio.setOpaque(false);
        panelFormulario.add(espacio, gbc);

        return panelFormulario;
    }

    private JPanel construirPanelTabla() {
        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(202, 213, 222)),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));

        JLabel lblTabla = new JLabel("Vehiculos actualmente dentro del parqueo");
        lblTabla.setFont(new Font("Segoe UI", Font.BOLD, 18));

        tblVehiculosActivos.setRowHeight(24);
        tblVehiculosActivos.getTableHeader().setReorderingAllowed(false);
        tblVehiculosActivos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblVehiculosActivos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scrollTabla = new JScrollPane(tblVehiculosActivos);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(214, 223, 230)));

        panelTabla.add(lblTabla, BorderLayout.NORTH);
        panelTabla.add(scrollTabla, BorderLayout.CENTER);

        return panelTabla;
    }

    private JPanel construirPie() {
        JPanel panelPie = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelPie.setBackground(new Color(245, 248, 250));
        panelPie.setBorder(BorderFactory.createEmptyBorder(4, 14, 10, 14));

        lblEstado.setHorizontalAlignment(SwingConstants.LEFT);
        lblEstado.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEstado.setForeground(new Color(28, 95, 64));

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
            cargarRegistrosActivos();
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), false);
        } catch (IOException ex) {
            mostrarMensaje("Ocurrio un problema al guardar la informacion.", false);
        }
    }

    private void cargarRegistrosActivos() {
        limpiarTabla();

        if (parqueoService == null) {
            return;
        }

        try {
            List<RegistroParqueo> registrosActivos = parqueoService.obtenerRegistrosActivos();

            for (RegistroParqueo registro : registrosActivos) {
                modeloTabla.addRow(new Object[]{
                    registro.getIdRegistro(),
                    registro.getVehiculo().getPlaca(),
                    registro.getVehiculo().getTipo(),
                    registro.getFechaEntrada(),
                    registro.getHoraEntrada(),
                    registro.getEstado()
                });
            }
        } catch (IOException ex) {
            mostrarMensaje("No fue posible cargar la tabla de ingresos.", false);
        }
    }

    private void limpiarTabla() {
        modeloTabla.setRowCount(0);
    }

    private void limpiarFormulario() {
        txtPlaca.setText("");
        cmbTipoVehiculo.setSelectedIndex(0);
        txtPlaca.requestFocusInWindow();
    }

    private void mostrarMensaje(String mensaje, boolean exito) {
        lblEstado.setText(mensaje);
        lblEstado.setForeground(exito ? new Color(28, 95, 64) : new Color(170, 51, 51));
    }
}
