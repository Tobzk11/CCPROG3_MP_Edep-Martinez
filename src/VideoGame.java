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
        boolean status = true;
        if (hours > 0)
            this.hoursPlayed += hours;
        else
            System.out.println("Invalid hours played.");
        return status;
    }

    public MediaEntry getEntry() {
        return this.entry;
    }

    public String getGameDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: Video Game").append("\n");
        word.append("Available On: ").append(this.platform).append("\n");
        word.append("Required Specs: ").append(this.requiredSpecs).append("\n");
        word.append("Developer: ").append(this.developer).append("\n");
        word.append("Hours Played: ").append(this.hoursPlayed).append("\n");

        return word.toString();
    }
}
