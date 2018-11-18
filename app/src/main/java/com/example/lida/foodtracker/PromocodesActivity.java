package com.example.lida.foodtracker;

import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Promocode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromocodesActivity extends BaseActivity {
    List<String> promocodesTitles;
    private List<Promocode> promocodesList;
    private ArrayAdapter<String> promocodesAdapter;
    private ListView promocodesListView;

    @Override
    int getContentViewId() {
        return R.layout.activity_promocodes;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.promocodes;
    }
    private final String TAG = "PromocodesActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promocodes);
        promocodesTitles = new ArrayList<String>();

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        promocodesListView = (ListView) findViewById(R.id.promocodes_list_view);
        promocodesAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, promocodesTitles);

        promocodesListView.setAdapter(promocodesAdapter);

        getPromocodes();
    }


    private void getPromocodes() {
        Call<Map<String, List<Promocode>>> call = App.getApi().getPromocodes();
        call.enqueue(new Callback<Map<String, List<Promocode>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Promocode>>> call, Response<Map<String, List<Promocode>>> response) {
                Map<String, List<Promocode>> promocodesResponse = response.body();
                Log.d(TAG, "Successful response");
                List<Promocode> promocodes = promocodesResponse.get("promocodes");

                for (Promocode promocode: promocodes) {
                    promocodesTitles.add(promocode.toString());
                }
                promocodesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<Map<String, List<Promocode>>> call, Throwable t) {

                Log.d(TAG, "Failed to load promocodes");
                try {
                    throw t;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }
}
