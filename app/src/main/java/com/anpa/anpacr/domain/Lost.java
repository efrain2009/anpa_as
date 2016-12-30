package com.anpa.anpacr.domain;

import java.io.Serializable;

public class Lost implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4824006939865349715L;
	
	private String _lId;
	private String _snombreMascota;
	private String _snombreDueno;
	private String _stelefono;
	private String _sdetalle;
	private Integer _iprovinvia;
	private Integer _icanton;
	private String _slatitud;
	private String _slongitud;
	private String _sraza;
	private String _sDate;
	private byte[] _bFoto;
	private Integer _iHabilitado;

	public Lost() {
		super();
	}

	public Lost(String sIdLost, String sNomMascota, String sNomDueno,
			String sTelefono, int iProvincia, int iCanton,
			String sDetalle, String sRaza, String sDate, byte[] bFoto, String sLatitud, String sLongitud,Integer _iHabilitado) {
		// TODO Auto-generated constructor stub
		this._lId = sIdLost;
		this._snombreMascota = sNomMascota;
		this._snombreDueno = sNomDueno;
		this._stelefono = sTelefono;
		this._sdetalle = sDetalle;
		this._iprovinvia = iProvincia;
		this._icanton = iCanton;
		this._sraza = sRaza;
		this._sDate = sDate;
		this._bFoto = bFoto;
		this._slatitud = sLatitud;
		this._slongitud = sLongitud;
		this._iHabilitado = _iHabilitado;
	}

	public String get_lId() {
		return _lId;
	}

	public void set_lId(String _lId) {
		this._lId = _lId;
	}

	public String get_snombreMascota() {
		return _snombreMascota;
	}

	public void set_snombreMascota(String _snombreMascota) {
		this._snombreMascota = _snombreMascota;
	}

	public String get_snombreDueno() {
		return _snombreDueno;
	}

	public void set_snombreDueno(String _snombreDueno) {
		this._snombreDueno = _snombreDueno;
	}

	public String get_stelefono() {
		return _stelefono;
	}

	public void set_stelefono(String _stelefono) {
		this._stelefono = _stelefono;
	}

	public String get_sdetalle() {
		return _sdetalle;
	}

	public void set_sdetalle(String _sdetalle) {
		this._sdetalle = _sdetalle;
	}

	public Integer get_iprovinvia() {
		return _iprovinvia;
	}

	public void set_iprovinvia(Integer _iprovinvia) {
		this._iprovinvia = _iprovinvia;
	}

	public Integer get_icanton() {
		return _icanton;
	}

	public void set_icanton(Integer _icanton) {
		this._icanton = _icanton;
	}

	public String get_slatitud() {
		return _slatitud;
	}

	public void set_slatitud(String _slatitud) {
		this._slatitud = _slatitud;
	}

	public String get_slongitud() {
		return _slongitud;
	}

	public void set_slongitud(String _slongitud) {
		this._slongitud = _slongitud;
	}

	public String get_sDate() {
		return _sDate;
	}

	public void set_sDate(String _sDate) {
		this._sDate = _sDate;
	}

	public byte[] get_bFoto() {
		return _bFoto;
	}

	public void set_bFoto(byte[] _bFoto) {
		this._bFoto = _bFoto;
	}

	public String get_sraza() {
		return _sraza;
	}

	public void set_sraza(String _sraza) {
		this._sraza = _sraza;
	}

	public Integer get_iHabilitado() {
		return _iHabilitado;
	}

	public void set_iHabilitado(Integer _iHabilitado) {
		this._iHabilitado = _iHabilitado;
	}
}
