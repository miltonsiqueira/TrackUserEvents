package com.titomilton.trackuserevents;

public class InvalidApiKeyException extends InvalidEventRequestException {

    public InvalidApiKeyException() {
        super("Api key cannot be null or empty");
    }

}
