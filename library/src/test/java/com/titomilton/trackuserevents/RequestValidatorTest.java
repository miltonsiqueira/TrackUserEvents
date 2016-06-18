package com.titomilton.trackuserevents;

import com.titomilton.trackuserevents.rest.EventRequest;
import com.titomilton.trackuserevents.rest.EventRequestMeta;

import org.junit.Test;


public class RequestValidatorTest {
    private final long EVENT_NO = 1L;
    private final long LOCAL_TIME_STAMP = 1L;
    private final String EVENT_NAME = "ev_MyCustomEvent";


    @Test
    public void validEventNameEventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_MOBILE));
    }

    @Test
    public void validEventName() throws InvalidEventRequestException {
        RequestValidator.validateName(EVENT_NAME);
        RequestValidator.validateName("ev_123MyCustomEvent1");
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidEventNameEventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, "ev1_MyCustomEvent", EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidEventName() throws InvalidEventRequestException {
        RequestValidator.validateName("ev1_MyCustomEvent");
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidEventName2() throws InvalidEventRequestException {
        RequestValidator.validateName("ev1_213MyCustomEvent");
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidEventName3() throws InvalidEventRequestException {
        RequestValidator.validateName("ev1_MyCust2332omEvent");
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidEventName4() throws InvalidEventRequestException {
        RequestValidator.validateName("ev1_myCustomEvent");
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameUpperCaseEventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME.toUpperCase(), EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameUpperCase() throws InvalidEventRequestException {
        RequestValidator.validateName(EVENT_NAME.toUpperCase());
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameEmptyEventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, "", EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameEmpty() throws InvalidEventRequestException {
        RequestValidator.validateName("");
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameNullEventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, null, EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNameNull() throws InvalidEventRequestException {
        RequestValidator.validateName(null);
    }

    @Test
    public void validEventNo() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(2L, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
        RequestValidator.validate(new EventRequest(100L, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_MOBILE));
        RequestValidator.validate(new EventRequest(1000L, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
        RequestValidator.validateEventNo(2L);
        RequestValidator.validateEventNo(100L);
        RequestValidator.validateEventNo(1000L);
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNoIs0EventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(0, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNoIs0() throws InvalidEventRequestException {
        RequestValidator.validateEventNo(0L);
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNoLessThan0EventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(-1L, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void eventNoLessThan0() throws InvalidEventRequestException {
        RequestValidator.validateEventNo(-1L);
    }

    @Test
    public void validLocalTimeStamp() throws InvalidEventRequestException {
        long currentLocalTimeStamp = (System.currentTimeMillis() / 1000L);
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
        RequestValidator.validate(new EventRequest(EVENT_NO, currentLocalTimeStamp, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
        RequestValidator.validateLocalTimeStamp(LOCAL_TIME_STAMP);
        RequestValidator.validateLocalTimeStamp(currentLocalTimeStamp);
    }

    @Test(expected = InvalidEventRequestException.class)
    public void localTimeStampIs0EventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, 0L, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void localTimeStampIs0() throws InvalidEventRequestException {
        RequestValidator.validateLocalTimeStamp(0L);
    }

    @Test(expected = InvalidEventRequestException.class)
    public void localTimeStampLessThan0EventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, -1L, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void localTimeStampLessThan0() throws InvalidEventRequestException {
        RequestValidator.validateLocalTimeStamp(-1L);
    }

    @Test
    public void validConnectionInfo() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_WIFI));
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, EventRequestMeta.CONNECTION_MOBILE));
        RequestValidator.validateConnectionInfo(EventRequestMeta.CONNECTION_WIFI);
        RequestValidator.validateConnectionInfo(EventRequestMeta.CONNECTION_MOBILE);
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidConnectionInfoEventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, LOCAL_TIME_STAMP, EVENT_NAME, "unknown"));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void invalidConnectionInfo() throws InvalidEventRequestException {
        RequestValidator.validateConnectionInfo("unknown");
    }

    @Test(expected = InvalidEventRequestException.class)
    public void connectionInfoIsNullEventRequest() throws InvalidEventRequestException {
        RequestValidator.validate(new EventRequest(EVENT_NO, 1L, EVENT_NAME, null));
    }

    @Test(expected = InvalidEventRequestException.class)
    public void connectionInfoIsNull() throws InvalidEventRequestException {
        RequestValidator.validateConnectionInfo(null);
    }


}