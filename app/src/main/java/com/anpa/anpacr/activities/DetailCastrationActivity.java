package com.anpa.anpacr.activities;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
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
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

public class DetailCastrationActivity extends AnpaAppFraqmentActivity {
	private LocationManager _locationManager;
	String _sLatitude, _sLongitude, titleCastracionCalendar, fechaFb;
	Calendar dateStartCastrationCalendar;
	Calendar dateEndCastrationCalendar;

	// part for facebook
	//Integration with facebook
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	AccessTokenTracker accessTokenTracker;
	AccessToken accessToken;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_castration);

		dateStartCastrationCalendar = Calendar.getInstance();
		dateEndCastrationCalendar = Calendar.getInstance();

		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CASTRATION);

		//Ir GPS
		Button btnGoLocation = (Button) findViewById(R.id.btn_send_google_maps);
		btnGoLocation.setOnClickListener(onGoLocation);

		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);


		accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(
					AccessToken oldAccessToken,
					AccessToken currentAccessToken) {

				accessToken = currentAccessToken;
				System.out.println("Set Current token");
			}
		};


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Castration value = (Castration) extras.get(Constants.ID_OBJ_DETAIL_CASTRATION);

			dateStartCastrationCalendar.setTime(value.get_dDateInicio());
			dateEndCastrationCalendar.setTime(value.get_dDateFin());

			TextView txt_detail_castration_title = (TextView) findViewById(R.id.txt_detail_castration_title);
			txt_detail_castration_title.setText(value.get_snombre());
			titleCastracionCalendar = value.get_snombre();
			
			TextView txt_detail_castration_direction = (TextView) findViewById(R.id.txt_direction_castration);
			txt_detail_castration_direction.setText(value.get_sdireccion());

			TextView txt_detail_castration_description = (TextView) findViewById(R.id.txt_detail_castration_description);
			txt_detail_castration_description.setText(value.get_sdescription());
			
			_sLatitude = value.get_sLatitud();
			_sLongitude = value.get_sLongitud();

			TextView txt_detail_castration_doctor = (TextView) findViewById(R.id.txt_detail_castration_doctor);
			txt_detail_castration_doctor.setText(value.get_sdoctor());

			TextView txt_detail_castration_attendant = (TextView) findViewById(R.id.txt_detail_castration_attendant);
			txt_detail_castration_attendant.setText(value.get_sEncargado());
			
			TextView txt_detail_castration_amount = (TextView) findViewById(R.id.txt_detail_castration_amount);

			DecimalFormat df = new DecimalFormat("####,###,###.00");

			String formatMonto = df.format(new BigDecimal(value.get_bgdMonto()));

			String monto = "";
			if(value.getMuestraMonto() == 1){
				monto =  "₡ " + formatMonto;
			}else{
				monto = "-";
			}
			txt_detail_castration_amount.setText(monto);
			
			TextView txt_detail_castration_date = (TextView) findViewById(R.id.txt_detail_castration_date);
			String fecha = "";
			String fechaInicio = value.get_sDateInicio();
			String fechaFin = value.get_sDateFin();

			TextView txt_detail_castration_schedule = (TextView) findViewById(R.id.txt_detail_castration_schedule);
			String horario = "";

			try {
				DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				Date dateInicio = format.parse(fechaInicio);
				Date dateFin = format.parse(fechaFin);

				///////////////////////////FORMAT FECHA DE LAS CASTRACION/////////////////////////
				// Converting to String again, using an alternative format fecha
				DateFormat dfFecha = new SimpleDateFormat("dd/MM/yyyy");
				String startDate = dfFecha.format(dateInicio);
				String endDate = dfFecha.format(dateFin);

				fecha = startDate + " a " + endDate;

				///////////////////////////FORMAT HORA DE LAS CASTRACION/////////////////////////
				// Converting to String again, using an alternative format hora
				SimpleDateFormat dthora = new SimpleDateFormat("hh:mm a");
				String startHora = dthora.format(dateInicio);
				String endHora = dthora.format(dateFin);
				horario = startHora + " a " + endHora;

				fechaFb = startDate + " de " + horario;

			} catch (ParseException e) {
				e.printStackTrace();
			}
			txt_detail_castration_schedule.setText(horario);
			txt_detail_castration_date.setText(fecha);



			if (value.get_bImagen() != null) {
				Bitmap bmpNewsDetail = BitmapFactory.decodeByteArray(
						value.get_bImagen(), 0, value.get_bImagen().length);
				ImageView img_detail_castration = (ImageView) findViewById(R.id.img_detail_castration);
				img_detail_castration.setImageBitmap(bmpNewsDetail);
			}
		}

		//Ir a agregar al calendario
		Button btnGoCalendar = (Button) findViewById(R.id.btn_add_calendar);
		btnGoCalendar.setOnClickListener(onGoCalendar);

		//Ir a compartir en facebook
		Button btnGoFacebook = (Button) findViewById(R.id.btn_add_facebook);
		btnGoFacebook.setOnClickListener(onGoFacebook);

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

	/*
	 * Listener del bot�n de ir a la ubicaci�n de la castraci�n
	 */
	private OnClickListener onGoCalendar = new OnClickListener() {

		@Override
		public void onClick(View v) {
			goCalendar();
		}
	};

	/**
	 * Agrega el evento al calendario
	 */
	public void goCalendar(){

		Calendar cal = Calendar.getInstance();
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", dateStartCastrationCalendar.getTimeInMillis());
		intent.putExtra("allDay", false);
		intent.putExtra("rrule", "FREQ=YEARLY");
		intent.putExtra("endTime", dateEndCastrationCalendar.getTimeInMillis()+60*60*1000);
		intent.putExtra("title", "Asistir a Castración en " + titleCastracionCalendar);
		startActivity(intent);
		Toast.makeText(getApplicationContext(), "Se ha agregado la castración a su calendario", Toast.LENGTH_SHORT).show();
	}

	/*
	 * Listener del bot�n de ir a la ubicaci�n de la castraci�n
	 */
	private OnClickListener onGoFacebook = new OnClickListener() {

		@Override
		public void onClick(View v) {
			shareOnFacebook();
		}
	};

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

					String msjFabebook= "El evento de castración será en " + titleCastracionCalendar;
					ShareOpenGraphObject object = new ShareOpenGraphObject
							.Builder()
							.putString("fb:app_id", "915274545177488")
							.putString("og:type", "article")
							.putString("og:title", Constants.TITTLE_CASTRACION_FB)
							.putString("og:url", Constants.ANPA_FACEBOOK_PUBLICA)
							.putString("og:description", msjFabebook)
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


	private void alertaLogeoFB() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Constants.MSJ_ERROR_LOGIN_FB)
				.setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
					}
				}).create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
