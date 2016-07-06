package com.titomilton.trackuserevents.cache;

import com.titomilton.trackuserevents.rest.CallbackResponse;

public interface CallbackSendCachedEvents extends CallbackResponse {
    void onEndSendEvents(int totalProcessedEvents);
}
