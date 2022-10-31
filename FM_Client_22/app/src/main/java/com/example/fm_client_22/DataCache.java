package com.example.fm_client_22;

import java.util.List;
import java.util.Map;
import java.util.Set;

import DAO_Models.Event;
import DAO_Models.Person;
import ReqRes.ResultMessage;

public class DataCache {
    private static DataCache instance = new DataCache();

    public static DataCache getInstance(){
        return instance;
    }

    private DataCache(){
    }

    public String authToken = "";
    public Map<String, Person> people;
    public Map<String, Event> events;
    public Map<String, List<Event>> personEvents;
    public Set<String> paternalAncestors;
    public Set<String> maternalAncestors;
    public ResultMessage recentResult = new ResultMessage(null, null, null,
            null, null, null, null, null,
            null, null, null, null, null, null,
            null, null, null, null, "null", false);
}
