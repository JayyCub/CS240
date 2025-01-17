package Service;

import DAO_Models.AuthToken;
import DAO_Models.Event;
import DAOs.AuthTokenDAO;
import DAOs.DatabaseUtil;
import DAOs.EventDAO;
import ReqRes.ResultMessage;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;

/**
 * Class to manage the retrieval of event command
 */
public class EventService {
    /** String, authtoken used to access DB */
    private String authToken;

    /** String with the eventID to be used for DB lookup */
    private String eventID;

    /**
     * Constructor to set value of authToken
     * @param authToken String, authToken ID
     */
    public EventService(String authToken){
        this.authToken = authToken;
    }

    /**
     * Find all events associated to a user, Username retrieved based on AuthToken ID
     * @return ResultMessage, returns data or error message
     */
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

    /**
     * Find one event from the DB based on AuthToken verification and Event ID
     * @param eventID String, EventID
     * @return Result message with data or error message
     */
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
