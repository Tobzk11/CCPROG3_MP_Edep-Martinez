/**
 * Serves as the object class for user profiles. Contains attributes such as the username and the library
 * of media. The class also contains methods that returns the library of the user, as well as a summary of
 * the user's library. 
 * 
 * @author Simon Jaxith T. Martinez
 */
public class UserProfile {
    private final String USERNAME;
    private final Library LIBRARY;

    /**
     * Constructs a UserProfile with username passed into parameter and an empty library. 
     * 
     * @param username the username of the UserProfile instance.
     */
    public UserProfile(String username) {
        this.USERNAME = username;
        LIBRARY = new Library();
    }

    /**
     * Returns the media library of the user.
     * 
     * @return the library of the user
     */
    public Library getLibrary() {
        return this.LIBRARY;
    }

    /**
     * Prints a summary of the user's library. 
     * 
     */
    public void viewSummary() {
        System.out.printf("Hello, %s!\n", this.USERNAME);
        System.out.println(this.LIBRARY.getSummary());
    }
}
