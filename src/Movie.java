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
        word.append("Title: ").append(entry.getTitle()).append("\n");
        word.append("Genre: ").append(entry.getGenre()).append("\n");  // needs getGenre()
        word.append("Director: ").append(this.director).append("\n");
        word.append("Duration: ").append(this.durationMinutes).append(" min\n");
        word.append("Release Year: ").append(this.releaseYear).append("\n");
        word.append("Status: ").append(entry.getStatus()).append("\n");
        if (entry.getRating() != null)
            word.append("Rating: ").append(entry.getRating()).append("/10\n");
        return word.toString();
    }
}
