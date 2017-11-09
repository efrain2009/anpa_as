package com.anpa.anpacr.activities;

import android.os.Bundle;

import com.anpa.anpacr.R;

public class CommingUpActivity extends AnpaAppFraqmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comming_up);
	
		// Btn de back (anterior)
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
	}

}
