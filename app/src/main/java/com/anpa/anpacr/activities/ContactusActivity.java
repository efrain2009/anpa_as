package com.anpa.anpacr.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.app42.AsyncApp42ServiceApi;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.Miscelaneo;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ContactusActivity extends AnpaAppFraqmentActivity implements AsyncApp42ServiceApi.App42StorageServiceListener{

	//App42:
	private AsyncApp42ServiceApi asyncService;
	private String docId = "";
	private ProgressDialog progressDialog;
	TextView tvCall1;
	TextView tvCall2;
	TextView tvDirecction;
	TextView tvEmail1;
	TextView tvEmail2;
	TextView tvURL;
	TextView tvFacebookLink;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		//App42:
		asyncService = AsyncApp42ServiceApi.instance(this);

		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CONTACTUS);

		tvCall1 = (TextView) findViewById(R.id.txt_description_phone1);
		tvCall2 = (TextView) findViewById(R.id.txt_description_phone2);
		tvDirecction = (TextView) findViewById(R.id.txt_description_location);
		tvEmail1 = (TextView) findViewById(R.id.txt_description_mail1);
		tvEmail2 = (TextView) findViewById(R.id.txt_description_mail2);
		tvURL = (TextView) findViewById(R.id.txt_description_webPage);
		tvFacebookLink = (TextView) findViewById(R.id.txt_description_facebook);

		try {
			/* App42 */
			progressDialog = ProgressDialog.show(ContactusActivity.this,
					Constants.ESPERA, Constants.ESPERA);

			asyncService.findDocByColletion(Constants.App42DBName, Constants.TABLE_MISCELANEOS, 1, this);

		} catch (Exception e) {
			progressDialog.dismiss();
			showMessage(Constants.MSJ_ERROR_CASTRATION);
			e.printStackTrace();
		}
	}

	// Llama al intent para llamar a un telefono.
	private void callPhone(String phoneNmber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNmber));
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			//    ActivityCompat#requestPermissions
			// here to request the missing permissions, and then overriding
			//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
			//                                          int[] grantResults)
			// to handle the case where the user grants the permission. See the documentation
			// for ActivityCompat#requestPermissions for more details.
			return;
		}
		startActivity(intent);
	}
	
	private void openUri(String uri){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(intent);
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
			case 1:
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

	}

	@Override
	public void onUpdateDocFailed(App42Exception ex) {

	}

	/**
	 * Muestra un mensaje TOAST.
	 *
	 * @param message
	 */
	private void showMessage(String message) {
		Toast.makeText(ContactusActivity.this, message, Toast.LENGTH_SHORT)
				.show();
	}

	private class AsyncLoadListTask extends AsyncTask<Storage, Integer, Boolean> {
		Miscelaneo newMiscelaneo;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		protected Boolean doInBackground(Storage... storage) {

			ArrayList<Storage.JSONDocument> jsonDocList = storage[0].getJsonDocList();
			String sId = "", direccion = "", facebook = "", tel1 = "",
					date = "", tel2 = "", correo1 = "", correo2 = "", url = "";

			for(int i=0; i < jsonDocList.size(); i ++){
				sId = jsonDocList.get(i).getDocId();
				date = jsonDocList.get(i).getCreatedAt();

				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(jsonDocList.get(i).getJsonDoc());
					direccion = jsonObject.getString(Constants.DIRECCION_MISCELANEOS);
					facebook = jsonObject.getString(Constants.FACEBOOK_MISCELANEOS);
					tel1 = jsonObject.getString(Constants.TELEFONO1_MISCELANEOS);
					tel2 = jsonObject.getString(Constants.TELEFONO2_MISCELANEOS);
					correo1 = jsonObject.getString(Constants.MAIL1_MISCELANEOS);
					correo2 = jsonObject.getString(Constants.MAIL2_MISCELANEOS);
					url = jsonObject.getString(Constants.URL_MISCELANEOS);

					newMiscelaneo = new Miscelaneo(sId,  Util.decode64AsText(direccion),  Util.decode64AsText(correo1),  Util.decode64AsText(correo2), tel1,
							tel2, url, facebook);

				} catch (JSONException e) {
					e.printStackTrace();
					return false;
				}
			}
			return true;
		}

		protected void onPostExecute(Boolean result) {
			if(result){
				tvCall1.setText(newMiscelaneo.getTelefono1());
				tvCall2.setText(newMiscelaneo.getTelefono2());
				tvDirecction.setText(newMiscelaneo.getDireccion());
				tvEmail1.setText(newMiscelaneo.getCorreo1());
				tvEmail2.setText(newMiscelaneo.getCorreo2());
				tvURL.setText(newMiscelaneo.getUrl());
				tvFacebookLink.setText(newMiscelaneo.getFacebook());

				tvCall1.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						callPhone(newMiscelaneo.getTelefono1());
					}
				});
				tvCall2.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						callPhone(newMiscelaneo.getTelefono2());
					}
				});

				tvFacebookLink.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						try {
							String uri = newMiscelaneo.getFacebook();
							openUri(uri);
						} catch (Exception e) {
							String uri = Constants.ANPA_FACEBOOK_LINK_BACKUP;
							openUri(uri);
						}
					}
				});
			}
			progressDialog.dismiss();
		}
	}


}
