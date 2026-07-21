public class VideoGame extends MediaEntry {

    private final String PLATFORM;
    private final String REQUIRED_SPECS;
    private final String DEVELOPER;
    private double hoursPlayed;

    public VideoGame(String title, String genre, MediaStatus status,
                     String platform, String requiredSpecs,
                     String developer, double hoursPlayed) {
        super(title, genre, status);
        this.PLATFORM = platform;
        this.REQUIRED_SPECS = requiredSpecs;
        this.DEVELOPER = developer;
        this.hoursPlayed = hoursPlayed;
    }

    public boolean updateHoursPlayed(double hours) {
        boolean updated = false;
        if (hours < 0)
            System.out.println("Invalid hours played.");
        else {
            this.hoursPlayed = hours;
            updated = true;
        }
        return updated;
    }

    public String getMediaType() {
        return "VideoGame";
    }

    public String getDetails() {
        return getGameDetails();
    }

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
        if (rating != null) {
            word.append("Rating: ").append(rating).append("/10\n");
            word.append("Review: ").append(review).append("\n");
        }
        return word.toString();
    }
}