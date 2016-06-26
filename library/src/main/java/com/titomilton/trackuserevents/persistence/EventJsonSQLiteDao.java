package com.titomilton.trackuserevents.persistence;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class EventJsonSQLiteDao implements EventJsonDao {

    private final String LOG_TAG = EventJsonSQLiteDao.class.getSimpleName();

    private final DataBaseHandler dataBaseHandler;

    public EventJsonSQLiteDao(DataBaseHandler dataBaseHandler) {
        this.dataBaseHandler = dataBaseHandler;
    }

    @Override
    public void addEventJson(String apiKey, String json) {
        SQLiteDatabase db = null;
        try {
            db = dataBaseHandler.getWritableDatabase();
            EventJson eventJson = new EventJson(apiKey, json);
            ContentValues values = new ContentValues();
            values.put(EventJsonTable.TABLE_EVENTS_COLUMN_API_KEY, eventJson.getApiKey());
            values.put(EventJsonTable.TABLE_EVENTS_COLUMN_JSON, eventJson.getJsonEvent());
            db.insert(EventJsonTable.TABLE_EVENTS, null, values);
            Log.d(LOG_TAG, "The tracked event was cached. " + eventJson.toString());
        } finally {
            if (db != null) db.close();
        }
    }

    @Override
    public List<EventJson> getAllEventsJSON() {
        List<EventJson> eventJsonList = new ArrayList<>();

        String selectQuery = "SELECT " +
                EventJsonTable.TABLE_EVENTS_COLUMN_ID + ", " +
                EventJsonTable.TABLE_EVENTS_COLUMN_API_KEY + ", " +
                EventJsonTable.TABLE_EVENTS_COLUMN_JSON +
                " FROM " + EventJsonTable.TABLE_EVENTS +
                " ORDER BY " + EventJsonTable.TABLE_EVENTS_COLUMN_ID;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dataBaseHandler.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EventJson eventJson = new EventJson();
                    eventJson.setId(Integer.parseInt(cursor.getString(0)));
                    eventJson.setApiKey(cursor.getString(1));
                    eventJson.setJsonEvent(cursor.getString(2));
                    eventJsonList.add(eventJson);
                } while (cursor.moveToNext());
            }
        } finally {
            if (db != null) db.close();
            if (cursor != null) cursor.close();
        }

        return eventJsonList;
    }

    @Override
    public void removeEventJson(int id) {
        SQLiteDatabase db = null;
        try {
            db = dataBaseHandler.getWritableDatabase();
            db.delete(EventJsonTable.TABLE_EVENTS, EventJsonTable.TABLE_EVENTS_COLUMN_ID + " = ?",
                    new String[]{String.valueOf(id)});
        } finally {
            if (db != null) db.close();
        }
    }
}
