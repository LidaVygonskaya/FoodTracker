package com.example.lida.foodtracker.Retrofit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface FoodTrackerApi {
    @GET("/api/v1/product/bar/")
    Call<Product> getProduct(@QueryMap Map<String, String> params);
}
