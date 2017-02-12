package com.anpa.anpacr.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.FreqAnswer;
import com.anpa.anpacr.fragments.FreqAnswerCastrationFragment;
import com.anpa.anpacr.fragments.LastCastrationFragment;
import com.anpa.anpacr.fragments.SuggestionCastrationFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FreqAnswerActivity  extends AnpaAppFraqmentActivity implements FreqAnswerCastrationFragment.OnLoadListListenerFreqAnswerCastration{
	
	List<FreqAnswer> freqAnswerList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_empty);
		
		freqAnswerList = new ArrayList<FreqAnswer>();
		
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CASTRATION);
		
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		/*Instacia de los tabs a crear*/
		ActionBar.Tab tab_last_castration = actionBar.newTab();
		tab_last_castration.setText(Constants.TITLE_LAST_CASTRATION);
		
		ActionBar.Tab tab_freq_Answer_castration = actionBar.newTab();
		tab_freq_Answer_castration.setText(Constants.TITLE_FREQ_ANSWER);
		
		ActionBar.Tab tab_suggestion = actionBar.newTab();
		tab_suggestion.setText(Constants.TITLE_SUGGESTION);
		
		//Se carga la lista de castraciones
				try {
					
					new LoadFreqAnswerCastrationParse().execute("").get(); //El ".get" Hace esperar hasta que el hilo termine.
					
				} catch (InterruptedException e) {
					showMessage("Ups! Perdimos el rastro de las preguntas frecuentes. Intenta más tarde.");
					e.printStackTrace();
				} catch (ExecutionException e) {
					showMessage("Ups! Perdimos el rastro de las preguntas frecuentes. Intenta más tarde.");
					e.printStackTrace();
				}
						
				/*Asigna a los tabs el listener*/
				tab_last_castration.setTabListener(new FreqAnswerListener());
				tab_freq_Answer_castration.setTabListener(new FreqAnswerListener());
				tab_suggestion.setTabListener(new FreqAnswerListener());
				
				/*Agrega los tabs a creat*/
				actionBar.addTab(tab_last_castration);
				actionBar.addTab(tab_freq_Answer_castration);
				actionBar.addTab(tab_suggestion);
			}
			
	/*Lisenner para cambio de tabs */
	private class FreqAnswerListener implements ActionBar.TabListener{

		@Override
		public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			if(tab.getPosition() == 0){
				LastCastrationFragment frag = new LastCastrationFragment();
				ft.replace(android.R.id.content, frag);
			}if(tab.getPosition() == 1){
				FreqAnswerCastrationFragment frag = new FreqAnswerCastrationFragment();
				ft.replace(android.R.id.content, frag);			
			}else{
				SuggestionCastrationFragment frag = new SuggestionCastrationFragment();
				ft.replace(android.R.id.content, frag);
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
		
		//Carga lista de preguntas frecuentes
		private class LoadFreqAnswerCastrationParse extends AsyncTask<String, Integer, Boolean> {
			private ProgressDialog progressDialog;
			@Override
			protected void onPreExecute() {
				progressDialog = ProgressDialog.show(FreqAnswerActivity.this,
						"Espera un momento", "Olfateando preguntas frecuentes....");
			}
			@Override
			protected Boolean doInBackground(String... param) {
				/*try {

					ParseQuery<ParseObject> query = ParseQuery
							.getQuery(Constants.TABLE_PREGUNTA_FREC);
					query.addAscendingOrder(Constants.ORDEN_PREGUNTA);
					query.whereEqualTo(Constants.TIPO_PREGUNTA, 1);
					query.selectKeys(Arrays.asList(Constants.DESC_PREGUNTA,
							Constants.ORDEN_PREGUNTA, Constants.RESPESTA_PREGUNTA, Constants.TIPO_PREGUNTA));// selecciona
					
					List<ParseObject> results = query.find();
					
					for (ParseObject parse : results) {
						final String sId = parse.getObjectId();
						final String sPregunta = parse.getString(Constants.DESC_PREGUNTA);
						final String sRespuesta = parse.getString(Constants.RESPESTA_PREGUNTA);
						final Date dCreationDate = parse.getCreatedAt();
						
						FreqAnswer newFreqAnswer = new FreqAnswer();
						newFreqAnswer.set_lId(sId);
						newFreqAnswer.set_spregunta(sPregunta);
						newFreqAnswer.set_srespuesta(sRespuesta);
					//	newFreqAnswer.set_dCreationDate(dCreationDate);
						
						
						freqAnswerList.add(newFreqAnswer);

					}				
				} catch (ParseException e) {
					showMessage(e.getMessage());
					e.printStackTrace();
				}*/
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					progressDialog.dismiss();
					//LastCastrationFragment.getInstance().refreshList(CastrationList);
				}
			}
		}
		
		/**
		 * Muestra un mensaje TOAST.
		 * @param message
		 */
		private void showMessage(String message){
			Toast.makeText(FreqAnswerActivity.this, message, Toast.LENGTH_SHORT).show();
		}

		
		/*
		 * Implementacion del Interface que envia la lista al fragment.
		 */
		
		@Override
		public List<FreqAnswer> loadFreqAnswerList() {
			return freqAnswerList;
		}
		
}
