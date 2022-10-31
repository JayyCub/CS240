package DAOs;

import DAO_Models.User;

import java.sql.*;

/**
 * UserDAO object, User database access
 */
public class UserDAO {
    /**
     * Connection to DB
     */
    private Connection conn;

    /**
     * Constructor for UserDAO object, associates connection to DB
     * @param conn Connection to DB
     */
    public UserDAO(Connection conn){this.conn = conn;}

    /**
     * Insert user object to the DB
     * @param newUser User object
     * @return Boolean, if the insertion was successful
     */
    public boolean insertUser(User newUser) {
        String sqlInsertion = "INSERT INTO Users (username, password, email, firstName, lastName, gender, " +
                "personId) VALUES(?,?,?,?,?,?,?)";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsertion)) {
            statement.setString(1, newUser.getUsername());
            statement.setString(2, newUser.getPassword());
            statement.setString(3, newUser.getEmail());
            statement.setString(4, newUser.getFirstName());
            statement.setString(5, newUser.getLastName());
            statement.setString(6, newUser.getGender());
            statement.setString(7, newUser.getPersonID());

            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error with UserDAO SQL Insertion");
            //System.out.println(e);
            return false;
        }
        return true;
    }

    /**
     * Clear all users from the DB
     */
    public void clearUsers(){
        try {
            Statement statement = conn.createStatement();
            String deleteUsers = "DELETE FROM Users";
            statement.executeUpdate(deleteUsers);

        } catch (SQLException e) {
            System.out.println("Error clearing Users from DB");
        }
    }

    /**
     * Get User object from database give a username
     * @param username String, username to search for
     * @return User object returned from DB
     */
    public User retrieveAccount(String username) {
        String sqlString = "SELECT * FROM Users WHERE username=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            return new User(resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6),
                    resultSet.getString(7));
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return new User(null, null, null, null, null, null,
                    null);
        }
    }

    /**
     * Check if the DB contains a User with the given username
     * @param username String, username to search for
     * @return Boolean, if the account exists
     */
    public boolean checkIfAccountExists(String username) {
        String sqlString = "SELECT * FROM Users WHERE username=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: checkIfAccountExists(): Error checking for account");
            //System.out.println(sqlException);
            // ACCOUNT NOT FOUND
            return false;
        }
    }

}
