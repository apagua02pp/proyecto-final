package proyectoFinal.backend;

import java.io.*;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;

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
	    // Usamos OutputStreamWriter para forzar UTF-8
	    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
	            new FileOutputStream(ARCHIVO_CIVILIZACIONES), StandardCharsets.UTF_8))) {
	        
	        for (Civilizacion c : civilizaciones) {
	            StringBuilder lineaCiv = new StringBuilder();
	            lineaCiv.append("CIV|")
	                    .append(c.getNombre()).append("|")
	                    .append(c.getRegion()).append("|")
	                    .append(c.getEpoca()).append("|")
	                    .append(c.getDescripcion().replace("\n", " ")).append("|")
	                    .append(c.getFotoCiv() != null ? c.getFotoCiv() : "SinFoto");
	            
	            bw.write(lineaCiv.toString());
	            bw.newLine();

	            if (c.getPersonaje() != null) {
	                for (Personaje p : c.getPersonaje()) {
	                    String lineaPers = "PERS|" + 
	                                       p.getNombre() + "|" +
	                                       p.getAportes() + "|" +
	                                       (p.getFechaNac() != null ? p.getFechaNac().getTime() : "0") + "|" + 
	                                       (p.getFotoPers() != null ? p.getFotoPers() : "SinFoto");
	                    bw.write(lineaPers);
	                    bw.newLine();
	                }
	            }

	            if (c.getEvento() != null) {
	                for (Evento e : c.getEvento()) {
	                    String lineaEvt = "EVT|" + 
	                                      e.getTitulo() + "|" +
	                                      e.getDescripcion() + "|" +
	                                      (e.getFecha() != null ? e.getFecha().getTime() : "0");
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

	    // Usamos InputStreamReader para forzar UTF-8
	    try (BufferedReader br = new BufferedReader(new InputStreamReader(
	            new FileInputStream(archivo), StandardCharsets.UTF_8))) {
	        
	        String linea;
	        Civilizacion civActual = null; 

	        while ((linea = br.readLine()) != null) {
	            String[] partes = linea.split("\\|");
	            
	            if (partes.length < 2) continue;

	            try {
	                String tipo = partes[0]; 

	                switch (tipo) {
	                    case "CIV":
	                        if (partes.length >= 5) { 
	                            String nombre = partes[1];
	                            String region = partes[2];
	                            String epoca = partes[3];
	                            String descripcion = partes[4];
	                            String foto = (partes.length > 5) ? partes[5] : "";
	                            
	                            civActual = new Civilizacion(nombre, region, epoca, descripcion, foto, new ArrayList<>(), new ArrayList<>());
	                            civilizaciones.add(civActual);
	                        }
	                        break;

	                    case "PERS":
	                        if (civActual != null && partes.length >= 4) {
	                            String nomPers = partes[1];
	                            String aportes = partes[2];
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
	                            long tiempoEvt = Long.parseLong(partes[3]);
	                            java.util.Date fechaEvt = new java.util.Date(tiempoEvt);

	                            Evento e = new Evento(fechaEvt, titulo, descEvt);
	                            civActual.agregarEvento(e);
	                        }
	                        break;
	                }
	            } catch (Exception e) {
	                System.err.println("Error leyendo línea: " + linea);
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
