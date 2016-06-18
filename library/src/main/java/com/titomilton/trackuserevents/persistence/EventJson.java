package com.titomilton.trackuserevents.persistence;

public class EventJson {
    private int id;
    private String jsonEvent;

    public EventJson() {
    }

    public EventJson(String jsonEvent) {
        this.jsonEvent = jsonEvent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJsonEvent() {
        return jsonEvent;
    }

    public void setJsonEvent(String jsonEvent) {
        this.jsonEvent = jsonEvent;
    }

    @Override
    public String toString() {
        return "id:" + this.id + " jsonEvent:" + this.jsonEvent;
    }

}
