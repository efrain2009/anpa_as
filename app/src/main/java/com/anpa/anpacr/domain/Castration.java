package com.anpa.anpacr.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Castration implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1218064557691828487L;
	
	private String _lId;
	private String _snombre;
	private String _sdoctor;
	private Double _bgdMonto;
	private String _sdireccion;
	private String _sdescription;
	private String _sEncargado;
	private String _sDateInicio;
	private String _sDateFin;
	private Date _dDateInicio;
	private Date _dDateFin;
	private String _sDateFormat;
	private Integer _sTipo;
	private String _sDate;
	private String _sLatitud;
	private String _sLongitud;
	private byte[] _bImagen;
	private Integer habilitado;
	private Integer muestraMonto;

			
	public Castration() {
		super();
	}

	public Castration(String _lId, String _snombre, String _sdoctor,
			Double _bgdMonto, String _sdireccion, String _sdescription,
			String _sEncargado, String _sDateInicio, String _sDateFin,
			Integer _sTipo, String _sDate, String _sLatitud, String _sLongitud,
			byte[] _bImagen, Integer habilitado, Integer muestraMonto) {
		super();
		this._lId = _lId;
		this._snombre = _snombre;
		this._sdoctor = _sdoctor;
		this._bgdMonto = _bgdMonto;
		this._sdireccion = _sdireccion;
		this._sdescription = _sdescription;
		this._sEncargado = _sEncargado;
		this._sDateInicio = _sDateInicio;
		this._sDateFin = _sDateFin;
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            this._dDateInicio = formatter.parse(_sDateInicio);
            this._dDateFin = formatter.parse(_sDateFin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
		this._sTipo = _sTipo;
		this._sDate = _sDate;
		this._sLatitud = _sLatitud;
		this._sLongitud = _sLongitud;
		this._bImagen = _bImagen;
		this.habilitado = habilitado;
		this.muestraMonto = muestraMonto;
	}

	public Integer getMuestraMonto() {
		return muestraMonto;
	}

	public void setMuestraMonto(Integer muestraMonto) {
		this.muestraMonto = muestraMonto;
	}

	public String get_sLatitud() {
		return _sLatitud;
	}



	public void set_sLatitud(String _sLatitud) {
		this._sLatitud = _sLatitud;
	}



	public String get_sLongitud() {
		return _sLongitud;
	}



	public void set_sLongitud(String _sLongitud) {
		this._sLongitud = _sLongitud;
	}



	public String get_sEncargado() {
		return _sEncargado;
	}

	public void set_sEncargado(String _sEncargado) {
		this._sEncargado = _sEncargado;
	}

	public String get_sDateInicio() {
		return _sDateInicio;
	}

	public void set_sDateInicio(String _sDateInicio) {
		this._sDateInicio = _sDateInicio;
	}

	public String get_sDateFin() {
		return _sDateFin;
	}

	public void set_sDateFin(String _sDateFin) {
		this._sDateFin = _sDateFin;
	}

	public Integer get_sTipo() {
		return _sTipo;
	}

	public void set_sTipo(Integer _sTipo) {
		this._sTipo = _sTipo;
	}

	public String get_sDate() {
		return _sDate;
	}

	public void set_sDate(String _sDate) {
		this._sDate = _sDate;
	}

	public String get_snombre() {
		return _snombre;
	}

	public void set_snombre(String _snombre) {
		this._snombre = _snombre;
	}

	public String get_sdescription() {
		return _sdescription;
	}

	public void set_sdescription(String _sdescription) {
		this._sdescription = _sdescription;
	}

	public byte[] get_bImagen() {
		return _bImagen;
	}

	public void set_bImagen(byte[] _bImagen) {
		this._bImagen = _bImagen;
	}

	public String get_sdoctor() {
		return _sdoctor;
	}

	public void set_sdoctor(String _sdoctor) {
		this._sdoctor = _sdoctor;
	}

	public String get_sdireccion() {
		return _sdireccion;
	}

	public void set_sdireccion(String _sdireccion) {
		this._sdireccion = _sdireccion;
	}

	public String get_lId() {
		return _lId;
	}

	public void set_lId(String _lId) {
		this._lId = _lId;
	}

	public Double get_bgdMonto() {
		return _bgdMonto;
	}

	public void set_bgdMonto(Double _bgdMonto) {
		this._bgdMonto = _bgdMonto;
	}

	public String get_sDateFormat() {
		return _sDateFormat;
	}

	public void set_sDateFormat(String _sDateFormat) {
		this._sDateFormat = _sDateFormat;
	}

	public Date get_dDateInicio() {
		return _dDateInicio;
	}

	public void set_dDateInicio(Date _dDateInicio) {
		this._dDateInicio = _dDateInicio;
	}

	public Date get_dDateFin() {
		return _dDateFin;
	}

	public void set_dDateFin(Date _dDateFin) {
		this._dDateFin = _dDateFin;
	}

	public Integer getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(Integer habilitado) {
		this.habilitado = habilitado;
	}
}

