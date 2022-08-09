package DAO_Models;

/** The Event object */
public class Event {
    /** String for the eventID */
    private String eventID;
    /** String associated username */
    private String associatedUsername;
    /** String for the personID */
    private String personID;
    /** Decimal for the latitude */
    private double latitude;
    /** Decimal for the longitude */
    private double longitude;
    /** String for the country name */
    private String country;
    /** String for the event city */
    private String city;
    /** String for the event type */
    private String eventType;
    /** Number for the event year */
    private int year;

    /**
     * Constructor for an Event object
     * @param eventID The given eventID
     * @param associatedUsername The given
     * @param personID The given personID
     * @param latitude The given latitude
     * @param longitude The given longitude
     * @param country The given country
     * @param city The given city
     * @param eventType The given eventType
     * @param year The given year
     */
    public Event(String eventID, String associatedUsername, String personID, double latitude, double longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    /**
     * Constructor for an empty event object
     */
    public Event(){}

    public String getEventID() { return eventID; }
    public void setEventID(String eventID) { this.eventID = eventID; }

    public String getAssociatedUsername() { return associatedUsername; }
    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public String getPersonID() { return personID; }
    public void setPersonID(String personID) { this.personID = personID; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

}
