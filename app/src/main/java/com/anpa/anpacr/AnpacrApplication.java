package com.anpa.anpacr;

import android.app.Application;
import android.content.Context;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class AnpacrApplication extends Application {

	private static AnpacrApplication _instancia;
	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "vSPnwHOnhkvzj4cYe0t623oe2x079bwQwUW5vWMv", "0GL5wtFA6HDM9ED0xOobY1orFC7uuchLPOu8EIZk"); //AnpaCRApp

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
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
