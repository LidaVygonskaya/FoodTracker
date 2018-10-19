package com.example.lida.foodtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Utils.ListViewItemWithCheckBoxAdapter;

import java.util.ArrayList;
import java.util.ListIterator;


public class ShoppingListActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        inflater = ShoppingListActivity.this.getLayoutInflater();

        shoppingList = (ListView) findViewById(R.id.shopp_list);
        shoppingList.setEmptyView(findViewById(R.id.empty_group));

        productList = new ArrayList<String>() ;

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, productList);
        shoppingList.setAdapter(adapter);

        addShoppingListButton = (FloatingActionButton) findViewById(R.id.add_shopping_list);
        addShoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v = inflater.inflate(R.layout.add_shopping_list, null);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(ShoppingListActivity.this);

                mBuilder.setTitle("Что купить?");
                mBuilder.setView(v);

                shoppingListText = (EditText)v.findViewById(R.id.add_shoppig_list_text);

                mBuilder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            String shopList = shoppingListText.getText().toString();
                            for (String p : shopList.split("\n|,")) {
                                productList.add(p);
                            }
                            adapter.notifyDataSetChanged();
                        }
                        catch (Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

                mBuilder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                mBuilder.show();
            }
        });

    }

}
