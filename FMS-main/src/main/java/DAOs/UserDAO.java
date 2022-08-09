package DAOs;

import DAO_Models.User;

import java.sql.*;
import java.util.Objects;

public class UserDAO {
    private Connection conn;

    public UserDAO(Connection conn){this.conn = conn;}

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
            System.out.println(e);
            return false;
        }
        return true;
    }

    public void clearUsers(){
        try {
            Statement statement = conn.createStatement();
            String deleteUsers = "DELETE FROM Users";
            statement.executeUpdate(deleteUsers);

        } catch (SQLException e) {
            System.out.println("Error clearing Users from DB");
        }
    }

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

    public boolean checkIfAccountExists(String username) {
        String sqlString = "SELECT * FROM Users WHERE username=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: checkIfAccountExists(): Error checking for account");
            System.out.println(sqlException);
            return false;
        }
    }

}
