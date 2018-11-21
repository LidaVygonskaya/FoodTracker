package com.example.lida.foodtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Utils.ProductAdapter;
import com.example.lida.foodtracker.Utils.ProductComparator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ShoppingListActivity extends BaseActivity {

    @Override
    int getContentViewId() {
        return R.layout.activity_shopping_list;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.shopping_list;
    }

    //private ImageButton settingsButton;
    private ImageButton accountButton;
    private Button loadButton;

    private Button saveButton;

    private ArrayAdapter<String> adapter;
    private ArrayList<String> productList;
    private ListView shoppingList;

    private List<Product> products;

    private EditText shoppingListText;
    private LayoutInflater inflater;
    private FloatingActionButton addShoppingListButton, saveProductsButton;

    private SharedPreferences sPref;
    private String shoppingListSharedKey = "ShoppingList";
    private String favouriteShoppingKey = "Favourite_list";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        products = new ArrayList<>();

        sPref = getPreferences(MODE_PRIVATE);

        accountButton = findViewById(R.id.account);
        accountButton.setOnClickListener(accountClickListener);

        loadButton = findViewById(R.id.empty_button);
        loadButton.setOnClickListener(loadClickListener);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        shoppingListText = findViewById(R.id.add_product_edit_text);
        shoppingListText.setOnFocusChangeListener(onFocusChangeListener);
        shoppingListText.setOnKeyListener(onEditTextClickListener);

        inflater = ShoppingListActivity.this.getLayoutInflater();

        shoppingList = (ListView) findViewById(R.id.shopp_list);
        shoppingList.setEmptyView(findViewById(R.id.empty_group));
        ImageView im = (ImageView) findViewById(R.id.empty);
        //im.setAlpha(50);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setTitle("Список покупок");
        setSupportActionBar(toolbar);


        loadShoppingList();

        adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item, productList);
        shoppingList.setAdapter(adapter);

        addShoppingListButton = (FloatingActionButton) findViewById(R.id.add_shopping_list);
        addShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shoppingListText.requestFocus();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(shoppingListText, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        shoppingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final LayoutInflater inflater = ShoppingListActivity.this.getLayoutInflater();
                View v = inflater.inflate(R.layout.add_product, null);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ShoppingListActivity.this);

                mBuilder.setTitle("Добавление продукта в холодильник");

                final EditText productNameView = (EditText) v.findViewById(R.id.add_product);
                productNameView.setText(productList.get(position));
                final DatePicker date = (DatePicker) v.findViewById(R.id.datePicker);
                final EditText numberPicker = (EditText) v.findViewById(R.id.numberPicker);
                numberPicker.setOnFocusChangeListener(onFocusChangeListener);
                numberPicker.setOnKeyListener(onEditTextClickListener);
                numberPicker.setSelection(numberPicker.getText().length());

                final Spinner spinner = (Spinner) v.findViewById(R.id.spinner_quantity_choise);
                ArrayAdapter<?> adapterSpinner = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.quantity_choise, android.R.layout.simple_spinner_item);
                adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
                spinner.setAdapter(adapterSpinner);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        ((TextView) view).setTextColor(Color.BLACK);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                mBuilder.setView(v);

                mBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        if (!productNameView.getText().toString().isEmpty() && !numberPicker.getText().toString().isEmpty()) {
                            Product product = new Product();
                            product.setDateEnd(new Date(date.getYear(), date.getMonth(), date.getDayOfMonth()));
                            product.setQuantity(Double.parseDouble(numberPicker.getText().toString()));
                            product.setName(productNameView.getText().toString());
                            product.setQuantityChoise(spinner.getSelectedItem().toString());
                            ////
                            product.setDescription("smth description");
                            product.setImgId(R.drawable.carrot);

                            products.add(product);
                            saveProductsButton.setVisibility(View.VISIBLE);

                            productList.remove(position);
                            adapter.notifyDataSetChanged();

                            Gson gson = new Gson();
                            SharedPreferences.Editor editor = sPref.edit();
                            String json = gson.toJson(productList);
                            editor.putString(shoppingListSharedKey, json);
                            editor.apply();

                            dialog1.dismiss();
                        } else {
                            //TODO: не закрывать диалог, а предлагать ввести название еще раз
                        }
                    }
                });

                mBuilder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog1, int which) {
                        dialog1.dismiss();
                    }
                });

                AlertDialog dialogProduct = mBuilder.create();
                dialogProduct.show();
            }
        });

        saveProductsButton = (FloatingActionButton) findViewById(R.id.save_products);
        saveProductsButton.setVisibility(View.INVISIBLE);
        saveProductsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("PRODUCT_LIST", (Serializable) products);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);

                startActivity(intent);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    EditText.OnKeyListener onEditTextClickListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                String textProduct = shoppingListText.getText().toString();
                if (textProduct.isEmpty()) {
                    hideKeyboard(v);
                    return true;
                }
                productList.add(textProduct);
                adapter.notifyDataSetChanged();
                shoppingListText.getText().clear();
                shoppingListText.clearFocus();

                addItemToSharedPref(textProduct);

                hideKeyboard(v);

                return true;
            }
            return false;
        }
    };

    EditText.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (!hasFocus) {
                hideKeyboard(v);
                return;
            }
        }
    };

    private void loadShoppingList() {
        productList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            List<String> resultList = (ArrayList<String>) bundle.getSerializable("SHOPPING_LIST");

            if (resultList != null) {
                for (String p : resultList) {
                    addItemToSharedPref(p);
                }
            }
        }

        if (sPref.contains(shoppingListSharedKey)) {
            Gson gson = new Gson();
            String json = sPref.getString(shoppingListSharedKey, null);
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            List<String> items = gson.fromJson(json, type);
            for (String item: items) {
                productList.add(item);
            }
        }
    }

    private void addItemToSharedPref(String name) {
        Gson gson = new Gson();
        List<String> names;
        if (sPref.contains(shoppingListSharedKey)){
            String json = sPref.getString(shoppingListSharedKey, null);
            Type type = new TypeToken<ArrayList<String>>(){}.getType();
            names = gson.fromJson(json, type);
        } else {
            names = new ArrayList<String>();
        }
        names.add(name);
        SharedPreferences.Editor editor = sPref.edit();
        String json = gson.toJson(names);
        editor.putString(shoppingListSharedKey, json);
        editor.apply();
    }

    View.OnClickListener settingsOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        }
    };


    View.OnClickListener accountClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent accountIntent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(accountIntent);
        }
    };

    View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.save_list:
                saveList();
                break;
            case R.id.settings:
                openSettings();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void saveList() {
        //TODO: Save list

        sPref = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sPref.edit();

        String email = sPref.getString("email", "");

        if (!email.isEmpty() && !productList.isEmpty()) {
            Gson gson = new Gson();
            String json = gson.toJson(productList);
            editor.putString(favouriteShoppingKey, json);
            editor.apply();
        } else {
            Toast.makeText(getApplicationContext(), "Необходимо войти", Toast.LENGTH_LONG).show();
        }
    }

    private void openSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private void loadRemoteShoppingList() {
        //TODO: load remote shopping list from server
        sPref = android.support.v7.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String products = sPref.getString(favouriteShoppingKey, "");
        if (products.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Кажется у вас еще нет списка", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Обязательно загружу", Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener loadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loadRemoteShoppingList();
        }
    };
}

