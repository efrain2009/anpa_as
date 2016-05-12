package com.anpa.anpacr.activities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.SpinnerAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Gps;
import com.anpa.anpacr.domain.Lost;
import com.parse.ParseObject;

public class AddLostActivity extends AnpaAppFraqmentActivity {
	private Spinner provinciaSpinner, cantonSpinner, specieSpinner;
	private SpinnerAdapter adapter;
	private SpinnerAdapter adapter1;
	EditText editxt_nomMascota, editxt_contacto, editxt_telefono, 
	editxt_detail_lost_description;
	Button saveLost;
	ImageView img_detail_lost;
	Bitmap bitMapPhoto;
	Lost lost;
	String _sLatitud;
	String _sLongitud;
	String _sRaza;
	
	private LocationManager _locationManager;

	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_add_lost);
			
			//Btn de back (anterior)
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_LOST);
			
			saveLost = (Button) findViewById(R.id.btn_save_lost);
			saveLost.setOnClickListener(onSave);
			
			Button photoLost = (Button) findViewById(R.id.btn_add_lost_photo);
			photoLost.setOnClickListener(onSetPhoto);
			
			//Crea la lista de especie:
			ArrayList<GenericNameValue> racesItems = new ArrayList<GenericNameValue>();
			for (String race : Constants.RACES) {
				String[] raceSplit = race.split(",");
				racesItems.add(new GenericNameValue(raceSplit[1], Integer.parseInt(raceSplit[0])));
			}
						
			adapter1 = new SpinnerAdapter(this,
					R.layout.spinner_item,
		            racesItems);
			specieSpinner = (Spinner) findViewById(R.id.spn_razaLost_selector);
			specieSpinner.setAdapter(adapter1); // Set the custom adapter to the spinner
			specieSpinner.setOnItemSelectedListener(onSelectItemRaza);
			
			
			//Crea la lista de provincias:
			ArrayList<GenericNameValue> provinciaItems = new ArrayList<GenericNameValue>();
			for (String provincia : Constants.PROVINCE) {
				String[] provinciaSplit = provincia.split(",");
				provinciaItems.add(new GenericNameValue(provinciaSplit[1], Integer.parseInt(provinciaSplit[0])));
			}
			
					
			adapter = new SpinnerAdapter(this,
					R.layout.spinner_item,
					provinciaItems);
			provinciaSpinner = (Spinner) findViewById(R.id.spn_provincia_selector);
			provinciaSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
			provinciaSpinner.setOnItemSelectedListener(onSelectItem);
			
			cantonSpinner = (Spinner)findViewById(R.id.spn_canton_selector);
			
			editxt_contacto = (EditText) findViewById(R.id.editxt_contacto);
			
			editxt_nomMascota = (EditText) findViewById(R.id.editxt_nom_mascota);
						
			editxt_detail_lost_description = (EditText) findViewById(R.id.editxt_detail_lost_description);
						
			editxt_telefono = (EditText) findViewById(R.id.editxt_telefono);
			
			img_detail_lost = (ImageView) findViewById(R.id.img_detail_lost);
			      
			img_detail_lost.buildDrawingCache();
			
			bitMapPhoto = img_detail_lost.getDrawingCache();
			    
			_locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			 Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
			if(respuestaLocalizacion != null){
			            _sLatitud = Double.toString(respuestaLocalizacion.getLatitude());
			            _sLongitud = Double.toString(respuestaLocalizacion.getLongitude());
			        }
			        else{
			            Toast.makeText(this, "Error obteniendo la ubicación exacta", Toast.LENGTH_LONG).show();
			            _sLatitud = Constants.LATITUD_COSTA_RICA;
			            _sLongitud = Constants.LONGITUD_COSTA_RICA;
			        }
		   
		}
		
		/**
		 * Listener del boton
		 */
		private OnClickListener onSetPhoto= new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pickImage();
			}
		};
		
		/**
		 * Listener del boton
		 */
		private OnClickListener onSave = new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				
				ParseObject objLost = new ParseObject(Constants.TABLE_PERDIDOS);
				objLost.put(Constants.NOM_MASCOTA, editxt_nomMascota.getText().toString());
				objLost.put(Constants.NOM_DUENO, editxt_contacto.getText().toString());
			
				//Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				//		R.drawable.ic);
				/*
				// Convert it to byte
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				// Compress image to lower quality scale 1 - 100
				bitMapPhoto.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] image = stream.toByteArray(); 
				// Create the ParseFile
				ParseFile file = new ParseFile("androidbegin.png", image);
				// Upload the image into Parse Cloud
				file.saveInBackground();	 

 				// Create a column named "ImageFile" and insert the image
				objLost.put(Constants.FOTO_PERDIDO, file);
				*/
				objLost.put(Constants.LATITUD_PERDIDO, _sLatitud);
				objLost.put(Constants.LONGITUD_PERDIDO, _sLongitud);
				objLost.put(Constants.PROVINCIA_PERDIDO, Integer.valueOf(provinciaSpinner.getSelectedItem().toString()));
				objLost.put(Constants.CANTON_PERDIDO, Integer.valueOf(cantonSpinner.getSelectedItem().toString()));
				objLost.put(Constants.RAZA_PERDIDO, _sRaza);
				objLost.put(Constants.TELEFONO_PERDIDO, editxt_telefono.getText().toString());
				objLost.put(Constants.DETALLE_PERDIDO, editxt_detail_lost_description.getText().toString());
				objLost.saveInBackground();	
				alertDialog ();
				saveLost.setEnabled(false);
			}
		};
		
		public void alertDialog (){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
	        builder.setMessage("Dentro de poco se publicara tu aviso")
	               .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
	                   }
	               }).create().show();
		}
		
		public void pickImage() {
			  Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			  intent.setType("image/*");
			  startActivityForResult(intent, 1);
			}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
		    super.onActivityResult(requestCode, resultCode, data);
		    if (requestCode == 1 && resultCode == this.RESULT_OK) {
		        if (data == null) {
		            //Display an error
		            return;
		        }
		        try {
					InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
					
					Bitmap bmp = BitmapFactory.decodeStream(inputStream);
					img_detail_lost.setImageBitmap(bmp);
					
					
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
		    }
		}
		
		/**
	     * Verifica que el GPS esta activado.
	     */
	    public void comprobarGPS(){
	        _locationManager = Gps.getInstance().revisarGPS(this, _locationManager);
	        if(_locationManager != null){
	            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            startActivityForResult(intent, Constants.SET_GPS);
	        }
	        else{
	            _locationManager= (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	            Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
	            if(respuestaLocalizacion != null){
	                _sLatitud = Double.toString(respuestaLocalizacion.getLatitude());
	                _sLongitud = Double.toString(respuestaLocalizacion.getLongitude());
	            }
	            else{
	                _sLatitud = Constants.LATITUD_COSTA_RICA;
	                _sLongitud = Constants.LONGITUD_COSTA_RICA;
	            }
	        }
	    }
	    
	    /**
		 * Listener del spinner
		 */
		private OnItemSelectedListener onSelectItem = new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                    int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                GenericNameValue selectedItem = adapter.getItem(position);
                // Here you can do the action you want to...
                readCantones(selectedItem.getValue());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
		};
		 /**
		 * Listener del spinner
		 */
		private OnItemSelectedListener onSelectItemRaza = new OnItemSelectedListener() {
			@Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                    int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                GenericNameValue selectedItem = adapter1.getItem(position);
                // Here you can do the action you want to...
                _sRaza = selectedItem.getName();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
		};
		
		
		/* carga la lista de cantones de una provncia */
        private void readCantones(int provinciaId)
        {
        	ArrayList<GenericNameValue> cantonesList = new ArrayList<GenericNameValue>();

           
            BufferedReader in = null;
            StringBuilder buf = new StringBuilder();
            try{
	            InputStream is = getApplicationContext().getAssets().open("cantones_costa_rica.txt");
	            in = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	            
	            String canton;
	            boolean isFirst = true;
	            while ((canton = in.readLine()) != null ){
	                if (isFirst)
	                    isFirst = false;
	                else
	                    buf.append('\n');
	                buf.append(canton);
	            }

	            String[] provinciaCantonArray = buf.toString().split("#");
                
	            for (String provincia : provinciaCantonArray)
	            {
	                String[] values = provincia.split(",");
	               cantonesList.add(new GenericNameValue(values[2], Integer.parseInt(values[1])));
	            }
            }
            catch(IOException e) {
                Log.e("OJO", "Error opening asset ");
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        Log.e("OJO", "Error closing asset ");
                    }
                }
            }
            
            //Carga el spinner:
            SpinnerAdapter adapterCanton = new SpinnerAdapter(this,
		            R.layout.spinner_item,
		            cantonesList);
            cantonSpinner.setAdapter(adapterCanton);
			
        }
}
