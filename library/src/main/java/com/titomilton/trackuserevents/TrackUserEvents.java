package com.titomilton.trackuserevents;

import android.content.Context;
import android.util.Log;

import com.titomilton.trackuserevents.persistence.DataBaseHandler;
import com.titomilton.trackuserevents.persistence.EventJson;
import com.titomilton.trackuserevents.persistence.EventJsonDao;
import com.titomilton.trackuserevents.persistence.EventJsonSQLiteDao;
import com.titomilton.trackuserevents.rest.TrackUserEventsService;

import org.json.JSONException;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackUserEvents {

    private static AtomicInteger eventsPendingSend = new AtomicInteger(0);
    private final String LOG_TAG = TrackUserEvents.class.getSimpleName();
    private final String apiKey;
    private final Context context;
    private final Retrofit retrofit;

    public TrackUserEvents(String apiKey, Context context) {
        this.apiKey = apiKey;
        this.context = context;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(TrackUserEventsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Event newEvent(String name) throws InvalidEventRequestException {
        return Event.createFromName(this.apiKey, name, this.context, this.retrofit);
    }

    public List<EventJson> getCachedEvents() {
        final EventJsonDao eventJsonDao = new EventJsonSQLiteDao(new DataBaseHandler(this.context));
        return eventJsonDao.getAllEventsJSON();
    }


    public void sendCachedEvents(final CallbackSendCachedEvents callbackSendCachedEvents) throws NetworkConnectionNotFoundException, CachedEventsAreBeingSendException {

        if (eventsPendingSend.get() > 0) {
            throw new CachedEventsAreBeingSendException(eventsPendingSend.get());
        }

        final EventJsonDao eventJsonDao = new EventJsonSQLiteDao(new DataBaseHandler(this.context));
        List<EventJson> eventJsonList = eventJsonDao.getAllEventsJSON();

        eventsPendingSend.set(eventJsonList.size());

        if (eventJsonList.isEmpty()) {

            callbackSendCachedEvents.onEndSendEvents(0);

        } else {

            final int totalEvents = eventJsonList.size();

            for (EventJson eventJson : eventJsonList) {
                final int id = eventJson.getId();

                Event event = Event.createFromJson(this.apiKey, eventJson.getJsonEvent(), this.context, this.retrofit);

                try {
                    event.sendJsonAddingConnectionInfo(false, eventJson.getJsonEvent(), new CallbackResponse() {
                        @Override
                        public void onResponse(int responseCode, String responseBody, String requestBody) {
                            eventJsonDao.removeEventJson(id);
                            callbackSendCachedEvents.onResponse(responseCode, responseBody, requestBody);
                            cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            callbackSendCachedEvents.onFailure(t);
                            cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                        }

                        @Override
                        public void onFailedResponse(int code, String responseBody, String requestBody) {
                            callbackSendCachedEvents.onFailedResponse(code, responseBody, requestBody);
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
                } catch (JSONException e) {
                    cachedEventProcessed(totalEvents, callbackSendCachedEvents);
                    Log.e(LOG_TAG, "Problem for parse json during send cached events. json=" + eventJson.getJsonEvent());
                }

            }
        }

    }

    private void cachedEventProcessed(int totalEvents, CallbackSendCachedEvents callbackSendCachedEvents) {
        eventsPendingSend.decrementAndGet();
        if (eventsPendingSend.get() <= 0) {
            callbackSendCachedEvents.onEndSendEvents(totalEvents);
        }
    }

}
