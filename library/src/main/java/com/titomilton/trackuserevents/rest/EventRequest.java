package com.titomilton.trackuserevents.rest;


import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class EventRequest {
    @SerializedName("meta")
    private EventRequestMeta meta;

    @SerializedName("data")
    private Map<String, Object> data = new HashMap<>();

    public EventRequest() {

    }

    public EventRequest(long eventNo, long localTimeStamp, String name, String connectionInfo) {
        this.meta = new EventRequestMeta(eventNo, localTimeStamp, name, connectionInfo);
    }

    public EventRequestMeta getMeta() {
        return meta;
    }

    public void setMeta(EventRequestMeta meta) {
        this.meta = meta;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
}
