/**
 * Serves as the object class for video games. Contains attributes such as the media type, 
 * the platforms playable on, the required specs to run the game, the developer, and the number
 * of hours played. The class also contains methods that updates the number of hours played, 
 * returns the media type, as well as a summary of the video games which contains all aforementioned 
 * attributes. 
 * 
 * @author Simon Jaxith T. Martinez
 */
public class VideoGame {
    private final MediaEntry ENTRY;
    private final String PLATFORM;
    private final String REQUIRED_SPECS;
    private final String DEVELOPER;
    private double hoursPlayed;

    /**
     * Constructs a VideoGame with entry, platform, requiredSpecs, developer and hoursPlayed 
     * passed into parameter. 
     * 
     * @param entry video game
     * @param platform platforms the game is available on
     * @param requiredSpecs the required specs to run the game
     * @param developer developer of the video game
     * @param hoursPlayed number of hours played on the game
     * 
     */
    public VideoGame(MediaEntry entry, String platform, String requiredSpecs, String developer, double hoursPlayed) {
        this.ENTRY = entry;
        this.PLATFORM = platform;
        this.REQUIRED_SPECS = requiredSpecs;
        this.DEVELOPER = developer;
        this.hoursPlayed = hoursPlayed;
    }

    /**
     * Updates by adding the number of hours played. Returns true or false if the update was successful. 
     * 
     * @param hours the episode watched
     * @return true if the update was successful, false if hours is not positive
     */
    public boolean updateHoursPlayed(double hours) {
        boolean updated = false;
        if (hours <= 0)
            System.out.println("Invalid hours played.");
        else {
            this.hoursPlayed = hours;
            updated = true;
        }
        return updated;
    }

    /**
     * Returns the kind of media entry. 
     * 
     * @return the kind of media (video game)
     */
    public MediaEntry getEntry() {
        return this.ENTRY;
    }

    /**
     * Returns a string with the video game's attributes, including ratings. 
     * 
     * @return the video game's details
     */
    public String getGameDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: Video Game").append("\n");
        word.append("Title: ").append(ENTRY.getTitle()).append("\n");
        word.append("Genre: ").append(ENTRY.getGenre()).append("\n");
        word.append("Platform: ").append(this.PLATFORM).append("\n");
        word.append("Required Specs: ").append(this.REQUIRED_SPECS).append("\n");
        word.append("Developer: ").append(this.DEVELOPER).append("\n");
        word.append("Hours Played: ").append(this.hoursPlayed).append("\n");
        word.append("Status: ").append(ENTRY.getStatus()).append("\n");
        if (ENTRY.getRating() != null) {
            word.append("Rating: ").append(ENTRY.getRating()).append("/10\n");
            word.append("Review: ").append(ENTRY.getReview()).append("\n");
        }
        return word.toString();
    }
}
