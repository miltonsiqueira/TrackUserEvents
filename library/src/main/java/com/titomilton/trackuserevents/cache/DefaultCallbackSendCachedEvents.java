package com.titomilton.trackuserevents.cache;

import com.titomilton.trackuserevents.cache.CallbackSendCachedEvents;

public class DefaultCallbackSendCachedEvents implements CallbackSendCachedEvents {
    @Override
    public void onEndSendEvents(int totalProcessedEvents) {

    }

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
