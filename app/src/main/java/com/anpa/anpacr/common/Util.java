package com.anpa.anpacr.common;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static byte[] readBytes(String location) throws IOException {

        URL url = new URL(location);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        int contentLength = connection.getContentLength();
        InputStream openStream = connection.getInputStream();
        byte[] binaryData = new byte[contentLength];
        openStream.read(binaryData);
        openStream.close();

        return binaryData;
    }
}
