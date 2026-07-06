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
    private MediaEntry entry;
    private String platform;
    private String requiredSpecs;
    private String developer;
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
        this.entry = entry;
        this.platform = platform;
        this.requiredSpecs = requiredSpecs;
        this.developer = developer;
        this.hoursPlayed = hoursPlayed;
    }

    /**
     * Updates by adding the number of hours played. Returns true or false if the update was successful. 
     * 
     * @param hours the episode watched
     * @return if the update was successful (hours is valid)
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
        return this.entry;
    }

    /**
     * Returns a string with the video game's attributes, including ratings. 
     * 
     * @return the video game's details
     */
    public String getGameDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: Video Game").append("\n");
        word.append("Title: ").append(entry.getTitle()).append("\n");
        word.append("Genre: ").append(entry.getGenre()).append("\n");
        word.append("Platform: ").append(this.platform).append("\n");
        word.append("Required Specs: ").append(this.requiredSpecs).append("\n");
        word.append("Developer: ").append(this.developer).append("\n");
        word.append("Hours Played: ").append(this.hoursPlayed).append("\n");
        word.append("Status: ").append(entry.getStatus()).append("\n");
        if (entry.getRating() != null)
            word.append("Rating: ").append(entry.getRating()).append("/10\n");
        return word.toString();
    }
}
