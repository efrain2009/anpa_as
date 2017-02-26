package com.anpa.anpacr.domain;

import java.io.Serializable;
import java.util.Date;

public class Miscelaneo implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 4824006939865349715L;

	private String id;
	private String direccion;
	private String correo1;
	private String correo2;
	private String telefono1;
	private String telefono2;
	private String url;
	private String facebook;

	public Miscelaneo() {
		super();
	}

	public Miscelaneo(String id, String direccion, String correo1, String correo2, String telefono1, String telefono2, String url, String facebook) {
		this.id = id;
		this.direccion = direccion;
		this.correo1 = correo1;
		this.correo2 = correo2;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.url = url;
		this.facebook = facebook;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo1() {
		return correo1;
	}

	public void setCorreo1(String correo1) {
		this.correo1 = correo1;
	}

	public String getCorreo2() {
		return correo2;
	}

	public void setCorreo2(String correo2) {
		this.correo2 = correo2;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}
}

