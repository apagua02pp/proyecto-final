package proyectoFinal.frontend;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;

public class VerCivilizacionesPanel extends FondoPanel { 
    private GestorEnciclopedia gestor;

    public VerCivilizacionesPanel(GestorEnciclopedia gestor, Usuario usuario) {
        super(usuario); 
        this.gestor = gestor;
        initUI();
    }

    private void initUI() {
        
        setLayout(new BorderLayout());

        // --- TÍTULO ---
        JLabel lblTitulo = new JLabel("Civilizaciones Disponibles", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24)); 
        lblTitulo.setOpaque(false);
        lblTitulo.setForeground(Color.BLACK); 
        lblTitulo.setBorder(new EmptyBorder(20, 0, 0, 0)); 
        add(lblTitulo, BorderLayout.NORTH);

        //panel centra
        JPanel panelCentral = new JPanel(null); 
        panelCentral.setOpaque(false);
        add(panelCentral, BorderLayout.CENTER);

        // --- LISTA ---
        DefaultListModel<String> model = new DefaultListModel<>();
        gestor.getCivilizaciones().forEach(c -> model.addElement(c.getNombre()));

        JList<String> lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        lista.setFont(new Font("Arial", Font.PLAIN, 18));
        lista.setBackground(new Color(0, 0, 0, 0)); 
        lista.setOpaque(false); 
        lista.setForeground(Color.BLACK); 
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) lista.getCellRenderer();
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        JScrollPane scroll = new JScrollPane(lista);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        
        scroll.setBorder(null);

        // ==========================================================
        // 📍 UBICACIÓN DE LA LISTA (X, Y, ANCHO, ALTO)
        // ==========================================================
        scroll.setBounds(80, 50, 800, 400); 

        panelCentral.add(scroll);

        // --- BOTÓN ---
        JButton btnVer = new JButton("Ver Detalles");
        btnVer.setFont(new Font("Arial", Font.BOLD, 14)); 
        
        btnVer.addActionListener(e -> {
            String sel = lista.getSelectedValue();
            if (sel != null) {
                Civilizacion c = gestor.buscarCivilizacion(sel);
                JFrame frame = new JFrame("Detalles: " + c.getNombre());
                frame.setContentPane(new DetalleCivilizacionPanel(c, usuario));
                frame.setSize(700, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una civilización.");
            }
        });

        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.setOpaque(false); 
        panelBoton.setBorder(new EmptyBorder(10, 0, 20, 0)); 
        panelBoton.add(btnVer);
        
        add(panelBoton, BorderLayout.SOUTH);
    }
}