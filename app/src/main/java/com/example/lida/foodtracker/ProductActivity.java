package com.example.lida.foodtracker;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Utils.ProductAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private static final String TAG = "ProductActivity";

    private Product product;

    private TextView name, desctiption, count, date;
    private ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product = (Product) getIntent().getExtras().getSerializable("PRODUCT");

        name = (TextView) findViewById(R.id.product_name);
        desctiption = (TextView) findViewById(R.id.product_description);
        count = (TextView) findViewById(R.id.product_count);
        date = (TextView) findViewById(R.id.product_date);

        exit = (ImageButton) findViewById(R.id.exit);

        name.setText(product.getName());
        desctiption.setText(product.getDescription());
        count.setText(product.getQuantity().toString());
        date.setText(product.getDateEndInStringFormat());

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

}