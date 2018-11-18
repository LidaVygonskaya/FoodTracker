package com.example.lida.foodtracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import de.cketti.mailto.EmailIntentBuilder;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private FloatingActionButton addProductButton;
    private ListView productList;

    private ImageButton settingsButton;
    private ImageButton accountButton;

    private ProductAdapter productAdapter;
    private List<Product> products;

    private Toolbar toolbar;
    private SharedPreferences sPref;
    private String productsSharedKey = "Products";

    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;

    // Идентификатор уведомления
    private static final int NOTIFY_ID = 101;

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
        addNotification();
        setContentView(R.layout.activity_main);

        sPref = getPreferences(MODE_PRIVATE);

        settingsButton = (ImageButton) findViewById(R.id.settings);
        settingsButton.setOnClickListener(settingsOnClick);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        notificationManager = NotificationManagerCompat.from(this);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = this.getResources();

        loadProducts();
        productAdapter = new ProductAdapter(this, R.layout.product_list_item, products);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        productList = (ListView) findViewById(R.id.productList);
        productList.setAdapter(productAdapter);

        productAdapter.sort(new ProductComparator());
        productAdapter.notifyDataSetChanged();
        //notifyIfNeed();

        productList.setEmptyView(findViewById(R.id.empty_group));
        ImageView im = (ImageView) findViewById(R.id.empty);
        //im.setAlpha(0.5f);
        productList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Удалить объект " + products.get(position).getName() + "?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                products.remove(position);
                                productAdapter.notifyDataSetChanged();
                                //notifyIfNeed();

                                Gson gson = new Gson();
                                SharedPreferences.Editor editor = sPref.edit();
                                String json = gson.toJson(products);
                                editor.putString(productsSharedKey, json);
                                editor.apply();

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create().show();

                return true;
            }
        });
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                intent.putExtra("PRODUCT", products.get(position));
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

        List<Product> oldProducts = getOldProducts();
        if (!oldProducts.isEmpty()) {
            createEmailDialog();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }

        Bundle bundle = data.getExtras();
        if (bundle == null) {
            return;
        }
        List<Product> resultList = (ArrayList<Product>) bundle.getSerializable("PRODUCT_LIST");

        for (Product p : resultList) {
            products.add(p);
            addProductToSharedPref(p);
        }

        productAdapter.sort(new ProductComparator());
        productAdapter.notifyDataSetChanged();
        //notifyIfNeed();
    }

    private void notifyIfNeed() {
        notificationManager.cancelAll();

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        for (Product product : products) {
            startDate.set(product.getDateEnd().getYear(), product.getDateEnd().getMonth(), product.getDateEnd().getDate());
            long start = startDate.getTimeInMillis();
            long end = endDate.getTimeInMillis();
            Long days = (long) (start - end) / (1000 * 60 * 60 * 24);
            if (days <= 5) {
                break;
            }
        }
    }

    private void loadProducts() {
        products = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "BUNDLE IS NOT NULL");
            List<Product> resultList = (ArrayList<Product>) bundle.getSerializable("PRODUCT_LIST");

            if (resultList != null) {
                for (Product p : resultList) {
                    addProductToSharedPref(p);
                }
            }
        }

        if (sPref.contains(productsSharedKey)) {
            Gson gson = new Gson();
            String json = sPref.getString(productsSharedKey, null);
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            List<Product> prods = gson.fromJson(json, type);
            for (Product p: prods) {
                products.add(p);

            }
        }
    }

    private void addProductToSharedPref(Product product) {
        Gson gson = new Gson();
        List<Product> products;
        if (sPref.contains(productsSharedKey)){
            String json = sPref.getString(productsSharedKey, null);
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            products = gson.fromJson(json, type);
        } else {
            products = new ArrayList<Product>();
        }
        products.add(product);
        SharedPreferences.Editor editor = sPref.edit();
        String json = gson.toJson(products);
        editor.putString(productsSharedKey, json);
        editor.apply();
    }

    private Integer getHours(Integer timeFromPrefs) {
        return timeFromPrefs / 60;
    }

    private Integer getMinutes(Integer timeFromPrefs) {
        return timeFromPrefs % 60;
    }

    private void addNotification() {
        // Shared preferences for time preferences
        SharedPreferences timePrefs = getSharedPreferences("com.example.lida.foodtracker_preferences", MODE_PRIVATE);
        Integer timeFromPrefs = timePrefs.getInt("key4", 90);

        Integer hours = getHours(timeFromPrefs);
        Integer minutes = getMinutes(timeFromPrefs);


        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }


    View.OnClickListener settingsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
    };


    private void sendEmail() {
        EmailIntentBuilder.from(this)
                .to("food@example.com")
                .cc("user@example.com")
                .subject("Заказ продуктов")
                .body("Какие-то продукты")
                .start();
    }

    private List<Product> getOldProducts() {
        List<Product> oldProducts = new ArrayList<Product>();

        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();

        for (Product product: products) {
            startDate.set(product.getDateEnd().getYear(), product.getDateEnd().getMonth(), product.getDateEnd().getDate());
            long start = startDate.getTimeInMillis();
            long end = endDate.getTimeInMillis();
            Long days = (long) (start - end) / (1000 * 60 * 60 * 24);
            if (days <= 3) {
                oldProducts.add(product);
            }
        }

        return oldProducts;
    }

    private void createEmailDialog() {
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        sendEmail();
                        finish();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Заказ продуктов")
                .setMessage("Кажется некоторые из продуктов скоро испортятся. Заказать?")
                .setPositiveButton("Купить", myClickListener)
                .setNegativeButton("Отмена", myClickListener);
        builder.create().show();
    }
}