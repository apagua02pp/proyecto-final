package proyectoFinal.frontend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;
import javax.swing.SwingUtilities;

public class AdminPanel extends FondoPanel { // ✅ Hereda de FondoPanel
    private GestorEnciclopedia gestor;
    private JList<String> listaCivilizaciones;
    private DefaultListModel<String> listModel;
    private JLabel lblImagen;
    private JTextArea txtDetalles;

    public AdminPanel(GestorEnciclopedia gestor, Usuario usuario) {
        super(usuario); // ✅ Llama al constructor de FondoPanel
        this.gestor = gestor;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Título
        JLabel lblTitulo = new JLabel("🔧 Administración de Civilizaciones", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setOpaque(false);
        add(lblTitulo, BorderLayout.NORTH);

        // Panel principal dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        // Panel izquierdo: Búsqueda + Lista
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Civilizaciones"));
        panelIzquierdo.setOpaque(false);

        // Barra de búsqueda
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.setOpaque(false);
        panelBusqueda.add(new JLabel("Buscar por nombre:"));
        JTextField txtBusqueda = new JTextField(5);
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> {
            String query = txtBusqueda.getText().trim().toLowerCase();
            listModel.clear();
            for (Civilizacion c : gestor.getCivilizaciones()) {
                if (c.getNombre().toLowerCase().contains(query)) {
                    listModel.addElement(c.getNombre());
                }
            }
        });
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(btnBuscar);
        panelIzquierdo.add(panelBusqueda, BorderLayout.NORTH);

        // Lista
        listModel = new DefaultListModel<>();
        actualizarLista();
        listaCivilizaciones = new JList<>(listModel);
        listaCivilizaciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaCivilizaciones.setOpaque(false);
        listaCivilizaciones.setBackground(new Color(0, 0, 0, 0)); // Transparente
        listaCivilizaciones.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String sel = listaCivilizaciones.getSelectedValue();
                if (sel != null) {
                    Civilizacion c = gestor.buscarCivilizacion(sel);
                    mostrarDetalles(c);
                }
            }
        });

        JScrollPane scrollLista = new JScrollPane(listaCivilizaciones);
        scrollLista.setOpaque(false);
        scrollLista.getViewport().setOpaque(false);
        panelIzquierdo.add(scrollLista, BorderLayout.CENTER);

        // Panel derecho: Detalles
        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Detalles"));
        panelDerecho.setOpaque(false);

        // Imagen
        lblImagen = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblImagen.setBorder(BorderFactory.createEtchedBorder());
        lblImagen.setPreferredSize(new Dimension(200, 200));
        lblImagen.setOpaque(false);
        JPanel panelImagen = new JPanel(new BorderLayout());
        panelImagen.setBorder(BorderFactory.createTitledBorder("Imagen"));
        panelImagen.setOpaque(false);
        panelImagen.add(lblImagen, BorderLayout.CENTER);

        // Detalles
        txtDetalles = new JTextArea();
        txtDetalles.setEditable(false);
        txtDetalles.setLineWrap(true);
        txtDetalles.setWrapStyleWord(true);
        txtDetalles.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtDetalles.setOpaque(false);
        txtDetalles.setForeground(Color.BLACK);
        JScrollPane scrollDetalles = new JScrollPane(txtDetalles);
        scrollDetalles.setOpaque(false);
        scrollDetalles.getViewport().setOpaque(false);

        JPanel panelContenido = new JPanel(new GridLayout(2, 1, 5, 5));
        panelContenido.setOpaque(false);
        panelContenido.add(panelImagen);
        panelContenido.add(scrollDetalles);
        panelDerecho.add(panelContenido, BorderLayout.CENTER);

        // Botones inferiores: Agregar, Editar, Eliminar, Salir
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false);

        // Botón Agregar
        JButton btnAgregar = new JButton("➕ Agregar");
        btnAgregar.setBackground(new Color(76, 175, 80)); // Verde
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Botón Editar
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(new Color(255, 152, 0)); // Naranja
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Botón Eliminar
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(244, 67, 54)); // Rojo
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Botón Salir
        JButton btnSalir = new JButton("🚪 Salir");
        btnSalir.setBackground(new Color(158, 158, 158)); // Gris
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
        btnSalir.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        // Acciones
        btnAgregar.addActionListener(e -> abrirFormulario(null));
        btnEditar.addActionListener(e -> {
            String sel = listaCivilizaciones.getSelectedValue();
            if (sel != null) {
                Civilizacion c = gestor.buscarCivilizacion(sel);
                if (c != null) abrirFormulario(c);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una civilización para editar.");
            }
        });
        btnEliminar.addActionListener(e -> {
            String sel = listaCivilizaciones.getSelectedValue();
            if (sel != null) {
                int confirma = JOptionPane.showConfirmDialog(this,
                    "¿Eliminar la civilización '" + sel + "'?\nEsta acción no se puede deshacer.",
                    "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                if (confirma == JOptionPane.YES_OPTION) {
                    Civilizacion c = gestor.buscarCivilizacion(sel);
                    if (c != null) {
                        gestor.eliminarCivilizacion(c);
                        actualizarLista();
                        limpiarDetalles();
                        JOptionPane.showMessageDialog(this, "Civilización eliminada.");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una civilización para eliminar.");
            }
        });
        btnSalir.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
        });

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnSalir);
        panelDerecho.add(panelBotones, BorderLayout.SOUTH);

        splitPane.setLeftComponent(panelIzquierdo);
        splitPane.setRightComponent(panelDerecho);
        add(splitPane, BorderLayout.CENTER);
    }

    private void actualizarLista() {
        listModel.clear();
        gestor.getCivilizaciones().forEach(c -> listModel.addElement(c.getNombre()));
    }

    private void mostrarDetalles(Civilizacion c) {
        if (c == null) {
            limpiarDetalles();
            return;
        }

        // Imagen
        String foto = c.getFotoCiv();
        if (foto != null && !foto.trim().isEmpty()) {
            String ruta = ImageUploader.getRutaCompleta(foto);
            if (ruta != null && new File(ruta).exists()) {
                ImageIcon icon = new ImageIcon(ruta);
                java.awt.Image scaled = icon.getImage().getScaledInstance(
                    200, 200, java.awt.Image.SCALE_SMOOTH
                );
                lblImagen.setIcon(new ImageIcon(scaled));
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("❌ Imagen no encontrada");
            }
        } else {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
        }

        // Texto
        StringBuilder sb = new StringBuilder();
        sb.append("📌 Región: ").append(c.getRegion()).append("\n");
        sb.append("📅 Época: ").append(c.getEpoca()).append("\n");
        if (c.getEvento() != null && !c.getEvento().isEmpty()) {
            sb.append("⚡ Evento clave: ").append(c.getEvento().get(0).getTitulo()).append("\n");
        }
        if (c.getPersonaje() != null && !c.getPersonaje().isEmpty()) {
            sb.append("👤 Figura: ").append(c.getPersonaje().get(0).getNombre()).append("\n");
        }
        sb.append("📝 Descripción: ").append(c.getDescripcion());

        txtDetalles.setText(sb.toString());
    }

    private void limpiarDetalles() {
        lblImagen.setIcon(null);
        lblImagen.setText("Sin imagen");
        txtDetalles.setText("");
    }

    private void abrirFormulario(Civilizacion c) {
        JDialog dialog = new JDialog(
            (JFrame) SwingUtilities.getWindowAncestor(this),
            (c == null ? "➕ Nueva civilización" : "✏️ Editar: " + c.getNombre()),
            true
        );
        dialog.setLayout(new BorderLayout());

        // Campos
        JTextField txtNombre = new JTextField(20);
        JTextField txtRegion = new JTextField(20);
        JTextField txtEpoca = new JTextField(20);
        JTextArea txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        JLabel lblFoto = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createEtchedBorder());
        lblFoto.setPreferredSize(new Dimension(150, 150));
        String[] fotoRef = {""};

        JButton btnSubirFoto = new JButton("📷 Subir foto");
        btnSubirFoto.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(dialog);
            String img = ImageUploader.seleccionarYCopiarImagen(parentFrame);
            if (img != null) {
                fotoRef[0] = img;
                String ruta = ImageUploader.getRutaCompleta(img);
                if (ruta != null && new File(ruta).exists()) {
                    ImageIcon original = new ImageIcon(ruta);
                    java.awt.Image scaled = original.getImage().getScaledInstance(
                        150, 150, java.awt.Image.SCALE_SMOOTH
                    );
                    lblFoto.setIcon(new ImageIcon(scaled));
                } else {
                    lblFoto.setText("❌ Imagen no encontrada");
                    lblFoto.setIcon(null);
                }
            }
        });

        // Rellenar datos si es edición
        if (c != null) {
            txtNombre.setText(c.getNombre());
            txtRegion.setText(c.getRegion());
            txtEpoca.setText(c.getEpoca());
            txtDescripcion.setText(c.getDescripcion());
            String foto = c.getFotoCiv();
            fotoRef[0] = (foto != null) ? foto : "";
            if (foto != null && !foto.trim().isEmpty()) {
                String ruta = ImageUploader.getRutaCompleta(foto);
                if (ruta != null && new File(ruta).exists()) {
                    ImageIcon original = new ImageIcon(ruta);
                    java.awt.Image scaled = original.getImage().getScaledInstance(
                        150, 150, java.awt.Image.SCALE_SMOOTH
                    );
                    lblFoto.setIcon(new ImageIcon(scaled));
                }
            }
        }

        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        formPanel.add(new JLabel("Nombre:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Región:"));
        formPanel.add(txtRegion);
        formPanel.add(new JLabel("Época:"));
        formPanel.add(txtEpoca);
        formPanel.add(new JLabel("Descripción:"));
        formPanel.add(new JScrollPane(txtDescripcion));
        formPanel.add(new JLabel("Foto:"));
        JPanel fotoPanel = new JPanel(new BorderLayout());
        fotoPanel.add(lblFoto, BorderLayout.CENTER);
        fotoPanel.add(btnSubirFoto, BorderLayout.SOUTH);
        formPanel.add(fotoPanel);

        // Botones
        JButton btnGuardar = new JButton("✅ Guardar");
        JButton btnCancelar = new JButton("❌ Cancelar");

        btnGuardar.addActionListener(e -> {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "El nombre es obligatorio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (c == null) {
                Civilizacion nueva = new Civilizacion(
                    nombre,
                    txtRegion.getText().trim(),
                    txtEpoca.getText().trim(),
                    txtDescripcion.getText().trim(),
                    fotoRef[0],
                    new ArrayList<>(),
                    new ArrayList<>()
                );
                gestor.agregarCivilizacion(nueva);
                JOptionPane.showMessageDialog(dialog, "Civilización creada.");
            } else {
                c.setNombre(nombre);
                c.setRegion(txtRegion.getText().trim());
                c.setEpoca(txtEpoca.getText().trim());
                c.setDescripcion(txtDescripcion.getText().trim());
                c.setFotoCiv(fotoRef[0]);
                JOptionPane.showMessageDialog(dialog, "Civilización actualizada.");
            }

            actualizarLista();
            dialog.dispose();
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnGuardar);
        botones.add(btnCancelar);
        dialog.add(botones, BorderLayout.SOUTH);
        dialog.setSize(500, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}