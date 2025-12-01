package proyectoFinal.backend;

import java.io.*;
import java.util.ArrayList;

public class GestorEnciclopedia {
	
	private static final String ARCHIVO_CIVILIZACIONES = "datos/civilizaciones.txt";
	
	//Atributos
	private ArrayList <Civilizacion> civilizaciones;
	private ArrayList <Usuario> users;
	
	//Constructor
	public GestorEnciclopedia() {
		this.civilizaciones = new ArrayList<>();
		this.users = new ArrayList<>();
		cargarDatos();
	}
	
	//Getters y Setters
	public ArrayList<Civilizacion> getCivilizaciones() {
		return civilizaciones;
	}
	public void setCivilizaciones(ArrayList<Civilizacion> civilizaciones) {
		this.civilizaciones = civilizaciones;
	}
	public ArrayList<Usuario> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<Usuario> users) {
		this.users = users;
	}
	
	//Métodos==========================================
	public void buscarPorCriterio() {
		//TODO: Integrar con el panel de swing.
	}
	
	public Civilizacion buscarCivilizacion(String nombre) {
	     for (Civilizacion c : civilizaciones) {
	         if (c.getNombre().equalsIgnoreCase(nombre))
	             return c;
	     }
	     return null;
	}
	
	public void agregarCivilizacion(Civilizacion c) {
        if (!civilizaciones.contains(c)) {
            civilizaciones.add(c);
            guardarDatos(); // <--- IMPORTANTE: Guardar cada vez que agregas
        }
    }
	
	public void eliminarCivilizacion(Civilizacion c) {
		civilizaciones.remove(c);
		guardarDatos();
	}
	
	private void guardarDatos() {
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO_CIVILIZACIONES))) {
	        for (Civilizacion c : civilizaciones) {
	            // 1. Guardamos la Civilización con la etiqueta |CIV|
	            StringBuilder lineaCiv = new StringBuilder();
	            lineaCiv.append("CIV|") // <--- ETIQUETA IMPORTANTE
	                    .append(c.getNombre()).append("|")
	                    .append(c.getRegion()).append("|")
	                    .append(c.getEpoca()).append("|")
	                    .append(c.getDescripcion().replace("\n", " ")).append("|")
	                    .append(c.getFotoCiv() != null ? c.getFotoCiv() : "SinFoto");
	            
	            bw.write(lineaCiv.toString());
	            bw.newLine();

	            // 2. Guardamos los Personajes de esta civilización con la etiqueta |PERS|)
	            if (c.getPersonaje() != null) {
	                for (Personaje p : c.getPersonaje()) {
	                    String lineaPers = "PERS|" + 
	                                       p.getNombre() + "|" +
	                                       p.getAportes() + "|" +
	                                       (p.getFechaNac() != null ? p.getFechaNac().getTime() : "0") + "|" + // Guardamos fecha como numero
	                                       (p.getFotoPers() != null ? p.getFotoPers() : "SinFoto");
	                    bw.write(lineaPers);
	                    bw.newLine();
	                }
	            }

	            // 3. Guardamos los Eventos de esta civilización con la etiqueta |EVT|)
	            if (c.getEvento() != null) {
	                for (Evento e : c.getEvento()) {
	                    String lineaEvt = "EVT|" + 
	                                      e.getTitulo() + "|" +
	                                      e.getDescripcion() + "|" +
	                                      (e.getFecha() != null ? e.getFecha().getTime() : "0"); // Guardamos fecha como numero
	                    bw.write(lineaEvt);
	                    bw.newLine();
	                }
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error al guardar datos: " + e.getMessage());
	    }
	}
	
	private void cargarDatos() {
	    File archivo = new File(ARCHIVO_CIVILIZACIONES);
	    if (!archivo.exists()) {
	         new File("datos").mkdirs();
	         return;
	    }

	    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
	        String linea;
	        Civilizacion civActual = null; 

	        while ((linea = br.readLine()) != null) {
	            String[] partes = linea.split("\\|");
	            
	            // Si la línea está vacía o incompleta, la saltamos
	            if (partes.length < 2) continue;

	            try {
	                String tipo = partes[0]; 

	                switch (tipo) {
	                    case "CIV":
	                        if (partes.length >= 5) { // Verificamos longitud mínima
	                            String nombre = partes[1];
	                            String region = partes[2];
	                            String epoca = partes[3];
	                            String descripcion = partes[4];
	                            String foto = (partes.length > 5) ? partes[5] : ""; // Foto es opcional
	                            
	                            civActual = new Civilizacion(nombre, region, epoca, descripcion, foto, new ArrayList<>(), new ArrayList<>());
	                            civilizaciones.add(civActual);
	                        }
	                        break;

	                    case "PERS":
	                        if (civActual != null && partes.length >= 4) {
	                            String nomPers = partes[1];
	                            String aportes = partes[2];
	                            // Usamos try-catch específico por si la fecha no es un número
	                            long tiempo = Long.parseLong(partes[3]); 
	                            java.util.Date fecha = new java.util.Date(tiempo);
	                            String fotoPers = (partes.length > 4) ? partes[4] : "";

	                            Personaje p = new Personaje(fecha, nomPers, aportes, fotoPers);
	                            civActual.agregarPersonaje(p);
	                        }
	                        break;

	                    case "EVT":
	                        if (civActual != null && partes.length >= 4) {
	                            String titulo = partes[1];
	                            String descEvt = partes[2];
	                            // Aquí es donde te daba el error:
	                            long tiempoEvt = Long.parseLong(partes[3]);
	                            java.util.Date fechaEvt = new java.util.Date(tiempoEvt);

	                            Evento e = new Evento(fechaEvt, titulo, descEvt);
	                            civActual.agregarEvento(e);
	                        }
	                        break;
	                }
	            } catch (NumberFormatException e) {
	                // Si falla una fecha, mostramos error pero NO detenemos el programa
	                System.err.println("Error de formato numérico en línea ignorada: " + linea);
	            } catch (Exception e) {
	                System.err.println("Error inesperado en línea: " + linea);
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error general al leer archivo: " + e.getMessage());
	    }
	}

	public Usuario buscarUsuario(String nombre) {
		for (Usuario u : users) {
            if (u.getNombreUsuario().equalsIgnoreCase(nombre))
                return u;
        }
        return null;
		}
	
	public void mostrarLineaTiempo() {
		//TODO: Ordenar eventos por fecha y construir un String.
	}
	
	public void gestionarDatos() {
		//TODO: Integrar con JSON?
	}

	public void validarDatos() {
		//TODO: Verificar campos obligatorios.
	}
	
	public void guardarCambios() {
	    guardarDatos(); // Llama a tu método privado que escribe el TXT
	}
}
