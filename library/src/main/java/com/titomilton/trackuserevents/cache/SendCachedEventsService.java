package com.titomilton.trackuserevents.cache;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.titomilton.trackuserevents.rest.CallbackResponse;
import com.titomilton.trackuserevents.ConnectionInfo;
import com.titomilton.trackuserevents.rest.DefaultCallbackSendCachedEvents;
import com.titomilton.trackuserevents.InvalidEventRequestException;
import com.titomilton.trackuserevents.NetworkConnectionNotFoundException;
import com.titomilton.trackuserevents.TrackUserEvents;
import com.titomilton.trackuserevents.persistence.DataBaseHandler;
import com.titomilton.trackuserevents.persistence.EventJson;
import com.titomilton.trackuserevents.persistence.EventJsonDao;
import com.titomilton.trackuserevents.persistence.EventJsonSQLiteDao;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class SendCachedEventsService extends IntentService {
    private static String LOG_TAG = SendCachedEventsService.class.getSimpleName();
    private static AtomicInteger eventsPendingSend = new AtomicInteger(0);

    public SendCachedEventsService() {
        super("SendCachedEventsService");
        setIntentRedelivery(false);
        Log.w(LOG_TAG, "Service created");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            sendCachedEvents(new DefaultCallbackSendCachedEvents());
        } catch (CachedEventsAreBeingSendException e) {
            Log.w(LOG_TAG, e.getMessage());
        }

    }


    private void sendCachedEvents(final CallbackSendCachedEvents callbackSendCachedEvents) throws CachedEventsAreBeingSendException {

        Log.d(LOG_TAG, "sendCachedEvents called");

        if (eventsPendingSend.get() > 0) {
            throw new CachedEventsAreBeingSendException(eventsPendingSend.get());
        }

        Context context = getApplicationContext();


        try {

            // Check if the device is online
            ConnectionInfo.getConnectionType(context);

            final EventJsonDao eventJsonDao = new EventJsonSQLiteDao(new DataBaseHandler(context));
            List<EventJson> eventJsonList = eventJsonDao.getAllEventsJSON();

            eventsPendingSend.set(eventJsonList.size());

            if (eventJsonList.isEmpty()) {

                callbackSendCachedEvents.onEndSendEvents(0);

            } else {

                final int totalEvents = eventJsonList.size();

                TrackUserEvents trackUserEvents = new TrackUserEvents(context);

                for (EventJson eventJson : eventJsonList) {
                    final int id = eventJson.getId();

                    Log.d(LOG_TAG, "Dealing with cached event. " + eventJson);

                    trackUserEvents.createEventByJson(eventJson.getJsonEvent());

                    try {
                        trackUserEvents.setApiKey(eventJson.getApiKey())
                                .createEventByJson(eventJson.getJsonEvent())
                                .sendWithouCache(new CallbackResponse() {
                                    @Override
                                    public void onResponse(String responseBody, String requestBody) {
                                        eventJsonDao.removeEventJson(id);
                                        callbackSendCachedEvents.onResponse(responseBody, requestBody);
                                        cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {
                                        callbackSendCachedEvents.onFailure(t);
                                        cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                                    }

                                    @Override
                                    public void onInvalidCodeResponse(int code, String responseBody, String requestBody) {
                                        callbackSendCachedEvents.onInvalidCodeResponse(code, responseBody, requestBody);
                                        cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                                    }

                                    @Override
                                    public void onFailureReadingResponse(int code, String requestBody, Throwable t) {
                                        callbackSendCachedEvents.onFailureReadingResponse(code, requestBody, t);
                                        cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                                    }
                                });

                    } catch (NetworkConnectionNotFoundException e) {
                        eventsPendingSend.set(0);
                        callbackSendCachedEvents.onEndSendEvents(totalEvents);
                        throw e;
                    } catch (InvalidEventRequestException e) {
                        cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                        Log.e(LOG_TAG, e.getMessage());

                    }

                }
            }

        } catch (NetworkConnectionNotFoundException e) {
            eventsPendingSend.set(0);
            Log.e(LOG_TAG, e.getMessage());
        }

    }

    private void cachedEventProcessed(int totalEvents, CallbackSendCachedEvents callbackSendCachedEvents) {
        eventsPendingSend.decrementAndGet();
        if (eventsPendingSend.get() <= 0) {
            callbackSendCachedEvents.onEndSendEvents(totalEvents);
        }
    }

}
