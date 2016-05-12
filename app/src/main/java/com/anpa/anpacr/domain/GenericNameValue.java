package com.anpa.anpacr.domain;

import java.io.Serializable;

/**
 * Clase para cargar cosas con el patron "Catalogo - Valor"
 * @author Efrain
 * @date 19/01/16
 */
public class GenericNameValue implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private int value;
	
	public GenericNameValue(String name, int value) {
		super();
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}

