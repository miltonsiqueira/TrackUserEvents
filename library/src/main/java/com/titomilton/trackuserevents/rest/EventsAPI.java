package com.titomilton.trackuserevents.rest;

public final class EventsAPI {
    public static String BASE_URL = "http://api.dev.staging.crm.slace.me/v2/";
    public static String EVENTS_METHOD = "events";
    public static String EVENTS_ENDPOINT = BASE_URL + EVENTS_METHOD;
    public static String KEY_HEADER = "D360-Api-Key";

    public static String REQUEST_DATA = "data";
    public static String REQUEST_META = "meta";
    public static String REQUEST_META_EVENTNO = "eventNo";
    public static String REQUEST_META_LOCAL_TIME_STAMP = "localTimeStamp";
    public static String REQUEST_META_NAME = "name";
    public static String REQUEST_META_CONNECTION_INFO = "connectionInfo";

    public static String REQUEST_META_CONNECTION_INFO_WIFI = "wifi";
    public static String REQUEST_META_CONNECTION_INFO_MOBILE = "mobile";

    private EventsAPI(){}

}
