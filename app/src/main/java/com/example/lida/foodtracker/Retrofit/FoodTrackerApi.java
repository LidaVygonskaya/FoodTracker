package com.example.lida.foodtracker.Retrofit;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface FoodTrackerApi {
    @GET("/api/v1/product/bar/")
    Call<Product> getProduct(@QueryMap Map<String, String> params);

    @GET("media/big_images/{imageName}")
    Call<ResponseBody> getImage(@Path("imageName") String imageName);

}
