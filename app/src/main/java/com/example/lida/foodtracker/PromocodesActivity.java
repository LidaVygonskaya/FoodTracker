package com.example.lida.foodtracker;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Promocode;
import com.example.lida.foodtracker.Utils.ProductAdapter;
import com.example.lida.foodtracker.Utils.PromocodeAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromocodesActivity extends BaseActivity {
    private PromocodeAdapter promocodeAdapter;
    //List<String> promocodesTitles;
    private List<Promocode> promocodesList;
    //private ArrayAdapter<String> promocodesAdapter;
    private ListView promocodesListView;
    private ImageButton accountButton;

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
        //promocodesTitles = new ArrayList<String>();
        promocodesList = new ArrayList<Promocode>();

        accountButton = findViewById(R.id.account);
        accountButton.setOnClickListener(accountClickListener);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        promocodesListView = (ListView) findViewById(R.id.promocodes_list_view);
        promocodesListView.setOnItemClickListener(goByUrl);
        //promocodesAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, promocodesTitles);

        promocodeAdapter = new PromocodeAdapter(this, R.layout.promocode_list_item, promocodesList);

        //promocodesListView.setAdapter(promocodesAdapter);
        promocodesListView.setAdapter(promocodeAdapter);
        getPromocodes();
    }


    private void getPromocodes() {
        Call<Map<String, List<Promocode>>> call = App.getApi().getPromocodes();
        call.enqueue(new Callback<Map<String, List<Promocode>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Promocode>>> call, Response<Map<String, List<Promocode>>> response) {
                Map<String, List<Promocode>> promocodesResponse = response.body();
                Log.d(TAG, "Successful response");
                promocodesList = promocodesResponse.get("promocodes");

                //for (Promocode promocode: promocodes) {
                //    promocodesTitles.add(promocode.toString());
                //}
                promocodeAdapter.addAll(promocodesList);
                promocodesListView.setAdapter(promocodeAdapter);
                promocodeAdapter.notifyDataSetChanged();
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

    private AdapterView.OnItemClickListener goByUrl = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Promocode promocode = promocodesList.get(position);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(promocode.getUrl()));
            startActivity(browserIntent);
        }
    };

    View.OnClickListener accountClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent accountIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(accountIntent);
        }
    };
}
