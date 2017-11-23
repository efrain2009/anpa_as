package com.anpa.anpacr.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.app42.AsyncApp42ServiceApi;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.Castration;
import com.anpa.anpacr.domain.FreqAnswer;
import com.anpa.anpacr.fragments.FreqAnswerCastrationFragment;
import com.anpa.anpacr.fragments.LastCastrationFragment;
import com.anpa.anpacr.fragments.SuggestionCastrationFragment;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class CastrationActivity extends AnpaAppFraqmentActivity implements
		LastCastrationFragment.OnLoadListListener,
		SuggestionCastrationFragment.OnLoadListListenerSuggestionCastration,
		FreqAnswerCastrationFragment.OnLoadListListenerFreqAnswerCastration,
		AsyncApp42ServiceApi.App42StorageServiceListener{

	List<Castration> castrationList;
	List<FreqAnswer> freqAnswerList;
	List<FreqAnswer> suggestionList;

	//App42:
	private AsyncApp42ServiceApi asyncService;
	private String docId = "";
	private ProgressDialog progressDialog;

	public static final String TAG_CASTRATION = "castraciones";

	private ViewPager viewPager;
	private TabLayout tabLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);

		//App42:
		asyncService = AsyncApp42ServiceApi.instance(this);

		castrationList = new ArrayList<Castration>();
		freqAnswerList = new ArrayList<FreqAnswer>();
		suggestionList = new ArrayList<FreqAnswer>();

		// Btn de back (anterior)
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CASTRATION);

		tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		// Set the text for each tab.
		tabLayout.addTab(tabLayout.newTab().setText(Constants.TITLE_LAST_CASTRATION));
		tabLayout.addTab(tabLayout.newTab().setText(Constants.TITLE_FREQ_ANSWER));
		tabLayout.addTab(tabLayout.newTab().setText(Constants.TITLE_SUGGESTION));
		// Set the tabs to fill the entire layout.
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		viewPager = (ViewPager) findViewById(R.id.pager);

		//Se carga la lista de noticias
		try {
			/* App42 */
			progressDialog = ProgressDialog.show(CastrationActivity.this,
					Constants.ESPERA, Constants.ESPERA_CASTRACION);
			//Ejecutar filtro de solo los habilitados de las castraciones
			Query queryCast1 = QueryBuilder.build(Constants.HABILITADO_CASTRACION, 1, QueryBuilder.Operator.EQUALS);

			//Set fechas para realizar filtro de 2 meses en castraciones
			Calendar fechaFilter = Calendar.getInstance();
			Date fechaInicioFilter = fechaFilter.getTime();
			fechaFilter.set(Calendar.MONTH, 2);
			Date fechaFinFilter = fechaFilter.getTime();

			//Ejecurar fiiltros de preguntas frecuentes estado = 0 y habilitados
			Query queryPreg1 = QueryBuilder.build(Constants.HABILITADO_PREGUNTA, 1, QueryBuilder.Operator.EQUALS);
			Query queryPreg2 = QueryBuilder.build(Constants.TIPO_PREGUNTA, 0, QueryBuilder.Operator.EQUALS);
			Query queryPreg3  = QueryBuilder.compoundOperator(queryPreg1, QueryBuilder.Operator.AND, queryPreg2);

			asyncService.findDocByColletionQuery(Constants.App42DBName, Constants.TABLE_CASTRACIONES, queryCast1, 1, this);
			asyncService.findDocByColletionQuery(Constants.App42DBName, Constants.TABLE_PREGUNTA_FREC, queryPreg3 , 2, this);

		} catch (Exception e) {
			showMessage(Constants.MSJ_ERROR_CASTRATION);
			e.printStackTrace();
		}
		finally {
			//updateAdapterLastCastrationFragment();
		}
	}

	private void setAdapter(){
		final CastrationActivity.PagerAdapter adapter = new CastrationActivity.PagerAdapter
				(getSupportFragmentManager(), tabLayout.getTabCount());
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new
				TabLayout.TabLayoutOnPageChangeListener(tabLayout));
		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				viewPager.setCurrentItem(tab.getPosition());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
			}
		});
	}

	@Override
	public void onDocumentInserted(Storage response) {
	}

	@Override
	public void onUpdateDocSuccess(Storage response) {

	}

	@Override
	public void onFindDocSuccess(Storage response, int type) {

		switch (type) {
			case 1://Castraciones
				new AsyncLoadListTask().execute(response);
				break;
			case 2://Preguntas
				new AsyncLoadFreqAnswerListTask().execute(response);
				break;
			default:
				progressDialog.dismiss();
				break;
		}
	}

	@Override
	public void onInsertionFailed(App42Exception ex) {

	}

	@Override
	public void onFindDocFailed(App42Exception ex) {
	    progressDialog.dismiss();
	}

	@Override
	public void onUpdateDocFailed(App42Exception ex) {

	}

	/**
	 * Muestra un mensaje TOAST.
	 *
	 * @param message
	 */
	private void showMessage(String message) {
		Toast.makeText(CastrationActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	/*
	 * Implementacion del Interface que envia la lista al fragment.
	 */
	@Override
	public List<Castration> loadList() {
		return castrationList;
	}

	@Override
	public List<FreqAnswer> loadFreqAnswerList() {
		return freqAnswerList;
	}

	@Override
	public List<FreqAnswer> loadSuggestionList() {
		return suggestionList;
	}


	private class AsyncLoadListTask extends AsyncTask<Storage, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(Storage... storage) {

			ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();
			String sIdCastration = "", sNombre = "", sDescripcion = "", sDoctor = "",
					date = "", direccion = "", encargado = "", latitud = "", longitud = "", dInicioDate = "", dFinDate = "", sPhotoURL = "";
			Integer tipo = 0, habilitado = 0, muestraMonto = 0;
			Double monto = Double.valueOf(0);
			Date dCreationDate;

			SimpleDateFormat dt = new SimpleDateFormat("dd-MM-yyyy HH:mm");

			for(int i=0; i < jsonDocList.size(); i ++){
				sIdCastration = jsonDocList.get(i).getDocId();
				date = jsonDocList.get(i).getCreatedAt();

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
					sNombre = jsonObject.getString(Constants.NOMBRE_CASTRACION);
					sDescripcion = jsonObject.getString(Constants.DESCRIPCION_CASTRACION);
					sDoctor = jsonObject.getString(Constants.DOCTOR_CASTRACION);
					direccion = jsonObject.getString(Constants.DIRECCION_CASTRACION);
					encargado = jsonObject.getString(Constants.ENCARGADO_CASTRACION);
					monto = jsonObject.getDouble(Constants.MONTO_CASTRACION);
					dInicioDate = jsonObject.getString(Constants.HORARIO_INICIO_CASTRACION);
					dFinDate = jsonObject.getString(Constants.HORARIO_FIN_CASTRACION);
					habilitado = jsonObject.getInt(Constants.HABILITADO_CASTRACION);
					sPhotoURL = jsonObject.getString(Constants.IMAGE_CASTRACION);
					muestraMonto = jsonObject.getInt(Constants.MUESTRA_MONTO_CASTRACION);
					latitud = jsonObject.getString(Constants.LATITUD_CASTRACION);
					longitud = jsonObject.getString(Constants.LONGITUD_CASTRACION);

					byte[] photo = getBitmap(sPhotoURL);

                    if(habilitado == 1) {
                        Castration newCastration = new Castration(sIdCastration, Util.decode64AsText(sNombre), Util.decode64AsText(sDoctor), monto, Util.decode64AsText(direccion),
                               Util.decode64AsText(sDescripcion), Util.decode64AsText(encargado), Util.decode64AsText(dInicioDate), Util.decode64AsText(dFinDate), tipo, date, Util.decode64AsText(latitud),Util.decode64AsText(longitud), photo, habilitado, muestraMonto);
                        castrationList.add(newCastration);
                    }
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			setAdapter();
			if(!result)
				Toast.makeText(getApplicationContext(), "No hay castraciones registradas por el momento", Toast.LENGTH_SHORT).show();
		}
	}


    //Obtiene la imagen desde una URL
    public static byte[] getBitmap(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Bitmap d = BitmapFactory.decodeStream(is);
            is.close();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            d.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        } catch (Exception e) {
            return null;
        }
    }

	/* Metodo para decodificar el json de preguntas */
	private class AsyncLoadFreqAnswerListTask extends AsyncTask<Storage, Integer, Boolean> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(Storage... storage) {

			ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();

			String sIdPreg = "", sPregunta = "", sRespuesta = "", dCreationDate = "";
			Integer iOrden = 0, itipo = 0, iHabilitado = 0;
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

			for (int i = 0; i < jsonDocList.size(); i++) {
				sIdPreg = jsonDocList.get(i).getDocId();
				dCreationDate = jsonDocList.get(i).getCreatedAt();

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
					sPregunta = jsonObject.getString(Constants.DESC_PREGUNTA);
					sRespuesta = jsonObject.getString(Constants.RESPESTA_PREGUNTA);
					iOrden = jsonObject.getInt(Constants.ORDEN_PREGUNTA);
					itipo = jsonObject.getInt(Constants.TIPO_PREGUNTA);
					iHabilitado = jsonObject.getInt(Constants.HABILITADO_PREGUNTA);

					if(iHabilitado == 1) {
						FreqAnswer newPreg = new FreqAnswer(sIdPreg, Util.decode64AsText(sPregunta), Util.decode64AsText(sRespuesta), iOrden, itipo, dCreationDate, iHabilitado);
						freqAnswerList.add(newPreg);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					return false;
				}
				Collections.sort(freqAnswerList, new Comparator<FreqAnswer>() {
					@Override
					public int compare(FreqAnswer freqAnswer1, FreqAnswer freqAnswer2) {
						//comparision for primitive int uses compareTo of the wrapper Integer
						return(new Integer(((FreqAnswer)freqAnswer1).get_iorden()))
								.compareTo(((FreqAnswer)freqAnswer2).get_iorden());
					}
				});
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
			setAdapter();
		}
	}

	/**
	 * Control de los tabs 2017
	 */
	public class PagerAdapter extends FragmentStatePagerAdapter {
		int mNumOfTabs;

		public PagerAdapter(FragmentManager fm, int NumOfTabs) {
			super(fm);
			this.mNumOfTabs = NumOfTabs;
		}
		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return new LastCastrationFragment();
				case 1:
					return new FreqAnswerCastrationFragment();
				case 2:
					return new SuggestionCastrationFragment();
				default:
					return null;
			}
		}
		@Override
		public int getCount() {
			return mNumOfTabs;
		}
	}
}
