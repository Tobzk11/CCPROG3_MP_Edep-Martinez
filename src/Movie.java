public class Movie extends MediaEntry {

    private final String DIRECTOR;
    private final int DURATION_MINUTES;
    private final int RELEASE_YEAR;

    public Movie(String title, String genre, MediaStatus status,
                 String director, int durationMinutes, int releaseYear) {
        super(title, genre, status);
        this.DIRECTOR = director;
        this.DURATION_MINUTES = durationMinutes;
        this.RELEASE_YEAR = releaseYear;
    }

    public String getMediaType() {
        return "Movie";
    }

    public String getDetails() {
        return getMovieDetails();
    }

    public String getMovieDetails() {
        StringBuilder word = new StringBuilder();
        word.append("Media: Movie").append("\n");
        word.append("Title: ").append(TITLE).append("\n");
        word.append("Genre: ").append(GENRE).append("\n");
        word.append("Director: ").append(this.DIRECTOR).append("\n");
        word.append("Duration: ").append(this.DURATION_MINUTES).append(" min\n");
        word.append("Release Year: ").append(this.RELEASE_YEAR).append("\n");
        word.append("Status: ").append(status).append("\n");
        if (rating != null) {
            word.append("Rating: ").append(rating).append("/10\n");
            word.append("Review: ").append(review).append("\n");
        }
        return word.toString();
    }
}