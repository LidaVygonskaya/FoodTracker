package com.example.lida.foodtracker;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private final String TAG = "RegisterActivity";
    private final String EMAIL_KEY = "email";
    private final String PHONE_KEY = "phone";
    private final String ADRESS_KEY = "adress";
    private EditText email;
    private EditText phone;
    private EditText adress;
    private Button saveButton;
    private ImageButton closeButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.email_text);
        loadText(EMAIL_KEY, email);

        phone = (EditText) findViewById(R.id.phone_text);
        loadText(PHONE_KEY, phone);

        adress = (EditText) findViewById(R.id.adress_text);
        loadText(ADRESS_KEY, adress);

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(saveButtonClickListener);

        closeButton = (ImageButton) findViewById(R.id.exit);
        closeButton.setOnClickListener(closeButtonClickListener);
    }

    View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveText();

        }
    };

    View.OnClickListener closeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    private void saveText() {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String email = this.email.getText().toString();
        String phone = this.phone.getText().toString();
        String adress = this.adress.getText().toString();

        editor.putString(EMAIL_KEY, email);
        editor.putString(PHONE_KEY, phone);
        editor.putString(ADRESS_KEY, adress);
        editor.apply();
        Toast.makeText(this, "Данные успешно сохранены :)", Toast.LENGTH_SHORT).show();

    }

    private void loadText(String STRING_KEY, EditText fieldEditText) {
        sharedPreferences = getPreferences(MODE_PRIVATE);
        String fieldString = sharedPreferences.getString(STRING_KEY, "");
        if (!fieldString.isEmpty()) {
            fieldEditText.setText(fieldString);
        }
    }


}
