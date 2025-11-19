package proyectoFinal.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;
import proyectoFinal.frontend.FondoPanel; 
import proyectoFinal.frontend.DetalleCivilizacionPanel;

public class VerCivilizacionesPanel extends FondoPanel {  // ✅ HEREDA DE FondoPanel
    private GestorEnciclopedia gestor;

    public VerCivilizacionesPanel(GestorEnciclopedia gestor, Usuario usuario) {
        super(usuario); // ✅ Llama al constructor de FondoPanel
        this.gestor = gestor;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Título (con fondo)
        JLabel lblTitulo = new JLabel("Civilizaciones Disponibles", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setOpaque(false); // ✅ Transparente para ver fondo
        add(lblTitulo, BorderLayout.NORTH);

        // Lista de civilizaciones
        DefaultListModel<String> model = new DefaultListModel<>();
        gestor.getCivilizaciones().forEach(c -> model.addElement(c.getNombre()));

        JList<String> lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setFont(new Font("Monospaced", Font.PLAIN, 14));
        lista.setOpaque(false); // ✅ Transparente
        lista.setBackground(new Color(0, 0, 0, 0)); // ✅ Fondo transparente

        // ScrollPane (transparente)
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false); // ✅ Crucial para ver fondo

        add(scroll, BorderLayout.CENTER);

        // Botón "Ver Detalles"
        JButton btnVer = new JButton("Ver Detalles");
        btnVer.addActionListener(e -> {
            String sel = lista.getSelectedValue();
            if (sel != null) {
                Civilizacion c = gestor.buscarCivilizacion(sel);
                JFrame frame = new JFrame("Detalles: " + c.getNombre());
                frame.setContentPane(new DetalleCivilizacionPanel(c, usuario)); // ✅ Pasa usuario
                frame.setSize(700, 500);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Selecciona una civilización.");
            }
        });

        // Panel inferior (para botón)
        JPanel panelBoton = new JPanel(new FlowLayout());
        panelBoton.setOpaque(false); // ✅ Transparente
        panelBoton.add(btnVer);
        add(panelBoton, BorderLayout.SOUTH);
    }
}