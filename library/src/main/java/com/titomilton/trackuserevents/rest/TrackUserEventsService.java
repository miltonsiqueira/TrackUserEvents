package com.titomilton.trackuserevents.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TrackUserEventsService {
    String BASE_URL = "http://api.dev.staging.crm.slace.me/v2/";

    @POST("events")
    Call<ResponseBody> sendEvent(@Header("D360-Api-Key") String apiKey, @Body EventRequest eventRequest);

}
