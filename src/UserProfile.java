/**
 * Represents a single MediaVault user, pairing a username with the media
 * library that belongs to them. The library is created inside the constructor
 * and is never handed in from outside, so a library cannot outlive the profile
 * that owns it.
 */
public class UserProfile {

    private final String USERNAME;
    private final Library LIBRARY;

    /**
     * Constructs a UserProfile with the given username and a new empty library.
     * <p>
     * <b>Precondition:</b> username is not null <br>
     * <b>Postcondition:</b> the profile owns a newly created empty Library
     * </p>
     *
     * @param username the username of this profile
     */
    public UserProfile(String username) {
        this.USERNAME = username;
        this.LIBRARY = new Library();
    }

    /**
     * Returns the username of this profile.
     *
     * @return the username
     */
    public String getUsername() {
        return this.USERNAME;
    }

    /**
     * Returns the media library owned by this profile.
     *
     * @return the Library of this user
     */
    public Library getLibrary() {
        return this.LIBRARY;
    }

    /**
     * Builds a greeting for this user followed by a summary of their library.
     * The text is returned rather than printed so that the View decides how
     * and where it is displayed.
     * <p>
     * <b>Postcondition:</b> the profile and its library are unchanged
     * </p>
     *
     * @return a String containing the greeting and the library summary
     */
    public String viewSummary() {
        return "Hello, " + this.USERNAME + "!\n" + this.LIBRARY.getSummary();
    }
}