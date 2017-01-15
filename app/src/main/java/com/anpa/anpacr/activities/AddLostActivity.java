package com.anpa.anpacr.activities;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.SpinnerAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Tip;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AddLostActivity extends AnpaAppFraqmentActivity {

	//App42:
	private String docId = "";
	private ProgressDialog progressDialog;
	ServiceAPI api;
	StorageService storageService;

	EditText editxt_description_tip, editxt_breed_author;
	private Spinner raceSpinner, specieSpinner;
	private SpinnerAdapter adapter;
	Button saveTip;
	String _sRaza;
	Tip tip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tip);

		// Instancia la BD App 42
		api = new ServiceAPI(Constants.App42ApiKey, Constants.App42ApiSecret);

		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);

		saveTip = (Button) findViewById(R.id.btn_add_tip);
		saveTip.setOnClickListener(onSave);

		editxt_description_tip = (EditText) findViewById(R.id.editxt_description_tip);

		editxt_breed_author = (EditText) findViewById(R.id.editxt_breed_author);

		//Crea la lista de razas:
		ArrayList<GenericNameValue> racesItems = new ArrayList<GenericNameValue>();
		for (String race : Constants.RACES) {
			String[] raceSplit = race.split(",");
			racesItems.add(new GenericNameValue(raceSplit[1], Integer.parseInt(raceSplit[0])));
		}

		adapter = new SpinnerAdapter(AddLostActivity.this,
				R.layout.spinner_item,
				racesItems);
		specieSpinner = (Spinner) findViewById(R.id.spn_specie_selector);
		specieSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
		specieSpinner.setOnItemSelectedListener(onSelectItem);

		raceSpinner = (Spinner)findViewById(R.id.spn_race_selector);
	}

	/**
	 * Listener del boton
	 */
	private OnClickListener onSave = new OnClickListener() {

		@Override
		public void onClick(View v) {

			String dbName = Constants.App42DBName;
			String collectionName = Constants.TABLE_CONSEJO;

			JSONObject tipJSON = new JSONObject();
			Util.textAsJSON(tipJSON, Constants.DESCR_CONSEJO, editxt_description_tip.getText().toString() , -1);
			Util.textAsJSON(tipJSON, Constants.AUTOR_CONSEJO, editxt_breed_author.getText().toString() , -1);
			Util.textAsJSON(tipJSON, Constants.RAZA_CONSEJO, "" , raceSpinner.getAdapter().getItemId(raceSpinner.getSelectedItemPosition()));
			Util.textAsJSON(tipJSON, Constants.ESPECIE_CONSEJO, "" ,  specieSpinner.getAdapter().getItemId(specieSpinner.getSelectedItemPosition()));
			Util.textAsJSON(tipJSON, Constants.ESTRELLA1_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA2_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA3_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA4_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA5_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.VOTOS_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTADO_CONSEJO, "" , 0);

			// instacia Storage App42
			storageService = api.buildStorageService();
			/* Below snippet will save JSON object in App42 Cloud */
			storageService.insertJSONDocument(dbName,collectionName,tipJSON,new App42CallBack() {
				public void onSuccess(Object response)
				{
					Storage storage  = (Storage )response;
					ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
					for(int i=0;i<jsonDocList.size();i++)
					{
						System.out.println("objectId is " + jsonDocList.get(i).getDocId());
						//Above line will return object id of saved JSON object
						System.out.println("CreatedAt is " + jsonDocList.get(i).getCreatedAt());
						System.out.println("UpdatedAtis " + jsonDocList.get(i).getUpdatedAt());
						System.out.println("Jsondoc is " + jsonDocList.get(i).getJsonDoc());
					}
				}
				public void onException(Exception ex)
				{
					System.out.println("Exception Message"+ex.getMessage());
				}
			});
			alertDialog ();
		}
	};

	public void alertDialog (){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Constants.MSJ_SUCCESS_TIP)
				.setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
					}
				}).create().show();
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
		SpinnerAdapter adapterRaces = new SpinnerAdapter(AddLostActivity.this,
				R.layout.spinner_item,
				speciesList);
		raceSpinner.setAdapter(adapterRaces);
	}
}
