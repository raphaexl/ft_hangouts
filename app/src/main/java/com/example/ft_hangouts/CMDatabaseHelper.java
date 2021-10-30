package com.example.ft_hangouts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CMDatabaseHelper  extends SQLiteOpenHelper {
    private  Context context;
    private static final String DATABASE_NAME = "ContactsManager.db";
    private static final int    DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_contacts";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name"; //First Name
    private static final String COLUMN_SURNAME = "surname"; //Last Name
    private static final String COLUMN_TEL = "tel";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_ABOUT = "about";
    private static final String COLUMN_ADDRESS = "address";

    CMDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_SURNAME + " TEXT, "
                + COLUMN_TEL + " TEXT, "
                + COLUMN_EMAIL + " TEXT, "
                + COLUMN_ABOUT + " TEXT, "
                + COLUMN_ADDRESS + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, contact.getName());
        cv.put(COLUMN_SURNAME, contact.getSurname());
        cv.put(COLUMN_TEL, contact.getTel());
        cv.put(COLUMN_EMAIL, contact.getEmail());
        cv.put(COLUMN_ABOUT, contact.getAbout());
        cv.put(COLUMN_ADDRESS, contact.getAddress());

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1){
            Toast.makeText(context, "Failed to save the new Contact", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "The new Contact Successfully saved", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if (db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, contact.getName());
        cv.put(COLUMN_SURNAME, contact.getSurname());
        cv.put(COLUMN_TEL, contact.getTel());
        cv.put(COLUMN_EMAIL, contact.getEmail());
        cv.put(COLUMN_ABOUT, contact.getAbout());
        cv.put(COLUMN_ADDRESS, contact.getAddress());
        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to update the Contact", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "The Contact Successfully updated", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1){
            Toast.makeText(context, "Failed to delete the Contact", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "The Contact Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
