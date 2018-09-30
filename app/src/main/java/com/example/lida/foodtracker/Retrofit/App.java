package com.example.lida.foodtracker.Retrofit;

import android.app.Application;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    private static FoodTrackerApi foodTrackerApi;
    private Retrofit retrofit;
    private Gson gson;

    @Override
    public void onCreate() {
        super.onCreate();

        gson = new GsonBuilder()
                .setLenient()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(Urls.BASE_URL) //Base part of adress
                .addConverterFactory(GsonConverterFactory.create(gson)) //Конвертер для преобразования JSON в объекты
                .build();

        foodTrackerApi = retrofit.create(FoodTrackerApi.class);
    }

    public static FoodTrackerApi getApi() {
        return foodTrackerApi;
    }
}
