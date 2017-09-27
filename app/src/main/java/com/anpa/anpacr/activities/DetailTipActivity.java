package com.anpa.anpacr.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Gps;
import com.anpa.anpacr.domain.Tip;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DetailTipActivity extends ActionBarActivity {
	private ImageView imgBtn1, imgBtn2, imgBtn3, imgBtn4, imgBtn5;
	private String sTipId;
	ServiceAPI api;
	StorageService storageService;
	Long razaBusqueda;
	Long especieBusqueda;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_tip);

		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);

		Bundle pantallaBusquedaTip = getIntent().getExtras();
		razaBusqueda = pantallaBusquedaTip.getLong("razaSearch");
		especieBusqueda = pantallaBusquedaTip.getLong("especieSearch");

        // Instancia la BD App 42
        api = new ServiceAPI(Constants.App42ApiKey, Constants.App42ApiSecret);

		Button btnAddTip = (Button) findViewById(R.id.btn_add_tip);
		btnAddTip.setOnClickListener(onAddTip);

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
    	String key = "";

    	switch (view.getId()) {
		case R.id.img_detail_tip_star1:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			key = Constants.ESTRELLA1_CONSEJO;
			break;
		case R.id.img_detail_tip_star2:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			key = Constants.ESTRELLA2_CONSEJO;
			break;
		case R.id.img_detail_tip_star3:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn3.setBackgroundResource(R.drawable.ic_footprint_color);
			key = Constants.ESTRELLA3_CONSEJO;
			break;
		case R.id.img_detail_tip_star4:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn3.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn4.setBackgroundResource(R.drawable.ic_footprint_color);
			key = Constants.ESTRELLA4_CONSEJO;
			break;
		default:
			imgBtn1.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn2.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn3.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn4.setBackgroundResource(R.drawable.ic_footprint_color);
			imgBtn5.setBackgroundResource(R.drawable.ic_footprint_color);
			key = Constants.ESTRELLA5_CONSEJO;
			break;
		}
    	imgBtn1.setEnabled(false);
    	imgBtn2.setEnabled(false);
    	imgBtn3.setEnabled(false);
    	imgBtn4.setEnabled(false);
    	imgBtn5.setEnabled(false);
    	

		addPuntuation(key);
		alertDialog();
    }

	private void addPuntuation(String key) {
		storageService = api.buildStorageService();
			storageService.incrementKeyByDocId(Constants.App42DBName, Constants.TABLE_CONSEJO, sTipId, key, BigDecimal.ONE, new App42CallBack() {
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

		storageService.incrementKeyByDocId(Constants.App42DBName, Constants.TABLE_CONSEJO, sTipId, Constants.VOTOS_CONSEJO, BigDecimal.ONE, new App42CallBack() {
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

	}

	public void alertDialog (){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(Constants.MSJ_SUCCESS_CALIFICATION_TIP)
				.setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// FIRE ZE MISSILES!
					}
				}).create().show();
	}


	/**
	 * Listener del boton
	 */
	private View.OnClickListener onAddTip = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(new Intent(DetailTipActivity.this, AddTipActivity.class));
			intent.putExtra("razaSearch", razaBusqueda);
			intent.putExtra("especieSearch", especieBusqueda);
			startActivity(intent);
		}
	};


}
