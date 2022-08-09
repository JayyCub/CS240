package DAOs;

import DAO_Models.Event;
import DAO_Models.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {
    private Connection conn;

    public EventDAO(Connection connection){this.conn = connection;}

    public boolean insertEvent(Event newEvent) {
        String sqlInsertion = "INSERT INTO Events (eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, eventYear) VALUES(?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsertion)) {
            statement.setString(1, newEvent.getEventID());
            statement.setString(2, newEvent.getAssociatedUsername());
            statement.setString(3, newEvent.getPersonID());
            statement.setString(4, String.valueOf(newEvent.getLatitude()));
            statement.setString(5, String.valueOf(newEvent.getLongitude()));
            statement.setString(6, newEvent.getCountry());
            statement.setString(7, newEvent.getCity());
            statement.setString(8, newEvent.getEventType());
            statement.setString(9, String.valueOf(newEvent.getYear()));

            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error with EventDAO SQL Insertion");
            System.out.println(e);
            return false;
        }
        return true;
    }

    public List<Event> getAssocatedEvents(String username){
        String sqlString = "SELECT * FROM Events WHERE associatedUsername=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            List<Event> events = new ArrayList<>();
            while(resultSet.next()){
                Event event = new Event(resultSet.getString(1),
                        resultSet.getString(2), resultSet.getString(3),
                        resultSet.getDouble(4), resultSet.getDouble(5),
                        resultSet.getString(6), resultSet.getString(7),
                        resultSet.getString(8), resultSet.getInt(9));

                events.add(event);
            }
            return events;

        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return null;
        }
    }

    public void clearEvents(){
        try {
            Statement statement = conn.createStatement();
            String deleteEvents = "DELETE FROM Events";
            statement.executeUpdate(deleteEvents);

        } catch (SQLException e) {
            System.out.println("Error clearing Events from DB");
        }
    }


    public Event find(String personID, String eventType) {
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE PersonID = ? AND EventType = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            stmt.setString(2, eventType);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return new Event(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getDouble(4), rs.getDouble(5),
                        rs.getString(6), rs.getString(7), rs.getString(8),
                        rs.getInt(9));
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;

    }
}
