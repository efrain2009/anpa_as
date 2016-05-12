package com.anpa.anpacr.domain;

import java.io.Serializable;

public class GenericListItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4824006939865349715L;
	
	private String _sTitle;
	private String[] _lArreglo;

	public GenericListItem() {
		super();
	}

	public String get_sTitle() {
		return _sTitle;
	}

	public void set_sTitle(String _sTitle) {
		this._sTitle = _sTitle;
	}

	public String[] get_lArreglo() {
		return _lArreglo;
	}

	public void set_lArreglo(String[] _lArreglo) {
		this._lArreglo = _lArreglo;
	}	
}

