package proyectoFinal.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.Evento;
import proyectoFinal.backend.Personaje;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;

public class LineaTiempoGraficaPanel extends FondoPanel {

    private GestorEnciclopedia gestor;
    private List<ItemTiempo> itemsOrdenados;
    
    // Configuración visual
    private static final int ALTURA_LINEA_BASE = 300; // Posición Y de la línea horizontal
    private static final int ESPACIO_ENTRE_PUNTOS = 220; // Píxeles entre cada evento
    private static final int ANCHO_TARJETA = 200;
    private static final int ALTO_TARJETA = 160;
    private static final int ALTURA_CONECTOR = 60; // Largo de la línea vertical
    
    private JScrollPane scrollPane;
    private JPanel panelDibujo;

    public LineaTiempoGraficaPanel(GestorEnciclopedia gestor, Usuario usuario) {
        super(usuario);
        this.gestor = gestor;
        recolectarDatos();
        initUI();
    }

    private void recolectarDatos() {
        itemsOrdenados = new ArrayList<>();

        for (Civilizacion c : gestor.getCivilizaciones()) {
            String fotoCiv = c.getFotoCiv();

            //Procesar Personajes
            if (c.getPersonaje() != null) {
                for (Personaje p : c.getPersonaje()) {
                    String fotoUsar = (p.getFotoPers() != null && !p.getFotoPers().equals("sin_foto")) 
                                      ? p.getFotoPers() : fotoCiv;

                    String fechaTexto = obtenerTextoAnio(p.getFechaNac());
                    
                    itemsOrdenados.add(new ItemTiempo(
                        p.getFechaNac(), 
                        p.getNombre(), 
                        "Nacimiento (" + c.getNombre() + ")", 
                        fotoUsar, 
                        fechaTexto // <--- Texto corregido (ej: 3500 a.C.)
                    ));
                }
            }

            //Procesar Eventos
            if (c.getEvento() != null) {
                for (Evento e : c.getEvento()) {
                    String fechaTexto = obtenerTextoAnio(e.getFecha());
                    
                    itemsOrdenados.add(new ItemTiempo(
                        e.getFecha(), 
                        e.getTitulo(), 
                        c.getNombre(), 
                        fotoCiv, 
                        fechaTexto // <--- Texto corregido
                    ));
                }
            }
        }

        //ordena la linea por fechas
        Collections.sort(itemsOrdenados);
    }

    // --- MÉTODO AUXILIAR NUEVO PARA FORMATEAR AÑO ---
    private String obtenerTextoAnio(Date fecha) {
        if (fecha == null) return "Desc.";
        
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(fecha);
        
        int year = cal.get(java.util.Calendar.YEAR);
        int era = cal.get(java.util.Calendar.ERA); // 0 es a.C. (BC), 1 es d.C. (AD)
        
        if (era == 0) { // Era 0 = Antes de Cristo
            return year + " a.C.";
        } else {        // Era 1 = Después de Cristo
            return year + " d.C.";
        }
    }

