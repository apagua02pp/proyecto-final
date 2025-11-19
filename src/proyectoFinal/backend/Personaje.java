package proyectoFinal.backend;

import java.util.Date;

import javax.swing.JOptionPane;

public class Personaje implements Informacion {
	//Atributos
	private Date fechaNac;
	private String nombre;
	private String aportes;
	private String fotoPers;
	
	//Constructor
	public Personaje(Date fechaNac, String nombre, String aportes, String fotoPers) {
		super();
		this.fechaNac = fechaNac;
		this.nombre = nombre;
		this.aportes = aportes;
		this.fotoPers = fotoPers;
	}
	
	//Getters y Setters
	public Date getFechaNac() {
		return fechaNac;
	}

	public void setFechaNac(Date fechaNac) {
		this.fechaNac = fechaNac;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAportes() {
		return aportes;
	}

	public void setAportes(String aportes) {
		this.aportes = aportes;
	}

	public String getFotoPers() {
		return fotoPers;
	}

	public void setFotoPers(String fotoPers) {
		this.fotoPers = fotoPers;
	}

	//Métodos
	@Override
	public String mostrarInformacion() {
		return "*****PERSONAJE*****\n" 
				+ "-Nombre: " + nombre
				+ "\n-Fecha de nacimiento: " + fechaNac
				+ "\n-Aportes: " + aportes
				+ "\n-Foto: " + fotoPers;
	}
	
	
	
	
}
