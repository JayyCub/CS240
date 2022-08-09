package DAOs;

import passoffmodels.User;

import java.sql.*;

/**
 * Class to manage communication with the Database
 */
public class DatabaseUtil {
    /** Connection to DB */
    private Connection conn;

    /**
     * Create connection to the DB
     * @return The established conneciton
     */
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

    /**
     * Return the current DB connection
     * @return DB Connection
     */
    public Connection getConn() {
        return conn;
    }

    /**
     * Save changes to the DB
     */
    public void commit(){
        try {
            conn.commit();
        } catch (SQLException sqlException){
            System.out.println("Error committing to database.");
            System.out.println(sqlException);
        }
    }

    /**
     * Close connection to the database
     * @param commit Determine if changes need to be saved
     */
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

    /**
     * Delete persons and events associaed to a given username
     * @param username String username
     * @throws SQLException Throws error if the database cannot be reached or statement cannot be executed
     */
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
