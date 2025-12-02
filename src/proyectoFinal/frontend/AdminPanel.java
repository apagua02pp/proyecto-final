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
    
    // Panel de texto enriquecido para ver HTML
    private JTextPane txtDetalles; 

    public AdminPanel(GestorEnciclopedia gestor, Usuario usuario) {
        super(usuario); 
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

        // --- Panel Izquierdo: Lista ---
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Civilizaciones"));
        panelIzquierdo.setOpaque(false);

        // Buscador
        JPanel panelBusqueda = new JPanel(new FlowLayout());
        panelBusqueda.setOpaque(false);
        panelBusqueda.add(new JLabel("Buscar:"));
        JTextField txtBusqueda = new JTextField(8);
        JButton btnBuscar = new JButton("🔍");
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

        // --- Panel Derecho: Detalles ---
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

        // Texto HTML
        txtDetalles = new JTextPane();
        txtDetalles.setEditable(false);
        txtDetalles.setContentType("text/html");
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

        // Botones inferiores
        JPanel panelBotones = new JPanel(new FlowLayout());
        panelBotones.setOpaque(false);

        JButton btnAgregar = new JButton("➕ Agregar");
        btnAgregar.setBackground(new Color(76, 175, 80)); btnAgregar.setForeground(Color.WHITE);
        
        JButton btnEditar = new JButton("✏️ Editar");
        btnEditar.setBackground(new Color(255, 152, 0)); btnEditar.setForeground(Color.WHITE);
        
        JButton btnEliminar = new JButton("🗑️ Eliminar");
        btnEliminar.setBackground(new Color(244, 67, 54)); btnEliminar.setForeground(Color.WHITE);
        
        JButton btnSalir = new JButton("🚪 Salir");
        btnSalir.setBackground(Color.GRAY); btnSalir.setForeground(Color.WHITE);

        // Acciones principales
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
                if (JOptionPane.showConfirmDialog(this, "¿Eliminar " + sel + "?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    gestor.eliminarCivilizacion(gestor.buscarCivilizacion(sel));
                    actualizarLista();
                    limpiarDetalles();
                }
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

    // --- HTML VISUALIZATION ---
    private void mostrarDetalles(Civilizacion c) {
        if (c == null) { limpiarDetalles(); return; }

        String foto = c.getFotoCiv();
        if (foto != null && !foto.trim().isEmpty()) {
            String ruta = ImageUploader.getRutaCompleta(foto);
            if (ruta != null && new File(ruta).exists()) {
                ImageIcon icon = new ImageIcon(ruta);
                lblImagen.setIcon(new ImageIcon(icon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
                lblImagen.setText("");
            } else {
                lblImagen.setIcon(null); lblImagen.setText("❌ Error img");
            }
        } else {
            lblImagen.setIcon(null); lblImagen.setText("Sin imagen");
        }

        StringBuilder html = new StringBuilder();
        html.append("<html><body style='font-family: sans-serif; font-size: 10px;'>");
        html.append("<b>📌 Región:</b> ").append(c.getRegion()).append("<br>");
        html.append("<b>📅 Época:</b> ").append(c.getEpoca()).append("<br>");
        html.append("<b>📝 Descripción:</b> ").append(c.getDescripcion()).append("<br><br>");

        html.append("<hr><b>👤 Personajes:</b><br>");
        if (c.getPersonaje() != null && !c.getPersonaje().isEmpty()) {
            for (Personaje p : c.getPersonaje()) {
                html.append("• <b>").append(p.getNombre()).append("</b><br>");
                html.append("<i>").append(p.getAportes()).append("</i><br>");
                String fotoP = p.getFotoPers();
                if (fotoP != null && !fotoP.isEmpty() && !fotoP.equals("sin_foto")) {
                     String rutaP = ImageUploader.getRutaCompleta(fotoP);
                     if (new File(rutaP).exists()) html.append("<img src='").append(new File(rutaP).toURI()).append("' width='80'><br>");
                }
                html.append("<br>");
            }
        } else { html.append("<i>(Ninguno)</i><br>"); }

        html.append("<hr><b>⚡ Eventos:</b><br>");
        if (c.getEvento() != null && !c.getEvento().isEmpty()) {
             for (Evento e : c.getEvento()) html.append("• <b>").append(e.getTitulo()).append("</b><br>");
        } else { html.append("<i>(Ninguno)</i>"); }

        html.append("</body></html>");
        txtDetalles.setText(html.toString());
        txtDetalles.setCaretPosition(0);
    }

    private void limpiarDetalles() {
        lblImagen.setIcon(null); lblImagen.setText("Sin imagen");
        txtDetalles.setText("");
    }

    // ========================================================================
    // 🛠️ FORMULARIO DE EDICIÓN COMPLETO (CON EDICIÓN DE PERSONAJES/EVENTOS)
    // ========================================================================
    private void abrirFormulario(Civilizacion c) {
        JDialog dialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), 
            (c == null ? "Nueva Civilización" : "Editar: " + c.getNombre()), true);
        dialog.setLayout(new BorderLayout());

        // --- PESTAÑA 1: DATOS GENERALES ---
        JPanel pnlGeneral = new JPanel(new GridLayout(0, 2, 5, 5));
        pnlGeneral.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField txtNombre = new JTextField(c!=null?c.getNombre():"", 20);
        JTextField txtRegion = new JTextField(c!=null?c.getRegion():"", 20);
        JTextField txtEpoca = new JTextField(c!=null?c.getEpoca():"", 20);
        JTextArea txtDesc = new JTextArea(c!=null?c.getDescripcion():"", 4, 20);
        txtDesc.setLineWrap(true); txtDesc.setWrapStyleWord(true);

        // Foto Civilización
        JLabel lblFoto = new JLabel("Sin imagen", SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createEtchedBorder());
        lblFoto.setPreferredSize(new Dimension(100, 100));
        String[] fotoRef = {(c!=null && c.getFotoCiv()!=null)?c.getFotoCiv():""};
        
        // Cargar foto previa si existe
        if(!fotoRef[0].isEmpty()) {
            String ruta = ImageUploader.getRutaCompleta(fotoRef[0]);
            if(new File(ruta).exists()) lblFoto.setIcon(new ImageIcon(new ImageIcon(ruta).getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH)));
        }

        JButton btnSubirFoto = new JButton("📷 Foto");
        btnSubirFoto.addActionListener(e -> {
            String img = ImageUploader.seleccionarYCopiarImagen(dialog);
            if (img != null) {
                fotoRef[0] = img;
                lblFoto.setIcon(new ImageIcon(new ImageIcon(ImageUploader.getRutaCompleta(img)).getImage().getScaledInstance(100,100,Image.SCALE_SMOOTH)));
                lblFoto.setText("");
            }
        });

        pnlGeneral.add(new JLabel("Nombre:")); pnlGeneral.add(txtNombre);
        pnlGeneral.add(new JLabel("Región:")); pnlGeneral.add(txtRegion);
        pnlGeneral.add(new JLabel("Época:")); pnlGeneral.add(txtEpoca);
        pnlGeneral.add(new JLabel("Descripción:")); pnlGeneral.add(new JScrollPane(txtDesc));
        
        JPanel pFoto = new JPanel(new BorderLayout());
        pFoto.add(lblFoto, BorderLayout.CENTER); pFoto.add(btnSubirFoto, BorderLayout.SOUTH);
        pnlGeneral.add(new JLabel("Foto:")); pnlGeneral.add(pFoto);

        // --- PESTAÑA 2: GESTIÓN DE CONTENIDO (PERSONAJES Y EVENTOS) ---
        JPanel pnlContenido = new JPanel();
        pnlContenido.setLayout(new BoxLayout(pnlContenido, BoxLayout.Y_AXIS));
        pnlContenido.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (c != null) {
            // -- GESTIÓN PERSONAJES --
            pnlContenido.add(new JLabel("--- 👤 PERSONAJES ---"));
            JComboBox<String> comboPers = new JComboBox<>();
            JButton btnEditP = new JButton("✏️ Editar");
            JButton btnDelP = new JButton("🗑️ Borrar");
            JButton btnAddP = new JButton("➕ Nuevo");

            // Función para recargar el combo
            Runnable recargarPers = () -> {
                comboPers.removeAllItems();
                if(c.getPersonaje()!=null) c.getPersonaje().forEach(p -> comboPers.addItem(p.getNombre()));
            };
            recargarPers.run();

            JPanel pnlBtnsP = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnlBtnsP.add(comboPers); pnlBtnsP.add(btnEditP); pnlBtnsP.add(btnDelP); pnlBtnsP.add(btnAddP);
            pnlContenido.add(pnlBtnsP);

            // Acciones Personajes
            btnAddP.addActionListener(e -> { 
                editarPersonajeDialogo(c, null, dialog); 
                recargarPers.run(); 
            });
            
            btnEditP.addActionListener(e -> {
                int idx = comboPers.getSelectedIndex();
                if(idx >= 0) {
                    editarPersonajeDialogo(c, c.getPersonaje().get(idx), dialog);
                    recargarPers.run();
                }
            });

            btnDelP.addActionListener(e -> {
                int idx = comboPers.getSelectedIndex();
                if(idx >= 0 && JOptionPane.showConfirmDialog(dialog, "¿Borrar personaje?") == JOptionPane.YES_OPTION) {
                    c.getPersonaje().remove(idx);
                    gestor.guardarCambios();
                    recargarPers.run();
                }
            });

            pnlContenido.add(Box.createVerticalStrut(10));

            // -- GESTIÓN EVENTOS --
            pnlContenido.add(new JLabel("--- ⚡ EVENTOS ---"));
            JComboBox<String> comboEvt = new JComboBox<>();
            JButton btnEditE = new JButton("✏️ Editar");
            JButton btnDelE = new JButton("🗑️ Borrar");
            JButton btnAddE = new JButton("➕ Nuevo");

            Runnable recargarEvt = () -> {
                comboEvt.removeAllItems();
                if(c.getEvento()!=null) c.getEvento().forEach(ev -> comboEvt.addItem(ev.getTitulo()));
            };
            recargarEvt.run();

            JPanel pnlBtnsE = new JPanel(new FlowLayout(FlowLayout.LEFT));
            pnlBtnsE.add(comboEvt); pnlBtnsE.add(btnEditE); pnlBtnsE.add(btnDelE); pnlBtnsE.add(btnAddE);
            pnlContenido.add(pnlBtnsE);

            // Acciones Eventos
            btnAddE.addActionListener(e -> { 
                editarEventoDialogo(c, null, dialog); 
                recargarEvt.run(); 
            });

            btnEditE.addActionListener(e -> {
                int idx = comboEvt.getSelectedIndex();
                if(idx >= 0) {
                    editarEventoDialogo(c, c.getEvento().get(idx), dialog);
                    recargarEvt.run();
                }
            });

            btnDelE.addActionListener(e -> {
                int idx = comboEvt.getSelectedIndex();
                if(idx >= 0 && JOptionPane.showConfirmDialog(dialog, "¿Borrar evento?") == JOptionPane.YES_OPTION) {
                    c.getEvento().remove(idx);
                    gestor.guardarCambios();
                    recargarEvt.run();
                }
            });

        } else {
            pnlContenido.add(new JLabel("Guarda la civilización primero para agregar contenido."));
        }

        // --- UNIR TODO EN TABS ---
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Datos Generales", pnlGeneral);
        tabs.addTab("Personajes y Eventos", pnlContenido);
        dialog.add(tabs, BorderLayout.CENTER);

        // --- BOTONES GUARDAR/CANCELAR ---
        JButton btnGuardar = new JButton("✅ Guardar Todo");
        btnGuardar.addActionListener(e -> {
            if(txtNombre.getText().trim().isEmpty()) return;
            
            if(c == null) { // Nuevo
                Civilizacion nueva = new Civilizacion(txtNombre.getText(), txtRegion.getText(), txtEpoca.getText(), 
                        txtDesc.getText(), fotoRef[0], new ArrayList<>(), new ArrayList<>());
                gestor.agregarCivilizacion(nueva);
            } else { // Editar
                c.setNombre(txtNombre.getText());
                c.setRegion(txtRegion.getText());
                c.setEpoca(txtEpoca.getText());
                c.setDescripcion(txtDesc.getText());
                c.setFotoCiv(fotoRef[0]);
                gestor.guardarCambios();
            }
            actualizarLista();
            if(c!=null) mostrarDetalles(c);
            dialog.dispose();
        });

        JPanel pnlSur = new JPanel();
        pnlSur.add(btnGuardar);
        dialog.add(pnlSur, BorderLayout.SOUTH);

        dialog.setSize(550, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    // ========================================================================
    // 🛠️ DIÁLOGOS DE EDICIÓN (REUTILIZABLES PARA AGREGAR Y EDITAR)
    // ========================================================================
    
    private void editarPersonajeDialogo(Civilizacion c, Personaje pExistente, JDialog parent) {
        JDialog d = new JDialog(parent, (pExistente==null?"Nuevo":"Editar") + " Personaje", true);
        d.setLayout(new BorderLayout());
        
        JTextField txtNom = new JTextField(pExistente!=null?pExistente.getNombre():"", 15);
        JTextField txtApor = new JTextField(pExistente!=null?pExistente.getAportes():"", 15);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaStr = (pExistente!=null && pExistente.getFechaNac()!=null) ? sdf.format(pExistente.getFechaNac()) : "0000-00-00";
        JTextField txtFec = new JTextField(fechaStr, 15);
        
        JLabel lblFoto = new JLabel("Sin foto", SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblFoto.setPreferredSize(new Dimension(80, 80));
        String[] fotoRef = {(pExistente!=null && pExistente.getFotoPers()!=null)?pExistente.getFotoPers():"sin_foto"};
        
        // Previsualizar foto
        if(!fotoRef[0].equals("sin_foto")) {
             String r = ImageUploader.getRutaCompleta(fotoRef[0]);
             if(new File(r).exists()) lblFoto.setIcon(new ImageIcon(new ImageIcon(r).getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH)));
        }

        JButton btnFoto = new JButton("📷");
        btnFoto.addActionListener(e -> {
            String img = ImageUploader.seleccionarYCopiarImagen(d);
            if(img!=null) {
                fotoRef[0] = img;
                lblFoto.setIcon(new ImageIcon(new ImageIcon(ImageUploader.getRutaCompleta(img)).getImage().getScaledInstance(80,80,Image.SCALE_SMOOTH)));
                lblFoto.setText("");
            }
        });

        JPanel form = new JPanel(new GridLayout(0,2));
        form.add(new JLabel("Nombre:")); form.add(txtNom);
        form.add(new JLabel("Aportes:")); form.add(txtApor);
        form.add(new JLabel("Fecha (YYYY-MM-DD):")); form.add(txtFec);
        form.add(new JLabel("Foto:")); 
        JPanel pFoto = new JPanel(); pFoto.add(lblFoto); pFoto.add(btnFoto);
        form.add(pFoto);

        JButton btnOk = new JButton("Guardar");
        btnOk.addActionListener(e -> {
            try {
                Date fecha = sdf.parse(txtFec.getText());
                if(pExistente == null) {
                    // Crear Nuevo
                    c.agregarPersonaje(new Personaje(fecha, txtNom.getText(), txtApor.getText(), fotoRef[0]));
                } else {
                    // Actualizar Existente
                    pExistente.setNombre(txtNom.getText());
                    pExistente.setAportes(txtApor.getText());
                    pExistente.setFechaNac(fecha);
                    pExistente.setFotoPers(fotoRef[0]);
                }
                gestor.guardarCambios();
                d.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(d, "Fecha inválida (YYYY-MM-DD)"); }
        });

        d.add(form, BorderLayout.CENTER);
        d.add(btnOk, BorderLayout.SOUTH);
        d.pack(); d.setLocationRelativeTo(parent); d.setVisible(true);
    }

    private void editarEventoDialogo(Civilizacion c, Evento eExistente, JDialog parent) {
        JDialog d = new JDialog(parent, (eExistente==null?"Nuevo":"Editar") + " Evento", true);
        d.setLayout(new BorderLayout());
        
        JTextField txtTit = new JTextField(eExistente!=null?eExistente.getTitulo():"", 15);
        JTextField txtDesc = new JTextField(eExistente!=null?eExistente.getDescripcion():"", 15);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String fechaStr = (eExistente!=null && eExistente.getFecha()!=null) ? sdf.format(eExistente.getFecha()) : "0000-00-00";
        JTextField txtFec = new JTextField(fechaStr, 15);

        JPanel form = new JPanel(new GridLayout(0,2));
        form.add(new JLabel("Título:")); form.add(txtTit);
        form.add(new JLabel("Descripción:")); form.add(txtDesc);
        form.add(new JLabel("Fecha (YYYY-MM-DD):")); form.add(txtFec);

        JButton btnOk = new JButton("Guardar");
        btnOk.addActionListener(e -> {
            try {
                Date fecha = sdf.parse(txtFec.getText());
                if(eExistente == null) {
                    c.agregarEvento(new Evento(fecha, txtTit.getText(), txtDesc.getText()));
                } else {
                    eExistente.setTitulo(txtTit.getText());
                    eExistente.setDescripcion(txtDesc.getText());
                    eExistente.setFecha(fecha);
                }
                gestor.guardarCambios();
                d.dispose();
            } catch(Exception ex) { JOptionPane.showMessageDialog(d, "Fecha inválida"); }
        });

        d.add(form, BorderLayout.CENTER);
        d.add(btnOk, BorderLayout.SOUTH);
        d.pack(); d.setLocationRelativeTo(parent); d.setVisible(true);
    }
}