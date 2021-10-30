package com.example.ft_hangouts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    private EditText name_input, surname_input, tel_input, email_input, about_input, address_input;
    private Button add_button;
    private ContactFormValidator cfv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_add);
        updateHeaderColor();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(getResources().getString(R.string.add_contact));
        }

        name_input = (EditText)findViewById(R.id.name_input);
        surname_input = (EditText)findViewById(R.id.surname_input);
        tel_input = (EditText)findViewById(R.id.phone_input);
        email_input = (EditText)findViewById(R.id.email_input);
        about_input = (EditText)findViewById(R.id.about_input);
        address_input = (EditText)findViewById(R.id.address_input);
        add_button = (Button) findViewById(R.id.add_button);
        cfv = new ContactFormValidator(name_input, surname_input, tel_input, email_input, about_input, address_input);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMDatabaseHelper cmDB = new CMDatabaseHelper(AddActivity.this);

                String name = name_input.getText().toString().trim();
                String surname = surname_input.getText().toString().trim();
                String tel = tel_input.getText().toString().trim();
                String email = email_input.getText().toString().trim();
                String about = about_input.getText().toString().trim();
                String address = address_input.getText().toString().trim();

                if (cfv.checkFormValidity(name, surname, tel, email, about, address)){
                    cmDB.addContact(new Contact(name, surname, tel, email, about, address));
                    goBackToMainActivity();
                }
            }
        });
    }
    void updateHeaderColor(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            String colorStr = getResources().getString(AppThemeAndHeader.getCurrentHeaderColor());
            ColorDrawable colorDrawable
                    = new ColorDrawable(Color.parseColor(colorStr));//);
            actionBar.setBackgroundDrawable(colorDrawable);
        }
    }

    private void changeLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getApplicationContext().getResources().getDisplayMetrics());
    }

    void goBackToMainActivity(){
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        changeLanguage(language);
    }
}