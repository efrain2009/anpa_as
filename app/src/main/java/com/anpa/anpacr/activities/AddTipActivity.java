package com.anpa.anpacr.activities;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;
import android.widget.Toast;

import com.anpa.anpacr.R;
import com.anpa.anpacr.adapter.SpinnerAdapter;
import com.anpa.anpacr.common.Constants;
import com.anpa.anpacr.common.Util;
import com.anpa.anpacr.domain.GenericNameValue;
import com.anpa.anpacr.domain.Tip;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.ServiceAPI;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;

import org.json.JSONObject;

public class AddTipActivity extends AnpaAppFraqmentActivity {

	//App42:
	private String docId = "";
	private ProgressDialog progressDialog;
	ServiceAPI api;
	StorageService storageService;

	EditText editxt_description_tip, editxt_breed_author;

	CheckBox check_facebook;

	Button saveTip;
	String _sRaza;
	Tip tip;
	//Integration with facebook
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	AccessTokenTracker accessTokenTracker;
	AccessToken accessToken;
	String consejo = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tip);

		// Instancia la BD App 42
		api = new ServiceAPI(Constants.App42ApiKey, Constants.App42ApiSecret);

		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_TIPS);

		Bundle pantallaBusquedaTip = getIntent().getExtras();
		Long razaBusqueda = pantallaBusquedaTip.getLong("razaSearch");
		Long especieBusqueda = pantallaBusquedaTip.getLong("especieSearch");

        /* Facebook*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

		/*Facbook*/
        LoginButton buttonFb = (LoginButton) findViewById(R.id.login_button);
        buttonFb.clearPermissions();

        callbackManager = CallbackManager.Factory.create();

        List<String> publishPermissions = Arrays.asList("publish_actions");
        //	buttonFb.setReadPermissions("user_friends");
        //LoginManager.getInstance().logInWithPublishPermissions(this, publishPermissions);

        buttonFb.setPublishPermissions(publishPermissions);

        buttonFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = loginResult.getAccessToken();
                System.out.print("Access Token: " + accessToken.getToken());
            }

            @Override
            public void onCancel() {
                System.out.print("Cancelado");
            }

            @Override
            public void onError(FacebookException error) {
                System.out.print("Error: " + error);
            }
        });

		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);

/*
		 accessTokenTracker = new AccessTokenTracker() {
			@Override
			protected void onCurrentAccessTokenChanged(
					AccessToken oldAccessToken,
					AccessToken currentAccessToken) {

				accessToken = currentAccessToken;
				System.out.println("Set Current token");
			}
		};
*/
		saveTip = (Button) findViewById(R.id.btn_add_tip);
		saveTip.setOnClickListener(onSave);

		editxt_description_tip = (EditText) findViewById(R.id.editxt_description_tip);

		editxt_breed_author = (EditText) findViewById(R.id.editxt_breed_author);

		check_facebook = (CheckBox) findViewById(R.id.ck_public_fb);

        //Button FB
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.login_button);
        if(AccessToken.getCurrentAccessToken() == null) {
            linearLayout.setVisibility(View.VISIBLE);
        }else{
            linearLayout.setVisibility(View.INVISIBLE);
        }

		//Obtener valores q se obtuvieron en el filtro//
		TextView txt_raza = (TextView) findViewById(R.id.txt_addRaza_consejo);
		String raza = readSpecies(especieBusqueda.intValue(), razaBusqueda.intValue());
		txt_raza.setText(raza);

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
			//Mandar los valores que se setearon en el listado de filtros
			Util.textAsJSON(tipJSON, Constants.RAZA_CONSEJO, "" , 1);
			Util.textAsJSON(tipJSON, Constants.ESPECIE_CONSEJO, "" ,  1);
			//
			Util.textAsJSON(tipJSON, Constants.ESTRELLA1_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA2_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA3_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA4_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.ESTRELLA5_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.VOTOS_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.HABILITADO_CONSEJO, "" , 0);
			Util.textAsJSON(tipJSON, Constants.USUARIO_CONSEJO, Constants.USUARIO_NOMBRE , -1);

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
						if(check_facebook.isChecked()) {
                            if(AccessToken.getCurrentAccessToken() != null) {
                                consejo = "Ha compartido una experiencia:  " + editxt_description_tip.getText().toString();
                                shareOnFacebook();
                            }else{
                                alertaLogeoFB();
                            }
						}
					}
				}
                public void onException(Exception ex)
				{
					System.out.println("Exception Message"+ex.getMessage());
				}
			});
			alertDialog ();
			consejo ="";
		}
	};

    private void alertaLogeoFB() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constants.MSJ_ERROR_LOGIN_FB)
                .setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                }).create().show();
    }

	public void alertDialog (){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(Constants.MSJ_SUCCESS_TIP)
               .setPositiveButton(Constants.BTN_ACEPTAR, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // FIRE ZE MISSILES!
                   }
               }).create().show();
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

	private void shareOnFacebook(){
			FacebookSdk.sdkInitialize(this.getApplicationContext(), new FacebookSdk.InitializeCallback() {

				@Override
				public void onInitialized() {
					if (AccessToken.getCurrentAccessToken() == null) {
					/*Facbook*/
						LoginButton buttonFb = (LoginButton) findViewById(R.id.login_button);
						buttonFb.clearPermissions();

						List<String> publishPermissions = Arrays.asList("publish_actions");
						//	buttonFb.setReadPermissions("user_friends");
						//LoginManager.getInstance().logInWithPublishPermissions(this, publishPermissions);

						buttonFb.setPublishPermissions(publishPermissions);

						buttonFb.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
							@Override
							public void onSuccess(LoginResult loginResult) {
								accessToken = loginResult.getAccessToken();
								System.out.print("Access Token: " + accessToken.getToken());
							}

							@Override
							public void onCancel() {
								System.out.print("Cancelado");
							}

							@Override
							public void onError(FacebookException error) {
								System.out.print("Error: " + error);
							}
						});
					} else {
						Bundle params = new Bundle();
						params.putString("message", consejo);
						//			.setImageUrl(Uri.parse("http://cdn.shephertz.com/repository/files/7389dc177e03422884045c7ac9227db10be51606e6bddbca4939f9d8d9b5cbb4/da5802be1fc4fd0ba497a9e6bb393627051789c9/ANPA.png"))
						//			.build();
						params.putString("link", "https://www.facebook.com/ANPACR/");
						new GraphRequest(
								AccessToken.getCurrentAccessToken(),
								"/me/feed",
								params,
								HttpMethod.POST,
								new GraphRequest.Callback() {
									public void onCompleted(GraphResponse response) {
										Log.d("", "------ graphResponse = " + response);
									}
								}
						).executeAsync();
						System.out.println("Logged in");
					}
				}
			});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
