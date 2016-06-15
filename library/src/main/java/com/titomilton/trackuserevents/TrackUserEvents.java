package com.titomilton.trackuserevents;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.titomilton.trackuserevents.rest.EventRequest;
import com.titomilton.trackuserevents.rest.EventRequestMeta;
import com.titomilton.trackuserevents.rest.TrackUserEventsService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackUserEvents {

    private String LOG_TAG = TrackUserEvents.class.getSimpleName();

    private final String apiKey;


    private final Retrofit retrofit;

    public TrackUserEvents(String apiKey) {
        this.apiKey = apiKey;
        this.retrofit = new Retrofit.Builder()
                .baseUrl(TrackUserEventsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public void sendEvent(String eventName, Callback<ResponseBody> callback) throws InvalidEventRequestException {

        EventRequest eventRequest = new EventRequest();
        eventRequest.setMeta(new EventRequestMeta());
        EventRequestMeta meta = eventRequest.getMeta();
        meta.setName(eventName);
        meta.setEventNo(1l);
        meta.setLocalTimeStamp(System.currentTimeMillis() / 1000L);
        meta.setConnectionInfo("wifi");

        RequestValidator.validate(eventRequest);


        Log.d(LOG_TAG, "Sending request " + new GsonBuilder().create().toJson(eventRequest));

        TrackUserEventsService service = retrofit.create(TrackUserEventsService.class);

        Call<ResponseBody> call = service.sendEvent(this.apiKey, eventRequest);

        call.enqueue(callback);

    }


}
