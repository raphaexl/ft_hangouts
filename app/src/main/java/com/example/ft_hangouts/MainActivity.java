package com.example.ft_hangouts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    TextView no_data;

    CMDatabaseHelper cmDB;
    ArrayList <String> contact_ids, names, surnames, tels, emails, abouts, addresses;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLocale();
        setContentView(R.layout.activity_main);
        updateHeaderColor();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        add_button = (FloatingActionButton) findViewById(R.id.add_button);
        no_data = (TextView) findViewById(R.id.no_data);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        cmDB = new CMDatabaseHelper(MainActivity.this);
        contact_ids = new ArrayList<>();
        names = new ArrayList<>();
        surnames = new ArrayList<>();
        tels = new ArrayList<>();
        emails = new ArrayList<>();
        abouts = new ArrayList<>();
        addresses = new ArrayList<>();

        loadDataInArrays();
        customAdapter = new CustomAdapter(MainActivity.this, this, contact_ids, names, surnames, tels, emails, abouts, addresses);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            recreate();
        }
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

    void loadDataInArrays(){
        Cursor cursor = cmDB.readAllData();
        if (cursor.getCount() == 0){
            no_data.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, R.string.no_contact, Toast.LENGTH_SHORT).show();
        }else{
            no_data.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                contact_ids.add(cursor.getString(0));
                names.add(cursor.getString(1));
                surnames.add(cursor.getString(2));
                tels.add(cursor.getString(3));
                emails.add(cursor.getString(4));
                abouts.add(cursor.getString(5));
                addresses.add(cursor.getString(6));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.contats_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all){
            confirmDeleteDialog();
        }else if (item.getItemId() == R.id.header_color){
            setHeaderColor();
        }else if (item.getItemId() == R.id.change_language){
            setApplicationLanguage();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setApplicationLanguage() {
        String [] languagesArray = new String[] {"English", "Francais"};
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_language)
                .setSingleChoiceItems(languagesArray, 0, null)
                .setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        AppLanguage.currentLanguage = selectedPosition;
                        changeLanguage(AppLanguage.getCurrentLanguage());
                        recreate();
                    }
                })
                .setNeutralButton(R.string.cancel_button_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }


    private void setHeaderColor() {
        String [] colorsArray = new String[] {"Purple", "Red", "Green", "Blue", "Black", "Cyan"};
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_color)
                .setSingleChoiceItems(colorsArray, 0, null)
                .setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        AppThemeAndHeader.currentHeaderColor = selectedPosition;
                        updateHeaderColor();
                    }
                })
                .setNeutralButton(R.string.cancel_button_label, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private void changeLanguage(String lang){
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getApplicationContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        changeLanguage(language);
    }


    private void confirmDeleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_all_title);
        builder.setMessage(R.string.delete_all_msg);
        builder.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CMDatabaseHelper cmDB = new CMDatabaseHelper(MainActivity.this);
                cmDB.deleteAllData();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel_button_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) { }
        });
        builder.create().show();
    }

}