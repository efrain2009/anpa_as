package com.anpa.anpacr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;

import com.anpa.anpacr.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class HomeActivity extends Activity {

    CallbackManager callbackManager;
    AccessToken accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

		/* Facebook*/
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);
        /*Facbook*/
        LoginButton buttonFb = (LoginButton) findViewById(R.id.login_button);
        buttonFb.clearPermissions();

        callbackManager = CallbackManager.Factory.create();

        //List<String> publishPermissions = Arrays.asList("publish_actions");
        //	buttonFb.setReadPermissions("user_friends");
        //LoginManager.getInstance().logInWithPublishPermissions(this, publishPermissions);

        //buttonFb.setPublishPermissions(publishPermissions);

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

		/* Mapeo de los componentes de pantalla */

        //Noticias
        RelativeLayout btn_news = (RelativeLayout) findViewById(R.id.btn_home_news);
        btn_news.setOnClickListener(onNewsClick);

        //Castracion
        RelativeLayout btn_castration = (RelativeLayout) findViewById(R.id.btn_home_castration);
        btn_castration.setOnClickListener(onCastrationClick);

        //Tips
        RelativeLayout btn_tips = (RelativeLayout) findViewById(R.id.btn_home_tips);
        btn_tips.setOnClickListener(onTipsClick);

        //Adoption
        RelativeLayout btn_adoption = (RelativeLayout) findViewById(R.id.btn_home_adoption);
        btn_adoption.setOnClickListener(onLostClick);

        //Donation
        RelativeLayout btn_donation = (RelativeLayout) findViewById(R.id.btn_home_donation);
        btn_donation.setOnClickListener(onDonationClick);

        //Contactenos
        RelativeLayout btn_contactus = (RelativeLayout) findViewById(R.id.btn_home_contactus);
        btn_contactus.setOnClickListener(onContactusClick);
    }

    private OnClickListener onNewsClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Como hacer mensaje System.out
            //Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
            startNewsActivity();
        }
    };

    private void startNewsActivity() {
        startActivity(new Intent(this, NewsActivity.class));
    }

    private OnClickListener onCastrationClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Como hacer mensaje System.out
            //Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
            startCastrationActivity();
        }
    };

    private void startCastrationActivity() {
        startActivity(new Intent(this, CastrationActivity.class));
    }

    private OnClickListener onTipsClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Como hacer mensaje System.out
            //Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
            startTipsActivity();
        }
    };

    private void startTipsActivity() {
        startActivity(new Intent(this, TipSearchActivity.class));
    }

    private OnClickListener onLostClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Como hacer mensaje System.out
            //Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
            startLostctivity();
        }
    };

    private void startLostctivity() {
        startActivity(new Intent(this, LostActivity.class));
    }

    private OnClickListener onDonationClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Como hacer mensaje System.out
            //Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
            startDonationActivity();
            //startCommingUpActivity();
        }
    };

    private void startDonationActivity() {
        startActivity(new Intent(this, DonationActivity.class));
    }

    private OnClickListener onContactusClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //Como hacer mensaje System.out
            //Toast.makeText(getApplicationContext(), "Hola", Toast.LENGTH_LONG).show();
            startContactusActivity();
        }
    };

    private void startContactusActivity() {
        startActivity(new Intent(this, ContactusActivity.class));
    }

    private void startCommingUpActivity() {
        startActivity(new Intent(this, CommingUpActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
