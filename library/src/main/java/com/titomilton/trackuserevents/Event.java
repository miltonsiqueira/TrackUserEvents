package com.titomilton.trackuserevents;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.titomilton.trackuserevents.persistence.DataBaseHandler;
import com.titomilton.trackuserevents.persistence.EventJsonDao;
import com.titomilton.trackuserevents.persistence.EventJsonSQLiteDao;
import com.titomilton.trackuserevents.rest.EventRequest;
import com.titomilton.trackuserevents.rest.EventRequestMeta;
import com.titomilton.trackuserevents.rest.TrackUserEventsService;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Event {

    private final String LOG_TAG = Event.class.getSimpleName();
    private final EventRequest eventRequest;
    private final Context context;
    private final String apiKey;
    private final Retrofit retrofit;
    private final EventJsonDao eventJsonDao;

    private Event(String apiKey, Context context, Retrofit retrofit, EventRequest eventRequest) {
        this.apiKey = apiKey;
        this.context = context;
        this.retrofit = retrofit;
        this.eventJsonDao = new EventJsonSQLiteDao(new DataBaseHandler(context));
        this.eventRequest = eventRequest;
    }

    protected static Event createByName(String apiKey, String name, Context context, Retrofit retrofit) throws InvalidEventRequestException {

        Event event = new Event(apiKey, context, retrofit, new EventRequest());

        RequestValidator.validateName(name);
        event.eventRequest.setMeta(new EventRequestMeta());
        EventRequestMeta meta = event.eventRequest.getMeta();
        meta.setName(name);
        meta.setEventNo(event.getNextEventNo());
        meta.setLocalTimeStamp(System.currentTimeMillis() / 1000L);

        return event;

    }

    protected static Event createFromJson(String apiKey, String json, Context context, Retrofit retrofit) {
        EventRequest eventRequest = new GsonBuilder().create().fromJson(json, EventRequest.class);

        return new Event(apiKey, context, retrofit, eventRequest);
    }

    /**
     * Add new parameter
     *
     * @param key   Key of the parameter
     * @param value Value of the parameter
     * @return {@code Event}
     * @throws IllegalArgumentException if a key or value cannot be added to this {@code Map}.
     * @throws NullPointerException     if a key or value is {@code null} and this {@code Map} does not
     *                                  support {@code null} keys or values.
     */
    public Event addParameter(String key, Object value) {
        eventRequest.getData().put(key, value);
        return this;
    }

    public Event addParameters(Map<String, Object> parameters) {
        this.eventRequest.getData().putAll(parameters);
        return this;
    }

    public void send(final CallbackResponse callbackResponse) throws NetworkConnectionNotFoundException, InvalidEventRequestException {
        send(true, callbackResponse);
    }

    public void sendWithouCache(final CallbackResponse callbackResponse) throws InvalidEventRequestException, NetworkConnectionNotFoundException {
        send(false, callbackResponse);
    }


    protected void send(final boolean isCache, final CallbackResponse callbackResponse) throws InvalidEventRequestException, NetworkConnectionNotFoundException {

        try {

            String connectionType = ConnectionInfo.getConnectionType(context);
            eventRequest.getMeta().setConnectionInfo(connectionType);

            RequestValidator.validate(eventRequest);

            final String requestBody = new GsonBuilder().create().toJson(eventRequest);

            sendJson(isCache, requestBody, callbackResponse);


        } catch (NetworkConnectionNotFoundException e) {

            if (isCache) {
                String requestBody = new GsonBuilder().create().toJson(eventRequest);
                cacheEvent(requestBody);
            }

            throw new NetworkConnectionNotFoundException("Event was cached to send later.");
        }

    }


    private void sendJson(final boolean isCache, final String requestBody, final CallbackResponse callbackResponse) {
        Log.d(LOG_TAG, "Sending request apiKey=" + this.apiKey + " body="  + requestBody);

        TrackUserEventsService service = retrofit.create(TrackUserEventsService.class);

        //Call<ResponseBody> call = service.sendEvent(this.apiKey, eventRequest);

        RequestBody body = RequestBody.create(MediaType.parse("application/json"), requestBody);
        Call<ResponseBody> call = service.sendEventRawJSON(this.apiKey, body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                final int code = response.code();
                final boolean isPostedSuccessfully = isHttpCreatedCode(code);
                String responseBody;
                try {

                    if (response.isSuccessful()) {
                        responseBody = response.body().string();
                    } else {
                        responseBody = response.errorBody().string();

                    }

                    if (isPostedSuccessfully) {
                        callbackResponse.onResponse(code, responseBody, requestBody);
                    } else {

                        if (isCache) {
                            cacheEvent(requestBody);
                        }

                        callbackResponse.onFailedResponse(code, responseBody, requestBody);
                    }

                } catch (Exception e) {

                    if (isCache) {
                        cacheEvent(requestBody);
                    }

                    callbackResponse.onFailureReadingResponse(code, requestBody, e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callbackResponse.onFailure(t);
            }

        });


    }


    private void cacheEvent(String json) {
        this.eventJsonDao.addEventJson(this.apiKey, json);
    }

    private boolean isHttpCreatedCode(int code) {
        return code == HttpURLConnection.HTTP_CREATED;
    }

    private long getNextEventNo() {
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        long eventNo = sharedPrefs.getLong("eventNo", 0L);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong("eventNo", ++eventNo);
        editor.apply();
        return eventNo;
    }
//
//    private String addConnectionInfo(String json, String connectionInfo) throws JSONException {
//        JSONObject requestBody = new JSONObject(json);
//        return addConnectionInfo(requestBody, connectionInfo);
//    }

    private String addConnectionInfo(JSONObject trackedEvent, String connectionInfo) throws JSONException {
        JSONObject meta = (JSONObject) trackedEvent.get(EventRequestMeta.ELEMENT_META);
        meta.put(EventRequestMeta.ELEMENT_CONNECTION_INFO, connectionInfo);
        return trackedEvent.toString();
    }

}
