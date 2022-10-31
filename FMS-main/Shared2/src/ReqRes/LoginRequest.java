package ReqRes;

/**
 * This class will structure the data passed in with a login API request
 */
public class LoginRequest {
    /**
     * String to hold the username given in the API request
     */
    private String username;
    /**
     * String to hold to given password in the API request
     */
    private String password;

    /**
     * Constructor to create a LoginRequest object
     * @param username
     * @param password
     */
    public LoginRequest(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}