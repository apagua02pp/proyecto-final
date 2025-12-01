package proyectoFinal.frontend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.Evento;
import proyectoFinal.backend.Personaje;
import proyectoFinal.backend.Usuario;

public class DetalleCivilizacionPanel extends FondoPanel {

    private Civilizacion civilizacion;

    public DetalleCivilizacionPanel(Civilizacion civilizacion, Usuario usuario) {
        super(usuario);
        this.civilizacion = civilizacion;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        //titulo
        JLabel lblTitulo = new JLabel(civilizacion.getNombre(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Serif", Font.BOLD, 30));
        // Ajusta el color de fondo
        lblTitulo.setForeground(Color.BLACK); 
        add(lblTitulo, BorderLayout.NORTH);

        JTextPane txtDetalles = new JTextPane();
        txtDetalles.setEditable(false);
        txtDetalles.setContentType("text/html"); 
        txtDetalles.setOpaque(false); 
        
        txtDetalles.setText(generarHTML(civilizacion));
      
        JScrollPane scroll = new JScrollPane(txtDetalles);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);

        add(scroll, BorderLayout.CENTER);
        JButton btnCerrar = new JButton("Cerrar Ventana");
        btnCerrar.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
        });

        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(btnCerrar);
        add(panelBoton, BorderLayout.SOUTH);
        
        SwingUtilities.invokeLater(() -> txtDetalles.setCaretPosition(0));
    }

    private String generarHTML(Civilizacion c) {
        StringBuilder html = new StringBuilder();
        
        html.append("<html><body style='font-family: Sans-Serif; padding: 20px; color: #000;'>");

 
        String fotoCiv = c.getFotoCiv();
        if (fotoCiv != null && !fotoCiv.isEmpty()) {
            String ruta = ImageUploader.getRutaCompleta(fotoCiv);
            File f = new File(ruta);
            if (f.exists()) {
                html.append("<div style='text-align: center; margin-bottom: 20px;'>");
                html.append("<img src='").append(f.toURI()).append("' width='350'>");
                html.append("</div>");
            }
        }

        // DATOS
        html.append("<div style='background-color: rgba(255,255,255,0.7); padding: 15px; border-radius: 10px;'>");
        html.append("<h2>📜 Información General</h2>");
        html.append("<b>📍 Región:</b> ").append(c.getRegion()).append("<br>");
        html.append("<b>📅 Época:</b> ").append(c.getEpoca()).append("<br>");
        html.append("<p style='font-size: 14px;'>").append(c.getDescripcion()).append("</p>");
        html.append("</div>");

        // PERSONAJES
        html.append("<br><div style='background-color: rgba(255,255,255,0.7); padding: 15px; border-radius: 10px;'>");
        html.append("<h2>👥 Personajes Históricos</h2>");
        
        if (c.getPersonaje() != null && !c.getPersonaje().isEmpty()) {
            for (Personaje p : c.getPersonaje()) {
                // Tabla para alinear foto y texto
                html.append("<table border='0' style='width:100%; margin-bottom: 15px; border-bottom: 1px solid #ccc;'><tr>");
                
                // Columna Texto
                html.append("<td valign='top'>");
                html.append("<b style='font-size: 16px;'>").append(p.getNombre()).append("</b><br>");
                html.append("<i>").append(p.getAportes()).append("</i>");
                html.append("</td>");

                // Columna Foto 
                String fotoPers = p.getFotoPers();
                if (fotoPers != null && !fotoPers.isEmpty() && !fotoPers.equals("sin_foto")) {
                    String rutaP = ImageUploader.getRutaCompleta(fotoPers);
                    File fP = new File(rutaP);
                    if (fP.exists()) {
                        html.append("<td width='100' align='right'>");
                        html.append("<img src='").append(fP.toURI()).append("' width='80' height='80' style='border: 2px solid #555;'>");
                        html.append("</td>");
                    }
                }
                html.append("</tr></table>");
            }
        } else {
            html.append("<i>No hay personajes registrados.</i>");
        }
        html.append("</div>");

        // EVENTOS
        html.append("<br><div style='background-color: rgba(255,255,255,0.7); padding: 15px; border-radius: 10px;'>");
        html.append("<h2>⚡ Eventos Importantes</h2>");
        if (c.getEvento() != null && !c.getEvento().isEmpty()) {
            html.append("<ul>");
            for (Evento e : c.getEvento()) {
                html.append("<li style='margin-bottom: 5px;'><b>").append(e.getTitulo()).append("</b>: ")
                    .append(e.getDescripcion()).append("</li>");
            }
            html.append("</ul>");
        } else {
            html.append("<i>No hay eventos registrados.</i>");
        }
        html.append("</div>");

        html.append("</body></html>");
        return html.toString();
    }
}