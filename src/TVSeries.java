public class TVSeries {
    private MediaEntry entry;
    private int totalEpisodes;
    private int watchedEpisodes;
    private int seasonCount;

    public TVSeries(MediaEntry entry, int totalEps, int watchedEps, int seasonCount) {
        this.entry = entry;
        this.totalEpisodes = totalEps;
        this.watchedEpisodes = watchedEps;
        this.seasonCount = seasonCount;
    }

    public boolean updateWatchedEpisodes(int episodes) {
        return true; // change later!
    }

    public MediaEntry getEntry() {
        return this.entry;
    }

    public String getSeriesDetails() {
        return "hi"; // change later!
    }
}
