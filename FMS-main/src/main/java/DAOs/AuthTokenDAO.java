package DAOs;

import DAO_Models.AuthToken;
import DAO_Models.User;

import java.sql.*;

public class AuthTokenDAO {
    private Connection conn;

    public AuthTokenDAO(Connection conn){
        this.conn = conn;
    }

    public void insertToken(AuthToken authToken){
        String sqlInsertion = "INSERT INTO AuthTable (auth_Token, userName, personID) VALUES(?,?,?)";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsertion)) {
            statement.setString(1, authToken.getAuthToken());
            statement.setString(2, authToken.getUsername());
            statement.setString(3, authToken.getPersonID());

            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error with AuthToken Insertion");
            System.out.println(e);
        }
    }

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

    public AuthToken retrieveToken(String username) {
        String sqlString = "SELECT * FROM AuthTable WHERE username=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            return new AuthToken(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return new AuthToken(null, null, null);
        }
    }

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

    public AuthToken retrieveTokenFromToken(String authToken) {
        String sqlString = "SELECT * FROM AuthTable WHERE auth_Token=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1,authToken);
            ResultSet resultSet = statement.executeQuery();

            return new AuthToken(resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3));
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return new AuthToken(null, null, null);
        }
    }

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
