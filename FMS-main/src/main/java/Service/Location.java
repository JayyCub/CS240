package Service;

/**
 * Class to structure location data
 */
public class Location {
    /** String with country name */
    private String country;
    /** String with city name */
    private String city;
    /** Decmial for latitude */
    private double latitude;
    /** Decimal for longitude */
    private double longitude;

    /**
     * Constructor to initialize variables
     * @param country String, country name
     * @param city String, city name
     * @param latitude Double, latitude
     * @param longitude Double, longitude
     */
    public Location(String country, String city, double latitude, double longitude) {
        this.country = country;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCountry() { return country; }

    public String getCity() { return city; }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

}
