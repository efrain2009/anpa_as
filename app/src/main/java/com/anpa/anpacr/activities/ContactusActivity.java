package com.anpa.anpacr.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.anpa.anpacr.R;
import com.anpa.anpacr.common.Constants;

public class ContactusActivity extends AnpaAppFraqmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);

		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(Constants.TITLE_DESCRIPTION_CONTACTUS);

		TextView tvCall1 = (TextView) findViewById(R.id.txt_description_phone1);
		TextView tvCall2 = (TextView) findViewById(R.id.txt_description_phone2);
		TextView tvFacebookLink = (TextView)findViewById(R.id.txt_description_facebook);
		TextView tvTwitterLink = (TextView)findViewById(R.id.txt_description_twitter);
		
		tvCall1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callPhone(Constants.ANPA_PHONE_PRIM);
			}
		});

		tvCall2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callPhone(Constants.ANPA_PHONE_SEC);
			}
		});
		
		tvFacebookLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					String uri = Constants.ANPA_FACEBOOK_LINK;
					openUri(uri);
				}
				catch(Exception e){
					String uri = Constants.ANPA_FACEBOOK_LINK_BACKUP;
					openUri(uri);
				}
			}
		});
		
		tvTwitterLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try{
					String uri = Constants.ANPA_TWITTER_LINK;
					openUri(uri);
				}
				catch(Exception e){
					String uri = Constants.ANPA_TWITTER_LINK_BACKUP;
					openUri(uri);
				}
			}
		});
	}

	// Llama al intent para llamar a un telefono.
	private void callPhone(String phoneNmber) {
		Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ phoneNmber));
		startActivity(intent);
	}
	
	private void openUri(String uri){
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		startActivity(intent);
	}
}
