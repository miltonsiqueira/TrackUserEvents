package com.titomilton.trackuserevents.rest;


import com.google.gson.annotations.SerializedName;

public class EventRequestMeta {

    public static final String CONNECTION_WIFI = "wifi";
    public static final String CONNECTION_MOBILE = "mobile";
    public static final String ELEMENT_META = "meta";
    public static final String ELEMENT_CONNECTION_INFO = "connectionInfo";
    public static final String ELEMENT_LOCAL_TIME_STAMP= "localTimeStamp";
    public static final String ELEMENT_NAME = "name";
    public static final String ELEMENT_EVENT_NO = "eventNo";

    @SerializedName(ELEMENT_EVENT_NO)
    private Long eventNo;

    @SerializedName(ELEMENT_LOCAL_TIME_STAMP)
    private Long localTimeStamp;

    @SerializedName(ELEMENT_NAME)
    private String name;

    @SerializedName(ELEMENT_CONNECTION_INFO)
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
