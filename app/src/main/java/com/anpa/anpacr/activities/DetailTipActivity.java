package com.anpa.anpacr.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Gps;
import com.anpa.anpacr.domain.Tip;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailTipActivity extends ActionBarActivity {
	private ImageView imgBtn1, imgBtn2, imgBtn3, imgBtn4, imgBtn5;
	private String sTipId;
	ServiceAPI api;
	StorageService storageService;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_tip);
		
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);

		// Instancia la BD App 42
		api = new ServiceAPI(Constants.App42ApiKey, Constants.App42ApiSecret);


		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			Tip value = (Tip) extras.get(Constants.ID_OBJ_DETAIL_TIP);

			TextView txt_raza = (TextView) findViewById(R.id.txt_raza_consejo);
			String raza = readSpecies(value.get_iEspecie(), value.get_iRaza());
			txt_raza.setText(raza);
			
			TextView txt_detail_consejo = (TextView) findViewById(R.id.txt_detail_consejo);
			txt_detail_consejo.setText(value.get_sConsejo());

			TextView txt_detail_autor = (TextView) findViewById(R.id.txt_detail_autor);
			txt_detail_autor.setText(value.get_sAuthor());
			
			sTipId = value.get_lId();
		}
		imgBtn1 = (ImageView)findViewById(R.id.img_detail_tip_star1);
		imgBtn2 = (ImageView)findViewById(R.id.img_detail_tip_star2);
		imgBtn3 = (ImageView)findViewById(R.id.img_detail_tip_star3);
		imgBtn4 = (ImageView)findViewById(R.id.img_detail_tip_star4);
		imgBtn5 = (ImageView)findViewById(R.id.img_detail_tip_star5);
	}
	
	/* carga la lista de razas de una especie */
    private String readSpecies(int specieId, int raza)
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
                if(raza == Integer.parseInt(values[0]))
                	return values[1];
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
		return "";
    }

    //Funci�n para tener el rating
    public void setRating(View view){
    	int nCalification = 0;
    	
    	switch (view.getId()) {
		case R.id.img_detail_tip_star1:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			nCalification = 1;
			break;
		case R.id.img_detail_tip_star2:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			nCalification = 2;
			break;
		case R.id.img_detail_tip_star3:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn3.setBackgroundResource(R.drawable.ic_footprint_color);
			nCalification = 3;
			break;
		case R.id.img_detail_tip_star4:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn3.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn4.setBackgroundResource(R.drawable.ic_footprint_color);
			nCalification = 4;
			break;
		default:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn3.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn4.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn5.setBackgroundResource(R.drawable.ic_footprint_color);
			nCalification = 5;
			break;
		}
    	imgBtn1.setEnabled(false);
    	imgBtn2.setEnabled(false);
    	imgBtn3.setEnabled(false);
    	imgBtn4.setEnabled(false);
    	imgBtn5.setEnabled(false);
    	
    	addCalification(nCalification);
    }
    
    //Actualiza la calificaci�n del consejo
    private void addCalification(final int nCalification){
		//Verifica que el usuario tenga internet:
    	boolean isInternet = Gps.getInstance().internetCheck(getApplicationContext());
    	if(!isInternet)
    		return;
    	
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.TABLE_CONSEJO);
		// Retrieve the object by id
		query.getInBackground(sTipId, new GetCallback<ParseObject>() {
		  public void done(ParseObject tip, ParseException e) {
		    if (e == null) {
		      // Now let's update it with some new data. In this case, only cheatMode and score
		      // will get sent to the Parse Cloud. playerName hasn't changed.
		    	String rowToUpdate = Constants.ESTRELLA1_CONSEJO;
		    	switch (nCalification) {
				case 2:
					rowToUpdate = Constants.ESTRELLA2_CONSEJO;
					break;
				case 3:
					rowToUpdate = Constants.ESTRELLA3_CONSEJO;
					break;
				case 4:
					rowToUpdate = Constants.ESTRELLA4_CONSEJO;
					break;
				default:
					rowToUpdate = Constants.ESTRELLA5_CONSEJO;
					break;
				}
		      tip.increment(rowToUpdate);
		      tip.saveInBackground();
				// instacia Storage App42
				storageService = api.buildStorageService();

				/* Below snippet will save JSON object in App42 Cloud */
				String dbName = Constants.App42DBName;
				String collectionName = Constants.TABLE_CONSEJO;
				JSONObject tipJSON = null;
				storageService.findDocumentById(dbName, collectionName, sTipId, new App42CallBack() {
					@Override
					public void onSuccess(Object response) {
						Storage storage = (Storage) response;
						ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
						/*
						try {
						//	tipJson = new JSONObject(jsonDocList.get(0).getJsonDoc());
						} catch (JSONException e1) {
							e1.printStackTrace();
						}
						*/
					}

					@Override
					public void onException(Exception e) {

					}
				});

					storageService.updateDocumentByDocId(dbName, collectionName, sTipId, tipJSON, new App42CallBack() {
						@Override
						public void onSuccess(Object response) {
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

						@Override
						public void onException(Exception e) {

						}
					});

				alertDialog ();
		      Toast.makeText(getApplicationContext(), Constants.TIP_SUCCESS, Toast.LENGTH_LONG).show();
		    }
		  }
		});
	};

	public void alertDialog (){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Constants.MSJ_SUCCESS_CALIFICATION_TIP)
				.setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
					}
				}).create().show();
	}

}
