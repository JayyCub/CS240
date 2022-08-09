package ReqRes;

import DAO_Models.User;
import DAO_Models.Event;
import DAO_Models.Person;

public class LoadRequest {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    public LoadRequest(User[] newUsers, Person[] newPersons, Event[] newEvents){
        this.users = newUsers;
        this.persons = newPersons;
        this.events = newEvents;
    }

    public String writeString (){
        return String.valueOf(this.users.length);
    }

    public User[] getUsers() {
        return users;
    }
    public Event[] getEvents() {
        return events;
    }
    public Person[] getPersons() {
        return persons;
    }
}
