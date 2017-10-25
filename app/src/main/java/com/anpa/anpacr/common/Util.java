package com.anpa.anpacr.common;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.shephertz.app42.paas.sdk.android.util.Base64;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * Created by Susanita on 04/09/2016.
 */
public class Util {

    public static void textAsJSON(JSONObject json, String column, String value1, long value2){
        try {
            if(value1.isEmpty()){
                json.put(column, value2);
            }else {
                json.put(column, new String (textAsEncode64(value1)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public static String textAsEncode64(String column){
        byte[] data;
        String base64 = "";
        try {
            data = column.getBytes("UTF-8");
            base64 = Base64.encodeBytes(data, Base64.DECODE);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return base64;
    }

    public static String decode64AsText(String column) throws UnsupportedEncodingException {
        byte[] decodeByte = new byte[0];
        String text = "";
        try {
            decodeByte = Base64.decode(column, Base64.DECODE);
            text = new String(decodeByte, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }


}
