package com.anpa.anpacr.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.SpinnerAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Gps;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class AddLostActivity extends AnpaAppFraqmentActivity {

	//App42:
	private String docId = "";
	private ServiceAPI api;
	private StorageService storageService;
	private Spinner provinciaSpinner, cantonSpinner, especieSpinner, razaSpinner;
	private SpinnerAdapter adapter;
	private SpinnerAdapter adapter1;
	SpinnerAdapter adapterRaces;
	private EditText editxt_nomMascota, editxt_contacto, editxt_telefono, editxt_detail_lost_description;
	private Button saveLost;
	private ImageView img_detail_lost;
	private Bitmap bitMapPhoto;
	private String _sLatitud;
	private String _sLongitud;
	private String _sRaza;
	private String _sEspecie;
	private String photoPath;
	private String app42PhotoURL;
	private LocationManager _locationManager;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_lost);

		// Instancia la BD App 42
		api = new ServiceAPI(Constants.App42ApiKey, Constants.App42ApiSecret);
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_LOST);
		photoPath = "";
		app42PhotoURL = "";

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

		razaSpinner = (Spinner) findViewById(R.id.spn_razaLost_selector);

		especieSpinner = (Spinner) findViewById(R.id.spn_especieLost_selector);
		especieSpinner.setAdapter(adapter1); // Set the custom adapter to the spinner
		especieSpinner.setOnItemSelectedListener(onSelectItemEspecie);
/*
        razaSpinner = (Spinner) findViewById(R.id.spn_razaLost_selector);
        razaSpinner.setAdapter(adapter1); // Set the custom adapter to the spinner
        razaSpinner.setOnItemSelectedListener(onSelectItemRaza);
*/
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
		cantonSpinner = (Spinner) findViewById(R.id.spn_canton_selector);
		editxt_contacto = (EditText) findViewById(R.id.editxt_contacto);
		editxt_nomMascota = (EditText) findViewById(R.id.editxt_nom_mascota);
		editxt_detail_lost_description = (EditText) findViewById(R.id.editxt_detail_lost_description);
		editxt_telefono = (EditText) findViewById(R.id.editxt_telefono);
		img_detail_lost = (ImageView) findViewById(R.id.img_detail_lost);
		img_detail_lost.buildDrawingCache();
		bitMapPhoto = img_detail_lost.getDrawingCache();

		_locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
		if (respuestaLocalizacion != null) {
			_sLatitud = Double.toString(respuestaLocalizacion.getLatitude());
			_sLongitud = Double.toString(respuestaLocalizacion.getLongitude());
		} else {
			Toast.makeText(this, "Error obteniendo la ubicación exacta", Toast.LENGTH_LONG).show();
			_sLatitud = Constants.LATITUD_COSTA_RICA;
			_sLongitud = Constants.LONGITUD_COSTA_RICA;
		}

	}

	/**
	 * Listener del boton
	 */
	private OnClickListener onSetPhoto = new OnClickListener() {

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
			//verifyImage();
			//new AsyncUploadInfoTask().execute("");
			saveInfo();
		}
	};
