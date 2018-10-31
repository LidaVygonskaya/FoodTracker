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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        shoppingListText = findViewById(R.id.add_product_edit_text);
        shoppingListText.setOnFocusChangeListener(onFocusChangeListener);
        shoppingListText.setOnKeyListener(onEditTextClickListener);

        inflater = ShoppingListActivity.this.getLayoutInflater();

        shoppingList = (ListView) findViewById(R.id.shopp_list);
        shoppingList.setEmptyView(findViewById(R.id.empty_group));

        productList = new ArrayList<String>() ;

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
                //shoppingListText.setOnKeyListener(null);

            }
        }
    };
}

