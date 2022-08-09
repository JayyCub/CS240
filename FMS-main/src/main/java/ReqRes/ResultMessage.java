package ReqRes;

/**
 * Class to structure the response messages given to the program user
 */
public class ResultMessage {
    /**
     * Variables to hold data or messages
     */
    private String authtoken;
    private String username;
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;
    private String eventID;
    private Double latitude;
    private Double longitude;
    private String country;
    private String city;
    private String eventType;
    private Integer year;
    private Object[] data;
    private String message;
    private boolean success;

    /**
     * Constructor to initialize response data variables
     * @param authtoken String
     * @param username String
     * @param personID String
     * @param associatedUsername String
     * @param firstName String
     * @param lastName String
     * @param gender String
     * @param fatherID String
     * @param motherID String
     * @param spouseID String
     * @param eventID String
     * @param latitude Double
     * @param longitude Double
     * @param country String
     * @param city String
     * @param eventType String
     * @param year Integer
     * @param data List of Objects
     * @param message String
     * @param success Boolean
     */
    public ResultMessage (String authtoken, String username, String personID, String associatedUsername,
                          String firstName, String lastName, String gender, String fatherID, String motherID,
                          String spouseID, String eventID, Double latitude, Double longitude, String country,
                          String city, String eventType, Integer year, Object[] data, String message, boolean success)
    {
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.eventID = eventID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.data = data;
        this.message = message;
        this.success = success;
    }

    /**
     * Get value of success, checks if resultMessage lists command as successful or not
     * @return Boolean, success
     */
    public boolean getSuccess(){
        return this.success;
    }
}
