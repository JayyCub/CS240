package ReqRes;

import DAO_Models.User;
import DAO_Models.Event;
import DAO_Models.Person;

/**
 * Class to structure Load data
 */
public class LoadRequest {
    /** List of User object */
    private User[] users;
    /** List of Person object */
    private Person[] persons;
    /** List of Event objects */
    private Event[] events;

    /**
     * Constructor to create a structure of data to load into DB
     * @param newUsers List of Users
     * @param newPersons List of Persons
     * @param newEvents List of Events
     */
    public LoadRequest(User[] newUsers, Person[] newPersons, Event[] newEvents){
        this.users = newUsers;
        this.persons = newPersons;
        this.events = newEvents;
    }

    public User[] getUsers() { return users; }
    public Event[] getEvents() { return events; }
    public Person[] getPersons() { return persons; }
}
