package Service;

import DAO_Models.Event;
import DAO_Models.Person;
import DAOs.EventDAO;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.*;

/**
 * Class to help with creating families to fill into the DB
 */
public class FillFamily {
    /** Person object, primary person */
    private Person child;
    /** Person object, paternal figure */
    private Person father;
    /** Person object, maternal figure */
    private Person mother;
    /** List of Events to be used */
    private ArrayList<Event> events = new ArrayList<>();
    /** Connection to DB */
    private Connection conn;

    /**
     * Constructor to establish child Person and connection to DB
     * @param child Person object
     * @param conn Connection to DB
     */
    FillFamily(Person child, Connection conn){
        this.conn = conn;
        this.child = child;
        Random randomNum = new Random();
        Gson gson = new Gson();

        String[] maleNames = null;
        String[] femaleNames = null;
        String[] surNames = null;

        try {
            String json = new Scanner(new File("json/mnames.json")).useDelimiter("\\A").next();
            maleNames = gson.fromJson(json, String[].class);

            json = new Scanner(new File("json/fnames.json")).useDelimiter("\\A").next();
            femaleNames = gson.fromJson(json, String[].class);

            json = new Scanner(new File("json/snames.json")).useDelimiter("\\A").next();
            surNames = gson.fromJson(json, String[].class);
        } catch (FileNotFoundException e) {
            System.out.println("Error reading json files");
        }

        assert maleNames != null;
        assert surNames != null;
        assert femaleNames != null;
        String fatherName = maleNames[randomNum.nextInt(maleNames.length)];
        String motherName = femaleNames[randomNum.nextInt(femaleNames.length)];
        String maidenName = surNames[randomNum.nextInt(surNames.length)];


        father = new Person(fatherName + "_" + child.getLastName() + "_" + UUID.randomUUID().toString().substring(0,5),
                child.getAssociatedUsername(), fatherName, child.getLastName(), "m",
                null, null, null);
        mother = new Person(motherName + "_" + maidenName + "_" + UUID.randomUUID().toString().substring(0,5),
                child.getAssociatedUsername(), motherName, maidenName, "f",
                null, null, null);

        father.setSpouseID(mother.getPersonID());
        mother.setSpouseID(father.getPersonID());

        child.setFatherID(father.getPersonID());
        child.setMotherID(mother.getPersonID());

        generateEvents();
    }

    /**
     * Create events based on JSON list of locations and age requirements
     */
    private void generateEvents() {
        Random rand = new Random();

        EventDAO eDao = new EventDAO(this.conn);
        Gson gson = new Gson();
        Location[] locations = null;

        // See if the child already has a birth year assigned.
        // Only the very first person (the user) should not have one at this point.
        Event birthEvent = null;
            birthEvent = eDao.find(child.getPersonID(), "Birth");

        int childBirthYear = (birthEvent == null) ? 2002 : birthEvent.getYear();

        // Assume data based on child's birth year
        int parentMarriageYear = childBirthYear - 3;
        int parentBirthYear = parentMarriageYear - 25;
        int parentDeathYear = parentBirthYear + 85;

        // Get all possible locations from json file
        try {
            String json = new Scanner(new File("json/locations.json")).useDelimiter("\\A").next();
            locations = gson.fromJson(json, Location[].class);
        } catch (FileNotFoundException e) {
            System.out.println("Error loading locations from json");
        }

        // BIRTH EVENTS
        assert locations != null;
        Location location = locations[rand.nextInt(locations.length)];
        events.add(new Event(child.getFirstName()+child.getLastName() + "_" + UUID.randomUUID().toString().substring(0,10),
                child.getAssociatedUsername(), child.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Birth", childBirthYear));

        location = locations[rand.nextInt(locations.length)];
        events.add(new Event(mother.getFirstName()+mother.getLastName() + "_" + UUID.randomUUID().toString().substring(0,10),
                mother.getAssociatedUsername(), mother.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Birth", parentBirthYear));

        location = locations[rand.nextInt(locations.length)];
        events.add(new Event(father.getFirstName()+father.getLastName() + "_" + UUID.randomUUID().toString().substring(0,10),
                father.getAssociatedUsername(), father.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Birth", parentBirthYear));

        // MARRIAGE EVENTS
        location = locations[rand.nextInt(locations.length)];
        events.add(new Event(father.getFirstName()+father.getLastName() + "_" + UUID.randomUUID().toString().substring(0,10),
                father.getAssociatedUsername(), father.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Marriage", parentMarriageYear));
        events.add(new Event(mother.getFirstName()+mother.getLastName() + "_" + UUID.randomUUID().toString().substring(0,10),
                mother.getAssociatedUsername(), mother.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Marriage", parentMarriageYear));

        // DEATH EVENTS
        location = locations[rand.nextInt(locations.length)];
        events.add(new Event(father.getFirstName()+father.getLastName()+"_"+UUID.randomUUID().toString().substring(0,10),
                father.getAssociatedUsername(), father.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Death", parentDeathYear));
        events.add(new Event(mother.getFirstName()+mother.getLastName()+"_"+UUID.randomUUID().toString().substring(0,10),
                mother.getAssociatedUsername(), mother.getPersonID(),
                location.getLatitude(), location.getLongitude(), location.getCountry(), location.getCity(),
                "Death", parentDeathYear));
    }

    public Person getChild() {
        return child;
    }

    public Person getFather() {
        return father;
    }

    public Person getMother() {
        return mother;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
