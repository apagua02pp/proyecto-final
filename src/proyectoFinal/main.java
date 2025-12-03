package proyectoFinal;

import proyectoFinal.backend.*;
import proyectoFinal.frontend.LoginFrame;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        GestorEnciclopedia gestor = new GestorEnciclopedia();
        Usuario user = new Usuario("lector", "1234", "default_user.jpg", new ArrayList<>());
        Administrador admin = new Administrador("admin", "admin123", "admin_icon.jpg", new ArrayList<>());
        admin.setGestor(gestor);       
        gestor.getUsers().add(user);
        gestor.getUsers().add(admin);
        System.out.println("✅ Sistema iniciado. Civilizaciones cargadas: " + gestor.getCivilizaciones().size());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("💾 Guardando cambios en civilizaciones.txt...");
            gestor.guardarCambios();
        }));

        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame(gestor).setVisible(true);
        });
    }
}

