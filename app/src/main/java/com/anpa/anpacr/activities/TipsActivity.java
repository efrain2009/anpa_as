package com.anpa.anpacr.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.anpa.anpacr.adapter.TipListAdapter;
import com.anpa.anpacr.app42.AsyncApp42ServiceApi;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.Tip;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TipsActivity extends AnpaAppFraqmentActivity implements
		AsyncApp42ServiceApi.App42StorageServiceListener{

	List<Tip> tipsList;
	private TipListAdapter tipsAdapter;
	private ListView lv_tips;

	//App42:
	private AsyncApp42ServiceApi asyncService;
	private String docId = "";
	private ProgressDialog progressDialog;
	Long razaBusqueda;
	Long especieBusqueda;
	public static final String TAG_TIPS = "tips";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_tip);

		Bundle pantallaBusquedaTip = getIntent().getExtras();
		razaBusqueda = pantallaBusquedaTip.getLong("razaSearch");
		especieBusqueda = pantallaBusquedaTip.getLong("especieSearch");

		//App42:
		asyncService = AsyncApp42ServiceApi.instance(this);

		tipsList = new ArrayList<Tip>();


		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);

		Button btnAddTip = (Button) findViewById(R.id.btn_add_tip);
		btnAddTip.setOnClickListener(onAddTip);

		lv_tips = (ListView) findViewById(R.id.list_tips);
		tipsAdapter = new TipListAdapter(this, tipsList);
		lv_tips.setOnItemClickListener(onclickListTips);
		lv_tips.setAdapter(tipsAdapter);

		// Se carga la lista de tips
		try {
			/* App42 */
			progressDialog = ProgressDialog.show(TipsActivity.this,
					Constants.ESPERA, Constants.ESPERA_CONSEJO);

			//Ejecurar filtros de consejos habilitados agregar la especie y raza
			Query queryConsejo1 = QueryBuilder.build(Constants.HABILITADO_CONSEJO, 1, QueryBuilder.Operator.EQUALS);
			Query queryConsejo2 = QueryBuilder.build(Constants.RAZA_CONSEJO, razaBusqueda, QueryBuilder.Operator.EQUALS);
			Query queryConsejo3  = QueryBuilder.compoundOperator(queryConsejo1, QueryBuilder.Operator.AND, queryConsejo2);
			Query queryConsejo4 = QueryBuilder.build(Constants.ESPECIE_CONSEJO, especieBusqueda, QueryBuilder.Operator.EQUALS);
			Query queryConsejo5  = QueryBuilder.compoundOperator(queryConsejo3, QueryBuilder.Operator.AND, queryConsejo4);

			asyncService.findDocByColletionQuery(Constants.App42DBName, Constants.TABLE_CONSEJO, queryConsejo5, 1, this);

		} catch (Exception e) {
			showMessage(Constants.MSJ_ERROR_CONSEJO);
			e.printStackTrace();
		}
		finally {
			tipsAdapter.notifyDataSetChanged();
		}

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
				case 1://tips
					//decodeTipsJson(response);
					new AsyncLoadListTask().execute(response);
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
		progressDialog.dismiss();
		Toast.makeText(getApplicationContext(), "No hay experiencias registradas por el momento", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onUpdateDocFailed(App42Exception ex) {
			// TODO Auto-generated method stub

		}

	private class AsyncLoadListTask extends AsyncTask<Storage, Integer, Boolean> {
		ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(Storage... storage) {
			ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();

			String sIdTip = "", sAutor = "", sConsejo = "", dCreationDate = "";
			Integer totalVotos = 0, cincoEstrellas = 0, cuatroEstrellas = 0, tresEstrellas = 0, dosEstrellas = 0, unoEstrellas = 0, especie = 0, raza = 0, estado = 0, iHabilitado = 0;
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

			for(int i=0; i < jsonDocList.size(); i ++) {
				sIdTip = jsonDocList.get(i).getDocId();
				dCreationDate = jsonDocList.get(i).getCreatedAt();
				//date = dt.format(dCreationDate);

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
					sAutor = jsonObject.getString(Constants.AUTOR_CONSEJO);
					sConsejo = jsonObject.getString(Constants.DESCR_CONSEJO);
					unoEstrellas = jsonObject.getInt(Constants.ESTRELLA1_CONSEJO);
					dosEstrellas = jsonObject.getInt(Constants.ESTRELLA2_CONSEJO);
					tresEstrellas = jsonObject.getInt(Constants.ESTRELLA3_CONSEJO);
					cuatroEstrellas = jsonObject.getInt(Constants.ESTRELLA4_CONSEJO);
					cincoEstrellas = jsonObject.getInt(Constants.ESTRELLA5_CONSEJO);
					totalVotos = jsonObject.getInt(Constants.VOTOS_CONSEJO);
					raza = jsonObject.getInt(Constants.RAZA_CONSEJO);
					especie = jsonObject.getInt(Constants.ESPECIE_CONSEJO);
					iHabilitado = jsonObject.getInt(Constants.HABILITADO_CONSEJO);
					if (iHabilitado == 1) {
						Tip newTip = new Tip(sIdTip, Util.decode64AsText(sAutor), Util.decode64AsText(sConsejo), null, unoEstrellas, dosEstrellas, tresEstrellas, cuatroEstrellas, cincoEstrellas, totalVotos, raza, especie, dCreationDate, estado, iHabilitado);
						tipsList.add(newTip);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				} catch (UnsupportedEncodingException e) {
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
		tipsAdapter = new TipListAdapter(this, tipsList);
		tipsAdapter.notifyDataSetChanged();
		lv_tips.setAdapter(tipsAdapter);
		progressDialog.dismiss();
	}


	/**
	 * Muestra un mensaje TOAST.
	 * 
	 * @param message
	 */
	private void showMessage(String message) {
		Toast.makeText(TipsActivity.this, message, Toast.LENGTH_SHORT).show();
	}

	private OnItemClickListener onclickListTips = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Tip tip = (Tip) tipsAdapter.getItem(position);

			Intent intent = new Intent(TipsActivity.this,
					DetailTipActivity.class);
			intent.putExtra(Constants.ID_OBJ_DETAIL_TIP, tip);
			intent.putExtra("razaSearch", razaBusqueda);
			intent.putExtra("especieSearch", especieBusqueda);
			startActivity(intent);
		}
	};

	/**
	 * Listener del boton
	 */
	private OnClickListener onAddTip = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(new Intent(TipsActivity.this, AddTipActivity.class));
			intent.putExtra("razaSearch", razaBusqueda);
			intent.putExtra("especieSearch", especieBusqueda);
			startActivity(intent);
		}
	};

	/*
	 * Implementacion del Interface que envia la lista al fragment.
	 */
	public List<Tip> loadList() {
		return tipsList;
	}
}
