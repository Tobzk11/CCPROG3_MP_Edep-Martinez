/**
 * Serves as the object class for TV series. Contains attributes such as the media type, 
 * the total episodes, the watched episodes, and the number of seasons.
 * The class also contains methods that updates episodes watched, returns the media type, 
 * as well as a summary of the TV series, which contains all aforementioned attributes. 
 * 
 * @author Simon Jaxith T. Martinez
 */
public class TVSeries {
    private final MediaEntry ENTRY;
    private final int TOTAL_EPISODES;
    private int watchedEpisodes;
    private final int SEASON_COUNT;

    /**
     * Constructs a TVSeries with entry, totalEpisodes, watchedEpisodes and seasonCount passed into parameter. 
     * 
     * @param entry TV series
     * @param totalEps total episodes of the series
     * @param watchedEps episodes watched
     * @param seasonCount number of seasons
     *
     */
    public TVSeries(MediaEntry entry, int totalEps, int watchedEps, int seasonCount) {
        this.ENTRY = entry;
        this.TOTAL_EPISODES = totalEps;
        this.watchedEpisodes = watchedEps;
        this.SEASON_COUNT = seasonCount;
    }

    /**
     * Updates the episodes watched. Returns true or false if the update was successful. 
     * 
     * @param episodes the episode watched
     * @return true if the update was successful, false if episode is not positive or greater than total episodes
     */
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

    /**
     * Returns the kind of media entry. 
     * 
     * @return the kind of media (TV series)
     */
    public MediaEntry getEntry() {
        return this.ENTRY;
    }

    /**
     * Returns a string with the TV series' attributes, including status if it was watched and ratings. 
     * 
     * @return the TV series' details
     */
    public String getSeriesDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: TV Series").append("\n");
        word.append("Title: ").append(ENTRY.getTitle()).append("\n");
        word.append("Genre: ").append(ENTRY.getGenre()).append("\n");
        word.append("Total Episodes: ").append(this.TOTAL_EPISODES).append("\n");
        word.append("Watched Episodes: ").append(this.watchedEpisodes).append("\n");
        word.append("Seasons: ").append(this.SEASON_COUNT).append("\n");
        word.append("Status: ").append(ENTRY.getStatus()).append("\n");
        if (ENTRY.getRating() != null)
            word.append("Rating: ").append(ENTRY.getRating()).append("/10\n");
        return word.toString();
    }
}
