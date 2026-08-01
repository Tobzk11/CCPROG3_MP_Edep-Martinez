/**
 * Represents an episodic television series in a MediaVault library. On top of
 * the attributes every media entry carries, a TVSeries tracks how many
 * episodes it contains, how many of those the user has watched, and how many
 * seasons it spans.
 */
public class TVSeries extends MediaEntry {

    private final int TOTAL_EPISODES;
    private final int SEASON_COUNT;
    private int watchedEpisodes;

    /**
     * Constructs a new TVSeries with the given shared and series-specific
     * details.
     * <p>
     * <b>Precondition:</b> totalEps and seasonCount are positive, and
     * watchedEps is between 0 and totalEps <br>
     * <b>Postcondition:</b> the TVSeries is created with no rating or review
     * </p>
     *
     * @param title       the title of the series
     * @param genre       the genre of the series
     * @param status      the initial status of the series
     * @param totalEps    the total number of episodes in the series
     * @param watchedEps  the number of episodes already watched
     * @param seasonCount the number of seasons in the series
     */
    public TVSeries(String title, String genre, MediaStatus status,
                    int totalEps, int watchedEps, int seasonCount) {
        super(title, genre, status);
        this.TOTAL_EPISODES = totalEps;
        this.watchedEpisodes = watchedEps;
        this.SEASON_COUNT = seasonCount;
    }

    /**
     * Updates how many episodes of this series have been watched. Viewing
     * progress only ever moves forward, so the update is rejected when the
     * given count is negative, exceeds the total number of episodes, or is not
     * greater than the count already recorded. Re-entering the current count
     * therefore counts as no progress and is rejected.
     * <p>
     * <b>Postcondition:</b> watchedEpisodes is updated only when the given
     * count is greater than the current count and at most the total number of
     * episodes; otherwise the series is left unchanged
     * </p>
     *
     * @param episodes the new number of watched episodes
     * @return true if the count was updated, false if it was out of range or
     *         would not move progress forward
     */
    public boolean updateWatchedEpisodes(int episodes) {
        boolean updated = false;

        if (episodes >= 0 && episodes <= this.TOTAL_EPISODES && episodes > this.watchedEpisodes) {
            this.watchedEpisodes = episodes;
            updated = true;
        }

        return updated;
    }

    /**
     * Returns the total number of episodes in this series.
     *
     * @return the total episode count
     */
    public int getTotalEpisodes() {
        return this.TOTAL_EPISODES;
    }

    /**
     * Returns the number of episodes of this series that have been watched.
     *
     * @return the watched episode count
     */
    public int getWatchedEpisodes() {
        return this.watchedEpisodes;
    }

    /**
     * Returns the number of seasons in this series.
     *
     * @return the season count
     */
    public int getSeasonCount() {
        return this.SEASON_COUNT;
    }

    /**
     * Returns the media type label for television series.
     *
     * @return the String "TVSeries"
     */
    @Override
    public String getMediaType() {
        return "TVSeries";
    }

    /**
     * Returns the full formatted details of this series by delegating to
     * getSeriesDetails.
     *
     * @return a formatted multi-line description of this series
     */
    @Override
    public String getDetails() {
        return getSeriesDetails();
    }

    /**
     * Describes progress on this series as a count of watched episodes out of
     * the total, along with the equivalent percentage.
     *
     * @return a one-line progress description for this series
     */
    @Override
    public String getProgressDescription() {
        String description;

        if (this.TOTAL_EPISODES <= 0) {
            description = this.watchedEpisodes + " episode(s) watched";
        } else {
            double percent = (double) this.watchedEpisodes / this.TOTAL_EPISODES * 100.0;
            description = this.watchedEpisodes + " of " + this.TOTAL_EPISODES
                    + " episodes watched (" + String.format("%.1f", percent) + "%)";
        }

        return description;
    }

    /**
     * Builds the full multi-line description of this series, listing every
     * shared and series-specific attribute. The rating and review lines appear
     * only once the series has actually been rated.
     *
     * @return a formatted multi-line description of this series
     */
    public String getSeriesDetails() {
        StringBuilder word = new StringBuilder();

        word.append("Media: TV Series").append("\n");
        word.append("Title: ").append(TITLE).append("\n");
        word.append("Genre: ").append(GENRE).append("\n");
        word.append("Total Episodes: ").append(this.TOTAL_EPISODES).append("\n");
        word.append("Watched Episodes: ").append(this.watchedEpisodes).append("\n");
        word.append("Seasons: ").append(this.SEASON_COUNT).append("\n");
        word.append("Status: ").append(status).append("\n");
        word.append("Progress: ").append(getProgressDescription()).append("\n");

        if (rating != null) {
            word.append("Rating: ").append(rating).append("/10\n");
            word.append("Review: ").append(review).append("\n");
        }

        return word.toString();
    }
}