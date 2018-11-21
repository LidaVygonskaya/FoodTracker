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
import java.util.List;

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
        String ingredientsString = recepie.getIngredients();
        if (ingredientsString.isEmpty()) {
            ingredientsString = "Упс! Кажется здесь пусто. Полный список продуктов указан в Приготовлении";

        } else {
            String[] arrayIngredients = ingredientsString.split(",");
            StringBuilder ingredientsBuilder = new StringBuilder();
            for (String elem: arrayIngredients) {
                if (elem.startsWith(" ")) {
                    elem = elem.substring(1);
                }
                ingredientsBuilder.append(elem);
                ingredientsBuilder.append(System.getProperty("line.separator"));
                ingredientsBuilder.append(System.getProperty("line.separator"));
            }

            ingredientsString = ingredientsBuilder.toString();
            ingredientsString = ingredientsString.replaceAll("\\\\n   \\\\n    \\\\n      ", "\n\n");
            ingredientsString = ingredientsString.replaceAll("\\\\n\\\\n ", "");
            ingredientsString = ingredientsString.replaceAll("\\\\n\\\\n", "");
            ingredientsString = ingredientsString.replaceAll("\\\\n", "");
            ingredientsString = ingredientsString.replaceAll(" {23}", "");
            ingredientsString = ingredientsString.replaceAll("Ингредиенты:", "");
            ingredientsString = ingredientsString.replaceAll("^\\s+", "");

        }
        ingridientsTextView.setText(ingredientsString);

        instructionTextView = findViewById(R.id.instruction_list);

        String instruction;
        String [] arrayInstruction = recepie.getInstruction().split("(?<=\\.)");
        StringBuilder instructionBuilder = new StringBuilder();
        for (String elem: arrayInstruction) {
            if (elem.startsWith(" ")) {
                elem = elem.substring(1);
            }
            instructionBuilder.append(elem);
            instructionBuilder.append(System.getProperty("line.separator"));
            instructionBuilder.append(System.getProperty("line.separator"));
            //instructionBuilder.append(System.getProperty("line.separator"));
        }
        instruction = instructionBuilder.toString();
        instruction = instruction.replaceAll("\\\\n   \\\\n    \\\\n      ", "\n\n");
        instruction = instruction.replaceAll("\\\\n\\\\n ", "");
        instruction = instruction.replaceAll("\\\\n\\\\n", "");
        instruction = instruction.replaceAll("\\\\n", "");
        instruction = instruction.replaceAll("Инструкции:", "");
        instruction = instruction.replaceAll("^\\s+", "");
        instructionTextView.setText(instruction);


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
