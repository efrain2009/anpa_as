package com.anpa.anpacr.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.SpinnerAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.GenericNameValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TipSearchActivity extends AnpaAppFraqmentActivity {
	private Spinner specieSpinner, raceSpinner;
	private SpinnerAdapter adapter;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_tip_search);
			
			//Btn de back (anterior)
			Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
			setSupportActionBar(toolbar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayShowHomeEnabled(true);
			getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);
			
			//Crea la lista de razas:
			ArrayList<GenericNameValue> racesItems = new ArrayList<GenericNameValue>();
			for (String race : Constants.RACES) {
				String[] raceSplit = race.split(",");
				racesItems.add(new GenericNameValue(raceSplit[1], Integer.parseInt(raceSplit[0])));
			}
			
			adapter = new SpinnerAdapter(TipSearchActivity.this,
					R.layout.spinner_item,
		            racesItems);
			specieSpinner = (Spinner) findViewById(R.id.spn_specie_selector);
			specieSpinner.setAdapter(adapter); // Set the custom adapter to the spinner
			specieSpinner.setOnItemSelectedListener(onSelectItem);
			
			raceSpinner = (Spinner)findViewById(R.id.spn_race_selector);
			
			Button btnSearchTip = (Button)findViewById(R.id.btn_tip_search);
			btnSearchTip.setOnClickListener(onSearch);
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
		
		/**
		 * Listener del boton
		 */
		private OnClickListener onSearch = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TipSearchActivity.this, TipsActivity.class);
				intent.putExtra("razaSearch", raceSpinner.getAdapter().getItemId(raceSpinner.getSelectedItemPosition()));
				intent.putExtra("especieSearch", specieSpinner.getAdapter().getItemId(specieSpinner.getSelectedItemPosition()));
				startActivity(intent);
			}
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
            SpinnerAdapter adapterRaces = new SpinnerAdapter(TipSearchActivity.this,
		            R.layout.spinner_item,
		            speciesList);
            raceSpinner.setAdapter(adapterRaces);
			
        }
}
