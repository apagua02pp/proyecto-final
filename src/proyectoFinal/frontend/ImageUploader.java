package proyectoFinal.frontend;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUploader {

    // ✅ Carpeta raíz: ./recursos/ (sin subcarpeta "imagenes")
    private static final String CARPETA_IMAGENES = "recursos";

    static {
        // Crear carpeta si no existe
        new File(CARPETA_IMAGENES).mkdirs();
    }

    /**
     * Abre un diálogo para seleccionar una imagen y la copia a ./recursos/
     * @return nombre del archivo copiado (ej. "gilgamesh.jpg"), o null si canceló
     */
    public static String seleccionarYCopiarImagen(JFrame parent) {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Imágenes (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));
        fc.setAcceptAllFileFilterUsed(false);

        int result = fc.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = fc.getSelectedFile();
            String nombreOriginal = archivoSeleccionado.getName();
            String extension = getExtension(nombreOriginal).toLowerCase();

            // Validar extensión
            if (!extension.equals("jpg") && !extension.equals("jpeg") &&
                !extension.equals("png") && !extension.equals("gif")) {
                JOptionPane.showMessageDialog(parent,
                    "Formato no soportado. Solo JPG, PNG o GIF.", "Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }

            // Generar nombre único
            String nombreUnico = generarNombreUnico(nombreOriginal);

            try {
                Path destino = Paths.get(CARPETA_IMAGENES, nombreUnico);
                Files.copy(archivoSeleccionado.toPath(), destino, StandardCopyOption.REPLACE_EXISTING);
                return nombreUnico;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent,
                    "Error al copiar la imagen: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return null;
            }
        }
        return null; // Canceló
    }

    // Método auxiliar: devuelve ruta completa para usar en JLabel.setIcon()
    public static String getRutaCompleta(String nombreImagen) {
        if (nombreImagen == null || nombreImagen.trim().isEmpty()) return null;
        return CARPETA_IMAGENES + File.separator + nombreImagen;
    }

    // Genera nombre único (para evitar sobreescribir)
    private static String generarNombreUnico(String nombre) {
        String nombreSinExt = nombre.substring(0, nombre.lastIndexOf('.'));
        String ext = getExtension(nombre);
        String nuevoNombre = nombre;
        int contador = 1;
        while (new File(CARPETA_IMAGENES, nuevoNombre).exists()) {
            nuevoNombre = nombreSinExt + "_" + contador + "." + ext;
            contador++;
        }
        return nuevoNombre;
    }

    private static String getExtension(String nombre) {
        int i = nombre.lastIndexOf('.');
        return (i > 0) ? nombre.substring(i + 1) : "";
    }
}