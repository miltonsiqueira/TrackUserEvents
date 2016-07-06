package com.titomilton.trackuserevents;

import android.content.Context;
import android.content.Intent;

import com.titomilton.trackuserevents.cache.ScheduleSendCachedEvents;
import com.titomilton.trackuserevents.cache.SendCachedEventsService;
import com.titomilton.trackuserevents.persistence.DataBaseHandler;
import com.titomilton.trackuserevents.persistence.EventJson;
import com.titomilton.trackuserevents.persistence.EventJsonDao;
import com.titomilton.trackuserevents.persistence.EventJsonSQLiteDao;

import java.util.List;

public class TrackUserEvents {

    private final String LOG_TAG = TrackUserEvents.class.getSimpleName();
    private String apiKey;
    private final Context context;


    public TrackUserEvents(Context context){
        this.context = context;

        setAlarm();
    }

    public TrackUserEvents(String apiKey, Context context) throws InvalidApiKeyException {
        this(context);
        setApiKey(apiKey);
    }

    public TrackUserEvents setApiKey(String apiKey) throws InvalidApiKeyException {
        if(apiKey == null || apiKey.isEmpty()){
            throw new InvalidApiKeyException();
        }
        this.apiKey = apiKey;
        return this;
    }

    public Event createEventByName(String name) throws InvalidEventRequestException {
        return Event.createByName(this.apiKey, name, this.context);
    }

    public Event createEventByJson(String json){
        return Event.createFromJson(this.apiKey, json, context);
    }

    public List<EventJson> getCachedEvents() {
        final EventJsonDao eventJsonDao = new EventJsonSQLiteDao(new DataBaseHandler(this.context));
        return eventJsonDao.getAllEventsJSON();
    }


    public TrackUserEvents sendCachedEvents(){
        context.startService(new Intent(context, SendCachedEventsService.class));
        return this;
    }

    /**
     * Turn on the alarm to send the cached events
     * with the interval {@value com.titomilton.trackuserevents.cache.ScheduleSendCachedEvents#INTERVAL_FIVE_MINUTES}
     * @return The same instance of {@link TrackUserEvents}
     */
    public TrackUserEvents setAlarm(){
        ScheduleSendCachedEvents scheduleSendCachedEvents = new ScheduleSendCachedEvents(context);
        scheduleSendCachedEvents.setAlarm();
        return this;
    }

    public TrackUserEvents cancelAlarm(){
        ScheduleSendCachedEvents scheduleSendCachedEvents = new ScheduleSendCachedEvents(context);
        scheduleSendCachedEvents.cancelAlarm();
        return this;
    }


}
