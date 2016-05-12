package com.anpa.anpacr.domain;


import com.anpa.anpacr.common.Constants;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

/**
 * Clase que verifica que el GPS esta activo, si no lo esta, solicita al usuario que lo active.
 * @author eperaza
 * Fecha de creacion 20/09/2013.
 */
public class Gps implements LocationListener{
	private static Gps _instancia;
	public static final int MIN_TIME = 10000;
	public static final int MIN_DISTANCE = 100;
	
	public static Gps getInstance(){
		if(_instancia == null)
			_instancia = new Gps();
		return _instancia;
	}
	
	/**
	 * Se verifica que el GPS esta activo, sino solicita al usuario que lo active.
	 * @param pActivity
	 * @param plocationManager
	 * @return
	 */
	public LocationManager revisarGPS(Activity pActivity, LocationManager plocationManager){
		plocationManager = (LocationManager) pActivity.getSystemService(Context.LOCATION_SERVICE);
		if (plocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			plocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
			return null;
		}
		else{
			Toast.makeText(pActivity.getApplicationContext(), Constants.ERROR_ACTIVAR_GPS, Toast.LENGTH_LONG).show();
			return plocationManager;
		}
	}
	
	/**
	 * Toma los datos de la respuesta del activity y comprueba que el GPS esta activo, sino lanza un mensaje
	 * informando que la operacion no se puede terminar si no se activa.
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @param pActivity
	 * @param plocationManager
	 * @param pMensajeError
	 * @return
	 */
	public Boolean revisarActivityResult(int requestCode, int resultCode, Intent data, Activity pActivity, LocationManager plocationManager, 
			String pMensajeError) 
	{
		if (requestCode == Constants.PICK_LOCATION && resultCode == Activity.RESULT_OK)
		{			
			//Log.i(Constants.LOG_TAG, "Entro al pick");
			Log.d(Constants.LOG_TAG, "Entro al pick");
		}
		else if(requestCode == Constants.SET_GPS && !plocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			Toast.makeText(pActivity.getApplicationContext(), pMensajeError, Toast.LENGTH_LONG).show();
			return false;
		}
		else{
			plocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
		}
		return true;
	}
	
	/**
	 * Retorna la localizacion actual.
	 * @param plocationManager
	 * @return
	 */
	public Location obtenerGeolocalizacion(LocationManager plocationManager){
		plocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
		Location location = plocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		return location;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}
	
	/***************************************************
	 * Internet
	 **************************************************/
	
	/**
	 * Verifica que el dispositivo esta conectado a internet, sino se abre la configuracion
	 * para que el usuario lo active.
	 * @param context
	 * @return
	 */
	public boolean internetCheck(Context context){
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		 
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		
		if(!isConnected){
			Toast.makeText(context, Constants.ERROR_LOGIN_CONEXION, Toast.LENGTH_SHORT).show();
		}
		
		return isConnected;
	}
}
