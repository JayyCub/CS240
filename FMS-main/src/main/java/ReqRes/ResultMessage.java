package ReqRes;

public class ResultMessage {

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

    public boolean getSuccess(){
        return this.success;
    }
}
