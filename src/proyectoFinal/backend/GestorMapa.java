package proyectoFinal.backend;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GestorMapa {
	//genera el archivo donde guarda la relacion indice/civilización 
    private static final String ARCHIVO_DATOS = "datos/mapa_relaciones.txt";
    
    //relaciona el indice con el numero de civilización
    private HashMap<Integer, String> relaciones;

    public GestorMapa() {
        relaciones = new HashMap<>();
        cargarDatos();
    }


    public void asignarCivilizacion(int indiceRegion, String nombreCivilizacion) {
        relaciones.put(indiceRegion, nombreCivilizacion);
        guardarDatos();
    }

    public String obtenerCivilizacion(int indiceRegion) {
        return relaciones.get(indiceRegion);
    }

    public void eliminarAsignacion(int indiceRegion) {
        relaciones.remove(indiceRegion);
        guardarDatos();
    }

    public boolean estaOcupada(int indiceRegion) {
        return relaciones.containsKey(indiceRegion);
    }



    private void guardarDatos() {

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_DATOS))) {
            for (Map.Entry<Integer, String> entry : relaciones.entrySet()) {
                //guarda indice/civilización

                bw.write(entry.getKey() + "|" + entry.getValue());
                bw.newLine(); 
            }
        } catch (IOException e) {
            System.err.println("Error al guardar mapa txt: " + e.getMessage());
        }
    }

    private void cargarDatos() {
        File archivo = new File(ARCHIVO_DATOS);
        if (!archivo.exists()) {
            //crear la carpeta en caso de que no exista para asegurar el funcinamiento
            new File("datos").mkdirs();
            return; 
        }

        //lee el texto línea por línea
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Separamos usando el caracter '|'
            
                String[] partes = linea.split("\\|");
                
                if (partes.length >= 2) {
                    try {
                        int idRegion = Integer.parseInt(partes[0]);
                        String nombreCiv = partes[1];
                        relaciones.put(idRegion, nombreCiv);
                    } catch (NumberFormatException e) {
                        System.err.println("Línea corrupta en mapa ignorada: " + linea);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error al cargar mapa txt: " + e.getMessage());
        }
    }
}