    private void initUI() {
        setLayout(new BorderLayout());

        //titulo
        JLabel lblTitulo = new JLabel("Línea del Tiempo", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 36));
        lblTitulo.setForeground(new Color(50, 50, 50));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        panelDibujo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujarLineaTiempo((Graphics2D) g);
            }
        };
        panelDibujo.setOpaque(false); //fondo

        // Calcular el ancho necesario para el scroll 
        int anchoTotal = Math.max(getWidth(), (itemsOrdenados.size() + 1) * ESPACIO_ENTRE_PUNTOS);
        panelDibujo.setPreferredSize(new Dimension(anchoTotal, 600));

        // --- SCROLL PANE ---
        scrollPane = new JScrollPane(panelDibujo);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void dibujarLineaTiempo(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int totalItems = itemsOrdenados.size();
        if (totalItems == 0) return;

        // 1. Dibujar la Línea principal
        g2.setColor(new Color(80, 80, 80));
        g2.setStroke(new BasicStroke(4));
        int startX = ESPACIO_ENTRE_PUNTOS / 2;
        int endX = startX + (totalItems - 1) * ESPACIO_ENTRE_PUNTOS;
        g2.drawLine(startX - 50, ALTURA_LINEA_BASE, endX + 50, ALTURA_LINEA_BASE);

        // 2. Dibujar cada punto y su tarjeta
        for (int i = 0; i < totalItems; i++) {
            ItemTiempo item = itemsOrdenados.get(i);
            int posX = startX + i * ESPACIO_ENTRE_PUNTOS;
            boolean arriba = (i % 2 == 0); // Alternar arriba/abajo

            //crea el punto del cual surge el recuadro
            g2.setColor(new Color(100, 149, 237)); // Azul claro
            g2.fillOval(posX - 10, ALTURA_LINEA_BASE - 10, 20, 20);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(posX - 10, ALTURA_LINEA_BASE - 10, 20, 20);
            
            //linea vertical
            g2.setColor(new Color(100, 149, 237));
            g2.setStroke(new BasicStroke(3));
            int conectorEndY = arriba ? ALTURA_LINEA_BASE - ALTURA_CONECTOR : ALTURA_LINEA_BASE + ALTURA_CONECTOR;
            g2.drawLine(posX, ALTURA_LINEA_BASE + (arriba ? -10 : 10), posX, conectorEndY);

            //tarjeta
            int cardX = posX - ANCHO_TARJETA / 2;
            int cardY = arriba ? conectorEndY - ALTO_TARJETA : conectorEndY;

            dibujarTarjeta(g2, item, cardX, cardY);
        }
    }

    private void dibujarTarjeta(Graphics2D g2, ItemTiempo item, int x, int y) {
        // Sombra
        g2.setColor(new Color(0, 0, 0, 50));
        g2.fill(new RoundRectangle2D.Double(x + 3, y + 3, ANCHO_TARJETA, ALTO_TARJETA, 15, 15));

        // Fondo Blanco
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Double(x, y, ANCHO_TARJETA, ALTO_TARJETA, 15, 15));
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1));
        g2.draw(new RoundRectangle2D.Double(x, y, ANCHO_TARJETA, ALTO_TARJETA, 15, 15));

        //contenido de la tarjeta
        int padding = 10;
        int currentY = y + padding;

        // 1. AÑO (Título grande y rojo)
        g2.setFont(new Font("Arial", Font.BOLD, 18));
        g2.setColor(new Color(220, 53, 69)); // Rojo
        g2.drawString(item.anioStr, x + padding, currentY + 15);
        currentY += 25;

        // 2. TÍTULO
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.BLACK);
        int anchoDisponible = ANCHO_TARJETA - (2 * padding);
        drawStringMultiLine(g2, item.titulo, x + padding, currentY, anchoDisponible);
        currentY += 40;

        // 3. IMAGEN Y DESCRIPCIÓN 
        int imgSize = 70;
        // Dibujar Imagen
        if (item.rutaFoto != null && !item.rutaFoto.equals("SinFoto")) {
            String rutaCompleta = ImageUploader.getRutaCompleta(item.rutaFoto);
            if (new File(rutaCompleta).exists()) {
                ImageIcon icon = new ImageIcon(rutaCompleta);
                Image img = icon.getImage();
                g2.drawImage(img, x + padding, currentY, imgSize, imgSize, null);
                
                // Borde de imagen
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRect(x + padding, currentY, imgSize, imgSize);
            }
        }

        // Dibujar Descripción (al lado de la imagen)
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(new Color(80, 80, 80));
        String descTruncada = truncarTexto(item.descripcion, 60);
        drawStringMultiLine(g2, descTruncada, x + padding + imgSize + 10, currentY, ANCHO_TARJETA - imgSize - 25);
    }

    // --- UTILIDADES DE TEXTO ---
    private String truncarTexto(String texto, int maxLength) {
        if (texto == null) return "";
        return (texto.length() > maxLength) ? texto.substring(0, maxLength) + "..." : texto;
    }
    
    // Función simple para dibujar texto en múltiples líneas
    private void drawStringMultiLine(Graphics2D g, String text, int x, int y, int lineWidth) {
        FontMetrics m = g.getFontMetrics();
        if(m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y + m.getAscent());
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for(int i = 1; i < words.length; i++) {
                if(m.stringWidth(currentLine + " " + words[i]) < lineWidth) {
                    currentLine += " " + words[i];
                } else {
                    g.drawString(currentLine, x, y + m.getAscent());
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            g.drawString(currentLine, x, y + m.getAscent());
        }
    }

    // --- CLASE AUXILIAR PARA ORDENAR ---
    private class ItemTiempo implements Comparable<ItemTiempo> {
        Date fecha;
        String titulo;
        String descripcion;
        String rutaFoto;
        String anioStr;

        public ItemTiempo(Date fecha, String titulo, String descripcion, String rutaFoto, String anioStr) {
            this.fecha = fecha;
            this.titulo = titulo;
            this.descripcion = descripcion;
            this.rutaFoto = rutaFoto;
            this.anioStr = anioStr;
        }

        @Override
        public int compareTo(ItemTiempo o) {
            if (this.fecha == null || o.fecha == null) return 0;
            return this.fecha.compareTo(o.fecha);
        }
    }
}