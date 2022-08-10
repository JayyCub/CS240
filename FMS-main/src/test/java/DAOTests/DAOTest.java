package DAOTests;

import DAO_Models.Person;
import DAO_Models.User;
import DAOs.DatabaseUtil;
import DAOs.PersonDAO;
import DAOs.UserDAO;
import client.Client;
import client.Proxy;
import client.ServerConnectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import logs.InitLogs;
import org.junit.jupiter.api.*;
import passoffrequest.LoadRequest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class DAOTest {
    private static String host = "localhost";
    private static String port = "8080";
    private static boolean displayCurrentTest = true;
    private Proxy proxy;
    private DatabaseUtil databaseUtil = new DatabaseUtil();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Connection conn;

    User jacobUser = new User("jacob", "jacob", "jakethom02@gmail.com", "Jacob",
            "Thomsen", "m", "jacob_t");

    Person jacobPerson = new Person("jacob_t", "jacob", "Jacob", "Thomsen",
            "M", "david_t", "debbie_t", "alex_b");

    private static final Logger logger;

    static {
        InitLogs.init();
        logger = Logger.getLogger(Client.class.getName());
    }

    @BeforeEach
    @DisplayName("Setup")
    public void setup() throws ServerConnectionException {
        proxy = new Proxy();
        proxy.clear(host, port);
        this.conn = this.databaseUtil.open();
    }

    /**
     * This test calls the function PersonDAO.clear(), then asserts if the People table is empty
     */
    @Test
    @DisplayName("PersonDAO Clear()")
    public void PersonClear() throws Exception {
        System.out.println("Testing UserDAO clear function...");

        // Check if database was not filled, an already empty database would invalidate the test
        load();
        PersonDAO PersonDAO = new PersonDAO(conn);
        String sqlString = "SELECT * FROM People";
        PreparedStatement statement = conn.prepareStatement(sqlString);
        ResultSet resultSet = statement.executeQuery();
        Assertions.assertTrue(resultSet.next(), "Persons is not filled, should be filled be load() before" +
                " testing clearing.\n" +
                "Boolean: is table populated?");

        PersonDAO.clearPeople();

        // Check if clearUsers() function emptied the Users table
        resultSet = statement.executeQuery();
        Assertions.assertFalse(resultSet.next(), "Persons table not cleared by PersonDAO clearing function.\n" +
                "Boolean: is table populated?");

        databaseUtil.close(true);
    }

    /**
     * This test calls the function UserDAO.clear(), then asserts if the Users table is empty
     */
    @Test
    @DisplayName("UserDAO Clear()")
    public void UserClear() {
        System.out.println("Testing UserDAO clear function...");

        // Check if database was not filled, an already empty database would invalidate the test
        load();
        UserDAO userDAO = new UserDAO(conn);
        userDAO.insertUser(jacobUser);
        boolean foundAccount = userDAO.checkIfAccountExists(jacobUser.getUsername());
        databaseUtil.commit();
        Assertions.assertTrue(foundAccount, "Users is not filled, should be filled be load() before" +
                " testing clearing.\n" +
                "Boolean: is table populated?");

        userDAO.clearUsers();

        // Check if clearUsers() function emptied the Users table
        foundAccount = userDAO.checkIfAccountExists(jacobUser.getUsername());
        databaseUtil.commit();
        Assertions.assertFalse(foundAccount, "Users table not cleared by UserDAO clearing function.\n" +
                "Boolean: is table populated?");
    }

    /**
     * This test inserts a user and then asserts if the user was inserted into the database
     */
    @Test
    @DisplayName("UserDAO Insertion")
    public void UserPositiveInsert(){
        System.out.println("Testing UserDAO insertion...");

        // Check if account already exists in database, if so, this invalidates this unit test
        UserDAO userDAO = new UserDAO(conn);
        boolean foundAccount = userDAO.checkIfAccountExists(jacobUser.getUsername());
        databaseUtil.commit();
        Assertions.assertFalse(foundAccount, "Account already found in database, test invalidated");

        // Insert user into DB
        boolean goodInsert = userDAO.insertUser(this.jacobUser);
        Assertions.assertTrue(goodInsert, "Did not complete insert");

        // Check if user was actually inserted into DB
        foundAccount = userDAO.checkIfAccountExists(jacobUser.getUsername());
        databaseUtil.commit();
        Assertions.assertTrue(foundAccount, "Could not find account in database");
    }

    /**
     * This test tries to insert the same user twice, expecting to cause an error, and returning a failed test
     */
    @Test
    @DisplayName("UserDAO Insertion (Fail)")
    public void UserNegativeInsert() {
        System.out.println("Testing Bad UserDAO insertion...");

        // Insert a user into the database
        UserDAO userDAO = new UserDAO(conn);
        boolean first = userDAO.insertUser(jacobUser);
        databaseUtil.commit();
        Assertions.assertTrue(first, "Did not insert user on first attempt");

        // Insert the same user a second time. Row uniqueness in DB is expected to not allow this and give error.
        boolean second = userDAO.insertUser(jacobUser);
        databaseUtil.commit();
        Assertions.assertTrue(second, "EXPECTED FAILURE: Did not insert user on second attempt");
    }

    /**
     * This test inserts a person and then asserts if the person was inserted into the database
     */
    @Test
    @DisplayName("PersonDAO Insertion")
    public void PersonPositiveInsert(){
        System.out.println("Testing PersonDAO insertion...");

        // Check if person already exists in database, if so, this invalidates this unit test
        PersonDAO personDAO = new PersonDAO(conn);
        boolean checkVal = personDAO.checkIfPersonExists(jacobPerson.getPersonID());
        databaseUtil.commit();
        Assertions.assertFalse(checkVal, "Person already found in database, test invalidated");

        // Insert person into DB
        boolean goodInsert = personDAO.insertPerson(jacobPerson);
        Assertions.assertTrue(goodInsert, "Did not complete insert");

        // Check if person was actually inserted into DB
        boolean checkVal2 = personDAO.checkIfPersonExists(jacobPerson.getPersonID());
        databaseUtil.commit();
        Assertions.assertTrue(checkVal2, "Could not find account in database");
    }

    /**
     * This test tries to insert the same person twice, expecting to cause an error, and returning a failed test
     */
    @Test
    @DisplayName("PersonDAO Insertion (Fail)")
    public void PersonNegativeInsert() {
        System.out.println("Testing Bad Insertion...");

        // Insert a person into the database
        PersonDAO personDAO = new PersonDAO(conn);
        boolean first = personDAO.insertPerson(jacobPerson);
        databaseUtil.commit();
        Assertions.assertTrue(first, "Did not insert person on first attempt");

        // Insert the same person a second time. Row uniqueness in DB is expected to not allow this and give error.
        boolean second = personDAO.insertPerson(jacobPerson);
        databaseUtil.commit();
        Assertions.assertTrue(second, "EXPECTED FAILURE: Did not insert person on second attempt");
    }

    /**
     * Add person to database, retrieve them and make sure the result is not null
     */
    @Test
    @DisplayName("PersonDAO Retrieval")
    public void PersonPositiveRetrieval(){
        System.out.println("Testing Person retrieval...");

        // Insert person into the database
        PersonDAO personDAO = new PersonDAO(conn);
        personDAO.insertPerson(jacobPerson);

        // Check if the person now exists in the database
        boolean insertionCheck = personDAO.checkIfPersonExists(jacobPerson.getPersonID());
        Assertions.assertTrue(insertionCheck, "Person was not inserted into the database");

        // Check if we can retrieve the account from the database
        Person resultPerson = personDAO.retrieveAccount(jacobPerson.getPersonID());
        Assertions.assertNotNull(resultPerson.getFirstName(), "Could not find person in database");
        databaseUtil.commit();
    }

    /**
     * Try to retrieve a person that does not exist, should fail.
     */
    @Test
    @DisplayName("PersonDAO Retrieval (Fail)")
    public void PersonNegativeRetrieval(){
        System.out.println("Testing Non-existent Person retrieval");

        //Make sure the database is empty
        PersonDAO personDAO = new PersonDAO(conn);
        personDAO.clearPeople();

        // Make sure person does not exist
        boolean exists = personDAO.checkIfPersonExists(jacobPerson.getPersonID());
        Assertions.assertFalse(exists, "Database is not empty/person is still in database");

        // Retrieve person that is not there
        Person nullPerson = personDAO.retrieveAccount(jacobPerson.getPersonID());
        databaseUtil.commit();
        Assertions.assertNotNull(nullPerson.getFirstName(), "EXPECTED FAILURE: Did not retrieve person from database");
    }

    /**
     * Add User to database, retrieve them and make sure the result is not null
     */
    @Test
    @DisplayName("UserDAO Retrieval")
    public void UserPositiveRetrieval(){
        System.out.println("Testing User retrieval...");

        // Insert user into the database
        UserDAO userDAO = new UserDAO(conn);
        userDAO.insertUser(jacobUser);
        databaseUtil.commit();

        // Check if the user now exists in the database
        boolean insertionCheck = userDAO.checkIfAccountExists(jacobUser.getUsername());
        Assertions.assertTrue(insertionCheck, "User was not inserted into the database");

        // Check if we can retrieve the account from the database
        User resultUser = userDAO.retrieveAccount(jacobUser.getUsername());
        Assertions.assertNotNull(resultUser.getFirstName(), "Could not find User in database");
    }

    /**
     * Try to retrieve a User that does not exist, should fail.
     */
    @Test
    @DisplayName("UserDAO Retrieval (Fail)")
    public void UserNegativeRetrieval(){
        System.out.println("Testing Non-existent User retrieval");

        //Make sure the database is empty
        UserDAO userDAO = new UserDAO(conn);
        userDAO.clearUsers();

        // Make sure User does not exist
        boolean exists = userDAO.checkIfAccountExists(jacobUser.getPersonID());
        Assertions.assertFalse(exists, "Database is not empty/User is still in database");

        // Retrieve User that is not there
        User nullUser = userDAO.retrieveAccount(jacobUser.getPersonID());
        databaseUtil.commit();
        Assertions.assertNotNull(nullUser.getFirstName(), "EXPECTED FAILURE: Did not retrieve User from database");
    }

    private void load() {
        try {
            JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
            LoadRequest loadRequest = GSON.fromJson(jsonReader, LoadRequest.class);

            proxy.load(host, port, loadRequest);
        } catch (ServerConnectionException e) {
            fail(e.getMessage());
        } catch (FileNotFoundException e) {
            Assertions.fail("passoffFiles/LoadData.json not found in project root directory");
        }
    }
}
