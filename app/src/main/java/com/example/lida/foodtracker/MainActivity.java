package com.example.lida.foodtracker;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.example.lida.foodtracker.Retrofit.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ImageButton addProductButton;
    private ListView productList;
    private ArrayAdapter<String> productAdapter;
    private List<String> productNames;
    private Toolbar toolbar;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        productNames = new ArrayList<String>();
        productAdapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, productNames);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        productList = (ListView) findViewById(R.id.productList);
        productList.setAdapter(productAdapter);
        productList.setEmptyView(findViewById(R.id.empty));

        addProductButton = (ImageButton) findViewById(R.id.add_product);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            //Intent for result Camera Scan
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraScanActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        ArrayList<Product> reslutList = (ArrayList<Product>)bundle.getSerializable("BARCODES_LIST");
        for (Product p:reslutList) {
            productNames.add(p.getName());
        }
        productAdapter.notifyDataSetChanged();
        Log.d("a", "as");
        //Toast toast = Toast.makeText(getApplicationContext(), barcode, Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER, 0, 0);
        //toast.show();
    }


}
