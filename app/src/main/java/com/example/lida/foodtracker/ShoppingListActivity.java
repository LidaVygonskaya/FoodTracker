package com.example.lida.foodtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.lida.foodtracker.Retrofit.Product;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class ShoppingListActivity extends BaseActivity {

    //TODO: При нажатии где-то кроме сделать анфокус
    //TODO: Уезжает Навигейшн
    @Override
    int getContentViewId() {
        return R.layout.activity_shopping_list;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.shopping_list;
    }

    private ArrayAdapter<String> adapter;
    private ArrayList<String> productList;
    private ListView shoppingList;

    private EditText shoppingListText;
    private LayoutInflater inflater;
    private FloatingActionButton addShoppingListButton;

    private SharedPreferences sPref;
    private String shoppingListSharedKey = "ShoppingList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        sPref = getPreferences(MODE_PRIVATE);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        shoppingListText = findViewById(R.id.add_product_edit_text);
        shoppingListText.setOnFocusChangeListener(onFocusChangeListener);
        shoppingListText.setOnKeyListener(onEditTextClickListener);

        inflater = ShoppingListActivity.this.getLayoutInflater();

        shoppingList = (ListView) findViewById(R.id.shopp_list);
        shoppingList.setEmptyView(findViewById(R.id.empty_group));

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
                new AlertDialog.Builder(ShoppingListActivity.this)
                        .setTitle("Удалить объект " + productList.get(position) + "?")
                        .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                productList.remove(position);
                                adapter.notifyDataSetChanged();

                                Gson gson = new Gson();
                                SharedPreferences.Editor editor = sPref.edit();
                                String json = gson.toJson(productList);
                                editor.putString(shoppingListSharedKey, json);
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
                productList.add(textProduct);
                adapter.notifyDataSetChanged();
                shoppingListText.getText().clear();
                shoppingListText.clearFocus();

                addItemToSharedPref(textProduct);

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

}

