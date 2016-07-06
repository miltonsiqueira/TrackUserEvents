package com.titomilton.trackuserevents.rest;

public interface CallbackResponse {
    void onResponse(String responseBody, String requestBody);
    void onFailure(Throwable t);
    void onInvalidCodeResponse(int code, String responseBody, String requestBody);
    void onFailureReadingResponse(int code, String requestBody, Throwable t);
}
