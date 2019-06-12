package com.klakier.ProRobIntranet;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


import static android.util.Base64.DEFAULT;


public class Token {

    final static String KEY = "token";
    SharedPreferences sharedPref;

    public Token(Context context){
        sharedPref = context.getSharedPreferences("PROROB", Context.MODE_PRIVATE);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(KEY, token);
        editor.commit();
    }

    public void resetToken(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(KEY);
        editor.commit();
    }

    public String getToken(){
        return sharedPref.getString(KEY, "");
    }

    public Boolean hasToken(){
        return sharedPref.contains(KEY);
    }

    public String getTokenJson() {
        String[] parts = getToken().split("[.]");
        byte[] data = Base64.decode(parts[1], DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getId() {
        int id = 0;
        try {
            JSONObject jo = new JSONObject(getTokenJson());
            id = jo.getInt("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getRole() {
        String role = null;
        try {
            JSONObject jo = new JSONObject(getTokenJson());
            role = jo.getString("role");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return role;
    }

}
