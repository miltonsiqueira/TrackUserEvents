package com.titomilton.trackuserevents;

import com.titomilton.trackuserevents.rest.EventRequest;

import org.junit.Test;


public class RequestValidatorTest {
    private final long EVENT_NO = 1L;
    private final long LOCAL_TIME_STAMP = 1L;
    private final String MOBILE = "mobile";
    private final String WIFI = "wifi";
    private final String EVENT_NAME = "ev_MyCustomEvent";


    @Test
    public void validEventName() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidEventName() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, "ev1_MyCustomEvent", WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameUpperCase() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME.toUpperCase(), WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameEmpty() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, "", WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameNull() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, null, WIFI));
    }

    @Test
    public void validEventNo() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(2L, LOCAL_TIME_STAMP, EVENT_NAME, WIFI));
        RequestValidator.validate(new EventRequest(100L, LOCAL_TIME_STAMP, EVENT_NAME, MOBILE));
        RequestValidator.validate(new EventRequest(1000L, LOCAL_TIME_STAMP, EVENT_NAME, WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNoIs0() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(0, LOCAL_TIME_STAMP, EVENT_NAME, WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNoLessThan0() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(-1L, LOCAL_TIME_STAMP, EVENT_NAME, WIFI));
    }

    @Test
    public void validLocalTimeStamp() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, 12321L, EVENT_NAME, WIFI));
        RequestValidator.validate(new EventRequest(EVENT_NO, (System.currentTimeMillis() / 1000L), EVENT_NAME, WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void localTimeStampIs0() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, 0L, EVENT_NAME, WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void localTimeStampLessThan0() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, -1L, EVENT_NAME, WIFI));
    }

    @Test
    public void validConnectionInfo() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, WIFI));
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, MOBILE));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidConnectionInfo() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, "unknown"));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void connectionInfoIsNull() throws InvalidEventRequestException{
        RequestValidator.validate(new EventRequest(EVENT_NO, 1L, EVENT_NAME, null));
    }



}