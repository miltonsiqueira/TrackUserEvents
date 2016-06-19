package com.titomilton.trackuserevents;

public interface CallbackResponse {
    void onResponse(int responseCode, String responseBody, String requestBody, String exceptionResponseBody);
    void onFailure(Throwable t);
}
