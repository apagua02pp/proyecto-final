package proyectoFinal.frontend;

import proyectoFinal.backend.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

// ✅ IMPORTACIONES NUEVAS NECESARIAS PARA QUE "normalizar" FUNCIONE
import java.text.Normalizer;
import java.util.regex.Pattern;

public class MapaPanel extends FondoPanel {

    private enum Modo { NAVEGAR, AGREGAR, EDITAR, ELIMINAR }
    private Modo modoActual = Modo.NAVEGAR;

    private GestorEnciclopedia gestorPrincipal;
    private GestorMapa gestorMapa;

    private boolean esAdmin;
    private JLabel lblInstruccion;
    private AreaMapaComponente componenteMapa;
    
    // Componentes de Búsqueda
    private JTextField txtBusqueda;
    private JButton btnBuscar;
    private JPopupMenu popupSugerencias; 

    public MapaPanel(GestorEnciclopedia gestor, Usuario usuario, boolean esAdmin) {
        super(usuario);
        this.gestorPrincipal = gestor;
        this.esAdmin = esAdmin;
        this.gestorMapa = new GestorMapa();
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // =======================================================
        // 1. PANEL SUPERIOR
        // =======================================================
        JPanel panelNorte = new JPanel();
        panelNorte.setLayout(new BoxLayout(panelNorte, BoxLayout.Y_AXIS));
        panelNorte.setOpaque(false);
        panelNorte.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- BARRA DE BÚSQUEDA ---
        JPanel panelBuscador = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBuscador.setOpaque(false);
        
        JLabel lblBuscar = new JLabel("🔍 Buscar (Civ, Personaje, Evento): ");
        lblBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        
        txtBusqueda = new JTextField(25); 
        btnBuscar = new JButton("Ir");
        
        popupSugerencias = new JPopupMenu();
        popupSugerencias.setFocusable(false);

        // Configurar Autocompletado
        configurarAutocompletado();
        
        ActionListener accionBuscar = e -> realizarBusqueda(txtBusqueda.getText().trim());
        btnBuscar.addActionListener(accionBuscar);
        
        txtBusqueda.addActionListener(e -> {
            popupSugerencias.setVisible(false);
            realizarBusqueda(txtBusqueda.getText().trim());
        });

        panelBuscador.add(lblBuscar);
        panelBuscador.add(txtBusqueda);
        panelBuscador.add(btnBuscar);
        
        panelNorte.add(panelBuscador);

        // --- INSTRUCCIONES ---
        lblInstruccion = new JLabel("Haz clic en una región del mapa para ver detalles", SwingConstants.CENTER);
        lblInstruccion.setFont(new Font("Arial", Font.PLAIN, 14));
        lblInstruccion.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblInstruccion.setOpaque(false);
        
        panelNorte.add(Box.createVerticalStrut(5));
        panelNorte.add(lblInstruccion);
        
        add(panelNorte, BorderLayout.NORTH);

        // =======================================================
        // 2. COMPONENTE DEL MAPA
        // =======================================================
        componenteMapa = new AreaMapaComponente("recursos/mapa.png");
        add(componenteMapa, BorderLayout.CENTER);

        // =======================================================
        // 3. PANEL DE HERRAMIENTAS (SOLO ADMIN)
        // =======================================================
        if (esAdmin) {
            JPanel panelAdmin = new JPanel();
            panelAdmin.setOpaque(false);
            
            JButton btnAsignar = new JButton("➕ Asignar");
            JButton btnBorrar = new JButton("🗑️ Liberar Región");
            JButton btnCancelar = new JButton("❌ Cancelar Acción");

            btnAsignar.addActionListener(e -> cambiarModo(Modo.AGREGAR, "MODO ASIGNAR: Selecciona una región vacía"));
            btnBorrar.addActionListener(e -> cambiarModo(Modo.ELIMINAR, "MODO ELIMINAR: Selecciona una región ocupada"));
            btnCancelar.addActionListener(e -> cambiarModo(Modo.NAVEGAR, "Modo Navegación"));

            panelAdmin.add(btnAsignar);
            panelAdmin.add(btnBorrar);
            panelAdmin.add(btnCancelar);
            add(panelAdmin, BorderLayout.SOUTH);
        }
    }

