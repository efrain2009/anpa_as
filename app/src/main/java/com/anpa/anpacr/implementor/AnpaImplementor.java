package com.anpa.anpacr.implementor;


public class AnpaImplementor {
//private ParqueoDataAccess _dataAccess;
//private static ParqueoImplementor _instancia;
//	
//	private ParqueoImplementor()
//	{
//		_dataAccess = new ParqueoDataAccess();
//	}
//	
//	public static ParqueoImplementor getInstance(){
//		if(_instancia == null)
//			_instancia = new ParqueoImplementor();
//		return _instancia;
//	}
//	
//	/**
//	 * Obtiene la lista de parqueos cercanos
//	 * @return
//	 */
//	public List<Parqueo> obtenerListaParqueosMapa(Double pLatitud, Double pLongitud){
//		_dataAccess.openForWriting();
//		List<Parqueo> lParqueos = _dataAccess.obtenerInfoParqueosMapa(pLatitud, pLongitud);
//		_dataAccess.close();
//		return lParqueos;
//	}
//	
//	/**
//	 * Obtiene la lista todos los parqueosen el sistema
//	 * @return
//	 */
//	public List<Parqueo> obtenerListaTodosParqueosMapa(){
//		_dataAccess.openForWriting();
//		List<Parqueo> lParqueos = _dataAccess.obtenerInfoTodosParqueosMapa();
//		_dataAccess.close();
//		return lParqueos;
//	}
//	
//	/**
//	 * Obtiene la lista de parqueos segun el parametro de busqueda
//	 * @return
//	 */
//	public List<Parqueo> obtenerListaParqueosBusqueda(String pParametro){
//		_dataAccess.openForWriting();
//		List<Parqueo> lParqueos = _dataAccess.obtenerInfoParqueosBusqueda(pParametro);
//		_dataAccess.close();
//		return lParqueos;
//	}
//	
//	
//	/**
//	 * Toma la lista de parqueos provenientes de parse y las almacena en la BD del dispositivo.
//	 * @param lParqueos
//	 */
//	public void insertarParqueos(List<Parqueo> lParqueos){
//		borrarParqueos();
//		_dataAccess.openForWriting();
//		for(Parqueo parqueo:lParqueos){
//			_dataAccess.insertarParqueo(parqueo);
//		}
//		_dataAccess.close();
//	}
//	
//	/**
//	 * Obtiene la info de un parqueo
//	 * @param pIdParqueo
//	 * @return
//	 */
//	public Parqueo obtenerInfoParqueo(String pIdParqueo){
//		_dataAccess.openForReading();
//		Parqueo parqueo = _dataAccess.obtenerInfoParqueo(pIdParqueo);
//		_dataAccess.close();
//		return parqueo;
//	}
//	
//	/**
//	 * Toma la lista de parqueos provenientes de parse y actualiza la cantidad de espacios
//	 * en cada parqueo.
//	 * @param lParqueos
//	 */
//	public void actualizarParqueos(List<Parqueo> lParqueos){
//		_dataAccess.openForWriting();
//		_dataAccess.actualizarEspaciosParqueo(lParqueos);
//		_dataAccess.close();
//	}
//	
//	/**
//	 * Borra todas los parqueos de la BD.
//	 */
//	public void borrarParqueos(){
//		_dataAccess.openForReading();
//		_dataAccess.borrarParqueos();
//		_dataAccess.close();
//	}
}
