package com.anpa.anpacr.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.SpinnerAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Tip;
import com.parse.ParseObject;

public class AddTipActivity extends AnpaAppFraqmentActivity {
	
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

		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);

		editxt_description_tip = (EditText) findViewById(R.id.editxt_description_tip);

		editxt_breed_author = (EditText) findViewById(R.id.editxt_breed_author);
		
		//Crea la lista de razas:
		ArrayList<GenericNameValue> racesItems = new ArrayList<GenericNameValue>();
		for (String race : Constants.RACES) {
			String[] raceSplit = race.split(",");
			racesItems.add(new GenericNameValue(raceSplit[1], Integer.parseInt(raceSplit[0])));
		}
		
		adapter = new SpinnerAdapter(AddTipActivity.this,
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
			
			ParseObject objTip = new ParseObject(Constants.TABLE_CONSEJO);
			objTip.put(Constants.DESCR_CONSEJO, editxt_description_tip.getText().toString());
			objTip.put(Constants.AUTOR_CONSEJO, editxt_breed_author.getText().toString());
			objTip.put(Constants.RAZA_CONSEJO, Integer.parseInt(raceSpinner.getSelectedItem().toString()));
			objTip.put(Constants.ESPECIE_CONSEJO, Integer.parseInt(specieSpinner.getSelectedItem().toString()));
			objTip.saveInBackground();	
			alertDialog ();
			saveTip.setEnabled(false);
		}
	};
	
	public void alertDialog (){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Dentro de poco se publicara tu aviso")
               .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
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
        SpinnerAdapter adapterRaces = new SpinnerAdapter(AddTipActivity.this,
	            R.layout.spinner_item,
	            speciesList);
        raceSpinner.setAdapter(adapterRaces);	
    }
}
