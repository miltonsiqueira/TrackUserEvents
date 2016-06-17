package com.titomilton.trackuserevents;

import android.content.Context;

import com.titomilton.trackuserevents.rest.TrackUserEventsService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TrackUserEvents {

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
        return new Event(this.apiKey, name, this.context, this.retrofit);
    }


}
