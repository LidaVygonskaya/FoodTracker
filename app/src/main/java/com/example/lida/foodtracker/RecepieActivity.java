package com.example.lida.foodtracker;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lida.foodtracker.Retrofit.Category;
import com.example.lida.foodtracker.Retrofit.Recepie;

import java.util.Arrays;

public class RecepieActivity extends AppCompatActivity {
    private TextView title;
    private Recepie recepie;
    private ImageButton closeButton;
    private TextView ingridientsTextView;
    private TextView instructionTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepie);

        recepie = (Recepie) getIntent().getExtras().getSerializable("RECEPIE");

        title = findViewById(R.id.title_view);
        title.setText(convertToNormal(recepie.getName()));

        ingridientsTextView = findViewById(R.id.ingredients_list);
        ingridientsTextView.setText(recepie.getIngredients());

        instructionTextView = findViewById(R.id.instruction_list);
        instructionTextView.setText(recepie.getInstruction());



        closeButton = findViewById(R.id.exit);
        closeButton.setOnClickListener(v -> finish());
    }

    private String convertToNormal(String str) {
        String convertedString = "";
        convertedString = str.toLowerCase();
        char array [] = convertedString.toCharArray();
        array[0] = Character.toUpperCase(array[0]);
        convertedString = new String(array);
        convertedString = convertedString.replace("\\", "");
        return convertedString;
    }
}
