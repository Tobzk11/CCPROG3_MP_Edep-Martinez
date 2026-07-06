/**
 * Serves as the object class for movies. Contains attributes such as the media type, 
 * the director, the duration of the film in minutes, and the year it was released.
 * The class also contains methods that returns the media type, as well as a summary of the movie,
 * which contains all aforementioned attributes. 
 * 
 * @author Simon Jaxith T. Martinez
 */
public class Movie {
    private MediaEntry entry;
    private String director;
    private int durationMinutes;
    private int releaseYear;

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
        this.entry = entry;
        this.director = director;
        this.durationMinutes = durationMinutes;
        this.releaseYear = releaseYear;
    }

    /**
     * Returns the kind of media entry. 
     * 
     * @return the kind of media (movie)
     */
    public MediaEntry getEntry() {
        return this.entry;
    }

    /**
     * Returns a string with the movie's attributes, including status if it was watched and ratings. 
     * 
     * @return the movie's details
     */
    public String getMovieDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: Movie").append("\n");
        word.append("Title: ").append(entry.getTitle()).append("\n");
        word.append("Genre: ").append(entry.getGenre()).append("\n");
        word.append("Director: ").append(this.director).append("\n");
        word.append("Duration: ").append(this.durationMinutes).append(" min\n");
        word.append("Release Year: ").append(this.releaseYear).append("\n");
        word.append("Status: ").append(entry.getStatus()).append("\n");
        if (entry.getRating() != null)
            word.append("Rating: ").append(entry.getRating()).append("/10\n");
        return word.toString();
    }
}
