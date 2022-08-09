package DAOs;

import passoffmodels.User;

import java.sql.*;

public class DatabaseUtil {
    private Connection conn;

    public Connection open(){
        String CONN_URL = "jdbc:sqlite:Family_Map.sqlite";
        try {
            conn = DriverManager.getConnection(CONN_URL);
            conn.setAutoCommit(false);
        } catch (SQLException e){
            System.out.println("Error connecting to DB");
            System.out.println(e);
        }
        return conn;
    }

    public Connection getConn() {
        return conn;
    }

    public void commit(){
        try {
            conn.commit();
        } catch (SQLException sqlException){
            System.out.println("Error committing to database.");
            System.out.println(sqlException);
        }
    }

    public void close(boolean commit) {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }
        } catch (SQLException e){
            System.out.println("Error Closing DB");
            System.out.println(e);
        }
    }

    public void clearAssociatedData(String username) throws SQLException {
        String peopleSQL = "DELETE FROM People WHERE associatedUsername=?";
        try (PreparedStatement statement = conn.prepareStatement(peopleSQL)){
            statement.setString(1, username);
            //System.out.println(statement);
            statement.executeUpdate();
        }
        String eventsSQL = "DELETE FROM Events WHERE associatedUsername=?";
        try (PreparedStatement statement = conn.prepareStatement(eventsSQL)){
            statement.setString(1, username);
            //System.out.println(statement);
            statement.executeUpdate();
        }
    }

}
