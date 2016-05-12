package com.anpa.anpacr.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.TipListAdapter;
import com.anpa.anpacr.app42.AsyncApp42ServiceApi;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.domain.News;
import com.anpa.anpacr.domain.Tip;
import com.anpa.anpacr.fragments.LastNewsFragment;
import com.anpa.anpacr.fragments.LastTipsFragment;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

public class TipsActivity extends AnpaAppFraqmentActivity implements
		AsyncApp42ServiceApi.App42StorageServiceListener{

	List<Tip> tipsList;
	private TipListAdapter tipsAdapter;
	private ListView lv_tips;

	//App42:
	private AsyncApp42ServiceApi asyncService;
	private String docId = "";
	private ProgressDialog progressDialog;

	public static final String TAG_TIPS = "tips";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_tip);

		tipsList = new ArrayList<Tip>();

		//App42:
		asyncService = AsyncApp42ServiceApi.instance(this);


		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);

		Button btnAddLost = (Button) findViewById(R.id.btn_add_tip);
		btnAddLost.setOnClickListener(onAddTip);

		lv_tips = (ListView) findViewById(R.id.list_tips);
		tipsAdapter = new TipListAdapter(this, tipsList);
		lv_tips.setOnItemClickListener(onclickListTips);

		// Se carga la lista de tips
		try {
			/* App42 */
			progressDialog = ProgressDialog.show(TipsActivity.this,
					"Espera un momento", "Olfateando consejos....");
			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_CONSEJO, 1, this);

		} catch (Exception e) {
			showMessage("Ups! Perdimos el rastro de los consejos. Intenta más tarde.");
			e.printStackTrace();
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
					decodeTipsJson(response);
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
			showMessage("Ups! Perdimos el rastro de la información. Intenta más tarde.");
		}

		@Override
		public void onUpdateDocFailed(App42Exception ex) {
			// TODO Auto-generated method stub

		}

		/* Metodo para decodificar el json de noticias */
		private void decodeTipsJson(Storage response){
			ArrayList<Storage.JSONDocument> jsonDocList = response.getJsonDocList();

			String sIdTip = "", sAutor = "", sConsejo = "", dCreationDate = "";
			Integer totalVotos = 0, cincoEstrellas = 0, cuatroEstrellas = 0, tresEstrellas = 0, dosEstrellas = 0, unoEstrellas = 0, especie = 0, raza = 0;
			SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy hh:mm aaa");

			for(int i=0; i < jsonDocList.size(); i ++){
				sIdTip = jsonDocList.get(i).getDocId();
				dCreationDate = jsonDocList.get(i).getCreatedAt();
				//date = dt.format(dCreationDate);

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
					sAutor = jsonObject.getString(Constants.AUTOR_CONSEJO);
					sConsejo = jsonObject.getString(Constants.CONSEJOS_PARA);
					unoEstrellas = jsonObject.getInt(Constants.ESTRELLA1_CONSEJO);
					dosEstrellas = jsonObject.getInt(Constants.ESTRELLA2_CONSEJO);
					tresEstrellas = jsonObject.getInt(Constants.ESTRELLA3_CONSEJO);
					cuatroEstrellas = jsonObject.getInt(Constants.ESTRELLA4_CONSEJO);
					cincoEstrellas = jsonObject.getInt(Constants.ESTRELLA5_CONSEJO);
					totalVotos = jsonObject.getInt(Constants.VOTOS_CONSEJO);
					raza = jsonObject.getInt(Constants.RAZA_CONSEJO);
					especie = jsonObject.getInt(Constants.ESPECIE_CONSEJO);
					Tip newTip = new Tip(sIdTip, sAutor, sConsejo, null, unoEstrellas, dosEstrellas, tresEstrellas, cuatroEstrellas,cincoEstrellas,totalVotos,raza,especie,dCreationDate);
					tipsList.add(newTip);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
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
			startActivity(intent);
		}
	};

	/**
	 * Listener del boton
	 */
	private OnClickListener onAddTip = new OnClickListener() {

		@Override
		public void onClick(View v) {
			startActivity(new Intent(TipsActivity.this, AddTipActivity.class));
		}
	};

	/*
	 * Implementacion del Interface que envia la lista al fragment.
	 */
	public List<Tip> loadList() {
		return tipsList;
	}
}
