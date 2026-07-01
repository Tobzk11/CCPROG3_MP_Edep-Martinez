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
        boolean status = true;
        if (episodes > 0 && episodes <= totalEpisodes)
            watchedEpisodes = episodes;
        else {
            System.out.println("Invalid episodes. ");
            status = false;
        }
        return status;
    }

    public MediaEntry getEntry() {
        return this.entry;
    }

    public String getSeriesDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: TV Series").append("\n");
        word.append("Total Episodes: ").append(this.totalEpisodes).append("\n");
        word.append("Watched Episodes: ").append(this.watchedEpisodes).append("\n");
        word.append("Seasons: ").append(this.seasonCount).append("\n");

        return word.toString();
    }
}
