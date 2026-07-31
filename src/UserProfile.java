/**
 * Represents a single MediaVault user, pairing a username with the media
 * library that belongs to them.
 * <p>
 * Two constructors are offered. The no-library constructor creates a fresh
 * empty Library that the profile owns outright. The second constructor lets a
 * profile be attached to a Library that already exists, which is what the
 * controller uses when a user logs in and their saved entries have just been
 * read back into the working library.
 * </p>
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
     * Constructs a UserProfile for the given username around a Library that
     * already exists. Used when a saved library has just been loaded from
     * file and should now be associated with the user who owns it.
     * <p>
     * <b>Precondition:</b> username and library are not null <br>
     * <b>Postcondition:</b> the profile refers to the given Library
     * </p>
     *
     * @param username the username of this profile
     * @param library  the Library this profile should be attached to
     */
    public UserProfile(String username, Library library) {
        this.USERNAME = username;
        this.LIBRARY = library;
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
     * Returns the media library belonging to this profile.
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