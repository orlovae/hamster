package ru.aleksandrorlov.crazyhamster.controllers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.aleksandrorlov.crazyhamster.rest.ApiUnrealMojo;

/**
 * Created by alex on 19.05.17.
 */

public class ApiController {
    private static final String LOG_TAG = "Controller";
    private static final String HAMSTER_BASE_URL = "https://unrealmojo.com/porn/";

    static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor
            (message -> Log.d("Retrofit", message)).setLevel(HttpLoggingInterceptor.Level.BODY);
    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor ).build();

    public static ApiUnrealMojo getApi() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(HAMSTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiUnrealMojo apiUnrealMojo = retrofit.create(ApiUnrealMojo.class);
        return apiUnrealMojo;
    }
}
