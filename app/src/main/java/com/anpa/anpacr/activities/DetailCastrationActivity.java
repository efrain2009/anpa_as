package com.anpa.anpacr.activities;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.Castration;
import com.anpa.anpacr.domain.Gps;

public class DetailCastrationActivity extends ActionBarActivity {
	private LocationManager _locationManager;
	String _sLatitude, _sLongitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_castration);
		
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CASTRATION);
		Button btnGoLocation = (Button) findViewById(R.id.btn_send_google_maps);
		btnGoLocation.setOnClickListener(onGoLocation);
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Castration value = (Castration) extras.get(Constants.ID_OBJ_DETAIL_CASTRATION);

			TextView txt_detail_castration_title = (TextView) findViewById(R.id.txt_detail_castration_title);
			txt_detail_castration_title.setText(value.get_snombre());
			
			TextView txt_detail_castration_direction = (TextView) findViewById(R.id.txt_direction_castration);
			txt_detail_castration_direction.setText(value.get_sdireccion());

			TextView txt_detail_castration_description = (TextView) findViewById(R.id.txt_detail_castration_description);
			txt_detail_castration_description.setText(value.get_sdescription());
			
			_sLatitude = value.get_sLatitud();
			_sLongitude = value.get_sLongitud();
			
			SimpleDateFormat dthora = new SimpleDateFormat(
					"hh:mm a");		

			TextView txt_detail_castration_schedule = (TextView) findViewById(R.id.txt_detail_castration_schedule);
			//String horario = dthora.format(value.get_dDateInicio()) +" a " + dthora.format(value.get_dDateFin());
			String horario = value.get_sDateInicio() +" a " + value.get_sDateFin();
			txt_detail_castration_schedule.setText(horario);
			
			TextView txt_detail_castration_doctor = (TextView) findViewById(R.id.txt_detail_castration_doctor);
			txt_detail_castration_doctor.setText(value.get_sdoctor());

			TextView txt_detail_castration_attendant = (TextView) findViewById(R.id.txt_detail_castration_attendant);
			txt_detail_castration_attendant.setText(value.get_sEncargado());
			
			TextView txt_detail_castration_amount = (TextView) findViewById(R.id.txt_detail_castration_amount);
			String monto =  "₡ " + value.get_bgdMonto().toString();
			txt_detail_castration_amount.setText(monto);
						
			SimpleDateFormat formatoFecha = 
				    new SimpleDateFormat("EEEE d 'de' MMMM 'de' yyyy", new Locale("es", "ES"));
			
			TextView txt_detail_castration_date = (TextView) findViewById(R.id.txt_detail_castration_date);
			//String fecha = formatoFecha.format(value.get_dDateInicio());
			String fecha = value.get_sDateInicio();
			txt_detail_castration_date.setText(fecha);

			if (value.get_bImagen() != null) {
				Bitmap bmpNewsDetail = BitmapFactory.decodeByteArray(
						value.get_bImagen(), 0, value.get_bImagen().length);
				ImageView img_detail_castration = (ImageView) findViewById(R.id.img_detail_castration);
				img_detail_castration.setImageBitmap(bmpNewsDetail);
			}
		}
		_locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	}
	
	/*
	 * Listener del bot�n de ir a la ubicaci�n de la castraci�n
	 */
	private OnClickListener onGoLocation = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			comprobarGPS();
		}
	}; 

	/**
     * Verifica que el GPS esta activado y si lo esta, abre el navegador de mapas.
     */
    public void comprobarGPS(){
        _locationManager = Gps.getInstance().revisarGPS(this, _locationManager);
        if(_locationManager != null){
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, Constants.SET_GPS);
        }
        else{
        	if(!_sLongitude.equals("0") && !_sLatitude.equals("0")){
	            _locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	            Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
	            if(respuestaLocalizacion != null){
	                String uri = "geo:" + _sLatitude + ","
	                        + _sLongitude + "?q=" + _sLatitude
	                        + "," + _sLongitude;
	                startActivity(new Intent(Intent.ACTION_VIEW,
	                        Uri.parse(uri)));
	            }
	            else{
	                Toast.makeText(getApplicationContext(), "Ha ocurrido un error obteniendo tu ubicación", Toast.LENGTH_SHORT).show();
	            }
        	}
            else{
                Toast.makeText(getApplicationContext(), "No tenemos la ubicación de este lugar :(", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
