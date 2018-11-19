package com.example.lida.foodtracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lida.foodtracker.Retrofit.Category;
import com.example.lida.foodtracker.Retrofit.Recepie;
import com.example.lida.foodtracker.Utils.DataBaseHelper;
import com.example.lida.foodtracker.Utils.RecepieAdapter;

import java.util.List;

public class CategoryRecepiesActivity extends AppCompatActivity {
    private TextView titleTextView;
    private ImageButton closeButton;
    private Category category;
    private List<Recepie> recepies;
    private RecepieAdapter recepieAdapter;
    private ListView recepieListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_recepies);
        category = (Category) getIntent().getExtras().getSerializable("CATEGORY");


        titleTextView = findViewById(R.id.title_view);
        titleTextView.setText(category.getName());

        closeButton = findViewById(R.id.exit);
        closeButton.setOnClickListener(v -> finish());


        DataBaseHelper myDbHelper = new DataBaseHelper(getApplicationContext());
        recepies = myDbHelper.getRecepiesInCategory(category.getId());

        recepieAdapter = new RecepieAdapter(this, R.layout.recipie_list_item, recepies);

        recepieListView = findViewById(R.id.recepies_list);
        recepieListView.setAdapter(recepieAdapter);
        recepieListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), RecepieActivity.class);
                intent.putExtra("RECEPIE", recepies.get(position));
                startActivity(intent);
            }
        });


    }
}
