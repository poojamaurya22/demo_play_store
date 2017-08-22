package com.mobilinker.mobiplace.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Pooja on 25-02-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts.db";
    private static final String TABLE_NAME = "contacts";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PHONE = "phone";
    SQLiteDatabase db;
    private static final String TABLE_CREATE = "create table contacts(uname text not null, email text not null, phone text not null);";

    public DatabaseHelper(Context context){

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    public void insertContact(Contact c){

        db = this.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(COLUMN_EMAIL, c.getEmail());
        value.put(COLUMN_UNAME, c.getUname());
        value.put(COLUMN_PHONE, c.getPhone());

        db.insert(TABLE_NAME, null, value);
        db.close();

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS contacts";
        db.execSQL(query);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
