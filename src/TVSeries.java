public class TVSeries extends MediaEntry {

    private final int TOTAL_EPISODES;
    private int watchedEpisodes;
    private final int SEASON_COUNT;

    public TVSeries(String title, String genre, MediaStatus status,
                    int totalEps, int watchedEps, int seasonCount) {
        super(title, genre, status);
        this.TOTAL_EPISODES = totalEps;
        this.watchedEpisodes = watchedEps;
        this.SEASON_COUNT = seasonCount;
    }

    public boolean updateWatchedEpisodes(int episodes) {
        boolean status = true;
        if (episodes >= 0 && episodes <= TOTAL_EPISODES)
            watchedEpisodes = episodes;
        else {
            System.out.println("Invalid episode. ");
            status = false;
        }
        return status;
    }

    public String getMediaType() {
        return "TVSeries";
    }

    public String getDetails() {
        return getSeriesDetails();
    }

    public String getSeriesDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: TV Series").append("\n");
        word.append("Title: ").append(TITLE).append("\n");
        word.append("Genre: ").append(GENRE).append("\n");
        word.append("Total Episodes: ").append(this.TOTAL_EPISODES).append("\n");
        word.append("Watched Episodes: ").append(this.watchedEpisodes).append("\n");
        word.append("Seasons: ").append(this.SEASON_COUNT).append("\n");
        word.append("Status: ").append(status).append("\n");
        if (rating != null) {
            word.append("Rating: ").append(rating).append("/10\n");
            word.append("Review: ").append(review).append("\n");
        }
        return word.toString();
    }
}