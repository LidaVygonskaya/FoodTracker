package com.example.lida.foodtracker;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lida.foodtracker.Retrofit.Category;
import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Retrofit.Recipe;
import com.example.lida.foodtracker.Utils.CategoryAdapter;
import com.example.lida.foodtracker.Utils.DataBaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RecipesActivity extends BaseActivity {

    @Override
    int getContentViewId() {
        return R.layout.activity_recipes;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.recipes;
    }

    private CategoryAdapter categoryAdapter;
    private ListView categoriesListView;

    private ImageButton accountButton;
    private ImageButton settingsButton;

    private LayoutInflater inflater;
    private FloatingActionButton addRecipesButton;

    private SharedPreferences sPref;
    private String recipesSharedKey = "Recipe";

    private List<Category> categories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
        categories = myDbHelper.getCategories();

        sPref = getPreferences(MODE_PRIVATE);

        categoryAdapter = new CategoryAdapter(this, R.layout.category_list_item, categories);

        categoriesListView = findViewById(R.id.category_list);
        categoriesListView.setAdapter(categoryAdapter);
        categoriesListView.setOnItemClickListener(categoryListener);

        accountButton = findViewById(R.id.account);
        accountButton.setOnClickListener(accountClickListener);

        settingsButton = (ImageButton) findViewById(R.id.settings);
        settingsButton.setOnClickListener(settingsOnClick);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
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

    AdapterView.OnItemClickListener categoryListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), CategoryRecepiesActivity.class);
            intent.putExtra("CATEGORY", categories.get(position));
            startActivity(intent);
        }
    };

}