package com.example.lida.foodtracker;


public class RecipesActivity extends BaseActivity {

    @Override
    int getContentViewId() {
        return R.layout.activity_recipes;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.recipes;
    }
}