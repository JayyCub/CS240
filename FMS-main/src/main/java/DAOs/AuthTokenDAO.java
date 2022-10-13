package DAOs;

import DAO_Models.AuthToken;

import java.sql.*;

/** AuthTokenDAO. Methods to store and access authTokens */
public class AuthTokenDAO {
    /** Connection to the Database */
    private Connection conn;

    /**
     * Constructor that creates stores connection to database
     * @param conn Connection to the DB
     */
    public AuthTokenDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Using a given authToken, insert into database
     * @param authToken The given authToken object
     */
    public void insertToken(AuthToken authToken){
        String sqlInsertion = "INSERT INTO AuthTable (auth_Token, userName) VALUES(?,?)";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsertion)) {
            statement.setString(1, authToken.getAuthToken());
            statement.setString(2, authToken.getUsername());

            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error with AuthToken Insertion");
            System.out.println(e);
        }
    }

    /**
     * Using a given AuthToken, update its row in the database
     * @param authToken A given AuthToken
     */
    public void updateToken(AuthToken authToken){
        String sqlInsertion = "UPDATE AuthTable SET auth_Token=? WHERE userName=?";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsertion)) {
            statement.setString(1, authToken.getAuthToken());
            statement.setString(2, authToken.getUsername());

            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error updating AuthToken Insertion");
            System.out.println(e);
        }
    }

    /**
     * Using a username, check if the user has an AuthToken
     * @param username Username string
     * @return Boolean, if token exists?
     */
    public boolean checkIfTokenExists(String username) {
        String sqlString = "SELECT * FROM AuthTable WHERE username=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: checkIfTokenExists(): Error checking for token");
            System.out.println(sqlException);
            return false;
        }
    }

    /**
     * Retrieve an AuthToken from DB, using a username
     * @param username A given username string
     * @return AuthToken object
     */
    public AuthToken retrieveToken(String username) {
        String sqlString = "SELECT * FROM AuthTable WHERE username=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            return new AuthToken(resultSet.getString(1),
                    resultSet.getString(2));
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return new AuthToken(null, null);
        }
    }

    /**
     * Check if the AuthToken given in a request exists in the database. Validation method.
     * @param authToken AuthToken ID string
     * @return Boolean, if token exists in DB
     */
    public boolean checkIfTokenExistsFromToken(String authToken) {
        String sqlString = "SELECT * FROM AuthTable WHERE auth_Token=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, authToken);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: checkIfTokenExists(): Error checking for token");
            System.out.println(sqlException);
            return false;
        }
    }

    /**
     * Retrieve a token form the database, based on a given token ID string
     * @param authToken given AuthToken string
     * @return AuthToken object pulled from DB
     */
    public AuthToken retrieveTokenFromToken(String authToken) {
        String sqlString = "SELECT * FROM AuthTable WHERE auth_Token=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1,authToken);
            ResultSet resultSet = statement.executeQuery();

            return new AuthToken(resultSet.getString(1),
                    resultSet.getString(2));
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return new AuthToken(null, null);
        }
    }

    /**
     * Clear all tokens from the database
     */
    public void clearTokens(){
        try {
            Statement statement = conn.createStatement();
            String deleteUsers = "DELETE FROM AuthTable";
            statement.executeUpdate(deleteUsers);

        } catch (SQLException e) {
            System.out.println("Error clearing Tokens from DB");
        }
    }
}
