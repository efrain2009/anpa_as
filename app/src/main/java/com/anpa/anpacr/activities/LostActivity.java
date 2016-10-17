package com.anpa.anpacr.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.LostListAdapter;
import com.anpa.anpacr.app42.AsyncApp42ServiceApi;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.Lost;
import com.anpa.anpacr.domain.News;
import com.anpa.anpacr.fragments.LastNewsFragment;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

public class LostActivity extends AnpaAppFraqmentActivity implements
		AsyncApp42ServiceApi.App42StorageServiceListener{

	
	List<Lost> lostList;
	private LostListAdapter lostAdapter;
	private ListView lv_lost;

	//App42:
	private AsyncApp42ServiceApi asyncService;
	private String docId = "";
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_lost);

		//App42:
		asyncService = AsyncApp42ServiceApi.instance(this);

		lostList = new ArrayList<Lost>();
		
		//Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_LOST);
		
		Button btnAddLost = (Button) findViewById(R.id.btn_add_lost);
		btnAddLost.setOnClickListener(onAddLost);
		
		lv_lost = (ListView) findViewById(R.id.list_lost);
		lostAdapter = new LostListAdapter(this, lostList);
		lv_lost.setOnItemClickListener(onclickListLost);
		lv_lost.setAdapter(lostAdapter);
		

		//Se carga la lista de perdidos
		try {
			/* App42 */
			progressDialog = ProgressDialog.show(LostActivity.this,
					Constants.ESPERA, Constants.ESPERA_PERDIDO);
			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_PERDIDOS, 1, this);

		} catch (Exception e) {
			showMessage(Constants.MSJ_ERROR_NOTICIA);
			e.printStackTrace();
		}
		finally {
			lostAdapter.notifyDataSetChanged();
		}
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
			case 1://Perdidos
				decodeLostJson(response);
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


	/* Metodo para decodificar el json de noticias */
	private void decodeLostJson(Storage response){
		ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();
		String sIdLost = "", sNomMascota = "", dCreationDate = "", sNomDueno = "", date = "", sTelefono ="", sDetalle ="", sLatitud ="", sLongitud ="", sRaza ="";
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		int iProvincia = 0, iCanton = 0;

		for(int i=0; i < jsonDocList.size(); i ++){
			sIdLost = jsonDocList.get(i).getDocId();
			dCreationDate = jsonDocList.get(i).getCreatedAt();
			//date = dt.format(dCreationDate);

			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
				sNomMascota = jsonObject.getString(Constants.NOM_MASCOTA);
				sNomDueno = jsonObject.getString(Constants.NOM_DUENO);
				sTelefono = jsonObject.getString(Constants.TELEFONO_PERDIDO);
				iProvincia = jsonObject.getInt(Constants.PROVINCIA_PERDIDO);
				iCanton = jsonObject.getInt(Constants.CANTON_PERDIDO);
				sDetalle = jsonObject.getString(Constants.DETALLE_PERDIDO);
				sLatitud = jsonObject.getString(Constants.LATITUD_PERDIDO);
				sLongitud = jsonObject.getString(Constants.LONGITUD_PERDIDO);
				sRaza = jsonObject.getString(Constants.RAZA_PERDIDO);
				/*
				ParseFile imageFile = lostParse
						.getParseFile(Constants.FOTO_PERDIDO);
				*/
				Lost newLost = new Lost(sIdLost, sNomMascota, sNomDueno,sTelefono, iProvincia, iCanton, sDetalle, sRaza, date, null, sLatitud, sLongitud);
				lostList.add(newLost);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		lostAdapter = new LostListAdapter(this, lostList);
		lostAdapter.notifyDataSetChanged();
		lv_lost.setAdapter(lostAdapter);
	}

	/**
	 * Muestra un mensaje TOAST.
	 * @param message
	 */
	private void showMessage(String message){
		Toast.makeText(LostActivity.this, message, Toast.LENGTH_SHORT).show();
	}
	
	private OnItemClickListener onclickListLost = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Lost lost = (Lost) lostAdapter.getItem(position);
						
			Intent intent = new Intent(LostActivity.this, DetailLostActivity.class);
			intent.putExtra(Constants.ID_OBJ_DETAIL_LOST, lost);
			startActivity(intent);
		}
	};
	
	/**
	 * Listener del boton
	 */
	private OnClickListener onAddLost = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(LostActivity.this, AddLostActivity.class));
		}
	};
}
