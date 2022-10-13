package ReqRes;

/**
 * This class structures the data needed in an API register user request
 */
public class RegisterRequest {
    /** These variables hold the information given in a user register request */
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private char gender;

    /**
     * Constructor to create a RegisterRequest object with the given data
     * @param username
     * @param password
     * @param email
     * @param firstName
     * @param lastName
     * @param gender
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, char gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public char getGender() {
        return gender;
    }

}
