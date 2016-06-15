package com.titomilton.trackuserevents;

public class NetworkConnectionNotFoundException extends Throwable {
    @Override
    public String getMessage() {
        return "Network connection not found.";
    }
}
