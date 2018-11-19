package com.example.lida.foodtracker;


import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;


public class SettingsActivity extends AppCompatActivity {
    private ImageButton closeButton;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        closeButton = findViewById(R.id.exit);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (savedInstanceState == null) {
            Fragment preferenceFragment = new PreferenceFragmentCustom();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            //ft.add(R.id.fragment, preferenceFragment);
            ft.add(R.id.pref_container, preferenceFragment);
            ft.commit();
        }

    }
    
}