    // ========================================================================
    // ✅ AUTOCOMPLETADO
    // ========================================================================
    private void configurarAutocompletado() {
        txtBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER || e.getKeyCode() == KeyEvent.VK_ESCAPE) return;

                String textoEscrito = normalizar(txtBusqueda.getText()); // Usamos normalizar aquí también
                popupSugerencias.setVisible(false);
                popupSugerencias.removeAll();

                if (textoEscrito.isEmpty()) return;

                List<String> sugerencias = new ArrayList<>();
                List<Civilizacion> todas = gestorPrincipal.getCivilizaciones();

                for (Civilizacion c : todas) {
                    // Buscar coincidencias normalizadas (sin acentos, minúsculas)
                    if (normalizar(c.getNombre()).contains(textoEscrito)) {
                        sugerencias.add(c.getNombre() + " (Civilización)");
                    }
                    if (c.getPersonaje() != null) {
                        for (Personaje p : c.getPersonaje()) {
                            if (normalizar(p.getNombre()).contains(textoEscrito)) {
                                sugerencias.add(p.getNombre() + " (Personaje)");
                            }
                        }
                    }
                    if (c.getEvento() != null) {
                        for (Evento ev : c.getEvento()) {
                            if (normalizar(ev.getTitulo()).contains(textoEscrito)) {
                                sugerencias.add(ev.getTitulo() + " (Evento)");
                            }
                        }
                    }
                }

                int limite = 0;
                for (String sugerencia : sugerencias) {
                    if (limite >= 6) break;
                    
                    String textoLimpio = sugerencia.replaceAll(" \\(.*\\)$", ""); 
                    
                    JMenuItem item = new JMenuItem(sugerencia);
                    item.setFont(new Font("Arial", Font.PLAIN, 12));
                    item.addActionListener(ev -> {
                        txtBusqueda.setText(textoLimpio); 
                        popupSugerencias.setVisible(false);
                        realizarBusqueda(textoLimpio); 
                    });
                    popupSugerencias.add(item);
                    limite++;
                }

