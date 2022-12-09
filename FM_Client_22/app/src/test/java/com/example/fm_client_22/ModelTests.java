package com.example.fm_client_22;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import DAO_Models.Event;
import DAO_Models.Person;
import ReqRes.RegisterRequest;

public class ModelTests {
    Person Jacob = new Person("JacobT", "jacobID", "Jacob",
            "Thomsen", "m", "DavidT", "DebbieT", "AlexB");
    Person Debbie =  new Person("DebbieT", "jacobID", "Debbie",
            "Tucker", "f", null, null, "DavidT");
    Person David =  new Person("DavidT", "jacobID", "David",
            "Thomsen", "m", null, null, "DebbieT");
    Person Alex =  new Person("AlexB", "jacobID", "Alexandra",
            "Burns", "f", null, null, "JacobT");
    Event event1 = new Event("event1", "JacobT", "JacobT", 0.0,
            0.0, "United States", "Winter Haven", "birth", 2002);
    Event event2 = new Event("event2", "JacobT", "JacobT", 0.0,
            0.0, "United States", "Winter Haven", "First Drone Flight", 2020);
    Event event3 = new Event("event3", "JacobT", "JacobT", 0.0,
            0.0, "United States", "Provo", "marriage", 2023);
    Event event4 = new Event("event4", "jacob", "", 0.0,
            0.0, "United States", "Washington D.C.", "marriage", 1988);
    Event event5 = new Event("event5", "jacob", "", 0.0,
            0.0, "United States", "Anaheim", "birth", 1967);
    private final RegisterRequest regReq = new RegisterRequest("jacob", "pass",
            "email", "Jacob", "Thomsen", 'm');


    @Test
    public void Relationship_Calc_Test() {
        System.out.println("STARTING RELATIONSHIP CALC TEST...");
        DataUtility util = new DataUtility();
        System.out.println("Checking mother relationship...");
        String motherString = util.relationship(Jacob, Debbie);
        assertEquals("Mother", motherString);
        System.out.println("Checking father relationship...");
        String fatherString = util.relationship(Jacob, David);
        assertEquals("Father", fatherString);
        System.out.println("Checking spouse relationship...");
        String spouseString = util.relationship(Jacob, Alex);
        assertEquals("Spouse", spouseString);
        System.out.println("Checking child relationship...");
        String childString = util.relationship(Debbie, Jacob);
        assertEquals("Child", childString);
        System.out.println("Success!\n\n");
    }

    @Test
    public void Sort_Events_Test(){
        System.out.println("STARTING EVENT SORTING TEST...\nAdding Events");
        List<Event> basePersonEvents = new ArrayList<>();
        basePersonEvents.add(event2);
        basePersonEvents.add(event1);
        basePersonEvents.add(event3);
        DataUtility utility = new DataUtility();
        System.out.println("Sorting...");
        Event[] sortedEvents = utility.sortEvents(basePersonEvents);
        boolean firstCheck = sortedEvents[0].getYear() < sortedEvents[1].getYear();
        boolean secondCheck = sortedEvents[1].getYear() < sortedEvents[2].getYear();
        assertTrue(firstCheck);
        assertTrue(secondCheck);
        System.out.println("Success!\n\n");
    }

    @Test
    public void Filter_Check_Test(){
        System.out.println("STARTING FILTER CHECK TEST...\nCreating new user and adding events to test");
        DataCache dataCache = DataCache.getInstance();
        ServerProxy serverProxy = new ServerProxy("localhost", "8080");
        assertTrue(serverProxy.clearDatabase());
        assertTrue(serverProxy.register(regReq));
        serverProxy.getPersonName();
        serverProxy.getAllPersonData();
        event4.setPersonID(dataCache.currentPerson.getMotherID());
        event5.setPersonID(dataCache.currentPerson.getFatherID());
        // Female event on mother's side
        dataCache.events.put(event4.getEventID(), event4);
        // Male event on father's side
        dataCache.events.put(event5.getEventID(), event5);

        // Filter out nothing
        System.out.println("Passing through filter, no restrictions");
        assertTrue(dataCache.checkFilter(event4.getPersonID()));
        assertTrue(dataCache.checkFilter(event5.getPersonID()));

        //Filter out mother's side
        System.out.println("Filtering out maternal events...");
        dataCache.motherSetting = false;
        assertFalse(dataCache.checkFilter(event4.getPersonID()));
        assertTrue(dataCache.checkFilter(event5.getPersonID()));
        System.out.println("Pass!");
        dataCache.motherSetting = true;

        //Filter out father's side
        System.out.println("Filtering out paternal events...");
        dataCache.fatherSetting = false;
        assertTrue(dataCache.checkFilter(event4.getPersonID()));
        assertFalse(dataCache.checkFilter(event5.getPersonID()));
        System.out.println("Pass!");
        dataCache.fatherSetting = true;

        //Filter out female events
        System.out.println("Filtering out female events...");
        dataCache.femaleSetting = false;
        assertFalse(dataCache.checkFilter(event4.getPersonID()));
        assertTrue(dataCache.checkFilter(event5.getPersonID()));
        System.out.println("Pass!");
        dataCache.femaleSetting = true;

        //Filter out male events
        System.out.println("Filtering out male events...");
        dataCache.maleSetting = false;
        assertTrue(dataCache.checkFilter(event4.getPersonID()));
        assertFalse(dataCache.checkFilter(event5.getPersonID()));
        System.out.println("Pass!");
        dataCache.maleSetting = true;

        System.out.println("Success!\n\n");
    }
}