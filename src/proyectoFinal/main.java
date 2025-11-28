package proyectoFinal;

import proyectoFinal.backend.*;
import proyectoFinal.frontend.LoginFrame;
import proyectoFinal.frontend.GestorDatosTXT;

import java.io.File;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) {
        GestorEnciclopedia gestor = new GestorEnciclopedia();

        // Usuarios de prueba
        Usuario user = new Usuario("lector", "1234", "default_user.jpg", new ArrayList<>());
        Administrador admin = new Administrador("admin", "admin123", "admin_icon.jpg", new ArrayList<>());
        admin.setGestor(gestor);
        gestor.getUsers().add(user);
        gestor.getUsers().add(admin);

        // Cargar datos
        String rutaDatos = "datos" + File.separator + "civilizaciones.txt";
        File archivo = new File(rutaDatos);
        if (archivo.exists()) {
            System.out.println("✅ Cargando datos desde: " + rutaDatos);
            GestorDatosTXT.cargarDesdeArchivo(gestor, rutaDatos);
        } else {
            System.out.println("⚠️ Archivo no encontrado. Iniciando con datos vacíos.");
        }

        // Guardar al salir
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("💾 Guardando cambios...");
            GestorDatosTXT.guardarEnArchivo(gestor, rutaDatos);
        }));

        // Iniciar interfaz
        java.awt.EventQueue.invokeLater(() -> {
            new LoginFrame(gestor).setVisible(true);
        });
    }
}

