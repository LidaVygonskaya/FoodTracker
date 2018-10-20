package com.example.lida.foodtracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.example.lida.foodtracker.Retrofit.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private ImageButton addProductButton;
    private ListView productList;
    private ArrayAdapter<String> productAdapter;
    private List<String> productNames;
    private Toolbar toolbar;
    private SharedPreferences sPref;
    private String productsSharedKey = "Products";

    @Override
    int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.fridge;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sPref = getPreferences(MODE_PRIVATE);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        productNames = loadProductNames();
        productAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, productNames);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productList = (ListView) findViewById(R.id.productList);
        productList.setAdapter(productAdapter);
        productList.setEmptyView(findViewById(R.id.empty_group));

        addProductButton = (ImageButton) findViewById(R.id.add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraScanActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        ArrayList<Product> resultList = (ArrayList<Product>)bundle.getSerializable("BARCODES_LIST");
        for (Product p:resultList) {
            productNames.add(p.getName());
        }
        productAdapter.notifyDataSetChanged();
        Log.d("a", "as");
    }

    private List<String> loadProductNames() {
        List<String> pNames = new ArrayList<String>();
        if (sPref.contains(productsSharedKey)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Product>>(){}.getType();
            List<Product> prods = gson.fromJson(productsSharedKey, type);
            for (Product p: prods) {
                pNames.add(p.getName());
            }
        } else {
            pNames = new ArrayList<String>();
        }

        return pNames;
    }

    private void addProductToSharedPref(Product product) {
        Gson gson = new Gson();
        List<Product> prods;
        if (sPref.contains(productsSharedKey)){
            Type type = new TypeToken<List<Product>>(){}.getType();
            prods = gson.fromJson(productsSharedKey, type);
            prods.add(product);
            SharedPreferences.Editor editor = sPref.edit();
            String json = gson.toJson(prods);
            editor.putString(productsSharedKey, json);
            editor.apply();

        } else {
            Log.d(TAG, "NET NICHEGO V SHARED PREFS");
        }
    }



}