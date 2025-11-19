package proyectoFinal.backend;

import java.util.ArrayList;

public class GestorEnciclopedia {
	//Atributos
	private ArrayList <Civilizacion> civilizaciones;
	private ArrayList <Usuario> users;
	
	//Constructor
	public GestorEnciclopedia() {
		this.civilizaciones = new ArrayList<>();
		this.users = new ArrayList<>();
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
		if (!civilizaciones.contains(c))
            civilizaciones.add(c);
	}
	
	public void eliminarCivilizacion(Civilizacion c) {
		civilizaciones.remove(c);
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
}
