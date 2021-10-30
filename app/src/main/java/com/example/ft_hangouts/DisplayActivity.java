package com.example.ft_hangouts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class DisplayActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private TelephonyManager telephonyManager;

    private TextView name_txt, surname_txt, tel_txt, email_txt, about_txt, address_txt;
    private Button call_button, edit_button, msg_button, delete_button;
    private Contact contact;
    private String id;
    private boolean phone_permission_call_granted;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_display);
        updateHeaderColor();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }

        telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        phone_permission_call_granted = false;
        call_button = (Button)findViewById(R.id.make_call_button);
        edit_button = (Button)findViewById(R.id.edit_button);
        msg_button = (Button)findViewById(R.id.message_button);
        delete_button = (Button)findViewById(R.id.delete_button);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performMessageEdition();
            }
        });

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTelephonyEnabled()) {
                    // ToDo: Check for phone permission.
                    checkForPhonePermission();
                    // ToDo: Register the PhoneStateListener.
                    //Do the call
                    if (phone_permission_call_granted){
                        makeCall();
                    }else {
                        Toast.makeText(DisplayActivity.this, "Call permission not granted yet :-(", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(DisplayActivity.this,
                            "TELEPHONY NOT ENABLED! ",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


        msg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, MessageActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", contact.getName());
                intent.putExtra("surname", contact.getSurname());
                intent.putExtra("tel", contact.getTel());
                startActivity(intent);
            }
        });

        name_txt = (TextView)findViewById(R.id.display_name_txt);
        surname_txt = (TextView)findViewById(R.id.display_surname_txt);
        tel_txt = (TextView)findViewById(R.id.display_phone_txt);
        email_txt = (TextView)findViewById(R.id.display_email_txt);
        about_txt = (TextView)findViewById(R.id.display_about_txt);
        address_txt = (TextView)findViewById(R.id.display_address_txt);

        getAndSetIntentData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (permissions[0].equalsIgnoreCase
                        (Manifest.permission.CALL_PHONE)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    phone_permission_call_granted = true;
                    makeCall();
                } else {
                    // Permission denied.
                    Toast.makeText(this,
                            "Failure to obtain permission!",
                            Toast.LENGTH_LONG).show();
                    phone_permission_call_granted = false;
                }
            }
        }
    }

    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("name") && getIntent().hasExtra("surname")
                && getIntent().hasExtra("tel") && getIntent().hasExtra("email") && getIntent().hasExtra("about")
                && getIntent().hasExtra("address")) {
            id = getIntent().getStringExtra("id");
            String name = getIntent().getStringExtra("name");
            String surname = getIntent().getStringExtra("surname");
            String tel = getIntent().getStringExtra("tel");
            String email = getIntent().getStringExtra("email");
            String about = getIntent().getStringExtra("about");
            String address = getIntent().getStringExtra("address");

            name_txt.setText(name);
            surname_txt.setText(surname);
            tel_txt.setText(tel);
            email_txt.setText(email);
            about_txt.setText(about);
            address_txt.setText(address);
            contact = new Contact(name, surname, tel, email, about, address);
        } else {
            //We get data from input we don't care about id anyway
            String name = name_txt.getText().toString().trim();
            String surname = surname_txt.getText().toString().trim();
            String tel = tel_txt.getText().toString().trim();
            String email = email_txt.getText().toString().trim();
            String about = about_txt.getText().toString().trim();
            String address = address_txt.getText().toString().trim();
            if (name != null && surname != null && tel != null && email != null && about != null && address != null){
                if (!name.isEmpty() && !surname.isEmpty() && !tel.isEmpty() && !email.isEmpty() && !about.isEmpty() && !address.isEmpty()){
                    contact = new Contact(name, surname, tel, email, about, address);

                }else{
                    Toast.makeText(this, "Empty field can't update", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private boolean isTelephonyEnabled() {
        if (telephonyManager != null) {
            if (telephonyManager.getSimState() ==
                    TelephonyManager.SIM_STATE_READY) {
                return true;
            }
        }
        return false;
    }


    private void checkForPhonePermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(DisplayActivity.this,"PERMISSION NOT GRANTED!", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            phone_permission_call_granted = true;
        }
    }

    void makeCall(){
        if (contact == null){
            Toast.makeText(DisplayActivity.this, "Phone call permission granted so you can call but contact is null", Toast.LENGTH_SHORT).show();
        }
        String phoneNumber = String.format("tel: %s",
                contact.getTel());
        Toast.makeText(DisplayActivity.this, "Phone call permission granted so you can call " + phoneNumber, Toast.LENGTH_SHORT).show();
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(phoneNumber));
        startActivity(callIntent);
    }
    void performMessageEdition(){
        Intent intent = new Intent(DisplayActivity.this, UpdateActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", contact.getName());
        intent.putExtra("surname", contact.getSurname());
        intent.putExtra("tel", contact.getTel());
        intent.putExtra("email", contact.getEmail());
        intent.putExtra("about", contact.getAbout());
        intent.putExtra("address", contact.getAddress());
        startActivity(intent);
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

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        changeLanguage(language);
    }
}