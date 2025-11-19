package proyectoFinal.backend;

import java.util.ArrayList;

public class Civilizacion implements Informacion, AgregarInformacion {
	//Atributos
	private String nombre;
	private String region;
	private String epoca;
	private String descripcion;
	private String fotoCiv;
	private ArrayList<Personaje> personaje;
	private ArrayList<Evento> evento;
	
	//Constructor (con composición)
	public Civilizacion(String nombre, String region, String epoca, String descripcion, String fotoCiv,
			ArrayList<Personaje> personaje, ArrayList<Evento> evento) {
		super();
		this.nombre = nombre;
		this.region = region;
		this.epoca = epoca;
		this.descripcion = descripcion;
		this.fotoCiv = fotoCiv;
		this.personaje = personaje;
		this.evento = evento;
	}
	
	//Getters y Setters
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getEpoca() {
		return epoca;
	}

	public void setEpoca(String epoca) {
		this.epoca = epoca;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getFotoCiv() {
		return fotoCiv;
	}

	public void setFotoCiv(String fotoCiv) {
		this.fotoCiv = fotoCiv;
	}

	public ArrayList<Personaje> getPersonaje() {
		return personaje;
	}

	public void setPersonaje(ArrayList<Personaje> personaje) {
		this.personaje = personaje;
	}

	public ArrayList<Evento> getEvento() {
		return evento;
	}

	public void setEvento(ArrayList<Evento> evento) {
		this.evento = evento;
	}

	//Métodos
	@Override
	public String mostrarInformacion() {
		 StringBuilder sb = new StringBuilder();

		    sb.append("===== CIVILIZACIÓN =====\n")
		      .append("Nombre: ").append(nombre).append("\n")
		      .append("Región: ").append(region).append("\n")
		      .append("Época: ").append(epoca).append("\n")
		      .append("Descripción: ").append(descripcion).append("\n")
		      .append("Foto: ").append(fotoCiv).append("\n\n");

		    sb.append("---- Personajes principales ----\n");
		    if (personaje != null && !personaje.isEmpty()) {
		        for (Personaje p : personaje) {
		            sb.append(p.mostrarInformacion()).append("\n");
		        }
		    } else {
		        sb.append("No hay personajes registrados.\n");
		    }

		    sb.append("\n---- Eventos principales ----\n");
		    if (evento != null && !evento.isEmpty()) {
		        for (Evento e : evento) {
		            sb.append(e.mostrarInformacion()).append("\n");
		        }
		    } else {
		        sb.append("No hay eventos registrados.\n");
		    }

		    return sb.toString();
	}

	@Override
	public void agregarPersonaje(Personaje p) {
		if (personaje == null) {
	        personaje = new ArrayList<>();
	    }
	    personaje.add(p);
	}

	@Override
	public void agregarEvento(Evento e) {
		if (evento == null) {
	        evento = new ArrayList<>();
	    }
	    evento.add(e);
	}
	
}