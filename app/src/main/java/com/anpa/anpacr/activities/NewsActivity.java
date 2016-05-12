package com.anpa.anpacr.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.app42.AsyncApp42ServiceApi;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.FreqAnswer;
import com.anpa.anpacr.domain.News;
import com.anpa.anpacr.domain.Sponsor;
import com.anpa.anpacr.fragments.FreqAnswerFragment;
import com.anpa.anpacr.fragments.LastNewsFragment;
import com.anpa.anpacr.fragments.SponsorFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

public class NewsActivity extends AnpaAppFraqmentActivity implements 
FreqAnswerFragment.OnLoadListListenerFreqAnswerNews,
SponsorFragment.OnLoadSponsorListListener,
LastNewsFragment.OnLoadListListener,
AsyncApp42ServiceApi.App42StorageServiceListener{
	
	private List<News> newsList;
	private List<FreqAnswer> freqAnswerList;
	private List<Sponsor> sponsorList;
	
	//App42:
	private AsyncApp42ServiceApi asyncService;
	private String docId = "";
	private ProgressDialog progressDialog;
	
	public static final String TAG_NEWS = "noticias";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		//App42:
		asyncService = AsyncApp42ServiceApi.instance(this);

		newsList = new ArrayList<News>();
		freqAnswerList = new ArrayList<FreqAnswer>();
		sponsorList = new ArrayList<Sponsor>();
		
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_NEWS);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		/*Instacia de los tabs a crear*/
		ActionBar.Tab tab_last_news = actionBar.newTab();
		tab_last_news.setText(Constants.TITLE_LAST_NEWS);
		
		ActionBar.Tab tab_freq_Answer = actionBar.newTab();
		tab_freq_Answer.setText(Constants.TITLE_FREQ_ANSWER);
		
		ActionBar.Tab tab_sponsor = actionBar.newTab();
		tab_sponsor.setText(Constants.TITLE_SPONSOR);
		
		//Se carga la lista de noticias
		try {
			/* App42 */
			progressDialog = ProgressDialog.show(NewsActivity.this,
					Constants.ESPERA, Constants.ESPERA_NOTICIAS);
			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_NOTICIA, 1, this);
			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_PREGUNTA_FREC, 2, this);
			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_PATROCINIO, 3, this);

		} catch (Exception e) {
			showMessage(Constants.MSJ_ERROR_NOTICIA);
			e.printStackTrace();
		}
				
		/*Asigna a los tabs el listener*/
		tab_last_news.setTabListener(new NewsListener());
		tab_freq_Answer.setTabListener(new NewsListener());
		tab_sponsor.setTabListener(new NewsListener());
		
		/*Agrega los tabs a creat*/
		actionBar.addTab(tab_last_news);
		actionBar.addTab(tab_freq_Answer);
		actionBar.addTab(tab_sponsor);
	}
	
	/*Lisenner para cambio de tabs */
	private class NewsListener implements ActionBar.TabListener{
		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(tab.getPosition() == 0){
				LastNewsFragment frag = new LastNewsFragment();
				ft.replace(android.R.id.content, frag, TAG_NEWS);
			}else if(tab.getPosition() == 1){
				FreqAnswerFragment frag = new FreqAnswerFragment();
				ft.replace(android.R.id.content, frag, TAG_NEWS);
			}else{
				SponsorFragment frag = new SponsorFragment();
				ft.replace(android.R.id.content, frag, TAG_NEWS);				
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
	 * @param message
	 */
	private void showMessage(String message){
		Toast.makeText(NewsActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	/*
	 * Implementacion del Interface que envia la lista al fragment.
	 */
	@Override
	public List<News> loadList() {
		return newsList;
	}
	
	@Override
	public List<FreqAnswer> loadFreqAnswerList() {
		return freqAnswerList;
	}
	
	@Override
	public List<Sponsor> loadSponsorList() {
		return sponsorList;
	}

	@Override
	public void onDocumentInserted(Storage response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdateDocSuccess(Storage response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFindDocSuccess(Storage response, int type) {
		progressDialog.dismiss();
		switch (type) {
		case 1://Noticias
			decodeNewsJson(response);			
			break;
		case 2://Preguntas
			decodePreguntasFrecuentesJson(response);
			break;
		case 3://Patrocinio
			decodePatrociniosJson(response);
			break;
		default:
			break;
		}
			
	}

	@Override
	public void onInsertionFailed(App42Exception ex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFindDocFailed(App42Exception ex) {
		showMessage(Constants.MSJ_ERROR);
	}

	@Override
	public void onUpdateDocFailed(App42Exception ex) {
		// TODO Auto-generated method stub
		
	}
	
	/* Metodo para decodificar el json de noticias */
	private void decodeNewsJson(Storage response){
		ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();
		String sIdNews = "", sTitle = "", dCreationDate = "", sContent = "", date = "";
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		
		for(int i=0; i < jsonDocList.size(); i ++){
			sIdNews = jsonDocList.get(i).getDocId();
			dCreationDate = jsonDocList.get(i).getCreatedAt();
			//date = dt.format(dCreationDate);
			
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
				sTitle = jsonObject.getString(Constants.TITULO_NOTICIA);
				sContent = jsonObject.getString(Constants.CONTENIDO_NOTICIA);
				News newNews = new News(sIdNews, sTitle, sContent, dCreationDate, null, null);
				newsList.add(newNews);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		getSupportActionBar().setSelectedNavigationItem(0);
        LastNewsFragment frag = new LastNewsFragment();
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(android.R.id.content, frag, TAG_NEWS).commit();
        fm.popBackStackImmediate();
	}


	/* Metodo para decodificar el json de preguntas */
	private void decodePreguntasFrecuentesJson(Storage response){
		ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();

		String sIdPreg = "", sPregunta = "", sRespuesta = "", dCreationDate = "";
		Integer iOrden = 0, itipo = 0;
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
				if(itipo == 1) {
					FreqAnswer newPreg = new FreqAnswer(sIdPreg, sPregunta, sRespuesta, iOrden, itipo, dCreationDate);
					freqAnswerList.add(newPreg);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/* Metodo para decodificar el json de patrocinios */
	private void decodePatrociniosJson(Storage response){
		ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();

		String sIdPatrocinios = "", sNombre = "", sDescripcion = "", sURL = "", dCreationDate="";
		Integer iOrden = 0;

		/*Ver como se hace las imagenes - pegadero*/
		//	ParseFile imageFile

		for(int i=0; i < jsonDocList.size(); i ++){
			sIdPatrocinios = jsonDocList.get(i).getDocId();
			dCreationDate = jsonDocList.get(i).getCreatedAt();

			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
				sNombre = jsonObject.getString(Constants.NOMBRE_PATROCINIO);
				sDescripcion = jsonObject.getString(Constants.DESCRIPCION_PATROCINIO);
				sURL = jsonObject.getString(Constants.URL_PATROCINIO);
				iOrden = jsonObject.getInt(Constants.ORDEN_PATROCINIO);

				Sponsor newSponsor = new Sponsor(sIdPatrocinios, sNombre, sDescripcion, sURL, iOrden, null, dCreationDate);
				sponsorList.add(newSponsor);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
