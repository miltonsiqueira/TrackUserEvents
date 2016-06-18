package com.titomilton.trackuserevents;

import com.titomilton.trackuserevents.rest.EventRequest;
import com.titomilton.trackuserevents.rest.EventRequestMeta;

import org.json.JSONException;
import org.json.JSONObject;


public final class RequestValidator {

    public static final String EVENT_NAME_PATTERN = "^ev_[A-Za-z0-9]+";

    public static void validate(EventRequest eventRequest) throws InvalidEventRequestException {
        if (eventRequest == null) {
            throw new InvalidEventRequestException("Event request cannot be bull");
        }
        if (eventRequest.getMeta() == null) {
            throw new InvalidEventRequestException("Meta event request cannot be null");
        }

        // Validate eventNo
        EventRequestMeta meta = eventRequest.getMeta();
        validateEventNo(meta.getEventNo());

        // Validate localTimeStamp
        validateLocalTimeStamp(meta.getLocalTimeStamp());

        // Validate event name
        validateName(meta.getName());

        // Validate type of the connection
        validateConnectionInfo(meta.getConnectionInfo());
    }

    public static void validateConnectionInfo(String connectionInfo) throws InvalidEventRequestException {
        if (connectionInfo == null
                ||
                !(connectionInfo.equals(EventRequestMeta.CONNECTION_MOBILE)
                        || connectionInfo.equals(EventRequestMeta.CONNECTION_WIFI))) {
            throw new InvalidEventRequestException("Invalid connection info: " + connectionInfo +
                    " . It must be 'wifi' or 'mobile'");
        }
    }

    public static void validateLocalTimeStamp(Long localTimeStamp) throws InvalidEventRequestException {
        if (localTimeStamp == null || localTimeStamp < 1) {
            throw new InvalidEventRequestException("Invalid localtimeStamp:" + localTimeStamp);
        }
    }

    public static void validateEventNo(Long eventNo) throws InvalidEventRequestException {
        if (eventNo == null || eventNo < 1) {
            throw new InvalidEventRequestException("EventNo cannot be null or less than 1: eventNo=" + eventNo);
        }
    }

    public static void validateName(String name) throws InvalidEventRequestException {
        if (name == null || !name.matches(EVENT_NAME_PATTERN)) {
            throw new InvalidEventRequestException("Invalid event name '" + name +
                    "'. Please, use the pattern '" + RequestValidator.EVENT_NAME_PATTERN + "'");
        }
    }

    // TODO create unit test of validateTrackedEventJson
    public static void validateTrackedEventJson(String trackedEventJson) throws InvalidEventRequestException {
        JSONObject trackedJsonObject;
        try {
            trackedJsonObject = new JSONObject(trackedEventJson);
        } catch (JSONException e) {
            throw new InvalidEventRequestJsonException(trackedEventJson, e.getMessage());
        }
        validateTrackedEventJSONObject(trackedJsonObject);
    }

    // TODO create unit test of validateTrackedEventJSONObject
    public static void validateTrackedEventJSONObject(JSONObject trackedEvent) throws InvalidEventRequestException {

        String attribute = "";

        try {

            attribute = EventRequestMeta.ELEMENT_META;
            JSONObject meta = (JSONObject) trackedEvent.get(EventRequestMeta.ELEMENT_META);

            validateEventNo(meta.getLong(EventRequestMeta.ELEMENT_EVENT_NO));
            validateName(meta.getString(EventRequestMeta.ELEMENT_NAME));
            validateLocalTimeStamp(meta.getLong(EventRequestMeta.ELEMENT_LOCAL_TIME_STAMP));
            validateConnectionInfo(meta.getString(EventRequestMeta.ELEMENT_CONNECTION_INFO));

        } catch (JSONException e) {
            throw new InvalidEventRequestJsonException(
                    attribute, trackedEvent.toString(), e.getMessage());
        }


    }


}
