package com.titomilton.trackuserevents.rest;

public class ReadingResponseException extends Throwable {

    private final int responseCode;
    private final Throwable errorSource;

    public ReadingResponseException(final int responseCode, Throwable errorSource) {
        this.responseCode = responseCode;
        this.errorSource = errorSource;
    }

    @Override
    public String getMessage() {
        return "Failure while reading response. ResponseCode=" + responseCode + "Throwable=" + errorSource.getMessage();
    }

    public int getResponseCode() {
        return responseCode;
    }

    public Throwable getErrorSource() {
        return errorSource;
    }
}
