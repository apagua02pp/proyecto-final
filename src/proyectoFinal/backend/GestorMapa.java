package proyectoFinal.backend;

import java.io.*;
import java.util.HashMap;

public class GestorMapa {
    // Archivo donde guardaremos las relaciones (región -> civilización)
    private static final String ARCHIVO_DATOS = "datos/mapa_relaciones.dat";
    
    // Mapa: Clave (Índice de la región en la imagen) -> Valor (Nombre de la Civilización)
    private HashMap<Integer, String> relaciones;

    public GestorMapa() {
        relaciones = new HashMap<>();
        cargarDatos();
    }

    /**
     * Vincula una región del mapa con una civilización.
     */
    public void asignarCivilizacion(int indiceRegion, String nombreCivilizacion) {
        relaciones.put(indiceRegion, nombreCivilizacion);
        guardarDatos();
    }

    /**
     * Obtiene el nombre de la civilización asignada a una región.
     * Retorna null si la región está vacía.
     */
    public String obtenerCivilizacion(int indiceRegion) {
        return relaciones.get(indiceRegion);
    }

    /**
     * Elimina la asignación de una región.
     */
    public void eliminarAsignacion(int indiceRegion) {
        relaciones.remove(indiceRegion);
        guardarDatos();
    }

    /**
     * Verifica si una región ya tiene dueño.
     */
    public boolean estaOcupada(int indiceRegion) {
        return relaciones.containsKey(indiceRegion);
    }

    private void guardarDatos() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO_DATOS))) {
            oos.writeObject(relaciones);
        } catch (IOException e) {
            System.err.println("Error al guardar datos del mapa: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
                relaciones = (HashMap<Integer, String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar datos del mapa: " + e.getMessage());
            }
        } else {
            // Asegurar que la carpeta exista
            new File("datos").mkdirs();
        }
    }
}