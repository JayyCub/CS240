package DAO_Models;

/** The User class */
public class User {
    /** String for the User username */
    private String username;
    /** String for the User password */
    private String password;
    /** String for the User email */
    private String email;
    /** String for the User first name */
    private String firstName;
    /** String for the User last name */
    private String lastName;
    /** String for the User gender */
    private String gender;
    /** String for the User personID */
    private String personID;

    /**
     * Constructor for the User that assigns params to variables
     * @param username String for the username
     * @param password String for the password
     * @param email String for the email
     * @param firstName String for the first name
     * @param lastName String for the last name
     * @param gender String for the gender
     * @param personID String for the personID
     */
    public User(String username, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public void setUsername(String username) {this.username = username;}
    public String getUsername() {return username;}

    public void setPassword(String password) {this.password = password;}
    public String getPassword() {return password;}

    public void setEmail(String email) {this.email = email;}
    public String getEmail() {return email;}

    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getFirstName() {return firstName;}

    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getLastName() {return lastName;}

    public void setGender(String gender) {this.gender = gender;}
    public String getGender() {return gender;}

    public void setPersonID(String personID) {this.personID = personID;}
    public String getPersonID() {return personID;}
}