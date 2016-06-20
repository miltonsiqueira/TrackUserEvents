package com.titomilton.trackuserevents;

public interface CallbackSendCachedEvents extends CallbackResponse {
    void onEndSendEvents(int totalProcessedEvents);
}
