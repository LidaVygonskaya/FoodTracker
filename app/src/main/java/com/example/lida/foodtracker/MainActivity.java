package com.example.lida.foodtracker;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Utils.ProductAdapter;
import com.example.lida.foodtracker.Utils.ProductComparator;
import com.example.lida.foodtracker.Utils.ProductViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import de.cketti.mailto.EmailIntentBuilder;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private FloatingActionButton addProductButton;
    private ListView productList;

    private ImageButton settingsButton;
    private ImageButton accountButton;
    private ImageButton cancelButton;

    private ProductAdapter productAdapter;
    private List<Product> products;

    private Toolbar toolbar;
    private SharedPreferences sPref;
    private String productsSharedKey = "Products";

    NotificationManagerCompat notificationManager;
    NotificationCompat.Builder builder;

    List<Integer> positions;
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

        accountButton = (ImageButton) findViewById(R.id.account);
        accountButton.setOnClickListener(accountClickListener);

        cancelButton = (ImageButton) findViewById(R.id.cancel);
        cancelButton.setOnClickListener(cancelClickListener);
        cancelButton.setVisibility(View.INVISIBLE);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        notificationManager = NotificationManagerCompat.from(this);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        Resources res = this.getResources();

        positions = new ArrayList<>();

        loadProducts();
        productAdapter = new ProductAdapter(this, R.layout.product_list_item, products);

        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Мой холодильник");
        setSupportActionBar(toolbar);

        productList = (ListView) findViewById(R.id.productList);
        productList.setAdapter(productAdapter);

        Collections.sort(products, new ProductComparator());

        productList.setEmptyView(findViewById(R.id.empty_group));
        ImageView im = (ImageView) findViewById(R.id.empty);

        productList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                productList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                settingsButton.setVisibility(View.INVISIBLE);
                accountButton.setVisibility(View.INVISIBLE);
                cancelButton.setVisibility(View.VISIBLE);
                addProductButton.setVisibility(View.INVISIBLE);

                productAdapter.setMultipleChoise(true);

                for (Product product : products) {
                    product.setChecked(false);
                }
                chooseItem(position, view);

                return true;
            }
        });
        productList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!productAdapter.isMultipleChoise()) {
                    Intent intent = new Intent(getApplicationContext(), ProductActivity.class);
                    intent.putExtra("PRODUCT", products.get(position));
                    startActivity(intent);
                } else {
                    chooseItem(position, view);
                }
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

    private void chooseItem(int position, View view) {
        Product product = productAdapter.getItem(position);
        product.toggleChecked();
        if (product.isChecked()) {
            view.setBackgroundColor(Color.rgb(204,204,204));
        } else {
            view.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.delete:
                getCheckedIds();
                createDeleteDialog();
                break;
            case R.id.add_to_shopping_list:
                getCheckedIds();
                createAddToShoppingListDialog();
                break;
            case R.id.order:
                getCheckedIds();
                createOrderDialog();
                break;
        }
        cancel();

        return super.onOptionsItemSelected(item);
    }

    private void getCheckedIds() {
        positions = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).isChecked()) {
                positions.add(i);
            }
        }
    }

    private void addToShoppingList() {
        List<String> newProducts = new ArrayList<>();
        for (int i : positions) {
            newProducts.add(products.get(i).toString());
        }

        Intent intent = new Intent(getApplicationContext(), ShoppingListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("SHOPPING_LIST", (Serializable) newProducts);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);

        startActivity(intent);
    }

    private void createAddToShoppingListDialog() {
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        addToShoppingList();
                        dialog.dismiss();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Добавить в список покупок?")
                .setPositiveButton("Добавить", myClickListener)
                .setNegativeButton("Отмена", myClickListener);
        builder.create().show();
    }

    private void createDeleteDialog() {
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        deleteItems();
                        dialog.dismiss();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Удалить выделеные элементы?")
                .setPositiveButton("Удалить", myClickListener)
                .setNegativeButton("Отмена", myClickListener);
        builder.create().show();
    }

    private void deleteItems() {
        for (int i = positions.size() - 1; i >= 0; i--) {
            products.remove(positions.get(i).intValue());
        }
        productAdapter.notifyDataSetChanged();

        Gson gson = new Gson();
        SharedPreferences.Editor editor = sPref.edit();
        String json = gson.toJson(products);
        editor.putString(productsSharedKey, json);
        editor.apply();
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

        Collections.sort(products, new ProductComparator());
        productAdapter.notifyDataSetChanged();
        //notifyIfNeed();
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

    View.OnClickListener cancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    private void cancel() {
        for (int position = 0; position < products.size(); position++) {
            Product product = productAdapter.getItem(position);
            product.setChecked(false);

            getViewByPosition(position).setBackgroundColor(Color.WHITE);
        }
        productList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        settingsButton.setVisibility(View.VISIBLE);
        accountButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.INVISIBLE);
        addProductButton.setVisibility(View.VISIBLE);

        productAdapter.setMultipleChoise(false);
    }

    private void sendEmail() {
        StringBuilder body = new StringBuilder();
        body.append("Заказ: \n");
        for (int i : positions) {
            body.append(products.get(i).toString());
            body.append("\n");
        }
        EmailIntentBuilder.from(this)
                .to("food@example.com")
                .cc("user@example.com")
                .subject("Заказ продуктов")
                .body(body.toString())
                .start();
    }

    private void createOrderDialog() {
        DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        sendEmail();
                        dialog.dismiss();
                        break;
                    case Dialog.BUTTON_NEUTRAL:
                        dialog.dismiss();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Заказ продуктов в магазине")
                .setPositiveButton("Купить", myClickListener)
                .setNegativeButton("Отмена", myClickListener);
        builder.create().show();
    }

    View.OnClickListener accountClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent accountIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(accountIntent);
        }
    };

    public View getViewByPosition(int pos) {
        final int firstListItemPosition = productList.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + productList.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return  productList.getAdapter().getView(pos, null, productList);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return productList.getChildAt(childIndex);
        }
    }
}