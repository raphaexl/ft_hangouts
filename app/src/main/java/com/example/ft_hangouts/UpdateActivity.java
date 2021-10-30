package com.example.ft_hangouts;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Locale;

public class UpdateActivity extends AppCompatActivity {
    private EditText name_input, surname_input, tel_input, email_input, about_input, address_input;
    private Button update_button, delete_button;

    String id, name, surname, tel, email, about, address;
    ContactFormValidator cfv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_update);
        updateHeaderColor();

        name_input = (EditText)findViewById(R.id.name_input2);
        surname_input = (EditText)findViewById(R.id.surname_input2);
        tel_input = (EditText)findViewById(R.id.phone_input2);
        email_input = (EditText)findViewById(R.id.email_input2);
        about_input = (EditText)findViewById(R.id.about_input2);
        address_input = (EditText)findViewById(R.id.address_input2);
        update_button = (Button) findViewById(R.id.update_button);
        delete_button = (Button) findViewById(R.id.delete_button);
        cfv = new ContactFormValidator(name_input, surname_input, tel_input, email_input, about_input, address_input);
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CMDatabaseHelper cmDB = new CMDatabaseHelper(UpdateActivity.this);
                String name = name_input.getText().toString().trim();
                String surname = surname_input.getText().toString().trim();
                String tel = tel_input.getText().toString().trim();
                String email = email_input.getText().toString().trim();
                String about = about_input.getText().toString().trim();
                String address = address_input.getText().toString().trim();
                if (cfv.checkFormValidity(name, surname, tel, email, about, address))
                {
                    cmDB.updateData(id, new Contact(name, surname, tel, email, about, address));
                    goBackToMainActivity();
                }
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });
        getAndSetIntentData();
        ActionBar ab = getSupportActionBar();
        if (ab != null){
            ab.setTitle(name);
        }
    }

    void getAndSetIntentData(){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("surname")
        && getIntent().hasExtra("tel") && getIntent().hasExtra("email") && getIntent().hasExtra("about")
        && getIntent().hasExtra("address")){
            id = getIntent().getStringExtra("id");
            name = getIntent().getStringExtra("name");
            surname = getIntent().getStringExtra("surname");
            tel = getIntent().getStringExtra("tel");
            email = getIntent().getStringExtra("email");
            about = getIntent().getStringExtra("about");
            address = getIntent().getStringExtra("address");

            name_input.setText(name);
            surname_input.setText(surname);
            tel_input.setText(tel);
            email_input.setText(email);
            about_input.setText(about);
            address_input.setText(address);

        }else{
            Toast.makeText(this, "Empty field can't update", Toast.LENGTH_SHORT);
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_row_title + name + "?");
        builder.setMessage(R.string.delete_row_msg + name + "?");
        builder.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CMDatabaseHelper cmDB = new CMDatabaseHelper(UpdateActivity.this);
                cmDB.deleteRow(id);
                goBackToMainActivity();
            }
        });
        builder.setNegativeButton(R.string.cancel_button_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
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

    void goBackToMainActivity(){
        Intent intent=new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private void changeLanguage(String language){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getApplicationContext().getResources().getDisplayMetrics());
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        changeLanguage(language);
    }
}