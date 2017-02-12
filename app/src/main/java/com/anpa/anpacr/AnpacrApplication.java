package com.anpa.anpacr;

import android.app.Application;
import android.content.Context;

public class AnpacrApplication extends Application {

	private static AnpacrApplication _instancia;
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	public AnpacrApplication(){
		_instancia = this;
	}
	/**
	 * Gets the application context
	 * @return
	 */
	public static Context getContext() {
        return _instancia;
    }
}
