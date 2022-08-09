package DAO_Models;

/**
 * AuthToken. Used to validate user actions
 */
public class AuthToken {
    /** The authoken string **/
    private String authToken;
    /** The username associaed with the AuthToken **/
    private String username;
    /** PersonID associated with an authtoken **/
    private String personID;

    /**
     * Constructor to create a AuthToken object and assign its variables
     * @param authToken The given AuthToken
     * @param username The given username
     * @param personID The given personID
     */
    public AuthToken(String authToken, String username, String personID){
        this.authToken = authToken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthToken() { return authToken; }
    public void setAuthToken(String authToken) { this.authToken = authToken; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPersonID(){ return personID; }
    public void setPersonID(String personID) { this.personID = personID; }
}
