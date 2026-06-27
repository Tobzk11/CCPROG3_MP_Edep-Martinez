public class Movie {
    private MediaEntry entry;
    private String director;
    private int durationMinutes;
    private int releaseYear;

    public Movie(MediaEntry entry, String director, int durationMinutes, int releaseYear) {
        this.entry = entry;
        this.director = director;
        this.durationMinutes = durationMinutes;
        this.releaseYear = releaseYear;
    }

    public MediaEntry getEntry() {
        return this.entry;
    }

    public String getMovieDetails() {
        return this.director; // change later!
    }
}
