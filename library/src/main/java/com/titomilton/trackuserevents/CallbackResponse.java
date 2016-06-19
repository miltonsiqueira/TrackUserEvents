package com.titomilton.trackuserevents;

public interface CallbackResponse {
    void onResponse(int responseCode, String responseBody, String requestBody);
    void onFailure(Throwable t);
    void onFailedResponse(int code, String responseBody, String requestBody);
    void onFailureReadingResponse(int code, String requestBody, Throwable t);
}
