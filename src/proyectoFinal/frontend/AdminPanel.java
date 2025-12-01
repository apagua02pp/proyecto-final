package proyectoFinal.frontend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.Evento;
import proyectoFinal.backend.Personaje;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;
import javax.swing.SwingUtilities;

public class AdminPanel extends FondoPanel { 
    private GestorEnciclopedia gestor;
    private JList<String> listaCivilizaciones;
    private DefaultListModel<String> listModel;
    private JLabel lblImagen;
    
    private JTextPane txtDetalles; 

    public AdminPanel(GestorEnciclopedia gestor, Usuario usuario) {
        super(usuario); 
        this.gestor = gestor;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Titulo 
        JLabel lblTitulo = new JLabel("🔧 Administración de Civilizaciones", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setOpaque(false);
        add(lblTitulo, BorderLayout.NORTH);

        // Panel principal dividido
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setOneTouchExpandable(true);

        // Panel izquierdo: Búsqueda y la  Lista
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
        listaCivilizaciones.setBackground(new Color(0, 0, 0, 0)); 
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

        // --- CAMBIO 2: Inicialización para soportar HTML ---
        txtDetalles = new JTextPane();
        txtDetalles.setEditable(false);
        txtDetalles.setContentType("text/html"); // Importante: Activamos modo HTML
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

        // Botones inferiores (Tus botones originales)
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false);

        JButton btnAgregar = new JButton("➕ Agregar");
        btnAgregar.setBackground(new Color(76, 175, 80)); 
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(new Color(255, 152, 0)); 
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(244, 67, 54)); 
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton btnSalir = new JButton("🚪 Salir");
        btnSalir.setBackground(new Color(158, 158, 158)); 
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

