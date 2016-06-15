package com.titomilton.trackuserevents.rest;


import com.google.gson.annotations.SerializedName;

public class EventRequestMeta {

    public static final String CONNECTION_WIFI = "wifi";
    public static final String CONNECTION_MOBILE = "mobile";

    @SerializedName("eventNo")
    private Long eventNo;

    @SerializedName("localTimeStamp")
    private Long localTimeStamp;

    @SerializedName("name")
    private String name;

    @SerializedName("connectionInfo")
    private String connectionInfo;

    public EventRequestMeta() {

    }

    public EventRequestMeta(long eventNo, long localTimeStamp, String name, String connectionInfo) {
        this.eventNo = eventNo;
        this.localTimeStamp = localTimeStamp;
        this.name = name;
        this.connectionInfo = connectionInfo;
    }

    public Long getEventNo() {
        return eventNo;
    }

    public void setEventNo(Long eventNo) {
        this.eventNo = eventNo;
    }

    public Long getLocalTimeStamp() {
        return localTimeStamp;
    }

    public void setLocalTimeStamp(Long localTimeStamp) {
        this.localTimeStamp = localTimeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConnectionInfo() {
        return connectionInfo;
    }

    public void setConnectionInfo(String connectionInfo) {
        this.connectionInfo = connectionInfo;
    }
}
