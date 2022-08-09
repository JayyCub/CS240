package Service;

import DAO_Models.AuthToken;
import DAO_Models.Event;
import DAO_Models.Person;
import DAOs.AuthTokenDAO;
import DAOs.DatabaseUtil;
import DAOs.EventDAO;
import DAOs.PersonDAO;
import ReqRes.ResultMessage;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

public class EventService {
    private String authToken;

    public EventService(String authToken){
        this.authToken = authToken;
    }

    // Do this by getting the user's personID based on the given authtoken
    // Then, find all people with the associated username
    public ResultMessage findAllEvents(){
        System.out.println("Finding events associated to authToken " + authToken);
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
        EventDAO eventDAO = new EventDAO(conn);

        List<Event> events = eventDAO.getAssocatedEvents(authTokenObj.getUsername());

        Event[] returnEvents = new Event[events.size()];
        for (int i = 0; i < events.size(); i++){
            returnEvents[i] = events.get(i);
        }
        DB.close(false);

        return new ResultMessage(null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, null, null, null, returnEvents, null, true);
    }

    // Find Event in database with given EventID, only if they are associated to authToken
    public ResultMessage findEvent(String eventID){
        System.out.println("Finding Event with ID " + eventID + ", authToken " + authToken);
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
        EventDAO eventDAO = new EventDAO(conn);

        List<Event> people = eventDAO.getAssocatedEvents(authTokenObj.getUsername());

        Event associatedEvent = null;
        for (Event event : people) {
            if (Objects.equals(event.getEventID(), eventID)) {
                associatedEvent = event;
            }
        }
        DB.close(false);
        if (associatedEvent != null) {
            return new ResultMessage(null, null, associatedEvent.getPersonID(),
                    associatedEvent.getAssociatedUsername(), null,null, null, null,
                    null, null, associatedEvent.getEventID(), associatedEvent.getLatitude(),
                    associatedEvent.getLongitude(), associatedEvent.getCountry(), associatedEvent.getCity(),
                    associatedEvent.getEventType(), associatedEvent.getYear(), null, null, true);
        }
        else {
            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: Could not find event with given eventID", false);

        }
    }

}