                if (!sugerencias.isEmpty()) {
                    popupSugerencias.show(txtBusqueda, 0, txtBusqueda.getHeight());
                    txtBusqueda.requestFocus();
                }
            }
        });
    }

    // ========================================================================
    // ✅ LÓGICA DE BÚSQUEDA PROFUNDA Y FLEXIBLE
    // ========================================================================
    private void realizarBusqueda(String texto) {
        if (texto.isEmpty()) return;
        
        // 1. Intento Rápido (Exacto)
        Civilizacion civ = gestorPrincipal.buscarCivilizacion(texto.trim());
        if (civ != null) {
            abrirDetalle(civ);
            txtBusqueda.setText(""); 
            popupSugerencias.setVisible(false);
            return;
        }

        // 2. Intento Flexible (Normalizado - Ignora acentos/mayúsculas)
        Civilizacion encontrada = buscarEntidadGlobal(texto);
        
        if (encontrada != null) {
            abrirDetalle(encontrada);
            txtBusqueda.setText("");
            popupSugerencias.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(this, 
                "No se encontró nada relacionado con: " + texto, 
                "Sin resultados", JOptionPane.WARNING_MESSAGE);
        }
    }

    private Civilizacion buscarEntidadGlobal(String query) {
        String queryNorm = normalizar(query); // Convertimos búsqueda a simple (sin acentos)
        
        for (Civilizacion c : gestorPrincipal.getCivilizaciones()) {
            
            // 1. Nombre Civilización
            if (normalizar(c.getNombre()).contains(queryNorm)) {
                return c;
            }

            // 2. Personajes
            if (c.getPersonaje() != null) {
                for (Personaje p : c.getPersonaje()) {
                    if (normalizar(p.getNombre()).contains(queryNorm)) {
                        return c; 
                    }
                }
            }
            // 3. Eventos
            if (c.getEvento() != null) {
                for (Evento ev : c.getEvento()) {
                    if (normalizar(ev.getTitulo()).contains(queryNorm)) {
                        return c; 
                    }
                }
            }
        }
        return null;
    }

    // ========================================================================
    // ✅ MÉTODO HELPER (ESTE ERA EL QUE FALTABA O DABA ERROR)
    // ========================================================================
    private String normalizar(String input) {
        if (input == null) return "";
        // Separa los acentos de las letras (Ej: á -> a + ´)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Borra los acentos usando Regex y convierte a minúsculas
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").toLowerCase().trim();
    }

    // ========================================================================
    // GESTIÓN DEL MAPA
    // ========================================================================

    private void cambiarModo(Modo nuevoModo, String mensaje) {
        this.modoActual = nuevoModo;
        lblInstruccion.setText(mensaje);
        if (nuevoModo != Modo.NAVEGAR) lblInstruccion.setForeground(Color.RED);
        else lblInstruccion.setForeground(Color.BLACK);
        componenteMapa.setCursorSegunModo(nuevoModo);
    }

    private void procesarClicRegion(int indiceRegion) {
        String nombreCiv = gestorMapa.obtenerCivilizacion(indiceRegion);
        
        switch (modoActual) {
            case NAVEGAR:
                if (nombreCiv != null) {
                    Civilizacion civ = gestorPrincipal.buscarCivilizacion(nombreCiv);
                    if (civ != null) {
                        abrirDetalle(civ);
                    } else {
                        JOptionPane.showMessageDialog(this, "Error: Datos corruptos para '" + nombreCiv + "'.");
                    }
                }
                break;
            case AGREGAR:
                if (nombreCiv == null) {
                    dialogoAsignarCivilizacion(indiceRegion);
                } else {
                    JOptionPane.showMessageDialog(this, "Región ya ocupada por " + nombreCiv);
                }
                cambiarModo(Modo.NAVEGAR, "Modo Navegación");
                break;
            case ELIMINAR:
                if (nombreCiv != null) {
                    int confirm = JOptionPane.showConfirmDialog(this, "¿Liberar " + nombreCiv + "?");
                    if (confirm == JOptionPane.YES_OPTION) gestorMapa.eliminarAsignacion(indiceRegion);
                }
                cambiarModo(Modo.NAVEGAR, "Modo Navegación");
                break;
             default: break;
        }
    }

    private void dialogoAsignarCivilizacion(int indiceRegion) {
        java.util.List<Civilizacion> lista = gestorPrincipal.getCivilizaciones();
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay civilizaciones registradas.");
            return;
        }
        String[] nombres = lista.stream().map(Civilizacion::getNombre).toArray(String[]::new);
        String seleccion = (String) JOptionPane.showInputDialog(
            this, "Selecciona civilización:", "Asignar",
            JOptionPane.QUESTION_MESSAGE, null, nombres, nombres[0]);

        if (seleccion != null) {
            gestorMapa.asignarCivilizacion(indiceRegion, seleccion);
            JOptionPane.showMessageDialog(this, "Asignado a " + seleccion);
        }
    }

    private void abrirDetalle(Civilizacion civ) {
        JFrame frameDetalle = new JFrame("Detalles: " + civ.getNombre());
        frameDetalle.setSize(600, 500);
        frameDetalle.setLocationRelativeTo(null);
        frameDetalle.setContentPane(new DetalleCivilizacionPanel(civ, usuario));
        frameDetalle.setVisible(true);
    }

    // ====================================================================================
    // CLASE INTERNA: Componente Mapa
    // ====================================================================================
    private class AreaMapaComponente extends JComponent {
        private BufferedImage imagenOriginal;
        private ArrayList<Shape> regionesDetectadas;
        private int indiceHover = -1;
        
        private double factorEscala = 1.0;
        private int transX = 0;
        private int transY = 0;

        public AreaMapaComponente(String ruta) {
            try {
                File f = new File(ruta);
                if (f.exists()) {
                    imagenOriginal = ImageIO.read(f);
                    Area contorno = getOutline(Color.WHITE, imagenOriginal, 20);
                    regionesDetectadas = separateShapeIntoRegions(contorno);
                } else {
                    imagenOriginal = new BufferedImage(800, 600, BufferedImage.TYPE_INT_RGB);
                    Graphics g = imagenOriginal.getGraphics();
                    g.setColor(Color.LIGHT_GRAY); g.fillRect(0,0,800,600);
                    g.setColor(Color.RED); g.drawString("ERROR: IMAGEN NO ENCONTRADA", 300, 300);
                }
            } catch (Exception e) { e.printStackTrace(); }

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (indiceHover != -1) procesarClicRegion(indiceHover);
                }
            });
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    actualizarHover(e.getPoint());
                }
                @Override
                public void mouseDragged(MouseEvent e) {}
            });
        }

        private void actualizarHover(Point pPantalla) {
            double xOriginal = (pPantalla.x - transX) / factorEscala;
            double yOriginal = (pPantalla.y - transY) / factorEscala;
            
            int nuevoIndice = -1;
            for (int i = 0; i < regionesDetectadas.size(); i++) {
                if (regionesDetectadas.get(i).contains(xOriginal, yOriginal)) {
                    nuevoIndice = i;
                    break;
                }
            }
            if (nuevoIndice != indiceHover) {
                indiceHover = nuevoIndice;
                repaint();
            }
        }

        public void setCursorSegunModo(Modo modo) {
            if (modo == Modo.NAVEGAR) setCursor(Cursor.getDefaultCursor());
            else setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagenOriginal == null) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

            double anchoPanel = getWidth();
            double altoPanel = getHeight();
            double anchoImg = imagenOriginal.getWidth();
            double altoImg = imagenOriginal.getHeight();

            factorEscala = Math.min(anchoPanel / anchoImg, altoPanel / altoImg);
            
            double anchoFinal = anchoImg * factorEscala;
            double altoFinal = altoImg * factorEscala;
            transX = (int) ((anchoPanel - anchoFinal) / 2);
            transY = (int) ((altoPanel - altoFinal) / 2);

            AffineTransform oldAT = g2.getTransform();
            g2.translate(transX, transY);
            g2.scale(factorEscala, factorEscala);

            g2.drawImage(imagenOriginal, 0, 0, null);

            if (indiceHover != -1 && indiceHover < regionesDetectadas.size()) {
                g2.setColor(new Color(255, 165, 0, 140)); // Naranja
                g2.fill(regionesDetectadas.get(indiceHover));
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2.0f)); 
                g2.draw(regionesDetectadas.get(indiceHover));
            }

            g2.setTransform(oldAT);
        }

        private Area getOutline(Color target, BufferedImage bi, int tolerance) {
            GeneralPath gp = new GeneralPath();
            boolean cont = false;
            for (int xx = 0; xx < bi.getWidth(); xx++) {
                for (int yy = 0; yy < bi.getHeight(); yy++) {
                    if (isIncluded(new Color(bi.getRGB(xx, yy)), target, tolerance)) {
                        if (cont) { gp.lineTo(xx, yy); gp.lineTo(xx, yy + 1); gp.lineTo(xx + 1, yy + 1); gp.lineTo(xx + 1, yy); gp.lineTo(xx, yy); } 
                        else { gp.moveTo(xx, yy); }
                        cont = true;
                    } else { cont = false; }
                }
                cont = false;
            }
            gp.closePath(); return new Area(gp);
        }

        private ArrayList<Shape> separateShapeIntoRegions(Shape shape) {
            ArrayList<Shape> regions = new ArrayList<>();
            PathIterator pi = shape.getPathIterator(null);
            GeneralPath gp = new GeneralPath();
            while (!pi.isDone()) {
                double[] coords = new double[6];
                int type = pi.currentSegment(coords);
                gp.setWindingRule(pi.getWindingRule());
                if (type == PathIterator.SEG_MOVETO) { gp = new GeneralPath(); gp.moveTo(coords[0], coords[1]); }
                else if (type == PathIterator.SEG_LINETO) gp.lineTo(coords[0], coords[1]);
                else if (type == PathIterator.SEG_QUADTO) gp.quadTo(coords[0], coords[1], coords[2], coords[3]);
                else if (type == PathIterator.SEG_CUBICTO) gp.curveTo(coords[0], coords[1], coords[2], coords[3], coords[4], coords[5]);
                else if (type == PathIterator.SEG_CLOSE) { gp.closePath(); regions.add(new Area(gp)); }
                pi.next();
            }
            return regions;
        }

        private boolean isIncluded(Color target, Color pixel, int tolerance) {
            int rT = target.getRed(), gT = target.getGreen(), bT = target.getBlue();
            int rP = pixel.getRed(), gP = pixel.getGreen(), bP = pixel.getBlue();
            return ((rP >= rT - tolerance && rP <= rT + tolerance) && (gP >= gT - tolerance && gP <= gT + tolerance) && (bP >= bT - tolerance && bP <= bT + tolerance));
        }
    }
}