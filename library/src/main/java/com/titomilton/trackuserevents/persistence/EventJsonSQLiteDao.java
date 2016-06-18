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
    public void addEventJson(String json) {
        SQLiteDatabase db = dataBaseHandler.getWritableDatabase();
        EventJson eventJson = new EventJson(json);
        ContentValues values = new ContentValues();
        values.put(DataBaseHandler.TABLE_EVENTS_KEY_NAME, eventJson.getJsonEvent());
        db.insert(DataBaseHandler.TABLE_EVENTS, null, values);
        db.close();
        Log.d(LOG_TAG, "The tracked event was cached. " + eventJson.toString());
    }

    @Override
    public List<EventJson> getAllEventsJSON() {
        List<EventJson> eventJsonList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + DataBaseHandler.TABLE_EVENTS +
                " ORDER BY " + DataBaseHandler.TABLE_EVENTS_KEY_ID;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = dataBaseHandler.getWritableDatabase();
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    EventJson eventJson = new EventJson();
                    eventJson.setId(Integer.parseInt(cursor.getString(0)));
                    eventJson.setJsonEvent(cursor.getString(1));
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
        SQLiteDatabase db = dataBaseHandler.getWritableDatabase();
        db.delete(DataBaseHandler.TABLE_EVENTS, DataBaseHandler.TABLE_EVENTS_KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }
}
