package DAOs;

import java.sql.Connection;

/**
 * The Clear function DAO
 */
public class ClearDAO {
    /** Connection to DB */
    private Connection conn;

    /**
     * Constructor for ClearDAO that sets the Connection to DB
     * @param conn Given connection to DB
     */
    public ClearDAO(Connection conn) {this.conn = conn;}

    /**
     * Calls each DAO's clear function, to totally empty the DB
     */
    public void clearDatabase(){
        UserDAO users = new UserDAO(conn);
        users.clearUsers();
        PersonDAO people = new PersonDAO(conn);
        people.clearPeople();
        EventDAO events = new EventDAO(conn);
        events.clearEvents();
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        authTokenDAO.clearTokens();
    }
}
