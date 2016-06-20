package com.titomilton.trackuserevents;


class InvalidEventRequestJsonException extends InvalidEventRequestException {

    public InvalidEventRequestJsonException(String json, String msg) {
        this("", json, msg);
    }

    public InvalidEventRequestJsonException(String attribute, String json, String msg) {
        super("Request body json invalid. " +
                (attribute == null || attribute.isEmpty() ? "" :  " Attribute " + attribute + " not found in the json. ") +
                " Json=" + json + "."
                + msg);
    }

}
