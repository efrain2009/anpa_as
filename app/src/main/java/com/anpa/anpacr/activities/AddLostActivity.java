package com.anpa.anpacr.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AddLostActivity extends AnpaAppFraqmentActivity {

	//App42:
	private String docId = "";
	private ServiceAPI api;
	private StorageService storageService;
	private Spinner provinciaSpinner, cantonSpinner, especieSpinner, razaSpinner;
	private SpinnerAdapter adapter;
	private SpinnerAdapter adapter1;
	SpinnerAdapter adapterRaces;
	SpinnerAdapter adapterCantones;
	private EditText editxt_nomMascota, editxt_contacto, editxt_telefono, editxt_detail_lost_description;
	private Button saveLost;
	private ImageView img_detail_lost;
	private String _sLatitud;
	private String _sLongitud;
	private String _sRaza;
	private String _sEspecie;
	private String photoPath;
	private String app42PhotoURL;
	private LocationManager _locationManager;
	CheckBox check_facebook;
	private Bitmap bmp;
	private String urlBmp;

	//Integration with facebook
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	AccessTokenTracker accessTokenTracker;
	AccessToken accessToken;
	String msjFabebooklost = "";



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_lost);

		// Instancia la BD App 42
		api = new ServiceAPI(Constants.App42ApiKey, Constants.App42ApiSecret);

		//Btn de back (anterior)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_LOST);

		photoPath = "";
		app42PhotoURL = "";

		/* Facebook*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        List<String> publishPermissions = Arrays.asList("publish_actions");

        callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);

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

		cantonSpinner = (Spinner) findViewById(R.id.spn_canton_selector);
		cantonSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
		cantonSpinner.setOnItemSelectedListener(onSelectItemCanton);


		editxt_contacto = (EditText) findViewById(R.id.editxt_contacto);
		editxt_nomMascota = (EditText) findViewById(R.id.editxt_nom_mascota);
		editxt_detail_lost_description = (EditText) findViewById(R.id.editxt_detail_lost_description);
		editxt_telefono = (EditText) findViewById(R.id.editxt_telefono);
		img_detail_lost = (ImageView) findViewById(R.id.img_detail_lost);
		img_detail_lost.buildDrawingCache();
		bmp = img_detail_lost.getDrawingCache();
		check_facebook = (CheckBox) findViewById(R.id.chkCompFB);


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


		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
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
			saveInfo();
		}
	};


	private void saveInfo() {
		if (check_facebook.isChecked() && AccessToken.getCurrentAccessToken() == null) {
			alertaLogeoFB();
		} else {
			String dbName = Constants.App42DBName;
			String collectionName = Constants.TABLE_PERDIDOS;
			JSONObject lostJSON = new JSONObject();

			if (app42PhotoURL != null && !app42PhotoURL.equals(""))
				Util.textAsJSON(lostJSON, Constants.FOTO_PERDIDO, app42PhotoURL, -1);
			else {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_no_image);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				byte[] bitMapData = stream.toByteArray();
				ByteArrayInputStream bs = new ByteArrayInputStream(bitMapData);
				saveImage(bs, "loadImgDefault");
				Util.textAsJSON(lostJSON, Constants.FOTO_PERDIDO, app42PhotoURL, -1);
			}
			Util.textAsJSON(lostJSON, Constants.NOM_MASCOTA, editxt_nomMascota.getText().toString(), -1);
			Util.textAsJSON(lostJSON, Constants.NOM_DUENO, editxt_contacto.getText().toString(), -1);
			Util.textAsJSON(lostJSON, Constants.LATITUD_PERDIDO, _sLatitud, -1);
			Util.textAsJSON(lostJSON, Constants.LONGITUD_PERDIDO, _sLongitud, -1);
			Util.textAsJSON(lostJSON, Constants.PROVINCIA_PERDIDO, "", provinciaSpinner.getAdapter().getItemId(provinciaSpinner.getSelectedItemPosition()));
			Util.textAsJSON(lostJSON, Constants.CANTON_PERDIDO, "", cantonSpinner.getAdapter().getItemId(cantonSpinner.getSelectedItemPosition()));
			Util.textAsJSON(lostJSON, Constants.RAZA_PERDIDO, "", razaSpinner.getAdapter().getItemId(razaSpinner.getSelectedItemPosition()));
			Util.textAsJSON(lostJSON, Constants.ESPECIE_PERDIDO, "", especieSpinner.getAdapter().getItemId(especieSpinner.getSelectedItemPosition()));
			Util.textAsJSON(lostJSON, Constants.TELEFONO_PERDIDO, editxt_telefono.getText().toString(), -1);
			Util.textAsJSON(lostJSON, Constants.DETALLE_PERDIDO, editxt_detail_lost_description.getText().toString(), -1);
			Util.textAsJSON(lostJSON, Constants.HABILITADO_PERDIDO, "", 0);
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
						if (check_facebook.isChecked()) {
							if (AccessToken.getCurrentAccessToken() != null) {
								msjFabebooklost = "\nNombre: " + editxt_nomMascota.getText().toString() + "." +
										"\nDetalles: " + editxt_detail_lost_description.getText().toString() + "." +
										"Descarga nuestra app y entérate.";
								shareOnFacebook();
							}
						}
					}
				}

				public void onException(Exception ex) {
					System.out.println("Exception Message" + ex.getMessage());
				}
			});
			alertDialog();
		}
	}

    private void alertaLogeoFB() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constants.MSJ_ERROR_LOGIN_FB)
                .setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                }).create().show();
    }

	public void alertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Constants.MSJ_SUCCESS_TIP)
				.setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						finish();
						// FIRE ZE MISSILES!
					}
				}).create().show();
	}

	public void pickImage() {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");
		startActivityForResult(intent, 1);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1 && resultCode == RESULT_OK) {

			if (data == null) {
				//Display an error
				return;
			}
			try {
				Uri selectedImage = data.getData();
				InputStream inputStream = getContentResolver().openInputStream(selectedImage);

				bmp = BitmapFactory.decodeStream(inputStream);
				bmp = getResizedBitmap(bmp, 600);
				img_detail_lost.setImageBitmap(bmp);
				String[] filePathColumn = {MediaStore.Images.Media.DISPLAY_NAME};
				Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();
				int fileNameIndex = cursor.getColumnIndex(filePathColumn[0]);
				String fileName = cursor.getString(fileNameIndex);
				cursor.close();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.JPEG, 90, bos);
				byte[] bitmapdata = bos.toByteArray();
				ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);

				uploadImage(bs, fileName);

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
	private OnItemSelectedListener onSelectItemProvincias = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
								   int position, long id) {
			// Here you get the current item (a User object) that is selected by its position
			GenericNameValue selectedItem = adapterCantones.getItem(position);
			// Here you can do the action you want to...
			_sRaza = selectedItem.getName();
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {
		}
	};

	/**
	 * Listener del spinner
	 */
	private OnItemSelectedListener onSelectItemCanton = new OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> adapterView, View view,
								   int position, long id) {
			GenericNameValue selectedItem = adapter.getItem(position);


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
			GenericNameValue selectedItem = adapterRaces.getItem(position);
			// Here you can do the action you want to...
			_sRaza = selectedItem.getName();
		}

		@Override
		public void onNothingSelected(AdapterView<?> adapter) {
		}
	};

	private void uploadImage(ByteArrayInputStream inputStream, String fileName) {
		Date currentDate = new Date();
		String[] nameSplit = fileName.split("\\.");
		StringBuilder sbUploadName = new StringBuilder();
		sbUploadName.append(nameSplit[0]);
		sbUploadName.append(currentDate.getTime());
		sbUploadName.append(".");
		sbUploadName.append(nameSplit[1]);
		saveImage(inputStream, sbUploadName.toString());
	}

	private void saveImage(ByteArrayInputStream inputStream, String sbUploadName) {

	UploadService uploadService = App42API.buildUploadService();
	UploadFileType fileType = UploadFileType.IMAGE;
	String description = "Mascota Perdida";

		uploadService.uploadFile(sbUploadName, inputStream, fileType, description, new App42CallBack() {
		public void onSuccess (Object response)
		{
			Upload upload = (Upload) response;
			ArrayList<Upload.File> fileList = upload.getFileList();
			for (int i = 0; i < fileList.size(); i++) {
				System.out.println("fileName is :" + fileList.get(i).getName());
				System.out.println("fileType is :" + fileList.get(i).getType());
				System.out.println("fileUrl is :" + fileList.get(i).getUrl());
				System.out.println("Tiny Url is :" + fileList.get(i).getTinyUrl());
				System.out.println("fileDescription is: " + fileList.get(i).getDescription());
				urlBmp = fileList.get(i).getUrl();
				app42PhotoURL = fileList.get(i).getUrl();
			}
		}
			public void onException(Exception ex) {
				System.out.println("Exception Message" + ex.getMessage());
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
		//racesSpinner el spinner:
		adapterRaces = new SpinnerAdapter(AddLostActivity.this,
				R.layout.spinner_item,
				speciesList);
		razaSpinner.setAdapter(adapterRaces);
		razaSpinner.setOnItemSelectedListener(onSelectItemEspecie);
	}


	/* carga la lista de cantones de una especie */
	private void readCantones(int provinciaId)
	{
		ArrayList<GenericNameValue> provinciasList = new ArrayList<GenericNameValue>();

		String selectedFile = "";
		switch (provinciaId) {
			case 2:
				selectedFile = "canton_alajuela";
				break;
			case 3:
				selectedFile = "canton_cartago";
				break;
			case 4:
				selectedFile = "canton_heredia";
				break;
			case 5:
				selectedFile = "canton_guanacaste";
				break;
			case 6:
				selectedFile = "canton_puntarenas";
				break;
			case 7:
				selectedFile = "canton_limon";
				break;
			default:
				selectedFile = "canton_san_jose";
				break;
		}

		BufferedReader in = null;
		StringBuilder buf = new StringBuilder();
		try{
			InputStream is = getApplicationContext().getAssets().open(selectedFile + ".txt");
			in = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cantones;
			boolean isFirst = true;
			while ((cantones = in.readLine()) != null ){
				if (isFirst)
					isFirst = false;
				else
					buf.append('\n');
				buf.append(cantones);
			}

			String[] cantonesArray = buf.toString().split("#");

			for (String canton : cantonesArray)
			{
				String[] values = canton.split(",");
				provinciasList.add(new GenericNameValue(values[1], Integer.parseInt(values[0])));
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
		adapterCantones = new SpinnerAdapter(AddLostActivity.this,
				R.layout.spinner_item,
				provinciasList);
		cantonSpinner.setAdapter(adapterCantones);
		cantonSpinner.setOnItemSelectedListener(onSelectItemProvincias);
	}

	private void shareOnFacebook(){
		FacebookSdk.sdkInitialize(this.getApplicationContext(), new FacebookSdk.InitializeCallback() {
					@Override
					public void onInitialized() {
						if (AccessToken.getCurrentAccessToken() == null) {
						/*Inicia Sesion*/
						/*Facbook*/
							LoginButton buttonFb = (LoginButton) findViewById(R.id.login_button);
							buttonFb.clearPermissions();

							List<String> publishPermissions = Arrays.asList("publish_actions");

							buttonFb.setPublishPermissions(publishPermissions);

							buttonFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
								@Override
								public void onSuccess(LoginResult loginResult) {
									accessToken = loginResult.getAccessToken();
									System.out.print("Access Token: " + accessToken.getToken());
								}

								@Override
								public void onCancel() {
									System.out.print("Cancelado");
								}

								@Override
								public void onError(FacebookException error) {
									System.out.print("Error: " + error);
								}
							});
						} else {
							ShareOpenGraphObject object = new ShareOpenGraphObject
									.Builder()
									.putString("fb:app_id", "915274545177488")
									.putString("og:type", "article")
									.putString("og:title", Constants.PUBLICA_PERDIDO)
									.putString("og:image",  urlBmp)
									.putString("og:url", Constants.ANPA_FACEBOOK_PUBLICA)
									.putString("og:description", msjFabebooklost)
									.build();

							// Create an action
							ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
								.setActionType("news.publishes")
								.putObject("article", object)
									.build();

							if (ShareDialog.canShow(ShareOpenGraphContent.class)) {
								ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
										.setPreviewPropertyName("article")
										.setAction(action)
										.build();
								shareDialog.show(content);
							}
						}
					}
		});

	}

/*
Redimensiona un bitmap, si la imagen es muy grande
 */
	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float)width / (float) height;
		if (bitmapRatio > 1) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}
}
