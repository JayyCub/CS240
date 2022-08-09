package Service;

import DAO_Models.*;
import DAOs.*;
import ReqRes.LoadRequest;
import ReqRes.ResultMessage;

import java.sql.Connection;
import java.sql.SQLException;

public class LoadService {

    public ResultMessage loadDataService(LoadRequest loadRequest){
        ClearService clearDB = new ClearService();
        clearDB.clear();

        int numUsers = loadRequest.getUsers().length;
        int numPeople = loadRequest.getPersons().length;
        int numEvents = loadRequest.getEvents().length;
        String message = "Successfully added " + numUsers + " users, " + numPeople + " persons, and " + numEvents + " events.";
        boolean success = true;
        DatabaseUtil DB = new DatabaseUtil();
        try {
            Connection conn = DB.open();

            UserDAO userDAO = new UserDAO(conn);
            for (int i = 0; i < numUsers; i++){
                User newUser = loadRequest.getUsers()[i];
                boolean valid = userDAO.insertUser(newUser);
                if (!valid){
                    throw new SQLException();
                }
            }
            PersonDAO personDAO = new PersonDAO(conn);
            for (int i = 0; i < numPeople; i++){
                Person newPerson = loadRequest.getPersons()[i];
                boolean valid = personDAO.insertPerson(newPerson);
                if (!valid){
                    throw new SQLException();
                }
            }
            EventDAO eventDAO = new EventDAO(conn);
            for (int i = 0; i < numEvents; i++){
                Event newEvent = loadRequest.getEvents()[i];
                boolean valid = eventDAO.insertEvent(newEvent);
                if (!valid){
                    throw new SQLException();
                }
            }
            DB.close(true);
            System.out.println("Added " + numUsers + " Users, " + numPeople + " People, and " + numEvents + " Events.");
        } catch (SQLException e){
            System.out.println("Error: Couldn't insert to DB");
            message = "Error: There was an error loading the data into the database. " +
                    "Please ensure all fields are valid and try again.";
            success = false;
            DB.close(true);

        }

        return new ResultMessage(null, null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, message, success );
    }

}
