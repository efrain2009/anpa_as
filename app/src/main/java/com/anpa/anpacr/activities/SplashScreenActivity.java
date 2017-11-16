package com.anpa.anpacr.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;

import com.anpa.anpacr.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends Activity {

	//Set the duration of the splash screen
	private static final long SPLASH_SCREEN_DELAY = 2500;

	@Override
	protected void onCreate(Bundle saveInstanceState) {
			super.onCreate(saveInstanceState);
			
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.splash_screen);

			TimerTask task = new TimerTask() {
				
				@Override
				public void run() {
					Intent intent = new Intent().setClass(SplashScreenActivity.this, HomeActivity.class);
					startActivity(intent);
					finish();
				}
			};
			//Simulate a long loading process on aplication startup
			Timer timer = new Timer();
			timer.schedule(task, SPLASH_SCREEN_DELAY);
			
	}
}
