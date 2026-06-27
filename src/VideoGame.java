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
        return true; // change this!
    }

    public MediaEntry getEntry() {
        return this.entry;
    }

    public String getGameDetails() {
        return "hi"; // change this!
    }
}
