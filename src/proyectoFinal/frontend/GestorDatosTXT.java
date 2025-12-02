package proyectoFinal.frontend;

import java.io.*;
import java.util.*;
import proyectoFinal.backend.*;
import java.nio.charset.StandardCharsets;

public class GestorDatosTXT {
    public static void cargarDesdeArchivo(GestorEnciclopedia gestor, String ruta) {
    	try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(ruta), StandardCharsets.UTF_8))) {
                
            String linea;
            Civilizacion civilizacionActual = null;
            List<Personaje> personajesTemp = new ArrayList<>();
            List<Evento> eventosTemp = new ArrayList<>();

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.equals("[CIVILIZACION]")) {
                    // Leer atributos básicos
                    String nombre = leerAtributo(br, "NOMBRE");
                    String region = leerAtributo(br, "REGION");
                    String epoca = leerAtributo(br, "EPOCA");
                    String descripcion = leerAtributo(br, "DESCRIPCION");
                    String foto = leerAtributo(br, "FOTO");

                    personajesTemp.clear();
                    eventosTemp.clear();
                    civilizacionActual = new Civilizacion(nombre, region, epoca, descripcion, foto, new ArrayList<>(), new ArrayList<>());

                } else if (linea.equals("[PERSONAJES]")) {
                    leerPersonajes(br, personajesTemp);
                    // Agregar al final cuando se cierre [/CIVILIZACION]

                } else if (linea.equals("[EVENTOS]")) {
                    leerEventos(br, eventosTemp);

                } else if (linea.equals("[/CIVILIZACION]")) {
                    // Asignar listas a civilización y agregar al gestor
                    civilizacionActual.setPersonaje(new ArrayList<>(personajesTemp));
                    civilizacionActual.setEvento(new ArrayList<>(eventosTemp));
                    gestor.agregarCivilizacion(civilizacionActual);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String leerAtributo(BufferedReader br, String clave) throws IOException {
        String linea = br.readLine().trim();
        if (linea.startsWith(clave + ": ")) {
            return linea.substring((clave + ": ").length());
        } else {
            throw new RuntimeException("Formato inválido: se esperaba '" + clave + "'");
        }
    }

    private static void leerPersonajes(BufferedReader br, List<Personaje> lista) throws IOException {
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.equals("[EVENTOS]") || linea.equals("[/CIVILIZACION]")) {
                br.mark(1000); // Retroceder
                br.reset();
                break;
            }
            if (linea.startsWith("PERSONAJE|")) {
                String[] partes = linea.split("\\|", 5);
                if (partes.length == 5) {
                    String nombre = partes[1];
                    Date fecha = parseDate(partes[2]); // formato: yyyy-MM-dd
                    String aportes = partes[3];
                    String foto = partes[4];
                    lista.add(new Personaje(fecha, nombre, aportes, foto));
                }
            }
        }
    }

    private static void leerEventos(BufferedReader br, List<Evento> lista) throws IOException {
        String linea;
        while ((linea = br.readLine()) != null) {
            linea = linea.trim();
            if (linea.equals("[/CIVILIZACION]")) {
                br.mark(1000);
                br.reset();
                break;
            }
            if (linea.startsWith("EVENTO|")) {
                String[] partes = linea.split("\\|", 4);
                if (partes.length == 4) {
                    String titulo = partes[1];
                    Date fecha = parseDate(partes[2]);
                    String desc = partes[3];
                    lista.add(new Evento(fecha, titulo, desc));
                }
            }
        }
    }

    private static Date parseDate(String fechaStr) {
        try {
            return new java.text.SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
        } catch (Exception e) {
            return new Date(0); // epoch fallback
        }
    }

	public static void guardarEnArchivo(GestorEnciclopedia gestor, String rutaDatos) {
		// TODO Auto-generated method stub
		
	}

   
}