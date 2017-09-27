package com.anpa.anpacr.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.Lost;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
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

			//Ejecutar filtro de solo los habilitados de las perdidos
			Query query1 = QueryBuilder.build(Constants.HABILITADO_PERDIDO, 1, QueryBuilder.Operator.EQUALS);
			asyncService.findDocByColletionQuery(Constants.App42DBName, Constants.TABLE_PERDIDOS, query1, 1, this);

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
				//decodeLostJson(response);
				new AsyncLoadListTask().execute(response);
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
		progressDialog.dismiss();
		Toast.makeText(getApplicationContext(),Constants.TITTLE_PERDIDO_NO_LIST, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onUpdateDocFailed(App42Exception ex) {

	}


	/* Metodo para decodificar el json de noticias */
	/*private void decodeLostJson(Storage response){
		ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();
		String sIdLost = "", sNomMascota = "", dCreationDate = "", sNomDueno = "", date = "", sTelefono ="", sDetalle ="", sLatitud ="", sLongitud ="", sRaza ="", sPhotoURL = "";
		SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
		int iProvincia = 0, iCanton = 0, iHabilitado = 0;

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
				iHabilitado =jsonObject.getInt(Constants.HABILITADO_PERDIDO);
				sPhotoURL =jsonObject.getString(Constants.FOTO_PERDIDO);

				Bitmap photo = getBitmap(sPhotoURL);

				if(iHabilitado == 1) {
					Lost newLost = new Lost(sIdLost, sNomMascota, sNomDueno, sTelefono, iProvincia, iCanton, sDetalle, sRaza, date, photo, sLatitud, sLongitud, iHabilitado);
					lostList.add(newLost);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		lostAdapter = new LostListAdapter(this, lostList);
		lostAdapter.notifyDataSetChanged();
		lv_lost.setAdapter(lostAdapter);
	}*/

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


	private class AsyncLoadListTask extends AsyncTask<Storage, Integer, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(Storage... storage) {
			ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();
			String sIdLost = "", sNomMascota = "", dCreationDate = "", sNomDueno = "", date = "", sTelefono ="", sDetalle ="", sLatitud ="", sLongitud ="", sPhotoURL = "";
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");
			int iProvincia = 0, iCanton = 0, iHabilitado = 0, iRaza = 0, iEspecie = 0;

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
					iRaza = jsonObject.getInt(Constants.RAZA_PERDIDO);
					iEspecie = jsonObject.getInt(Constants.ESPECIE_PERDIDO);
					iHabilitado =jsonObject.getInt(Constants.HABILITADO_PERDIDO);
					sPhotoURL =jsonObject.getString(Constants.FOTO_PERDIDO);

					byte[] photo = getBitmap(Util.decode64AsText(sPhotoURL));

					if(iHabilitado == 1) {
						Lost newLost = new Lost(sIdLost,  Util.decode64AsText(sNomMascota),  Util.decode64AsText(sNomDueno), Util.decode64AsText(sTelefono), iProvincia, iCanton,  Util.decode64AsText(sDetalle), iRaza, iEspecie, date, photo, Util.decode64AsText(sLatitud), Util.decode64AsText(sLongitud), iHabilitado);
						lostList.add(newLost);
					}
				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result)
				updateAdapter();
		}
	}

	private void updateAdapter(){
		lostAdapter = new LostListAdapter(this, lostList);
		lostAdapter.notifyDataSetChanged();
		lv_lost.setAdapter(lostAdapter);
		progressDialog.dismiss();
	}
}
