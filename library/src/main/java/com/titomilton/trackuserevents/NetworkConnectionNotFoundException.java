package com.titomilton.trackuserevents;

public class NetworkConnectionNotFoundException extends Throwable {
    private final String msg;

    public NetworkConnectionNotFoundException(){
        this("");
    }

    public NetworkConnectionNotFoundException(String msg){
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return "Network connection not found. " + msg;
    }
}
