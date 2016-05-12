package com.anpa.anpacr.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;


public class AnpaAppFraqmentActivity extends ActionBarActivity{

	@Override
	   protected void onCreate(Bundle savedInstanceState)
	   {
	       super.onCreate(savedInstanceState);
	   }

	   public boolean onOptionsItemSelected(int featureId, MenuItem p)
	       {
	               if (p.getItemId() == android.R.id.home)
	               {
	                       finish();
	               }
	               return true;
	       }
}
