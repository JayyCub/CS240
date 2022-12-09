package com.example.fm_client_22;

import java.util.List;
import java.util.Objects;

import DAO_Models.Event;
import DAO_Models.Person;

public class DataUtility {
    DataCache dataCache = DataCache.getInstance();

    public DataUtility(){}

    public void addPaternalSide(Person parent) {
        dataCache.paternalAncestors.add(parent.getPersonID());
        try {
            dataCache.paternalAncestors.add(Objects.requireNonNull(dataCache.people.get(parent.getFatherID())).getPersonID());
            addPaternalSide(dataCache.people.get(parent.getFatherID()));
        } catch (NullPointerException e) {
            return;
        }
        try {
            dataCache.paternalAncestors
                    .add(Objects.requireNonNull(dataCache.people.get(parent.getMotherID())).getPersonID());
            addPaternalSide(dataCache.people.get(parent.getMotherID()));
        } catch (NullPointerException e) {
            return;
        }
    }

    public void addMaternalSide(Person parent) {
        dataCache.maternalAncestors.add(parent.getPersonID());
        try {
            dataCache.maternalAncestors.add(Objects.requireNonNull(dataCache.people.get(parent.getFatherID())).getPersonID());
            addMaternalSide(dataCache.people.get(parent.getFatherID()));
        } catch (NullPointerException e) {
            return;
        }
        try {
            dataCache.maternalAncestors
                    .add(Objects.requireNonNull(dataCache.people.get(parent.getMotherID())).getPersonID());
            addMaternalSide(dataCache.people.get(parent.getMotherID()));
        } catch (NullPointerException e) {
            return;
        }
    }

    public int getMarker(String type){
        type = type.toLowerCase();
        switch (type) {
            case "birth":
                return R.drawable.birthmarker;
            case "marriage":
                return R.drawable.marriagemarker;
            case "death":
                return R.drawable.deathmarker;
        }

        if (type.charAt(0) == 'a' | type.charAt(0) == 'b')
            return R.drawable.blue2marker;
        else if (type.charAt(0) == 'c' | type.charAt(0) == 'd')
            return R.drawable.bluemarker;
        else if (type.charAt(0) == 'e' | type.charAt(0) == 'f')
            return R.drawable.greenmarker;
        else if (type.charAt(0) == 'g' | type.charAt(0) == 'h')
            return R.drawable.lightbluemarker;
        else if (type.charAt(0) == 'i' | type.charAt(0) == 'j')
            return R.drawable.lightpurplemarker;
        else if (type.charAt(0) == 'k' | type.charAt(0) == 'l')
            return R.drawable.limemarker;
        else if (type.charAt(0) == 'm' | type.charAt(0) == 'n')
            return R.drawable.navymarker;
        else if (type.charAt(0) == 'o' | type.charAt(0) == 'p')
            return R.drawable.orangemarker;
        else if (type.charAt(0) == 'q' | type.charAt(0) == 'r')
            return R.drawable.pinkmarker;
        else if (type.charAt(0) == 's' | type.charAt(0) == 't')
            return R.drawable.purplemarker;
        else if (type.charAt(0) == 'u' | type.charAt(0) == 'v')
            return R.drawable.redmarker;
        else if (type.charAt(0) == 'w' | type.charAt(0) == 'x')
            return R.drawable.tealmarker;
        else if (type.charAt(0) == 'y' | type.charAt(0) == 'z')
            return R.drawable.yellowmarker;

        return R.drawable.redmarker;
    }

    public String relationship(Person primaryPerson, Person relative){
        if (Objects.equals(relative.getSpouseID(), primaryPerson.getPersonID())){
            return "Spouse";
        } else if (Objects.equals(relative.getMotherID(), primaryPerson.getPersonID())
                || Objects.equals(relative.getFatherID(), primaryPerson.getPersonID())) {
            return "Child";
        } else if (Objects.equals(primaryPerson.getFatherID(), relative.getPersonID())){
            return "Father";
        } else if (Objects.equals(primaryPerson.getMotherID(), relative.getPersonID())){
            return "Mother";
        }
        return "Relative";
    }

    public Event[] sortEvents(List<Event> basePersonEvents){
        Event[] sortedEvents = basePersonEvents.toArray(new Event[0]);
        Event temp;
        for (int i = 0; i < sortedEvents.length; i++){
            for (int j = 0; j < sortedEvents.length - 1; j++){
                if (sortedEvents[j+1].getYear() < sortedEvents[j].getYear()){
                    temp = sortedEvents[j+1];
                    sortedEvents[j+1] = sortedEvents[j];
                    sortedEvents[j] = temp;
                }
            }
        }
        return sortedEvents;
    }
}
