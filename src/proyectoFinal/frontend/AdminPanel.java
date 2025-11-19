package proyectoFinal.frontend;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import proyectoFinal.backend.Civilizacion;
import proyectoFinal.backend.GestorEnciclopedia;
import proyectoFinal.backend.Usuario;
import javax.swing.SwingUtilities;

public class AdminPanel extends FondoPanel {
    private GestorEnciclopedia gestor;
    private JList<String> lista;

    public AdminPanel(GestorEnciclopedia gestor, Usuario usuario) {
        super(usuario);
        this.gestor = gestor;
        initUI();
    }

    private void initUI() {
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel lblTitulo = new JLabel("🔧 Administración", SwingConstants.CENTER);
        lblTitulo.setFont(lblTitulo.getFont().deriveFont(Font.BOLD, 18f));
        lblTitulo.setOpaque(false);
        add(lblTitulo, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setDividerLocation(250);
        split.setLeftComponent(crearPanelLista());
        split.setRightComponent(crearPanelDetalles());
        add(split, BorderLayout.CENTER);
    }

    private JPanel crearPanelLista() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Civilizaciones"));
        panel.setOpaque(false);

        DefaultListModel<String> model = new DefaultListModel<>();
        gestor.getCivilizaciones().forEach(c -> model.addElement(c.getNombre()));
        lista = new JList<>(model);
        lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lista.setOpaque(false);

        panel.add(new JScrollPane(lista), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelDetalles() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalles"));
        panel.setOpaque(false);

        JLabel lblInfo = new JLabel("Selecciona una civilización para ver detalles.", SwingConstants.CENTER);
        lblInfo.setOpaque(false);
        panel.add(lblInfo, BorderLayout.CENTER);

        return panel;
    }
}