    // --- CAMBIO 3: Lógica para pintar las imágenes en HTML ---
    private void mostrarDetalles(Civilizacion c) {
        if (c == null) {
            limpiarDetalles();
            return;
        }

        // Imagen Civilización (JLabel)
        String foto = c.getFotoCiv();
        if (foto != null && !foto.trim().isEmpty()) {
            String ruta = ImageUploader.getRutaCompleta(foto);
            if (ruta != null && new File(ruta).exists()) {
                ImageIcon icon = new ImageIcon(ruta);
                java.awt.Image scaled = icon.getImage().getScaledInstance(200, 200, java.awt.Image.SCALE_SMOOTH);
                lblImagen.setIcon(new ImageIcon(scaled));
                lblImagen.setText("");
            } else {
                lblImagen.setIcon(null);
                lblImagen.setText("❌ Imagen no encontrada");
            }
        } else {
            lblImagen.setIcon(null);
            lblImagen.setText("Sin imagen");
        }

        // Texto con HTML (Personajes con foto)
        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: sans-serif; font-size: 10px;'>");
        
        html.append("<b>📌 Región:</b> ").append(c.getRegion()).append("<br>");
        html.append("<b>📅 Época:</b> ").append(c.getEpoca()).append("<br>");
        html.append("<b>📝 Descripción:</b> ").append(c.getDescripcion()).append("<br><br>");

        // Personajes
        html.append("<hr><b>👤 Personajes:</b><br>");
        if (c.getPersonaje() != null && !c.getPersonaje().isEmpty()) {
            for (Personaje p : c.getPersonaje()) {
                html.append("• <b>").append(p.getNombre()).append("</b><br>");
                html.append("<i>").append(p.getAportes()).append("</i><br>");
                
                // --- AQUÍ MOSTRAMOS LA FOTO DEL PERSONAJE ---
                String fotoP = p.getFotoPers();
                if (fotoP != null && !fotoP.isEmpty() && !fotoP.equals("sin_foto") && !fotoP.equals("SinFoto")) {
                     String rutaP = ImageUploader.getRutaCompleta(fotoP);
                     File f = new File(rutaP);
                     if (f.exists()) {
                         // Usamos f.toURI() para que HTML encuentre la imagen en el disco
                         html.append("<img src='").append(f.toURI()).append("' width='100'><br>");
                     }
                }
                html.append("<br>");
            }
        } else {
            html.append("<i>(Ninguno)</i><br>");
        }

        // Eventos
        html.append("<hr><b>⚡ Eventos:</b><br>");
        if (c.getEvento() != null && !c.getEvento().isEmpty()) {
             for (Evento e : c.getEvento()) {
                 html.append("• <b>").append(e.getTitulo()).append("</b>: ").append(e.getDescripcion()).append("<br>");
             }
        } else {
             html.append("<i>(Ninguno)</i>");
        }

        html.append("</body></html>");
        txtDetalles.setText(html.toString());
        txtDetalles.setCaretPosition(0);
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
                    java.awt.Image scaled = original.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(scaled));
                    lblFoto.setText("");
                } else {
                    lblFoto.setText("❌ Error img");
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
                    java.awt.Image scaled = original.getImage().getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH);
                    lblFoto.setIcon(new ImageIcon(scaled));
                    lblFoto.setText("");
                }
            }
        }

        // Panel Extras
        JPanel panelExtras = new JPanel(new FlowLayout(FlowLayout.LEFT));
        if (c != null) { 
            JButton btnAddPers = new JButton("👤 +Personaje");
            JButton btnAddEvt = new JButton("⚡ +Evento");

            btnAddPers.addActionListener(e -> agregarPersonajeDialogo(c));
            btnAddEvt.addActionListener(e -> agregarEventoDialogo(c));

            panelExtras.add(btnAddPers);
            panelExtras.add(btnAddEvt);
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

        if (c != null) {
            formPanel.add(new JLabel("Agregar contenido:"));
            formPanel.add(panelExtras);
        } else {
            formPanel.add(new JLabel("")); 
            formPanel.add(new JLabel("(Guarda para añadir personajes)"));
        }

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
                
                gestor.guardarCambios();
                JOptionPane.showMessageDialog(dialog, "Civilización actualizada.");
            }

            actualizarLista();
            dialog.dispose();
            if(c!=null && listaCivilizaciones.getSelectedValue()!=null 
               && listaCivilizaciones.getSelectedValue().equals(c.getNombre())) mostrarDetalles(c);
        });

        btnCancelar.addActionListener(e -> dialog.dispose());

        dialog.add(formPanel, BorderLayout.CENTER);
        JPanel botones = new JPanel(new FlowLayout());
        botones.add(btnGuardar);
        botones.add(btnCancelar);
        dialog.add(botones, BorderLayout.SOUTH);
        dialog.setSize(550, 500); 
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // --- CAMBIO 4: Agregar Personaje con FOTO ---
    private void agregarPersonajeDialogo(Civilizacion c) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Nuevo Personaje", true);
        dialog.setLayout(new BorderLayout());
        
        JTextField txtNombre = new JTextField();
        JTextField txtAportes = new JTextField();
        JTextField txtFecha = new JTextField("0000-01-01"); 

        // Componentes para la foto
        JLabel lblFotoP = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFotoP.setPreferredSize(new Dimension(100, 100));
        lblFotoP.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        JButton btnFoto = new JButton("📷 Foto");
        String[] fotoRefP = {"sin_foto"};

        btnFoto.addActionListener(e -> {
             String img = ImageUploader.seleccionarYCopiarImagen(dialog);
             if (img != null) {
                 fotoRefP[0] = img;
                 String ruta = ImageUploader.getRutaCompleta(img);
                 if (new File(ruta).exists()) {
                     ImageIcon original = new ImageIcon(ruta);
                     lblFotoP.setIcon(new ImageIcon(original.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
                     lblFotoP.setText("");
                 }
             }
        });

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(new EmptyBorder(10,10,10,10));
        
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Aportes:")); form.add(txtAportes);
        form.add(new JLabel("Fecha (YYYY-MM-DD):")); form.add(txtFecha);
        form.add(new JLabel("Foto Personaje:")); 
        
        JPanel pFoto = new JPanel(new BorderLayout());
        pFoto.add(lblFotoP, BorderLayout.CENTER);
        pFoto.add(btnFoto, BorderLayout.SOUTH);
        form.add(pFoto);

        JButton btnOk = new JButton("Guardar");
        btnOk.addActionListener(e -> {
            try {
                String nombre = txtNombre.getText();
                String aportes = txtAportes.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = sdf.parse(txtFecha.getText());

                // Usamos la foto seleccionada
                Personaje p = new Personaje(fecha, nombre, aportes, fotoRefP[0]);
                c.agregarPersonaje(p);
                
                gestor.guardarCambios(); 
                JOptionPane.showMessageDialog(this, "Personaje agregado correctamente.");
                
                // Refrescar si estamos viendo esta civilización
                if(listaCivilizaciones.getSelectedValue() != null && 
                   listaCivilizaciones.getSelectedValue().equals(c.getNombre())) {
                    mostrarDetalles(c);
                }
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en la fecha. Usa formato YYYY-MM-DD");
            }
        });
        
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(btnOk, BorderLayout.SOUTH);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void agregarEventoDialogo(Civilizacion c) {
        JTextField txtTitulo = new JTextField();
        JTextField txtDesc = new JTextField();
        JTextField txtFecha = new JTextField("0000-01-01");

        Object[] message = {
            "Título:", txtTitulo,
            "Descripción:", txtDesc,
            "Fecha (YYYY-MM-DD):", txtFecha
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Nuevo Evento", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String titulo = txtTitulo.getText();
                String desc = txtDesc.getText();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = sdf.parse(txtFecha.getText());

                Evento evt = new Evento(fecha, titulo, desc);
                c.agregarEvento(evt);
                
                gestor.guardarCambios(); 
                JOptionPane.showMessageDialog(this, "Evento agregado correctamente.");
                
                if(listaCivilizaciones.getSelectedValue() != null && 
                   listaCivilizaciones.getSelectedValue().equals(c.getNombre())) {
                    mostrarDetalles(c);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error en la fecha. Usa formato YYYY-MM-DD");
            }
        }
    }
}