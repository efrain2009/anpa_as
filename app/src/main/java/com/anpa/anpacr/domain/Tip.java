package com.anpa.anpacr.domain;

import java.io.Serializable;
import java.util.Date;

public class Tip implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4824006939865349715L;
	
	private String _lId;
	private String _sAuthor;
	private String _sConsejo;
	private Date _dDate;
	private Integer _i1Estrella;
	private Integer _i2Estrella;
	private Integer _i3Estrella;
	private Integer _i4Estrella;
	private Integer _i5Estrella;
	private Integer _iTotalVotos;
	private Integer _iRaza;
	private Integer _iEspecie;
	private String _sDate;
	private Integer _iEstado;
	private Integer _iHabilitado;
	
	public Tip() {
		super();
	}

	public Tip(String _lId, String _sAuthor, String _sConsejo, Date _dDate, Integer _i1Estrella, Integer _i2Estrella, Integer _i3Estrella, Integer _i4Estrella, Integer _i5Estrella, Integer _iTotalVotos, Integer _iRaza, Integer _iEspecie, String _sDate, Integer _iEstado, Integer iHabilitado) {
		this._lId = _lId;
		this._sAuthor = _sAuthor;
		this._sConsejo = _sConsejo;
		this._dDate = _dDate;
		this._i1Estrella = _i1Estrella;
		this._i2Estrella = _i2Estrella;
		this._i3Estrella = _i3Estrella;
		this._i4Estrella = _i4Estrella;
		this._i5Estrella = _i5Estrella;
		this._iTotalVotos = _iTotalVotos;
		this._iRaza = _iRaza;
		this._iEspecie = _iEspecie;
		this._sDate = _sDate;
		this._iEstado = _iEstado;
		this._iHabilitado = iHabilitado;
	}

	public String get_lId() {
		return _lId;
	}


	public void set_lId(String _lId) {
		this._lId = _lId;
	}


	public String get_sAuthor() {
		return _sAuthor;
	}
	public void set_sAuthor(String _sAuthor) {
		this._sAuthor = _sAuthor;
	}
	public String get_sConsejo() {
		return _sConsejo;
	}
	public void set_sConsejo(String _sConsejo) {
		this._sConsejo = _sConsejo;
	}

	public Date get_dDate() {
		return _dDate;
	}

	public void set_dDate(Date _dDate) {
		this._dDate = _dDate;
	}

	public String get_sDate() {
		return _sDate;
	}

	public void set_sDate(String _sDate) {
		this._sDate = _sDate;
	}

	public Integer get_i1Estrella() {
		return _i1Estrella;
	}
	public void set_i1Estrella(Integer _i1Estrella) {
		this._i1Estrella = _i1Estrella;
	}
	public Integer get_i2Estrella() {
		return _i2Estrella;
	}
	public void set_i2Estrella(Integer _i2Estrella) {
		this._i2Estrella = _i2Estrella;
	}
	public Integer get_i3Estrella() {
		return _i3Estrella;
	}
	public void set_i3Estrella(Integer _i3Estrella) {
		this._i3Estrella = _i3Estrella;
	}
	public Integer get_i4Estrella() {
		return _i4Estrella;
	}
	public void set_i4Estrella(Integer _i4Estrella) {
		this._i4Estrella = _i4Estrella;
	}
	public Integer get_i5Estrella() {
		return _i5Estrella;
	}
	public void set_i5Estrella(Integer _i5Estrella) {
		this._i5Estrella = _i5Estrella;
	}
	public Integer get_iRaza() {
		return _iRaza;
	}
	public void set_iRaza(Integer _iRaza) {
		this._iRaza = _iRaza;
	}
	public Integer get_iEspecie() {
		return _iEspecie;
	}
	public void set_iEspecie(Integer _iEspecie) {
		this._iEspecie = _iEspecie;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Integer get_iTotalVotos() {
		return _iTotalVotos;
	}
	public void set_iTotalVotos(Integer _iTotalVotos) {
		this._iTotalVotos = _iTotalVotos;
	}

	public Integer get_iEstado() {
		return _iEstado;
	}

	public void set_iEstado(Integer _iEstado) {
		this._iEstado = _iEstado;
	}

	public Integer get_iHabilitado() {
		return _iHabilitado;
	}

	public void set_iHabilitado(Integer _iHabilitado) {
		this._iHabilitado = _iHabilitado;
	}
}

