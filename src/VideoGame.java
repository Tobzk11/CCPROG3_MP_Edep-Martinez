/**
 * Represents a video game in a MediaVault library. On top of the attributes
 * every media entry carries, a VideoGame records the platform it runs on, the
 * hardware it requires, its developer, and how many hours have been played.
 */
public class VideoGame extends MediaEntry {

    private final String PLATFORM;
    private final String REQUIRED_SPECS;
    private final String DEVELOPER;
    private double hoursPlayed;

    /**
     * Constructs a new VideoGame with the given shared and game-specific
     * details.
     * <p>
     * <b>Precondition:</b> hoursPlayed is not negative <br>
     * <b>Postcondition:</b> the VideoGame is created with no rating or review
     * </p>
     *
     * @param title         the title of the game
     * @param genre         the genre of the game
     * @param status        the initial status of the game
     * @param platform      the platform the game runs on
     * @param requiredSpecs the hardware specifications the game requires
     * @param developer     the studio that developed the game
     * @param hoursPlayed   the number of hours already played
     */
    public VideoGame(String title, String genre, MediaStatus status,
                     String platform, String requiredSpecs,
                     String developer, double hoursPlayed) {
        super(title, genre, status);
        this.PLATFORM = platform;
        this.REQUIRED_SPECS = requiredSpecs;
        this.DEVELOPER = developer;
        this.hoursPlayed = hoursPlayed;
    }

    /**
     * Updates how many hours of this game have been played. The update is
     * rejected when the given value is negative.
     * <p>
     * <b>Postcondition:</b> hoursPlayed is updated only when the given value
     * is not negative; otherwise the game is left unchanged
     * </p>
     *
     * @param hours the new number of hours played
     * @return true if the value was updated, false if it was negative
     */
    public boolean updateHoursPlayed(double hours) {
        boolean updated = false;

        if (hours >= 0 && hours > this.hoursPlayed) {
            this.hoursPlayed = hours;
            updated = true;
        }

        return updated;
    }

    /**
     * Returns the platform this game runs on.
     *
     * @return the platform
     */
    public String getPlatform() {
        return this.PLATFORM;
    }

    /**
     * Returns the hardware specifications this game requires.
     *
     * @return the required specifications
     */
    public String getRequiredSpecs() {
        return this.REQUIRED_SPECS;
    }

    /**
     * Returns the studio that developed this game.
     *
     * @return the developer
     */
    public String getDeveloper() {
        return this.DEVELOPER;
    }

    /**
     * Returns the number of hours this game has been played.
     *
     * @return the hours played
     */
    public double getHoursPlayed() {
        return this.hoursPlayed;
    }

    /**
     * Returns the media type label for video games.
     *
     * @return the String "VideoGame"
     */
    @Override
    public String getMediaType() {
        return "VideoGame";
    }

    /**
     * Returns the full formatted details of this game by delegating to
     * getGameDetails.
     *
     * @return a formatted multi-line description of this game
     */
    @Override
    public String getDetails() {
        return getGameDetails();
    }

    /**
     * Describes progress on this game as the number of hours played, since a
     * game has no fixed end point the way an episode count does.
     *
     * @return a one-line progress description for this game
     */
    @Override
    public String getProgressDescription() {
        return String.format("%.1f", this.hoursPlayed) + " hour(s) played";
    }

    /**
     * Builds the full multi-line description of this game, listing every
     * shared and game-specific attribute. The rating and review lines appear
     * only once the game has actually been rated.
     *
     * @return a formatted multi-line description of this game
     */
    public String getGameDetails() {
        StringBuilder word = new StringBuilder();

        word.append("Media: Video Game").append("\n");
        word.append("Title: ").append(TITLE).append("\n");
        word.append("Genre: ").append(GENRE).append("\n");
        word.append("Platform: ").append(this.PLATFORM).append("\n");
        word.append("Required Specs: ").append(this.REQUIRED_SPECS).append("\n");
        word.append("Developer: ").append(this.DEVELOPER).append("\n");
        word.append("Hours Played: ").append(this.hoursPlayed).append("\n");
        word.append("Status: ").append(status).append("\n");
        word.append("Progress: ").append(getProgressDescription()).append("\n");

        if (rating != null) {
            word.append("Rating: ").append(rating).append("/10\n");
            word.append("Review: ").append(review).append("\n");
        }

        return word.toString();
    }
}