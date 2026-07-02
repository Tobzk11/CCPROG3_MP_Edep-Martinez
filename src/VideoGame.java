public class VideoGame {
    private MediaEntry entry;
    private String platform;
    private String requiredSpecs;
    private String developer;
    private double hoursPlayed;

    public VideoGame(MediaEntry entry, String platform, String requiredSpecs, String developer, double hoursPlayed) {
        this.entry = entry;
        this.platform = platform;
        this.requiredSpecs = requiredSpecs;
        this.developer = developer;
        this.hoursPlayed = hoursPlayed;
    }

    public boolean updateHoursPlayed(double hours) {
        if (hours <= 0) {
            System.out.println("Invalid hours played.");
            return false;
        }
        this.hoursPlayed = hours;
        return true;
    }

    public MediaEntry getEntry() {
        return this.entry;
    }

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