/*
    private void verifyImage(){
        mProgressDialog = ProgressDialog.show(this, "Cargando la información", "", true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(photoPath.equals("") || (!photoPath.equals("") && !app42PhotoURL.equals(""))) {
            mProgressDialog.hide();
            saveInfo();
        }
        else{
            verifyImage();
        }
    }*/

	private class AsyncUploadInfoTask extends AsyncTask<String, Integer, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(AddLostActivity.this);
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog.show(AddLostActivity.this, "Cargando", "");
		}

		protected Boolean doInBackground(String... storage) {
			int maxAttemps = 0;
			boolean canSave = false;
			if (!photoPath.equals("") && app42PhotoURL.equals("")) {
				do {
					maxAttemps += 1;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}while (app42PhotoURL.equals("") && maxAttemps < 15);
			}
			else if(!app42PhotoURL.equals(""))
				canSave = true;
			return canSave;
		}

		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();

			if(result)
				saveInfo();
			else
				Toast.makeText(getApplicationContext(), "Ocurrió un error, intente de nuevo", Toast.LENGTH_SHORT).show();
		}
	}

	private void saveInfo() {
		String dbName = Constants.App42DBName;
		String collectionName = Constants.TABLE_PERDIDOS;
		JSONObject lostJSON = new JSONObject();

		if (app42PhotoURL != null && !app42PhotoURL.equals(""))
			Util.textAsJSON(lostJSON, Constants.FOTO_PERDIDO, app42PhotoURL, -1);
		else
			Util.textAsJSON(lostJSON, Constants.FOTO_PERDIDO, "null", -1);
		Util.textAsJSON(lostJSON, Constants.NOM_MASCOTA, editxt_nomMascota.getText().toString(), -1);
		Util.textAsJSON(lostJSON, Constants.NOM_DUENO, editxt_contacto.getText().toString(), -1);
		Util.textAsJSON(lostJSON, Constants.LATITUD_PERDIDO, _sLatitud, -1);
		Util.textAsJSON(lostJSON, Constants.LONGITUD_PERDIDO, _sLongitud, -1);
		Util.textAsJSON(lostJSON, Constants.PROVINCIA_PERDIDO, "", provinciaSpinner.getAdapter().getItemId(provinciaSpinner.getSelectedItemPosition()));
		Util.textAsJSON(lostJSON, Constants.CANTON_PERDIDO, "", cantonSpinner.getAdapter().getItemId(cantonSpinner.getSelectedItemPosition()));
		Util.textAsJSON(lostJSON, Constants.RAZA_PERDIDO, _sRaza, -1);
		Util.textAsJSON(lostJSON, Constants.ESPECIE_PERDIDO, _sEspecie, -1);
		Util.textAsJSON(lostJSON, Constants.TELEFONO_PERDIDO, editxt_telefono.getText().toString(), -1);
		Util.textAsJSON(lostJSON, Constants.DETALLE_PERDIDO, editxt_detail_lost_description.getText().toString(), -1);
		Util.textAsJSON(lostJSON, Constants.HABILITADO_PERDIDO, "", 1);
		Util.textAsJSON(lostJSON, Constants.USUARIO, Constants.USUARIO_NOMBRE, -1);
		// instacia Storage App42
		storageService = api.buildStorageService();
            /* Below snippet will save JSON object in App42 Cloud */
		storageService.insertJSONDocument(dbName, collectionName, lostJSON, new App42CallBack() {
			public void onSuccess(Object response) {
				Storage storage = (Storage) response;
				ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
				for (int i = 0; i < jsonDocList.size(); i++) {
					System.out.println("objectId is " + jsonDocList.get(i).getDocId());
					//Above line will return object id of saved JSON object
					System.out.println("CreatedAt is " + jsonDocList.get(i).getCreatedAt());
					System.out.println("UpdatedAtis " + jsonDocList.get(i).getUpdatedAt());
					System.out.println("Jsondoc is " + jsonDocList.get(i).getJsonDoc());
				}
			}

			public void onException(Exception ex) {
				System.out.println("Exception Message" + ex.getMessage());
			}
		});
		alertDialog();
	}

	public void alertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Constants.MSJ_SUCCESS_TIP)
				.setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
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
				String[] filePathColumn = {MediaStore.Images.Media.DATA};
				Uri selectedImage = data.getData();
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				photoPath = cursor.getString(columnIndex);
				cursor.close();
				uploadImage();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Verifica que el GPS esta activado.
	 */
	public void comprobarGPS() {
		_locationManager = Gps.getInstance().revisarGPS(this, _locationManager);
		if (_locationManager != null) {
			Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivityForResult(intent, Constants.SET_GPS);
		} else {
			_locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
			if (respuestaLocalizacion != null) {
				_sLatitud = Double.toString(respuestaLocalizacion.getLatitude());
				_sLongitud = Double.toString(respuestaLocalizacion.getLongitude());
			} else {
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
		public void onNothingSelected(AdapterView<?> adapter) {
		}
	};
	/**
	 * Listener del spinner
	 */
	private OnItemSelectedListener onSelectItemRaza = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
								   int position, long id) {
			// Here you get the current item (a User object) that is selected by its position
			GenericNameValue selectedItem = adapterRaces.getItem(position);
			// Here you can do the action you want to...
			_sRaza = selectedItem.getName();
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {
		}
	};


	/* carga la lista de cantones de una provncia */
	private void readCantones(int provinciaId) {
		ArrayList<GenericNameValue> cantonesList = new ArrayList<GenericNameValue>();

		BufferedReader in = null;
		StringBuilder buf = new StringBuilder();
		try {
			InputStream is = getApplicationContext().getAssets().open("cantones_costa_rica.txt");
			in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String canton;
			boolean isFirst = true;
			while ((canton = in.readLine()) != null) {
				if (isFirst)
					isFirst = false;
				else
					buf.append('\n');
				buf.append(canton);
			}

			String[] provinciaCantonArray = buf.toString().split("#");

			for (String provincia : provinciaCantonArray) {
				String[] values = provincia.split(",");
				cantonesList.add(new GenericNameValue(values[2], Integer.parseInt(values[1])));
			}
		} catch (IOException e) {
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

	private void uploadImage() {
		UploadService uploadService = App42API.buildUploadService();
		long time= System.currentTimeMillis();
		Random r = new Random();
		int i1 = r.nextInt(100);
		String name = Long.toString(time) + i1;

		String description = "Mascota Perdida";

		uploadService.uploadFile(name, photoPath, UploadFileType.IMAGE, description, new App42CallBack() {
			public void onSuccess(Object response) {
				Upload upload = (Upload) response;
				// This will have only a single file uploaded above
				ArrayList<Upload.File> fileList = upload.getFileList();
				String filePath = "";
				for (int i = 0; i < fileList.size(); i++) {
					System.out.println("fileName is :" + fileList.get(i).getName());
					System.out.println("fileType is :" + fileList.get(i).getType());
					System.out.println("fileUrl is :" + fileList.get(i).getUrl());
					System.out.println("Tiny Url is :" + fileList.get(i).getTinyUrl());
					System.out.println("fileDescription is: " + fileList.get(i).getDescription());
					Log.e("RUTA", fileList.get(i).getUrl());
					app42PhotoURL = fileList.get(i).getUrl();
				}
			}

			public void onException(Exception ex) {
				System.out.println("Exception Message" + ex.getMessage());
				Toast.makeText(getApplicationContext(), Constants.MSG_IMAGE_UPLOAD_ERROR, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * Listener del spinner
	 */
	private OnItemSelectedListener onSelectItemEspecie = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
								   int position, long id) {
			GenericNameValue selectedItem = adapter1.getItem(position);
			_sEspecie = selectedItem.getName();

			readSpecies(selectedItem.getValue());
		}
		@Override
		public void onNothingSelected(AdapterView<?> adapter) {  }
	};

	/* carga la lista de razas de una especie */
	private void readSpecies(int specieId)
	{
		ArrayList<GenericNameValue> speciesList = new ArrayList<GenericNameValue>();

		String selectedFile = "";
		switch (specieId) {
			case 2:
				selectedFile = "razas_gatos";
				break;
			case 3:
				selectedFile = "razas_aves";
				break;
			case 4:
				selectedFile = "razas_peces";
				break;
			case 5:
				selectedFile = "razas_roedores";
				break;
			default:
				selectedFile = "razas_perros";
				break;
		}

		BufferedReader in = null;
		StringBuilder buf = new StringBuilder();
		try{
			InputStream is = getApplicationContext().getAssets().open(selectedFile + ".txt");
			in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String races;
			boolean isFirst = true;
			while ((races = in.readLine()) != null ){
				if (isFirst)
					isFirst = false;
				else
					buf.append('\n');
				buf.append(races);
			}

			String[] specieRacesArray = buf.toString().split("#");

			for (String race : specieRacesArray)
			{
				String[] values = race.split(",");
				speciesList.add(new GenericNameValue(values[1], Integer.parseInt(values[0])));
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
		adapterRaces = new SpinnerAdapter(AddLostActivity.this,
				R.layout.spinner_item,
				speciesList);
		razaSpinner.setAdapter(adapterRaces);
		razaSpinner.setOnItemSelectedListener(onSelectItemRaza);
	}
}
