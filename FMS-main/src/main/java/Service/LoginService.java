package Service;

import DAO_Models.AuthToken;
import DAO_Models.User;
import DAOs.AuthTokenDAO;
import DAOs.DatabaseUtil;
import DAOs.UserDAO;
import ReqRes.ResultMessage;

import java.sql.Connection;
import java.util.Objects;
import java.util.UUID;

/**
 * Class to log a user in
 */
public class LoginService {
    /** User object */
    private User user;

    /**
     * This method sets the value for the User variable and creates the LoginService
     * @param user User object
     */
    public LoginService(User user){ this.user = user; }

    // If account exists, retrieve it
    // Else, error message
    // Create authToken
    // Return authToken

    /**
     * Looks for the username's account in database, if it exists, check if the password matches, if so, return Token
     * @return Message with validation state of log in attempt
     */
    public ResultMessage login(){
        DatabaseUtil DB = new DatabaseUtil();
        Connection conn = DB.open();
        UserDAO userDAO = new UserDAO(conn);
        String username = user.getUsername();
        if (!userDAO.checkIfAccountExists(username)){
            System.out.println("Account not found for " + username);
            DB.close(false);
            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: No account found for " + username, false);
        }
        User retrievedUser = userDAO.retrieveAccount(username);
        if (!Objects.equals(user.getPassword(), retrievedUser.getPassword())){
            System.out.println("Incorrect password ");
            DB.close(false);
            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: Incorrect password. Please try again. ", false);

        }

        user = retrievedUser;

        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        AuthToken authToken;
        if (!authTokenDAO.checkIfTokenExists(username)){
            authToken = new AuthToken(UUID.randomUUID().toString().substring(0, 10), username);
            authTokenDAO.insertToken(authToken);
        } else {
            authToken = new AuthToken(UUID.randomUUID().toString().substring(0, 10), username);
            authTokenDAO.updateToken(authToken);
        }

        DB.close(true);

        return new ResultMessage(authToken.getAuthToken(), authToken.getUsername(), user.getPersonID(),
                null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, "Logged in!", true);
    }
}
