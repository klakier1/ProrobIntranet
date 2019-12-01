package com.klakier.proRobIntranet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.klakier.proRobIntranet.api.ProRobApi;
import com.klakier.proRobIntranet.api.response.deserializer.DateDeserializer;
import com.klakier.proRobIntranet.api.response.deserializer.TimeDeserializer;
import com.klakier.proRobIntranet.api.response.deserializer.TimestampDeserializer;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "http://prorob-intranet-api.herokuapp.com/";
    private static final String BASE_URL_LOCAL = "http://10.0.2.2:80/prorob-intranet-api/public/";
    //private static final String BASE_URL_LOCAL = "http://localhost/prorob-intranet-api/public/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateDeserializer())
                .registerTypeAdapter(Timestamp.class, new TimestampDeserializer())
                .registerTypeAdapter(Time.class, new TimeDeserializer())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL_LOCAL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public ProRobApi getApi(){
        return retrofit.create(ProRobApi.class);
    }
}
