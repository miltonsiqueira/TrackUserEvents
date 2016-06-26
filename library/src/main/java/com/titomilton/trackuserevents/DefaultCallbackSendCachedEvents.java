package com.titomilton.trackuserevents;

public class DefaultCallbackSendCachedEvents implements CallbackSendCachedEvents {
    @Override
    public void onEndSendEvents(int totalProcessedEvents) {

    }

    @Override
    public void onResponse(int responseCode, String responseBody, String requestBody) {

    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void onFailedResponse(int code, String responseBody, String requestBody) {

    }

    @Override
    public void onFailureReadingResponse(int code, String requestBody, Throwable t) {

    }
}
