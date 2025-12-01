package proyectoFinal.frontend;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import proyectoFinal.backend.Usuario;

public abstract class FondoPanel extends JPanel {
    protected Usuario usuario;
    private Image backgroundImage;

    public FondoPanel(Usuario usuario) {
        this.usuario = usuario;
        setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            
            g.setColor(new Color(245, 248, 255)); 
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        
        String ruta = "recursos/fondo_general.jpg";
        File file = new File(ruta);
        if (file.exists()) {
            ImageIcon icon = new ImageIcon(ruta);
            backgroundImage = icon.getImage();
            repaint();
        } else {
            System.err.println("❌ Imagen de fondo no encontrada: " + ruta);
        }
    }
}