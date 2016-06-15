package com.titomilton.trackuserevents;


public class InvalidEventRequestException extends Throwable {

    private String msg;

    public InvalidEventRequestException() {

    }

    public InvalidEventRequestException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return "Invalid request." + msg;
    }
}
