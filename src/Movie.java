/**
 * Serves as the object class for movies. Contains attributes such as the media type, 
 * the director, the duration of the film in minutes, and the year it was released.
 * The class also contains methods that returns the media type, as well as a summary of the movie,
 * which contains all aforementioned attributes. 
 * 
 * @author Simon Jaxith T. Martinez
 */
public class Movie {
    private final MediaEntry ENTRY;
    private final String DIRECTOR;
    private final int DURATION_MINUTES;
    private final int RELEASE_YEAR;

    /**
     * Constructs a Movie with entry, director, durationMinutes and releaseYear passed into parameter. 
     * 
     * @param entry movie
     * @param director director
     * @param durationMinutes the duration of the movie in minutes
     * @param releaseYear the year the movie was released
     *
     */
    public Movie(MediaEntry entry, String director, int durationMinutes, int releaseYear) {
        this.ENTRY = entry;
        this.DIRECTOR = director;
        this.DURATION_MINUTES = durationMinutes;
        this.RELEASE_YEAR = releaseYear;
    }

    /**
     * Returns the kind of media entry. 
     * 
     * @return the kind of media (movie)
     */
    public MediaEntry getEntry() {
        return this.ENTRY;
    }

    /**
     * Returns a string with the movie's attributes, including status if it was watched and ratings. 
     * 
     * @return the movie's details
     */
    public String getMovieDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: Movie").append("\n");
        word.append("Title: ").append(ENTRY.getTitle()).append("\n");
        word.append("Genre: ").append(ENTRY.getGenre()).append("\n");
        word.append("Director: ").append(this.DIRECTOR).append("\n");
        word.append("Duration: ").append(this.DURATION_MINUTES).append(" min\n");
        word.append("Release Year: ").append(this.RELEASE_YEAR).append("\n");
        word.append("Status: ").append(ENTRY.getStatus()).append("\n");
        if (ENTRY.getRating() != null)
            word.append("Rating: ").append(ENTRY.getRating()).append("/10\n");
        return word.toString();
    }
}
