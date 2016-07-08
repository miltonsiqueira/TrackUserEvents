package com.titomilton.trackuserevents.rest;

public class DefaultCallbackResponse implements CallbackResponse{
    @Override
    public void onResponse(String responseBody, String requestBody) {

    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void onInvalidCodeResponse(int code, String responseBody, String requestBody) {

    }

    @Override
    public void onFailureReadingResponse(int code, String requestBody, Throwable t) {

    }
}
