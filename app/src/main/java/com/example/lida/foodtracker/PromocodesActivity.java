package com.example.lida.foodtracker;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Promocode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromocodesActivity extends BaseActivity {
    @Override
    int getContentViewId() {
        return R.layout.activity_promocodes;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.promocodes;
    }
    private final String TAG = "PromocodesActivity";
    private BottomNavigationView bottomNavigation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocodes);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        getPromocodes();
    }


    private void getPromocodes() {
        Call<Promocode> call = App.getApi().getPromocodes();
        call.enqueue(new Callback<Promocode>() {
            @Override
            public void onResponse(Call<Promocode> call, Response<Promocode> response) {
                Log.d(TAG, "Successful response");

            }

            @Override
            public void onFailure(Call<Promocode> call, Throwable t) {

                Log.d(TAG, "Failed to load promocodes");
            }
        });
    }
}
