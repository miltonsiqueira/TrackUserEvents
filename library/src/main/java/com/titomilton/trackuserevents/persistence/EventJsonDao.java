package com.titomilton.trackuserevents.persistence;

import java.util.List;

public interface EventJsonDao {
    void addEventJson(String json);
    List<EventJson> getAllEventsJSON();
    void removeEventJson(int id);
}
