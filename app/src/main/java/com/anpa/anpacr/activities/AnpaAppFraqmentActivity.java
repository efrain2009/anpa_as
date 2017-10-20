package com.anpa.anpacr.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


public class AnpaAppFraqmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
    public boolean onOptionsItemSelected(MenuItem p) {
        if (p.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
