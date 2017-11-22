package com.anpa.anpacr.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
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
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareOpenGraphAction;
import com.facebook.share.model.ShareOpenGraphContent;
import com.facebook.share.model.ShareOpenGraphObject;
import com.facebook.share.widget.ShareDialog;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DetailCastrationActivity extends AnpaAppFraqmentActivity {
	private LocationManager _locationManager;
	String _sLatitude, _sLongitude, titleCastracionCalendar, fechaFb;
	Calendar dateStartCastrationCalendar;
	Calendar dateEndCastrationCalendar;
	private Animator mCurrentAnimator;
	private int mShortAnimationDuration;

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
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CASTRATION);;

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
				DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
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
				final Bitmap bmpNewsDetail = BitmapFactory.decodeByteArray(
						value.get_bImagen(), 0, value.get_bImagen().length);
				final ImageView img_detail_castration = (ImageView) findViewById(R.id.img_detail_castration);
				img_detail_castration.setImageBitmap(bmpNewsDetail);

				img_detail_castration.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						zoomImageFromThumb(img_detail_castration, bmpNewsDetail);
					}
				});
				mShortAnimationDuration = getResources().getInteger(
						android.R.integer.config_shortAnimTime);
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
		if(checkLocationPermission()) {
			_locationManager = Gps.getInstance().revisarGPS(this, _locationManager);
			if (_locationManager != null) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, Constants.SET_GPS);
			} else {
				if (!_sLongitude.equals("0") && !_sLatitude.equals("0")) {
					_locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
					Location respuestaLocalizacion = Gps.getInstance().obtenerGeolocalizacion(_locationManager);
					if (respuestaLocalizacion != null) {
						String uri = "geo:" + _sLatitude + ","
								+ _sLongitude + "?q=" + _sLatitude
								+ "," + _sLongitude;
						startActivity(new Intent(Intent.ACTION_VIEW,
								Uri.parse(uri)));
					} else {
						Toast.makeText(getApplicationContext(), "Ha ocurrido un error obteniendo tu ubicación", Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(getApplicationContext(), "No tenemos la ubicación de este lugar :(", Toast.LENGTH_SHORT).show();
				}
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
					alertaLogeoFB();
				} else {

					String msjFabebook= "Será en " + titleCastracionCalendar + ". " +
							"Descarga nuestra app y entérate.";
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


	//Zoom de la imagen
	private void zoomImageFromThumb(final View thumbView, Bitmap imageResId) {
		// If there's an animation in progress, cancel it
		// immediately and proceed with this one.
		if (mCurrentAnimator != null) {
			mCurrentAnimator.cancel();
		}

		// Load the high-resolution "zoomed-in" image.
		/*final ImageView expandedImageView = new ImageView(DetailNewsActivity.this);
		expandedImageView.setImageBitmap(imageResId);*/
		final ImageView expandedImageView = (ImageView) findViewById(
				R.id.expanded_image);
		expandedImageView.setImageBitmap(imageResId);

		// Calculate the starting and ending bounds for the zoomed-in image.
		// This step involves lots of math. Yay, math.
		final Rect startBounds = new Rect();
		final Rect finalBounds = new Rect();
		final Point globalOffset = new Point();

		// The start bounds are the global visible rectangle of the thumbnail,
		// and the final bounds are the global visible rectangle of the container
		// view. Also set the container view's offset as the origin for the
		// bounds, since that's the origin for the positioning animation
		// properties (X, Y).
		thumbView.getGlobalVisibleRect(startBounds);
		findViewById(R.id.ll_content_detail_castration)
				.getGlobalVisibleRect(finalBounds, globalOffset);
		startBounds.offset(-globalOffset.x, -globalOffset.y);
		finalBounds.offset(-globalOffset.x, -globalOffset.y);

		// Adjust the start bounds to be the same aspect ratio as the final
		// bounds using the "center crop" technique. This prevents undesirable
		// stretching during the animation. Also calculate the start scaling
		// factor (the end scaling factor is always 1.0).
		float startScale;
		if ((float) finalBounds.width() / finalBounds.height()
				> (float) startBounds.width() / startBounds.height()) {
			// Extend start bounds horizontally
			startScale = (float) startBounds.height() / finalBounds.height();
			float startWidth = startScale * finalBounds.width();
			float deltaWidth = (startWidth - startBounds.width()) / 2;
			startBounds.left -= deltaWidth;
			startBounds.right += deltaWidth;
		} else {
			// Extend start bounds vertically
			startScale = (float) startBounds.width() / finalBounds.width();
			float startHeight = startScale * finalBounds.height();
			float deltaHeight = (startHeight - startBounds.height()) / 2;
			startBounds.top -= deltaHeight;
			startBounds.bottom += deltaHeight;
		}

		// Hide the thumbnail and show the zoomed-in view. When the animation
		// begins, it will position the zoomed-in view in the place of the
		// thumbnail.
		thumbView.setAlpha(0f);
		expandedImageView.setVisibility(View.VISIBLE);

		// Set the pivot point for SCALE_X and SCALE_Y transformations
		// to the top-left corner of the zoomed-in view (the default
		// is the center of the view).
		expandedImageView.setPivotX(0f);
		expandedImageView.setPivotY(0f);

		// Construct and run the parallel animation of the four translation and
		// scale properties (X, Y, SCALE_X, and SCALE_Y).
		AnimatorSet set = new AnimatorSet();
		set
				.play(ObjectAnimator.ofFloat(expandedImageView, View.X,
						startBounds.left, finalBounds.left))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
						startBounds.top, finalBounds.top))
				.with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
						startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
				View.SCALE_Y, startScale, 1f));
		set.setDuration(mShortAnimationDuration);
		set.setInterpolator(new DecelerateInterpolator());
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mCurrentAnimator = null;
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				mCurrentAnimator = null;
			}
		});
		set.start();
		mCurrentAnimator = set;

		// Upon clicking the zoomed-in image, it should zoom back down
		// to the original bounds and show the thumbnail instead of
		// the expanded image.
		final float startScaleFinal = startScale;
		expandedImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mCurrentAnimator != null) {
					mCurrentAnimator.cancel();
				}

				// Animate the four positioning/sizing properties in parallel,
				// back to their original values.
				AnimatorSet set = new AnimatorSet();
				set.play(ObjectAnimator
						.ofFloat(expandedImageView, View.X, startBounds.left))
						.with(ObjectAnimator
								.ofFloat(expandedImageView,
										View.Y,startBounds.top))
						.with(ObjectAnimator
								.ofFloat(expandedImageView,
										View.SCALE_X, startScaleFinal))
						.with(ObjectAnimator
								.ofFloat(expandedImageView,
										View.SCALE_Y, startScaleFinal));
				set.setDuration(mShortAnimationDuration);
				set.setInterpolator(new DecelerateInterpolator());
				set.addListener(new AnimatorListenerAdapter() {
					@Override
					public void onAnimationEnd(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						thumbView.setAlpha(1f);
						expandedImageView.setVisibility(View.GONE);
						mCurrentAnimator = null;
					}
				});
				set.start();
				mCurrentAnimator = set;
			}
		});
	}

	/*
    Revisión permisos Android 6.+
     */
	public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

	public boolean checkLocationPermission() {
		if (ContextCompat.checkSelfPermission(this,
				Manifest.permission.ACCESS_FINE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {

			// Should we show an explanation?
			if (ActivityCompat.shouldShowRequestPermissionRationale(this,
					Manifest.permission.ACCESS_FINE_LOCATION)) {

				new AlertDialog.Builder(this)
						.setTitle(R.string.txt_title_location_permission)
						.setMessage(R.string.txt_location_permission)
						.setPositiveButton(R.string.txt_location_accept, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								//Prompt the user once explanation has been shown
								ActivityCompat.requestPermissions(DetailCastrationActivity.this,
										new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
										MY_PERMISSIONS_REQUEST_LOCATION);
							}
						})
						.create()
						.show();
			} else {
				ActivityCompat.requestPermissions(this,
						new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
						MY_PERMISSIONS_REQUEST_LOCATION);
			}
			return false;
		} else {
			return true;
		}
	}
}
