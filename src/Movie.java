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
        StringBuilder word = new StringBuilder();
        word.append("Media: Movie").append("\n");
        word.append("Director: ").append(this.director).append("\n");
        word.append("Duration: ").append(this.durationMinutes).append("\n");

        return word.toString();
    }
}
