/**
 * Represents a single feature film in a MediaVault library. In addition to the
 * attributes every media entry carries, a Movie records its director, its
 * runtime in minutes, and the year it was released.
 */
public class Movie extends MediaEntry {

    private final String DIRECTOR;
    private final int DURATION_MINUTES;
    private final int RELEASE_YEAR;

    /**
     * Constructs a new Movie with the given shared and movie-specific details.
     * <p>
     * <b>Precondition:</b> durationMinutes and releaseYear are positive <br>
     * <b>Postcondition:</b> the Movie is created with no rating or review
     * </p>
     *
     * @param title           the title of the movie
     * @param genre           the genre of the movie
     * @param status          the initial status of the movie
     * @param director        the name of the movie's director
     * @param durationMinutes the runtime of the movie in minutes
     * @param releaseYear     the year the movie was released
     */
    public Movie(String title, String genre, MediaStatus status,
                 String director, int durationMinutes, int releaseYear) {
        super(title, genre, status);
        this.DIRECTOR = director;
        this.DURATION_MINUTES = durationMinutes;
        this.RELEASE_YEAR = releaseYear;
    }

    /**
     * Returns the name of this movie's director.
     *
     * @return the director
     */
    public String getDirector() {
        return this.DIRECTOR;
    }

    /**
     * Returns the runtime of this movie in minutes.
     *
     * @return the duration in minutes
     */
    public int getDurationMinutes() {
        return this.DURATION_MINUTES;
    }

    /**
     * Returns the year this movie was released.
     *
     * @return the release year
     */
    public int getReleaseYear() {
        return this.RELEASE_YEAR;
    }

    /**
     * Returns the media type label for movies.
     *
     * @return the String "Movie"
     */
    @Override
    public String getMediaType() {
        return "Movie";
    }

    /**
     * Returns the full formatted details of this movie by delegating to
     * getMovieDetails.
     *
     * @return a formatted multi-line description of this movie
     */
    @Override
    public String getDetails() {
        return getMovieDetails();
    }

    /**
     * Describes progress on a movie. A film is watched in one sitting rather
     * than in parts, so the description is drawn from the entry's status.
     *
     * @return a one-line progress description for this movie
     */
    @Override
    public String getProgressDescription() {
        String description;

        if (this.status == MediaStatus.COMPLETED) {
            description = "Watched (" + this.DURATION_MINUTES + " min runtime)";
        } else if (this.status == MediaStatus.IN_PROGRESS) {
            description = "Currently watching (" + this.DURATION_MINUTES + " min runtime)";
        } else {
            description = "Not yet watched (" + this.DURATION_MINUTES + " min runtime)";
        }

        return description;
    }

    /**
     * Builds the full multi-line description of this movie, listing every
     * shared and movie-specific attribute. The rating and review lines appear
     * only once the movie has actually been rated.
     *
     * @return a formatted multi-line description of this movie
     */
    public String getMovieDetails() {
        StringBuilder word = new StringBuilder();

        word.append("Media: Movie").append("\n");
        word.append("Title: ").append(TITLE).append("\n");
        word.append("Genre: ").append(GENRE).append("\n");
        word.append("Director: ").append(this.DIRECTOR).append("\n");
        word.append("Duration: ").append(this.DURATION_MINUTES).append(" min\n");
        word.append("Release Year: ").append(this.RELEASE_YEAR).append("\n");
        word.append("Status: ").append(status).append("\n");
        word.append("Progress: ").append(getProgressDescription()).append("\n");

        if (rating != null) {
            word.append("Rating: ").append(rating).append("/10\n");
            word.append("Review: ").append(review).append("\n");
        }

        return word.toString();
    }
}