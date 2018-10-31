package com.example.lida.foodtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Utils.ProductAdapter;
import com.example.lida.foodtracker.Utils.ProductComparator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private FloatingActionButton addProductButton;
    private ListView productList;

    private ProductAdapter productAdapter;

    private List<String> productNames, descriptions;
    private List<Integer> counts, imgIds;
    private List<Date> dates;
    private List<Product> prods;

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

        loadProducts();
        prods = new ArrayList<>();
        for (int i = 0; i < productNames.size(); i++) {
            Product prod = new Product(productNames.get(i), descriptions.get(i),
                    counts.get(i), dates.get(i), imgIds.get(i));
            prods.add(prod);
        }
        Collections.sort(prods, new ProductComparator());
        productAdapter = new ProductAdapter(this, productNames, descriptions, counts, dates, imgIds);
        productAdapter.sort(new Comparator<Product>() {
            @Override
            public int compare(Product pr0, Product pr1) {
                return pr0.getDateEnd().compareTo(pr1.getDateEnd());
            }
        });
        productAdapter.notifyDataSetChanged();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productList = (ListView) findViewById(R.id.productList);
        productList.setAdapter(productAdapter);
        productList.setEmptyView(findViewById(R.id.empty_group));
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);

                Product product = new Product(productNames.get(position), descriptions.get(position),
                        counts.get(position), dates.get(position), imgIds.get(position));
                intent.putExtra("PRODUCT", product);

                startActivity(intent);
            }
        });

        addProductButton = (FloatingActionButton) findViewById(R.id.add_product);
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
        List<Product> resultList = (ArrayList<Product>)bundle.getSerializable("BARCODES_LIST");
        for (Product p:resultList) {
            productNames.add(p.getName());
            descriptions.add(p.getDescription());
            counts.add(p.getQuantity());
            dates.add(p.getDateEnd());
            imgIds.add(p.getImgId());
            addProductToSharedPref(p);
        }
        productAdapter.notifyDataSetChanged();
        Log.d("a", "as");
    }

    private void loadProducts() {
        productNames = new ArrayList<>();
        descriptions = new ArrayList<>();
        counts = new ArrayList<>();
        dates = new ArrayList<>();
        imgIds = new ArrayList<>();

        if (sPref.contains(productsSharedKey)) {
            Gson gson = new Gson();
            String json = sPref.getString(productsSharedKey, null);
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            List<Product> prods = gson.fromJson(json, type);
            //sortProducts(prods);
            for (Product p: prods) {
                productNames.add(p.getName());
                descriptions.add(p.getDescription());
                counts.add(p.getQuantity());
                dates.add(p.getDateEnd());
                imgIds.add(p.getImgId());
            }
        }
    }

    private void addProductToSharedPref(Product product) {
        Gson gson = new Gson();
        List<Product> prods;
        if (sPref.contains(productsSharedKey)){
            String json = sPref.getString(productsSharedKey, null);
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            prods = gson.fromJson(json, type);


        } else {
            prods = new ArrayList<Product>();
            Log.d(TAG, "NET NICHEGO V SHARED PREFS");
        }
        prods.add(product);
        SharedPreferences.Editor editor = sPref.edit();
        String json = gson.toJson(prods);
        editor.putString(productsSharedKey, json);
        editor.apply();
    }

   /* @SuppressLint("NewApi")
    private void sortProducts(List<Product> prod) {
        Log.d(TAG, "Представь что я отсортировался");
        /* Не работает разберись почему
        NoClassDefFoundError
        prod.sort(Comparator.comparing(Product::getDateEnd));
        */
    //}
}