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

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Event {

    private final String LOG_TAG = Event.class.getSimpleName();
    private final EventRequest eventRequest;
    private final Context context;
    private final String apiKey;
    private final Retrofit retrofit;
    private final EventJsonDao eventJsonDao;

    protected Event(String apiKey, String name, Context context, Retrofit retrofit) throws InvalidEventRequestException {
        this.apiKey = apiKey;
        this.context = context;
        this.retrofit = retrofit;
        this.eventJsonDao = new EventJsonSQLiteDao(new DataBaseHandler(context));
        this.eventRequest = new EventRequest();
        RequestValidator.validateName(name);
        eventRequest.setMeta(new EventRequestMeta());
        EventRequestMeta meta = eventRequest.getMeta();
        meta.setName(name);
        meta.setEventNo(getNextEventNo());
        meta.setLocalTimeStamp(System.currentTimeMillis() / 1000L);

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

    public Map<String, Object> getParameters() {
        return eventRequest.getData();
    }

    public Event setParameters(Map<String, Object> parameters) {
        this.eventRequest.setData(parameters);
        return this;
    }

    public void send(Callback<ResponseBody> callback) throws InvalidEventRequestException, JSONException, NetworkConnectionNotFoundException {

        //eventRequest.getMeta().setConnectionInfo(ConnectionInfo.getConnectionType(context));

        String json = new GsonBuilder().create().toJson(eventRequest);

        String connectionType;
        try {
            connectionType = ConnectionInfo.getConnectionType(context);
            json = addConnectionInfo(json, connectionType);
            RequestValidator.validate(eventRequest);
            Log.d(LOG_TAG, "Sending request " + json);

            TrackUserEventsService service = retrofit.create(TrackUserEventsService.class);

            //Call<ResponseBody> call = service.sendEvent(this.apiKey, eventRequest);

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
            Call<ResponseBody> call = service.sendEventRawJSON(this.apiKey, body);

            call.enqueue(callback);
        } catch (NetworkConnectionNotFoundException e) {
            cacheEvent(json);
            throw new NetworkConnectionNotFoundException("Event was cached to send later.");
        }

    }

    private void cacheEvent(String json) {
        this.eventJsonDao.addEventJson(json);
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

    private String addConnectionInfo(String json, String connectionInfo) throws JSONException {
        JSONObject requestBody = new JSONObject(json);
        return addConnectionInfo(requestBody, connectionInfo);
    }

    private String addConnectionInfo(JSONObject trackedEvent, String connectionInfo) throws JSONException {
        JSONObject meta = (JSONObject) trackedEvent.get(EventRequestMeta.ELEMENT_META);
        meta.put(EventRequestMeta.ELEMENT_CONNECTION_INFO, connectionInfo);
        return trackedEvent.toString();
    }

}
