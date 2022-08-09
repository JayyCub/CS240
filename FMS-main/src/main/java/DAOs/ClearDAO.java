package DAOs;

import java.sql.Connection;

public class ClearDAO {
    private Connection conn;

    public ClearDAO(Connection conn) {this.conn = conn;}

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
