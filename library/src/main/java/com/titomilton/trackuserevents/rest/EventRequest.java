package com.titomilton.trackuserevents.rest;


import com.google.gson.annotations.SerializedName;

public class EventRequest {
    @SerializedName("meta")
    private EventRequestMeta meta;

    public EventRequest(){

    }
    public EventRequest(long eventNo, long localTimeStamp, String name, String connectionInfo){
        this.meta = new EventRequestMeta(eventNo, localTimeStamp, name, connectionInfo);
    }

    public EventRequestMeta getMeta() {
        return meta;
    }

    public void setMeta(EventRequestMeta meta) {
        this.meta = meta;
    }


}
