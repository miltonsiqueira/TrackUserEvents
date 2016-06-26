package com.titomilton.trackuserevents.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "CacheTrackEvents";


    public DataBaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + EventJsonTable.TABLE_EVENTS + "(" +
                EventJsonTable.TABLE_EVENTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                EventJsonTable.TABLE_EVENTS_COLUMN_API_KEY + " TEXT NOT NULL," +
                EventJsonTable.TABLE_EVENTS_COLUMN_JSON + " TEXT NOT NULL)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EventJsonTable.TABLE_EVENTS);
        onCreate(db);
    }

}
