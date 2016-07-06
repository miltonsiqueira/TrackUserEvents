package com.titomilton.trackuserevents.cache;

public class CachedEventsAreBeingSendException extends Throwable {
    private int totalPending;

    public CachedEventsAreBeingSendException(int totalPending) {
        this.totalPending = totalPending;
    }

    @Override
    public String getMessage() {
        return "There are " + totalPending + " events being sent.";
    }

}
