package proyectoFinal.frontend;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.*;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;
import proyectoFinal.frontend.VerCivilizacionesPanel;

public class PrincipalFrame extends JFrame {
    private GestorEnciclopedia gestor;
    private Usuario usuario;
    private boolean esAdmin;

    public PrincipalFrame(GestorEnciclopedia gestor, Usuario usuario, boolean esAdmin) {
        this.gestor = gestor;
        this.usuario = usuario;
        this.esAdmin = esAdmin;
        initUI();
    }

    private void initUI() {
        setTitle("Enciclopedia Histórica - " + usuario.getNombreUsuario());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuCivilizaciones = new JMenu("Civilizaciones");
        
        JMenuItem itemVerTodas = new JMenuItem("Ver todas");
        itemVerTodas.addActionListener(e -> {
            VerCivilizacionesPanel panel = new VerCivilizacionesPanel(gestor, usuario);
            setContentPane(panel);
            revalidate();
        });
        menuCivilizaciones.add(itemVerTodas);
        
        JMenuItem itemTiempo = new JMenuItem("⏳ Línea de Tiempo Gráfica");
        itemTiempo.addActionListener(e -> {
            // USAMOS EL NUEVO PANEL GRÁFICO
            LineaTiempoGraficaPanel panel = new LineaTiempoGraficaPanel(gestor, usuario);
            setContentPane(panel);
            revalidate();
        });
        menuCivilizaciones.add(itemTiempo);
        
        JMenuItem itemMapa = new JMenuItem("Mapa Global"); 
        itemMapa.addActionListener(e -> { 
            MapaPanel panelMapa = new MapaPanel(gestor, usuario, esAdmin); 
            setContentPane(panelMapa);                    
            revalidate();                             
        });                                                 
        menuCivilizaciones.add(itemMapa);
        
        if (esAdmin) {
            JMenuItem itemAdmin = new JMenuItem("Administración");
            itemAdmin.addActionListener(e -> {
                AdminPanel panel = new AdminPanel(gestor, usuario);
                setContentPane(panel);
                revalidate();
            });
            menuCivilizaciones.add(itemAdmin);
        }

        menuBar.add(menuCivilizaciones);
        setJMenuBar(menuBar);

        FondoPanel fondoPanel = new FondoPanel(usuario) {};

        JLabel lblBienvenida = new JLabel(
            "<html><center>" +
            "<h2 style='color:Black; text-shadow: 2px 2px 4px #000;'>¡Bienvenido, " + usuario.getNombreUsuario() + "!</h2>" +
            "<p style='color:Black;'>Explora civilizaciones históricas y sus legados.</p>" +
            (esAdmin ? "<p style='color:#ffeb3b;'>🔹 Eres administrador</p>" : "") +
            "</center></html>",
            SwingConstants.CENTER
        );
        lblBienvenida.setFont(lblBienvenida.getFont().deriveFont(Font.BOLD, 16f));
        lblBienvenida.setOpaque(false);
        fondoPanel.add(lblBienvenida, BorderLayout.CENTER);

        JButton btnIniciar = new JButton("▶️ Iniciar exploración");
        btnIniciar.addActionListener(e -> {
            VerCivilizacionesPanel panel = new VerCivilizacionesPanel(gestor, usuario);
            setContentPane(panel);
            revalidate();
        });
        fondoPanel.add(btnIniciar, BorderLayout.SOUTH);

        add(fondoPanel);
    }
}