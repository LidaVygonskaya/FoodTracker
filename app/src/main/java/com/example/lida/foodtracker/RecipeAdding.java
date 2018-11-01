package com.example.lida.foodtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.lida.foodtracker.Retrofit.Product;
import com.example.lida.foodtracker.Retrofit.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeAdding extends AppCompatActivity {

   ImageButton exitButton;
   Recipe recipe;
   Button saveButton;

   TextView name;
   TextView ingredients;
   TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe);

        exitButton = (ImageButton) findViewById(R.id.exit);
        exitButton.setOnClickListener(exitListener);

        recipe = new Recipe();

        saveButton = (Button) findViewById(R.id.save_recipe);
        saveButton.setOnClickListener(saveListener);

        name = (TextView) findViewById(R.id.recipe_name);
        ingredients = (TextView) findViewById(R.id.recipe_ingredients);
        description = (TextView) findViewById(R.id.recipe_description);
    }

    public void createRecipe() {
        final LayoutInflater inflater = RecipeAdding.this.getLayoutInflater();

        recipe.setName(name.toString());
        recipe.setDescription(description.toString());
    }


    View.OnClickListener saveListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            createRecipe();
            if (name.toString() == "") {
                finish();
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("RECIPE", (Serializable) recipe);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    View.OnClickListener exitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
    }
}