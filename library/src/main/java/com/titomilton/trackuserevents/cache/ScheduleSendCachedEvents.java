package com.titomilton.trackuserevents.cache;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScheduleSendCachedEvents {
    private final String LOG_TAG = ScheduleSendCachedEvents.class.getSimpleName();
    private final AlarmManager alarmManager;
    private final Context context;

    /**
     * Interval to send the cached events in milliseconds
     */
    public static final long INTERVAL_FIVE_MINUTES = 2 * 60 * 1000;

    public ScheduleSendCachedEvents(Context context) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }


    public void setAlarm() {

        PendingIntent alarmIntent = getPendingIntent();
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                INTERVAL_FIVE_MINUTES,
                INTERVAL_FIVE_MINUTES,
                alarmIntent);
        Log.d(LOG_TAG, "Alarm " + AlarmReceiver.class.getSimpleName() + " was set");
    }

    public void cancelAlarm() {
        PendingIntent pendingIntent = getPendingIntent();
        alarmManager.cancel(pendingIntent);
        pendingIntent.cancel();
        Log.d(LOG_TAG, "Alarm " + AlarmReceiver.class.getSimpleName() + " was canceled");
    }

    private PendingIntent getPendingIntent(){
        Intent intent = new Intent(context, AlarmReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }


}
