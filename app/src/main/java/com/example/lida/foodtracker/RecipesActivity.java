package com.example.lida.foodtracker;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Retrofit.Recept;

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

    private ArrayAdapter<String> adapter;
    private ArrayList<String> recipesList;
    private ListView recipes;

    private LayoutInflater inflater;
    private FloatingActionButton addRecipesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        recipesList = new ArrayList<>();

        adapter = new ArrayAdapter<String>(this, R.layout.add_recept, recipesList);

        recipes = (ListView) findViewById(R.id.recipes_list);
        recipes.setAdapter(adapter);
        recipes.setEmptyView(findViewById(R.id.empty_group));

        addRecipesButton = (FloatingActionButton) findViewById(R.id.add_recept);
        addRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), ReceptAdding.class);
                //startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        List<Recept> resultList = (ArrayList<Recept>)bundle.getSerializable("RECEPT");
        //TODO
    }
}