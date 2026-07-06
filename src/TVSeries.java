/**
 * Serves as the object class for TV series. Contains attributes such as the media type, 
 * the total episodes, the watched episodes, and the number of seasons.
 * The class also contains methods that updates episodes watched, returns the media type, 
 * as well as a summary of the TV series, which contains all aforementioned attributes. 
 * 
 * @author Simon Jaxith T. Martinez
 */
public class TVSeries {
    private MediaEntry entry;
    private int totalEpisodes;
    private int watchedEpisodes;
    private int seasonCount;

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
        this.entry = entry;
        this.totalEpisodes = totalEps;
        this.watchedEpisodes = watchedEps;
        this.seasonCount = seasonCount;
    }

    /**
     * Updates the episodes watched. Returns true or false if the update was successful. 
     * 
     * @param episodes the episode watched
     * @return if the update was successful (episodes is valid)
     */
    public boolean updateWatchedEpisodes(int episodes) {
        boolean status = true;
        if (episodes >= 0 && episodes <= totalEpisodes)
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
        return this.entry;
    }

    /**
     * Returns a string with the TV series' attributes, including status if it was watched and ratings. 
     * 
     * @return the TV series' details
     */
    public String getSeriesDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: TV Series").append("\n");
        word.append("Title: ").append(entry.getTitle()).append("\n");
        word.append("Genre: ").append(entry.getGenre()).append("\n");
        word.append("Total Episodes: ").append(this.totalEpisodes).append("\n");
        word.append("Watched Episodes: ").append(this.watchedEpisodes).append("\n");
        word.append("Seasons: ").append(this.seasonCount).append("\n");
        word.append("Status: ").append(entry.getStatus()).append("\n");
        if (entry.getRating() != null)
            word.append("Rating: ").append(entry.getRating()).append("/10\n");
        return word.toString();
    }
}
