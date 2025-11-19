
package proyectoFinal.frontend;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;

public class LoginFrame extends JFrame {
    private GestorEnciclopedia gestor;

    public LoginFrame(GestorEnciclopedia gestor) {
        this.gestor = gestor;
        initUI();
    }

    private void initUI() {
        setTitle("Enciclopedia Histórica - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 320); 
        setLocationRelativeTo(null);

        JPanel fondoPanel = new JPanel(new GridBagLayout()) {
            private Image backgroundImage;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(new Color(240, 240, 240));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }

            @Override
            public void addNotify() {
                super.addNotify();
                String ruta = "recursos/portada_proyecto_poo.jpg"; // ✅ Fondo fijo para login
                File file = new File(ruta);
                if (file.exists()) {
                    ImageIcon icon = new ImageIcon(ruta);
                    backgroundImage = icon.getImage();
                    repaint();
                }
            }
        };

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblUsuario = new JLabel("Usuario:");
        JLabel lblPass = new JLabel("Contraseña:");
        JTextField txtUsuario = new JTextField(20);
        JPasswordField txtPassword = new JPasswordField(20);
        JButton btnIngresar = new JButton("Ingresar");

        gbc.gridx = 0; gbc.gridy = 0; fondoPanel.add(lblUsuario, gbc);
        gbc.gridx = 1; fondoPanel.add(txtUsuario, gbc);
        gbc.gridx = 0; gbc.gridy = 1; fondoPanel.add(lblPass, gbc);
        gbc.gridx = 1; fondoPanel.add(txtPassword, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        fondoPanel.add(btnIngresar, gbc);

        btnIngresar.addActionListener(e -> {
            String user = txtUsuario.getText().trim();
            String pass = new String(txtPassword.getPassword());
            Usuario u = gestor.buscarUsuario(user);
            if (u != null && u.getPassword().equals(pass)) {
                boolean esAdmin = u instanceof proyectoFinal.backend.Administrador;
                dispose();
                new PrincipalFrame(gestor, u, esAdmin).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        add(fondoPanel);
    }
}