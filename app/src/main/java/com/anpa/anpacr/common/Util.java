package com.anpa.anpacr.common;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Susanita on 04/09/2016.
 */
public class Util {

    public static void textAsJSON(JSONObject json, String column, String value1, long value2){
        try {
            if(value1.isEmpty()){
                json.put(column, value2);
            }else {
                json.put(column, value1);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
