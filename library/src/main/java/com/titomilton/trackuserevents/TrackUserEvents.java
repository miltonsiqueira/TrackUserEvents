package com.titomilton.trackuserevents;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.titomilton.trackuserevents.rest.EventRequest;
import com.titomilton.trackuserevents.rest.EventRequestMeta;
import com.titomilton.trackuserevents.rest.TrackUserEventsService;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackUserEvents {

    private String LOG_TAG = TrackUserEvents.class.getSimpleName();

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

    public void sendEvent(String eventName, Map<String, Object> keyPairData, Callback<ResponseBody>callback) throws InvalidEventRequestException, NetworkConnectionNotFoundException {

        EventRequest eventRequest = new EventRequest();
        if(keyPairData != null && !keyPairData.isEmpty()){
            eventRequest.setData(keyPairData);
        }
        eventRequest.setMeta(new EventRequestMeta());
        EventRequestMeta meta = eventRequest.getMeta();
        meta.setName(eventName);
        meta.setEventNo(getNextEventNo());
        meta.setLocalTimeStamp(System.currentTimeMillis() / 1000L);
        meta.setConnectionInfo(ConnectionInfo.getConnectionType(context));


        RequestValidator.validate(eventRequest);


        Log.d(LOG_TAG, "Sending request " + new GsonBuilder().create().toJson(eventRequest));

        TrackUserEventsService service = retrofit.create(TrackUserEventsService.class);

        Call<ResponseBody> call = service.sendEvent(this.apiKey, eventRequest);

        call.enqueue(callback);

    }

    private long getNextEventNo(){
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        long eventNo = sharedPrefs.getLong("eventNo", 0l);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong("eventNo", ++eventNo);
        editor.commit();
        return eventNo;
    }


}
