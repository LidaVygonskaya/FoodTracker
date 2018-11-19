package com.example.lida.foodtracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lida.foodtracker.Retrofit.Category;

public class CategoryRecepiesActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageButton closeButton;
    private Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_recepies);
        category = (Category) getIntent().getExtras().getSerializable("CATEGORY");

        titleTextView = findViewById(R.id.title_view);
        titleTextView.setText(category.getName());

        closeButton = findViewById(R.id.exit);
        closeButton.setOnClickListener(v -> finish());
    }
}
