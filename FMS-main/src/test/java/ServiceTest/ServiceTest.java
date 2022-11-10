package ServiceTest;

import DAO_Models.Event;
import DAO_Models.Person;
import DAO_Models.User;
import DAOs.DatabaseUtil;
import DAOs.EventDAO;
import DAOs.PersonDAO;
import DAOs.UserDAO;
import ReqRes.ResultMessage;
import Service.*;
import client.Client;
import client.Proxy;
import client.ServerConnectionException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import logs.InitLogs;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import passoffrequest.LoadRequest;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceTest {
    private static final String host = "localhost";
    private static final String port = "8080";
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
        clearData();
    }

    public void clearData(){
        ClearService clearService = new ClearService();
        clearService.clear();
    }

    @Test
    @DisplayName("Successful Registration")
    public void positiveRegistration(){
        System.out.println("\nBegin Positive Registration...");

        System.out.println("Registering Jacob");
        RegisterService registerService = new RegisterService(jacobUser);
        ResultMessage resultMessage = registerService.register();
        Assertions.assertTrue(resultMessage.getSuccess());
        System.out.println("Successful test!");
    }

    @Test
    @DisplayName("Failed Registration")
    public void negativeRegistration(){
        System.out.println("\nBegin Negative Registration...");

        System.out.println("Registering Jacob");
        RegisterService registerService = new RegisterService(jacobUser);
        ResultMessage resultMessage = registerService.register();
        Assertions.assertTrue(resultMessage.getSuccess());

        System.out.println("Registering Jacob a second time");
        RegisterService registerService2 = new RegisterService(jacobUser);
        ResultMessage resultMessage2 = registerService2.register();
        Assertions.assertFalse(resultMessage2.getSuccess());
        System.out.println("Did not register twice. Successful test!");
    }

    @Test
    @DisplayName("Successful Login")
    public void positiveLogin(){
        System.out.println("\nBegin Positive Login...");

        RegisterService registerService = new RegisterService(jacobUser);
        ResultMessage resultMessage = registerService.register();
        Assertions.assertTrue(resultMessage.getSuccess());

        System.out.println("Logging in to new account");
        LoginService loginService = new LoginService(jacobUser);
        ResultMessage loginResult = loginService.login();
        Assertions.assertTrue(loginResult.getSuccess());
        System.out.println("Successful test!");
    }

    @Test
    @DisplayName("Failed Login")
    public void negativeLogin(){
        System.out.println("\nBegin Positive Login...");

        RegisterService registerService = new RegisterService(jacobUser);
        ResultMessage resultMessage = registerService.register();
        Assertions.assertTrue(resultMessage.getSuccess());

        System.out.println("Logging in to new account");
        LoginService loginService = new LoginService(new User("not_Jacob", "not_my_password",
                null, null, null, null, null));
        ResultMessage loginResult = loginService.login();
        Assertions.assertFalse(loginResult.getSuccess());
        System.out.println("Couldn't log in to non-existent account. Successful test!");
    }

    @Test
    @DisplayName("Successful Person Retrieval")
    public void positivePersonService(){
        System.out.println("\nBegin Positive PersonService Person Retrieval...");

        // Registering User
        RegisterService registerService = new RegisterService(jacobUser);
        ResultMessage resultMessage = registerService.register();
        Assertions.assertTrue(resultMessage.getSuccess());

        // Searching for user that was just registered
        System.out.println("Searching for Jacob");
        PersonService personService = new PersonService(resultMessage.getAuthtoken());
        ResultMessage personResult = personService.findPerson(resultMessage.getPersonID());
        Assertions.assertTrue(personResult.getSuccess());
        System.out.println("Found Jacob. Successful test!");
    }

    @Test
    @DisplayName("Failed Person Retrieval")
    public void negativePersonService(){
        System.out.println("\nBegin negative PersonService Person Retrieval...");

        // Registering User
        RegisterService registerService = new RegisterService(jacobUser);
        ResultMessage resultMessage = registerService.register();
        Assertions.assertTrue(resultMessage.getSuccess());

        // Searching with invalid personID
        System.out.println("Searching for Jacob");
        PersonService personService = new PersonService(resultMessage.getAuthtoken());
        ResultMessage personResult = personService.findPerson("Fake_Person_ID");
        Assertions.assertFalse(personResult.getSuccess());
        System.out.println("Did not Jacob. Successful test!");
    }

    @Test
    @DisplayName("Successful Event retrieval")
    public void positiveEvent() {
        System.out.println("\nBegin positive EventService event retrieval");

        // Load default data into table
        load();

        // Login to get auth token
        LoginService loginService = new LoginService(new User("sheila", "parker", null, null,
                null, null, null));
        ResultMessage resultMessage = loginService.login();
        assertTrue(resultMessage.getSuccess());

        // Retrieve that event
        EventService eventService = new EventService(resultMessage.getAuthtoken());
        ResultMessage eventResult = eventService.findEvent("Sheila_Asteroids");
        assertTrue(eventResult.getSuccess());
        System.out.println("Found event. Successful test!");
    }

    @Test
    @DisplayName("Failed Event retrieval")
    public void negativeEvent() {
        System.out.println("\nBegin negative EventService event retrieval");
        // Load default data into table
        load();

        // Login to get auth token
        LoginService loginService = new LoginService(new User("sheila", "parker", null, null,
                null, null, null));
        ResultMessage resultMessage = loginService.login();
        assertTrue(resultMessage.getSuccess());

        // Retrieve not that event, and fail
        EventService eventService = new EventService(resultMessage.getAuthtoken());
        ResultMessage eventResult = eventService.findEvent("Sheila_Water_Polo");
        assertFalse(eventResult.getSuccess());
        System.out.println("Couldn't find event. Successful test!");
    }

    @Test
    @DisplayName("Successful Load")
    public void positiveLoad() throws FileNotFoundException {
        System.out.println("\nBegin Positive Load...");

        JsonReader jsonReader = new JsonReader(new FileReader("passoffFiles/LoadData.json"));
        ReqRes.LoadRequest loadRequest = GSON.fromJson(jsonReader, ReqRes.LoadRequest.class);
        LoadService loadService = new LoadService();
        ResultMessage resultMessage = loadService.loadDataService(loadRequest);
        assertTrue(resultMessage.getSuccess());
        System.out.println("Loaded good data. Successful test!");
    }

    @Test
    @DisplayName("Failed Load")
    public void negativeLoad() {
        System.out.println("\nBegin Negative Load...");

        // Inserting bad data
        User[] users = new User[1];
        users[0] = new User(null, null, null, null, null, null, null);
        Person[] persons = new Person[0];
        Event[] events = new Event[0];
        ReqRes.LoadRequest loadRequest = new ReqRes.LoadRequest(users, persons, events);
        LoadService loadService = new LoadService();
        ResultMessage resultMessage = loadService.loadDataService(loadRequest);
        assertFalse(resultMessage.getSuccess());
        System.out.println("Did not load bad data. Successful test!");
    }

    @Test
    @DisplayName("Successful Fill")
    public void positiveFill() {
        System.out.println("\nBegin positive FillService");

        // Creating a user to fill
        RegisterService registerService = new RegisterService(jacobUser);
        ResultMessage resultMessage = registerService.register();
        Assertions.assertTrue(resultMessage.getSuccess());

        // Test out filling
        FillService fillService = new FillService(2, jacobUser.getUsername(), databaseUtil);
        ResultMessage fillResult = fillService.FillGenerations();
        assertTrue(fillResult.getSuccess());
        System.out.println("Filled for user. Successful test!");
    }

    @Test
    @DisplayName("Failed Fill")
    public void negativeFill() {
        System.out.println("\nBegin negative EventService event retrieval");

        // Try to fill nonexistent user, fail
        FillService fillService = new FillService(2, "invalid_user", databaseUtil);
        ResultMessage fillResult = fillService.FillGenerations();
        assertFalse(fillResult.getSuccess());
        System.out.println("Did not fill for nonexistent user. Successful test!");
    }

    @Test
    @DisplayName("Successful Clear")
    public void positiveClear(){
        load();
        ClearService clearService = new ClearService();
        ResultMessage resultMessage = clearService.clear();
        assertTrue(resultMessage.getSuccess());
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
