package DAO_Models;

/** The Person object */
public class Person {
    /** String for the personID */
    private String personID;
    /** String for the associatedUsername */
    private String associatedUsername;
    /** String for the person's first name */
    private String firstName;
    /** String for the person's last name */
    private String lastName;
    /** String for the person's gender, expected to be "m" or "f" */
    private String gender;
    /** String for the fatherID */
    private String fatherID;
    /** String for the motherID */
    private String motherID;
    /** String for the spouseID */
    private String spouseID;

    /**
     * Constructor for an empty Person object
     */
    public Person(){}

    /**
     * Constructor that creates and ses all values of a Person object.
     * @param personID Given personID string
     * @param associatedUsername Given associatedUsername string
     * @param firstName Given firstName string
     * @param lastName Given lastName string
     * @param gender Given gender string ('m' or 'f')
     * @param fatherID Given fatherID string
     * @param motherID Given motherID string
     * @param spouseID Given spouseID string
     */
    public Person (String personID, String associatedUsername, String firstName, String lastName, String gender,
                   String fatherID, String motherID, String spouseID){
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;

    }

    public String getPersonID() { return personID; }
    public void setPersonID(String personID) { this.personID = personID; }

    public String getAssociatedUsername() { return associatedUsername; }
    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName;}
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getFatherID() { return fatherID; }
    public void setFatherID(String fatherID) { this.fatherID = fatherID; }

    public String getMotherID() { return motherID; }
    public void setMotherID(String motherID) { this.motherID = motherID; }

    public String getSpouseID() { return spouseID; }
    public void setSpouseID(String spouseID) { this.spouseID = spouseID; }

}
