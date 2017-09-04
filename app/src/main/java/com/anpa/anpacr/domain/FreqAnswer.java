package com.anpa.anpacr.domain;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class FreqAnswer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4824006939865349715L;
	
	private String _lId;
	private String _spregunta;
	private String _srespuesta;
	private Integer _iorden;
	private Integer _itipo;
	private String _dCreationDate;
	private Integer _iHabilitado;

	public FreqAnswer(String _lId, String _spregunta, String _srespuesta, Integer _iorden, Integer _itipo, String _dCreationDate, Integer iHabilitado) {
		this._lId = _lId;
		this._spregunta = _spregunta;
		this._srespuesta = _srespuesta;
		this._iorden = _iorden;
		this._itipo = _itipo;
		this._dCreationDate = _dCreationDate;
		this._iHabilitado = iHabilitado;
	}

	public FreqAnswer() {
		super();
	}
	
	public String get_lId() {
		return _lId;
	}

	public void set_lId(String _lId) {
		this._lId = _lId;
	}

	public String get_spregunta() {
		return _spregunta;
	}

	public void set_spregunta(String _spregunta) {
		this._spregunta = _spregunta;
	}

	public String get_srespuesta() {
		return _srespuesta;
	}

	public void set_srespuesta(String _srespuesta) {
		this._srespuesta = _srespuesta;
	}

	public Integer get_iorden() {
		return _iorden;
	}

	public void set_iorden(Integer _iorden) {
		this._iorden = _iorden;
	}

	public String get_dCreationDate() {
		return _dCreationDate;
	}

	public void set_dCreationDate(String _dCreationDate) {
		this._dCreationDate = _dCreationDate;
	}

	public Integer get_itipo() {
		return _itipo;
	}

	public void set_itipo(Integer _itipo) {
		this._itipo = _itipo;
	}

	public Integer get_iHabilitado() {
		return _iHabilitado;
	}

}

