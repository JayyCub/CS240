package Service;

import DAO_Models.AuthToken;
import DAO_Models.Person;
import DAOs.AuthTokenDAO;
import DAOs.DatabaseUtil;
import DAOs.PersonDAO;
import ReqRes.ResultMessage;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

/**
 * Class that services the request for a specific user or family of users
 */
public class PersonService {
    /** String with the authToken ID used for verification */
    private String authToken;

    /** String with the personID to be used for DB lookup */
    private String personID;

    /**
     * Constructor to sets the value of the authToken ID string
     * @param authToken
     */
    public PersonService(String authToken){
        this.authToken = authToken;
    }

    // Do this by getting the user's personID based on the given authtoken
    // Then, find all people with the associated username

    /**
     * This method uses the Authtoken's associated username to find all people in the database with the same
     * associated username
     * @return Message, success: list of all people, or failure message
     */
    public ResultMessage findFamily(){
        System.out.println("Finding people associated to authToken " + authToken);
        DatabaseUtil DB = new DatabaseUtil();
        Connection conn = DB.open();
        AuthToken authTokenObj;
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        if (!authTokenDAO.checkIfTokenExistsFromToken(authToken)){
            DB.close(false);
            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: AuthToken not found.", false);
        }
        authTokenObj = authTokenDAO.retrieveTokenFromToken(authToken);
        PersonDAO personDAO = new PersonDAO(conn);

        List<Person> people = personDAO.getAssocatedPeople(authTokenObj.getUsername());

        Person[] persons = new Person[people.size()];
        for (int i = 0; i < people.size(); i++){
            persons[i] = people.get(i);
        }
        DB.close(false);

        return new ResultMessage(null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, persons, null, true);
    }

    /**
     * Using the authToken's associated username and the given personID, this message finds the user's relative
     * @param personID String personID used to search DB
     * @return Message with found Person's data or an error message
     */
    // Find person in database with given personID, only if they are associated to authToken person
    public ResultMessage findPerson(String personID){
        System.out.println("Finding person with ID " + personID + ", authToken " + authToken);
        DatabaseUtil DB = new DatabaseUtil();
        Connection conn = DB.open();
        AuthToken authTokenObj;
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        if (!authTokenDAO.checkIfTokenExistsFromToken(authToken)){
            DB.close(false);
            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: Invalid authToken", false);
        }
        authTokenObj = authTokenDAO.retrieveTokenFromToken(authToken);
        PersonDAO personDAO = new PersonDAO(conn);

        List<Person> people = personDAO.getAssocatedPeople(authTokenObj.getUsername());

        Person associatedPerson = null;
        for (Person person : people) {
            if (Objects.equals(person.getPersonID(), personID)) {
                associatedPerson = person;
            }
        }
        DB.close(false);
        if (associatedPerson != null) {
            return new ResultMessage(null, null, associatedPerson.getPersonID(),
                    associatedPerson.getAssociatedUsername(), associatedPerson.getFirstName(),
                    associatedPerson.getLastName(), associatedPerson.getGender(), associatedPerson.getFatherID(),
                    associatedPerson.getMotherID(), associatedPerson.getSpouseID(), null, null, null,
                    null, null, null, null, null, null, true);
        }
        else {
            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: Could not find person with given personID", false);

        }
    }
}
