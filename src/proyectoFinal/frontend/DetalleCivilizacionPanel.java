
package proyectoFinal.frontend;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.io.File;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.Personaje;
import proyectoFinal.backend.Usuario;
import proyectoFinal.frontend.FondoPanel;  // ✅ Importa FondoPanel
import proyectoFinal.frontend.ImageUploader;

public class DetalleCivilizacionPanel extends FondoPanel {  // ✅ HEREDA DE FondoPanel
    private Civilizacion civilizacion;

    // ✅ Constructor nuevo (con usuario)
    public DetalleCivilizacionPanel(Civilizacion c, Usuario usuario) {
        super(usuario); // ✅ Llama al constructor de FondoPanel
        this.civilizacion = c;
        initUI();
    }

    // ✅ Constructor antiguo (para compatibilidad)
    public DetalleCivilizacionPanel(Civilizacion c) {
        this(c, null);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Panel de contenido (transparente para ver fondo)
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false); // ✅ Transparente
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Título
        JLabel lblTitulo = new JLabel("==== " + civilizacion.getNombre() + " ====", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 20f));
        lblTitulo.setOpaque(false);
        contentPanel.add(lblTitulo);
        contentPanel.add(Box.createVerticalStrut(20));

        // Foto
        String foto = civilizacion.getFotoCiv();
        if (foto != null && !foto.trim().isEmpty()) {
            String ruta = ImageUploader.getRutaCompleta(foto);
            if (ruta != null && new File(ruta).exists()) {
                ImageIcon icon = new ImageIcon(ruta);
                Image img = icon.getImage().getScaledInstance(400, 250, java.awt.Image.SCALE_SMOOTH);
                JLabel lblImg = new JLabel(new ImageIcon(img));
                lblImg.setBorder(BorderFactory.createEtchedBorder());
                lblImg.setOpaque(false);
                contentPanel.add(lblImg);
                contentPanel.add(Box.createVerticalStrut(20));
            }
        }

        // Texto
        JTextArea txt = new JTextArea(civilizacion.mostrarInformacion());
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txt.setOpaque(false); // ✅ Transparente
        txt.setForeground(Color.BLACK); // ✅ Negro para legibilidad
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        contentPanel.add(new JScrollPane(txt));

        add(contentPanel, BorderLayout.CENTER);

        // Botón cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if (w != null) w.dispose();
        });
        add(btnCerrar, BorderLayout.SOUTH);
    }
}