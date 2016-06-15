package com.titomilton.trackuserevents;

import com.titomilton.trackuserevents.rest.EventRequest;
import com.titomilton.trackuserevents.rest.EventRequestMeta;


public final class RequestValidator {

    public static final String EVENT_NAME_PATTERN = "^ev_[A-Za-z0-9]+";

    public static void validate(EventRequest eventRequest) throws InvalidEventRequestException {
        if (eventRequest == null) {
            throw new InvalidEventRequestException("Event request cannot be bull");
        }
        if (eventRequest.getMeta() == null) {
            throw new InvalidEventRequestException("Meta event request cannot be null");
        }
        EventRequestMeta meta = eventRequest.getMeta();
        if (meta.getEventNo() == null || meta.getEventNo().longValue() < 1) {
            throw new InvalidEventRequestException("EventNo cannot be null or less than 1: eventNo=" + meta.getEventNo());
        }
        if (meta.getLocalTimeStamp() == null || meta.getLocalTimeStamp() < 1) {
            throw new InvalidEventRequestException("Invalid localtimeStamp:" + meta.getLocalTimeStamp());
        }
        if (meta.getName() == null || !meta.getName().matches(EVENT_NAME_PATTERN)) {
            throw new InvalidEventRequestException(" Invalid event name '" + meta.getName() +
                    "'. Please, use the pattern '" + RequestValidator.EVENT_NAME_PATTERN + "'");
        }
        if (meta.getConnectionInfo() == null
                ||
                !(meta.getConnectionInfo().equals(EventRequestMeta.CONNECTION_MOBILE)
                        || meta.getConnectionInfo().equals(EventRequestMeta.CONNECTION_WIFI))) {
            throw new InvalidEventRequestException("Invalid connection info: " + meta.getConnectionInfo() +
                    " . It must be 'wifi' or 'mobile'");
        }
    }

}
