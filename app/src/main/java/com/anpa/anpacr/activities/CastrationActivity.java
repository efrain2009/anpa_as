package com.anpa.anpacr.activities;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
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
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.upload.Upload;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CASTRATION);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		/* Instacia de los tabs a crear */
		ActionBar.Tab tab_last_castration = actionBar.newTab();
		tab_last_castration.setText(Constants.TITLE_LAST_CASTRATION);

		ActionBar.Tab tab_freq_Answer_castration = actionBar.newTab();
		tab_freq_Answer_castration.setText(Constants.TITLE_FREQ_ANSWER);

		ActionBar.Tab tab_suggestion = actionBar.newTab();
		tab_suggestion.setText(Constants.TITLE_SUGGESTION);

		//Se carga la lista de noticias
		try {
			/* App42 */
			progressDialog = ProgressDialog.show(CastrationActivity.this,
					Constants.ESPERA, Constants.ESPERA_CASTRACION);
			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_CASTRACIONES, 1, this);
			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_PREGUNTA_FREC, 2, this);

		} catch (Exception e) {
			showMessage(Constants.MSJ_ERROR_CASTRATION);
			e.printStackTrace();
		}
		finally {
			progressDialog.dismiss();
		}

		/*Asigna a los tabs el listener*/
		tab_last_castration.setTabListener(new CastrationListener());
		tab_freq_Answer_castration.setTabListener(new CastrationListener());
		tab_suggestion.setTabListener(new CastrationListener());
		

		/* Agrega los tabs a creat */
		actionBar.addTab(tab_last_castration);
		actionBar.addTab(tab_freq_Answer_castration);
		actionBar.addTab(tab_suggestion);
	}

	@Override
	public void onDocumentInserted(Storage response) {
	}

	@Override
	public void onUpdateDocSuccess(Storage response) {

	}

	@Override
	public void onFindDocSuccess(Storage response, int type) {

		progressDialog.dismiss();
		switch (type) {
			case 1://Castraciones
				decodeCastrationJson(response);
				break;
			case 2://Preguntas
				decodePreguntasFrecuentesJson(response);
				break;
			default:
				break;
		}
	}

	@Override
	public void onInsertionFailed(App42Exception ex) {

	}

	@Override
	public void onFindDocFailed(App42Exception ex) {

	}

	@Override
	public void onUpdateDocFailed(App42Exception ex) {

	}

	/* Lisenner para cambio de tabs */
	private class CastrationListener implements ActionBar.TabListener {


		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if (tab.getPosition() == 0) {
				LastCastrationFragment frag = new LastCastrationFragment();
				ft.replace(android.R.id.content, frag, TAG_CASTRATION);
			} else if (tab.getPosition() == 1) {
				FreqAnswerCastrationFragment frag = new FreqAnswerCastrationFragment();
				ft.replace(android.R.id.content, frag, TAG_CASTRATION);
			} else {
				SuggestionCastrationFragment frag = new SuggestionCastrationFragment();
				ft.replace(android.R.id.content, frag, TAG_CASTRATION);
			}
		}

		@Override
		public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub

		}
	}

	/**
	 * Muestra un mensaje TOAST.
	 *
	 * @param message
	 */
	private void showMessage(String message) {
		Toast.makeText(CastrationActivity.this, message, Toast.LENGTH_SHORT)
				.show();
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

	/* Metodo para decodificar el json de castraciones */
	private void decodeCastrationJson(Storage response) {
		ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();
		String sIdCastration = "", sNombre = "", sDescripcion = "", sDoctor = "",
				date = "", direccion = "", encargado = "", latitud = "", longitud = "", dInicioDate = "", dFinDate = "", fileId = "", fileName = "", fileType = "", fileURL = "";
		Integer tipo = 0, habilitado = 0;
		Double monto = Double.valueOf(0);
		Date dCreationDate;

		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

		for (int i = 0; i < jsonDocList.size(); i++) {
			sIdCastration = jsonDocList.get(i).getDocId();
			date = jsonDocList.get(i).getCreatedAt();
			//date = dt.format(dCreationDate);

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
				JSONObject fotos = (JSONObject) jsonObject.getJSONObject(Constants.FILE);

				if(fotos != null) {
					fileId = fotos.getString(Constants.ID_FILE);
					fileName = fotos.getString(Constants.NAME_FILE);
					fileType = fotos.getString(Constants.TYPE_FILE);
					fileURL = fotos.getString(Constants.URL_FILE);
				}

				byte[] imagen = Util.readBytes(fileURL);

				if (habilitado == 1) {
					Castration newCastration = new Castration(sIdCastration, sNombre, sDoctor, monto, direccion,
							sDescripcion, encargado, dInicioDate, dFinDate, tipo, date, latitud, longitud, imagen, habilitado);

					castrationList.add(newCastration);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			getSupportActionBar().setSelectedNavigationItem(0);
			LastCastrationFragment frag = new LastCastrationFragment();
			FragmentManager fm = getSupportFragmentManager();
			fm.beginTransaction().replace(android.R.id.content, frag, TAG_CASTRATION).commit();
			fm.popBackStackImmediate();
		}
	}

	/* Metodo para decodificar el json de preguntas */
	private void decodePreguntasFrecuentesJson(Storage response){
		ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();

		String sIdPreg = "", sPregunta = "", sRespuesta = "", dCreationDate = "";
		Integer iOrden = 0, itipo = 0, ihabilitado = 0;
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

		for(int i=0; i < jsonDocList.size(); i ++){
			sIdPreg = jsonDocList.get(i).getDocId();
			dCreationDate = jsonDocList.get(i).getCreatedAt();

			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
				sPregunta = jsonObject.getString(Constants.DESC_PREGUNTA);
				sRespuesta = jsonObject.getString(Constants.RESPESTA_PREGUNTA);
				iOrden = jsonObject.getInt(Constants.ORDEN_PREGUNTA);
				itipo = jsonObject.getInt(Constants.TIPO_PREGUNTA);
				ihabilitado = jsonObject.getInt(Constants.HABILITADO_PREGUNTA);
				if(itipo == 0 && ihabilitado == 1) {
					FreqAnswer newPreg = new FreqAnswer(sIdPreg, sPregunta, sRespuesta, iOrden, itipo, dCreationDate, ihabilitado);
					freqAnswerList.add(newPreg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}
