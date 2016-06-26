package com.titomilton.trackuserevents.cache;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver{
    private static String LOG_TAG = AlarmReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(LOG_TAG, "onReceive was triggered");
        Intent intentService = new Intent(context, SendCachedEventsService.class);
        context.startService(intentService);
    }

}
