package com.example.lida.foodtracker;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lida.foodtracker.Utils.ListViewItemWithCheckBoxAdapter;

import java.util.ArrayList;


public class ShoppingListActivity extends BaseActivity {

    @Override
    int getContentViewId() {
        return R.layout.activity_shopping_list;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.shopping_list;
    }

    ImageButton addProduct;
    ImageButton clearText;
    EditText addProductText;
    ListView productList;
    ListViewItemWithCheckBoxAdapter adapter;
    ArrayList<String> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        products = new ArrayList<String>();

        productList = (ListView) findViewById(R.id.product_list);
        adapter = new ListViewItemWithCheckBoxAdapter(getApplicationContext(), products);
        productList.setAdapter(adapter);
        productList.setEmptyView(findViewById(R.id.empty));

        addProduct = (ImageButton) findViewById(R.id.done_button);
        addProduct.setOnClickListener(addProductListener);

        clearText = (ImageButton) findViewById(R.id.clear_button);
        clearText.setOnClickListener(clearTextListener);

        addProductText = (EditText) findViewById(R.id.add_product_text);
        addProductText.setOnFocusChangeListener(changeFocusListener);

    }

    EditText.OnFocusChangeListener changeFocusListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                addProduct.setVisibility(View.VISIBLE);
                clearText.setVisibility(View.VISIBLE);
            } else {
                addProduct.setVisibility(View.INVISIBLE);
                clearText.setVisibility(View.INVISIBLE);
                hideKeyboard(v)
                ;
            }
        }
    };

    Button.OnClickListener addProductListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String productName = addProductText.getText().toString();
            if (!productName.isEmpty()) {
                products.add(productName);
                adapter.notifyDataSetChanged();
                addProductText.getText().clear();
            }
        }
    };

    Button.OnClickListener clearTextListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addProductText.getText().clear();
        }
    };

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
