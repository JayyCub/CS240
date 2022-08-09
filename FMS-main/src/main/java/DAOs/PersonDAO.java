package DAOs;

import DAO_Models.Person;
import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonDAO {
    private Connection conn;

    public PersonDAO(Connection connection){this.conn = connection;}

    public boolean insertPerson(Person newPerson) {
        String sqlInsertion = "INSERT INTO People (personID, associatedUsername, firstName, lastName, gender, father, "+
                "mother, spouse) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement statement = conn.prepareStatement(sqlInsertion)) {
            statement.setString(1, newPerson.getPersonID());
            statement.setString(2, newPerson.getAssociatedUsername());
            statement.setString(3, newPerson.getFirstName());
            statement.setString(4, newPerson.getLastName());
            statement.setString(5, newPerson.getGender());
            statement.setString(6, newPerson.getFatherID());
            statement.setString(7, newPerson.getMotherID());
            statement.setString(8, newPerson.getSpouseID());

            statement.executeUpdate();
        } catch (SQLException e){
            System.out.println("Error with PersonDAO SQL Insertion");
            System.out.println(e);
            return false;
        }
        return true;
    }

    public Person retrieveAccount(String personID) {
        String sqlString = "SELECT * FROM People WHERE personID=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, personID);
            ResultSet resultSet = statement.executeQuery();

            return new Person(resultSet.getString(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getString(4),
                    resultSet.getString(5), resultSet.getString(6),
                    resultSet.getString(7), resultSet.getString(8));
        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return new Person(null, null, null, null, null, null,
                    null, null);
        }
    }

    public List<Person> getAssocatedPeople(String username){
        String sqlString = "SELECT * FROM People WHERE associatedUsername=?";
        try (PreparedStatement statement = conn.prepareStatement(sqlString)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            List<Person> people = new ArrayList<>();
            while(resultSet.next()){
                Person person = new Person(resultSet.getString("personID"),
                        resultSet.getString("associatedUsername"), resultSet.getString("firstName"),
                        resultSet.getString("lastName"), resultSet.getString("gender"),
                        resultSet.getString("father"), resultSet.getString("mother"),
                        resultSet.getString("spouse"));

                people.add(person);
            }
            return people;

        } catch (SQLException sqlException){
            System.out.println("Error: UserDAO: retrieveAccount(): Error retrieving account");
            System.out.println(sqlException);
            return null;
        }
    }

    public void clearPeople(){
        try {
            Statement statement = conn.createStatement();
            String deletePeople = "DELETE FROM People";
            statement.executeUpdate(deletePeople);

        } catch (SQLException e) {
            System.out.println("Error clearing People from DB");
        }
    }
}
