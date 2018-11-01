package com.example.lida.foodtracker;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Retrofit.Recipe;
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

    private ArrayAdapter<Recipe> adapter;
    private ArrayList<Recipe> recipesList;
    private ListView recipes;

    private LayoutInflater inflater;
    private FloatingActionButton addRecipesButton;

    private SharedPreferences sPref;
    private String recipesSharedKey = "Recipe";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        sPref = getPreferences(MODE_PRIVATE);

        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);

        loadRecipes();

        adapter = new ArrayAdapter<Recipe>(this, R.layout.add_recipe, recipesList);



        addRecipesButton = (FloatingActionButton) findViewById(R.id.add_recipe);
        addRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), RecipeAdding.class);
//                startActivity(intent);
                //             startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle bundle = data.getExtras();
        Recipe result = (Recipe)bundle.getSerializable("RECIPE");
        recipesList.add(result);
        adapter.notifyDataSetChanged();
        addRecipeToSharedPref(result);
    }

    private void loadRecipes() {
        recipesList = new ArrayList<>();

        if (sPref.contains(recipesSharedKey)) {
            Gson gson = new Gson();
            String json = sPref.getString(recipesSharedKey, null);
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            List<Recipe> recs = gson.fromJson(json, type);
            for (Recipe r: recs) {
                recipesList.add(r);
            }
        }
    }

    private void addRecipeToSharedPref(Recipe recipe) {
        Gson gson = new Gson();
        List<Recipe> recipes;
        if (sPref.contains(recipesSharedKey)){
            String json = sPref.getString(recipesSharedKey, null);
            Type type = new TypeToken<ArrayList<Product>>(){}.getType();
            recipes = gson.fromJson(json, type);
        } else {
            recipes = new ArrayList<Recipe>();
        }
        recipes.add(recipe);
        SharedPreferences.Editor editor = sPref.edit();
        String json = gson.toJson(recipes);
        editor.putString(recipesSharedKey, json);
        editor.apply();
    }

}