package DAO_Models;

/**
 * AuthToken. Used to validate user actions
 */
public class AuthToken {
    /** The authoken string **/
    private String authtoken;
    /** The username associaed with the AuthToken **/
    private String username;

    /**
     * Constructor to create a AuthToken object and assign its variables
     * @param authToken The given AuthToken
     * @param username The given username
     */
    public AuthToken(String authToken, String username){
        this.authtoken = authToken;
        this.username = username;
    }

    public String getAuthToken() { return authtoken; }
    public void setAuthToken(String authToken) { this.authtoken = authToken; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
