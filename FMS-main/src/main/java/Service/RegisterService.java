package Service;

import DAO_Models.AuthToken;
import DAO_Models.User;
import DAOs.AuthTokenDAO;
import DAOs.DatabaseUtil;
import DAOs.UserDAO;
import ReqRes.ResultMessage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class RegisterService {
    private final User user;

    public RegisterService(User user) {
        this.user = user;
    }

    // Get data from input, should be User entity data
    // Check if account already exists in database
    // If not, insert user into database and fill 4 generations
    // Log the user in
    // Returns an authtoken

    public ResultMessage register() {
        DatabaseUtil DB = new DatabaseUtil();
        Connection conn = DB.open();

        UserDAO userDAO = new UserDAO(conn);
        if (userDAO.checkIfAccountExists(user.getUsername())) {
            System.out.println("Account already found in database, cannot register with username " + user.getUsername());
            DB.close(false);

            return new ResultMessage(null, null, null, null, null,
                    null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    "Error: Account already exists for " + user.getUsername(), false);

        } else {
            // Insert user into database and fill for them
            user.setPersonID(user.getFirstName() + "_" + user.getLastName() + "_" + UUID.randomUUID().toString().substring(0,5));
            userDAO.insertUser(user);
            DB.commit();

            FillService fillService = new FillService(4, user.getUsername(), DB);
            ResultMessage fillResultMessage = fillService.FillGenerations();

            if (!fillResultMessage.getSuccess()) {
                System.out.println("Error: RegisterService: Could not fill for new user");
                DB.close(false);
                return new ResultMessage(null, null, null, null, null,
                        null, null, null, null, null, null, null,
                        null, null, null, null, null, null,
                        "Error: Could not fill data for new user: " + user.getUsername(), false);
            }
        }


        // Create auth token for user and "log them in"
        // Generates random UUID, adds it to Authtoken DB table

        AuthToken authToken = new AuthToken(UUID.randomUUID().toString().substring(0, 10), user.getUsername(), user.getPersonID());
        AuthTokenDAO authTokenDAO = new AuthTokenDAO(conn);
        authTokenDAO.insertToken(authToken);

        DB.close(true);
        System.out.println("Created account for " + user.getUsername());

        return new ResultMessage(authToken.getAuthToken(), authToken.getUsername(), authToken.getPersonID(),
                null, null, null, null, null, null,
                null, null, null, null, null, null, null,
                null, null, "Registered and logged in!", true);
    }
}
