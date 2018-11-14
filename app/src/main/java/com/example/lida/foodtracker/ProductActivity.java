package com.example.lida.foodtracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.lida.foodtracker.Retrofit.App;
import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Utils.ProductAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    private static final String TAG = "ProductActivity";

    private Product product;

    private TextView productName;
    private TextView date;
    private TextView count;
    private TextView sostav;
    private ImageView productImage;
    private ImageButton exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product = (Product) getIntent().getExtras().getSerializable("PRODUCT");

        productName = (TextView) findViewById(R.id.prod_name);
        date = (TextView) findViewById(R.id.date);
        count = (TextView) findViewById(R.id.amount);
        sostav = (TextView) findViewById(R.id.sostav);
        productImage = (ImageView) findViewById(R.id.imageView);

        getProductImage(product);

        exit = (ImageButton) findViewById(R.id.exit);

        productName.setText(product.getName());
        sostav.setText(product.getComposition());
        count.setText(product.getQuantity().toString());
        date.setText(product.getDateEndInStringFormat());

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    private void getProductImage(Product product) {
        //TODO: Сейчас продукт не хранит свой штрихкод представляешь!
        String productBarcode = product.getBarCode() + ".jpg";
        Call<ResponseBody> call = App.getApi().getImage(productBarcode);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        productImage.setImageBitmap(bitmap);

                    } else {
                        Log.d(TAG, "response body is empty. Setting default image");
                        productImage.setImageResource(R.drawable.carrot);
                    }
                } else {
                    Log.d(TAG, "reesponseCode is not successful. Setting default image");
                    productImage.setImageResource(R.drawable.carrot);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "Resposne Failed. Setting default image");
                productImage.setImageResource(R.drawable.carrot);
            }
        });
    }

}