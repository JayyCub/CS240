package Service;

import DAO_Models.Event;
import DAO_Models.Person;
import DAO_Models.User;
import DAOs.DatabaseUtil;
import DAOs.EventDAO;
import DAOs.PersonDAO;
import DAOs.UserDAO;
import ReqRes.ResultMessage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FillService {
    private int numGens;
    private String username;
    private int numNewEvents;
    private int numNewPeople;
    private DatabaseUtil DB;

    public int getNumNewEvents() {
        return numNewEvents;
    }

    public int getNumNewPeople() {
        return numNewPeople;
    }

    public FillService(int numGens, String username, DatabaseUtil DB){
        this.numGens = numGens;
        this.username = username;
        this.DB = DB;
    }

    public ResultMessage FillGenerations() {
        Connection conn = this.DB.getConn();
        UserDAO userDAO = new UserDAO(conn);
        try {
            // Check if account exists, if so, return the user
            if (!userDAO.checkIfAccountExists(username)) {
                DB.close(false);
                System.out.println("No account found for " + username);
                return new ResultMessage(null, null, null, null, null,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null,
                        "Error: No account found for " + username, false);
            }
            User user = userDAO.retrieveAccount(username);
            Person person = new Person(user.getPersonID(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getGender(),
                    null, null, null);

            // Clear associated data from the DB
            DB.clearAssociatedData(username);

            // Actually generate and fill data;
            insertFamilyRecursive(conn, person, 0, numGens);



            DB.close(true);

            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Successfully added "+ numNewPeople + " persons and " + numNewEvents + " events to the database.",
                    true);

        } catch (SQLException sqlException) {
            System.out.println("Error: couldn't clear associated data");
            //System.out.println(sqlException);

            DB.close(false);

            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: Could not fill data for " + username, false);

        }
    }
    private void insertFamilyRecursive(Connection conn, Person child, int currentGeneration, final int totalGenerations) {
        FillFamily family = new FillFamily(child, conn);
        PersonDAO pDao = new PersonDAO(conn);
        if (currentGeneration == totalGenerations) {
            child.setMotherID(null);
            child.setFatherID(null);
        }
        pDao.insertPerson(child);
        numNewPeople++;
        if (currentGeneration == 0) {
            insertChildEvents(conn, family);
            numNewEvents++;
        }

        if (currentGeneration < totalGenerations) {
            insertParentEvents(conn, family);
            numNewEvents += 6;
            insertFamilyRecursive(conn, family.getFather(), currentGeneration + 1, totalGenerations);
            insertFamilyRecursive(conn, family.getMother(), currentGeneration + 1, totalGenerations);
        }
    }

    private void insertChildEvents(Connection conn, FillFamily family){
        EventDAO eDao = new EventDAO(conn);
        for (Event event : family.getEvents()) {
            if (event.getPersonID().equals(family.getChild().getPersonID())) {
                eDao.insertEvent(event);
            }
        }
    }

    private void insertParentEvents(Connection conn, FillFamily family){
        EventDAO eDao = new EventDAO(conn);
        for (Event event : family.getEvents()) {
            if (event.getPersonID().equals(family.getMother().getPersonID()) ||
                    event.getPersonID().equals(family.getFather().getPersonID())) {
                eDao.insertEvent(event);
            }
        }
    }


}