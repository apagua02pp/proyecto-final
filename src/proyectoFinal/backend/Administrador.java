package proyectoFinal.backend;

import java.util.ArrayList;

public class Administrador extends Usuario implements AgregarInformacion {
	//Atributo
	 private GestorEnciclopedia gestor;
	
	//Constructor (de herencia)
	public Administrador(String nombreUsuario, String password, String fotoUsuario, ArrayList<Civilizacion> civFav) {
		super(nombreUsuario, password, fotoUsuario, civFav);
		this.gestor = gestor;
	}

	//Getters y setters
	public GestorEnciclopedia getGestor() {
		return gestor;
	}

	public void setGestor(GestorEnciclopedia gestor) {
		this.gestor = gestor;
	}

	//Métodos
	 public void agregarCivilizacion(Civilizacion c) {
	        gestor.agregarCivilizacion(c);
	    }

	    public void editarCivilizacion(Civilizacion civ, String nuevoNombre, String nuevaDesc) {
	        civ.setNombre(nuevoNombre);
	        civ.setDescripcion(nuevaDesc);
	    }

	    public void eliminarCivilizacion(Civilizacion c) {
	        gestor.eliminarCivilizacion(c);
	    }
	
	@Override
	public void agregarPersonaje(Personaje p) {
		// TODO Auto-generated method stub
		
	}
	
	public void editarPersonaje() {
			
	}
		
	public void eliminarPersonaje() {
			
	}

	@Override
	public void agregarEvento(Evento e) {
		// TODO Auto-generated method stub
		
	}
	
	public void editarEvento() {
		
	}
		
	public void eliminarEvento() {
			
	}
	

}
