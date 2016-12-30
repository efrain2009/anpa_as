package com.anpa.anpacr.domain;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4824006939865349715L;
	
	private String _lId;
	private String _stitle;
	private String _sdescription;
	private String _sDate;
	private Date _dDate;
	private byte[] _bImagen;
	private Integer _iHabilitado;

	public News() {
		super();
	}
	
	public News(String _lId, String _stitle, String _sdescription, String _sDate,
			byte[] _bImagen, Date _dDate, Integer _iHabilitado) {
		super();
		this._lId = _lId;
		this._stitle = _stitle;
		this._sdescription = _sdescription;
		this._sDate = _sDate;
		this._bImagen = _bImagen;
		this._dDate = _dDate;
		this._iHabilitado = _iHabilitado;
	}



	public String get_sDate() {
		return _sDate;
	}

	public void set_sDate(String _sDate) {
		this._sDate = _sDate;
	}



	public String get_lId() {
		return _lId;
	}

	public void set_lId(String _lId) {
		this._lId = _lId;
	}

	public String get_stitle() {
		return _stitle;
	}

	public void set_stitle(String _stitle) {
		this._stitle = _stitle;
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

	public Date get_dDate() {
		return _dDate;
	}

	public void set_dDate(Date _dDate) {
		this._dDate = _dDate;
	}
	
}
