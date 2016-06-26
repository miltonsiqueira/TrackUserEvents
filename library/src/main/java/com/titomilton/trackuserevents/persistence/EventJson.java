package com.titomilton.trackuserevents.persistence;

public class EventJson {
    private int id;
    private String apiKey;
    private String jsonEvent;

    public EventJson() {
    }

    public EventJson(String apiKey, String jsonEvent) {
        this.apiKey = apiKey;
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

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return "id:" + this.id + " apiKey:" + apiKey + " jsonEvent:" + this.jsonEvent;
    }

}
