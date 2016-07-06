package com.titomilton.trackuserevents.rest;

public class InvalidCodeResponseException extends Throwable {

    private final int responseCode;
    private final String responseBody;

    public InvalidCodeResponseException(int responseCode, String responseBody) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String getMessage() {
        return "Invalid code response " + this.responseCode + ". " +
                "ResponseBody=" + responseBody;
    }

}
