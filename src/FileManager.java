import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles saving a Library to a plain text file and rebuilding a Library from
 * that file. Keeping this logic in its own class means the model classes stay
 * free of any knowledge of how they are stored on disk.
 * <p>
 * Each entry occupies one line, with fields separated by a vertical bar. The
 * first field is always the media type label, which is what allows the parser
 * to decide whether a line should become a Movie, a TVSeries, or a VideoGame:
 * </p>
 * <pre>
 * Movie|title|genre|status|rating|review|director|duration|releaseYear
 * TVSeries|title|genre|status|rating|review|totalEps|watchedEps|seasons
 * VideoGame|title|genre|status|rating|review|platform|specs|developer|hours
 * </pre>
 * <p>
 * An empty rating field means the entry has not been rated. Any vertical bar
 * or line break typed by the user is replaced with a space before writing, so
 * that a single entry can never spill across field or line boundaries.
 * </p>
 */
public class FileManager {

    /** The character used to separate fields within a saved line. */
    public static final String DELIMITER = "|";

    /** The regular expression form of the delimiter, for splitting lines. */
    private static final String SPLIT_PATTERN = "\\|";

    private final String FILE_NAME;
    private int skippedLineCount;

    /**
     * Constructs a FileManager bound to the given file.
     * <p>
     * <b>Precondition:</b> fileName is not null <br>
     * <b>Postcondition:</b> the FileManager targets the given file, which does
     * not need to exist yet
     * </p>
     *
     * @param fileName the path of the save file to read from and write to
     */
    public FileManager(String fileName) {
        this.FILE_NAME = fileName;
        this.skippedLineCount = 0;
    }

    /**
     * Returns the name of the file this manager reads from and writes to.
     *
     * @return the file name
     */
    public String getFileName() {
        return this.FILE_NAME;
    }

    /**
     * Returns how many lines were skipped as unreadable during the most recent
     * call to loadLibrary. A value of zero means the whole file parsed cleanly.
     *
     * @return the number of malformed lines skipped during the last load
     */
    public int getSkippedLineCount() {
        return this.skippedLineCount;
    }

    /**
     * Writes every entry in the given library to the save file, replacing any
     * previous contents. Each entry becomes one delimited line.
     * <p>
     * <b>Precondition:</b> library is not null <br>
     * <b>Postcondition:</b> the save file contains one line per entry
     * </p>
     *
     * @param library the Library whose entries should be written to disk
     * @throws IOException if the file cannot be created or written to
     */
    public void saveLibrary(Library library) throws IOException {
        BufferedWriter writer = null;

        try {
            writer = new BufferedWriter(new FileWriter(this.FILE_NAME));

            for (MediaEntry e : library.getAllEntries()) {
                writer.write(formatEntry(e));
                writer.newLine();
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Reads the save file and rebuilds a Library from it. A file that does not
     * exist yet is treated as an empty library rather than an error, so that
     * the very first run of the program behaves sensibly. Individual lines
     * that cannot be parsed are skipped and counted instead of aborting the
     * whole load.
     * <p>
     * <b>Postcondition:</b> a Library is returned holding every entry that
     * could be parsed, and skippedLineCount reflects how many could not
     * </p>
     *
     * @return a Library containing every entry successfully read from the file
     * @throws IOException if the file exists but cannot be read
     */
    public Library loadLibrary() throws IOException {
        Library library = new Library();
        File file = new File(this.FILE_NAME);
        this.skippedLineCount = 0;

        if (file.exists()) {
            BufferedReader reader = null;

            try {
                reader = new BufferedReader(new FileReader(file));
                String line = reader.readLine();

                while (line != null) {
                    if (line.trim().length() > 0) {
                        MediaEntry entry = parseEntry(line);

                        if (entry == null) {
                            this.skippedLineCount++;
                        } else {
                            library.addEntry(entry);
                        }
                    }

                    line = reader.readLine();
                }
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }

        return library;
    }

    /**
     * Rebuilds a single MediaEntry from one delimited line of the save file.
     * The first field decides which subclass is constructed. A line with too
     * few fields, an unrecognized type, a bad status name, or a non-numeric
     * value in a numeric field is rejected rather than allowed to produce a
     * half-built object.
     * <p>
     * <b>Precondition:</b> line is not null <br>
     * <b>Postcondition:</b> nothing outside the returned object is modified
     * </p>
     *
     * @param line one delimited line read from the save file
     * @return the reconstructed MediaEntry, or null if the line is malformed
     */
    public MediaEntry parseEntry(String line) {
        MediaEntry result = null;
        String[] parts = line.split(SPLIT_PATTERN, -1);

        if (parts.length >= 6) {
            try {
                String type = parts[0].trim();
                String title = parts[1];
                String genre = parts[2];
                MediaStatus status = MediaStatus.valueOf(parts[3].trim());
                String ratingText = parts[4].trim();
                String review = parts[5];

                if (type.equalsIgnoreCase("Movie") && parts.length >= 9) {
                    result = new Movie(title, genre, status, parts[6],
                            Integer.parseInt(parts[7].trim()),
                            Integer.parseInt(parts[8].trim()));
                } else if (type.equalsIgnoreCase("TVSeries") && parts.length >= 9) {
                    result = new TVSeries(title, genre, status,
                            Integer.parseInt(parts[6].trim()),
                            Integer.parseInt(parts[7].trim()),
                            Integer.parseInt(parts[8].trim()));
                } else if (type.equalsIgnoreCase("VideoGame") && parts.length >= 10) {
                    result = new VideoGame(title, genre, status, parts[6],
                            parts[7], parts[8],
                            Double.parseDouble(parts[9].trim()));
                }

                if (result != null && ratingText.length() > 0) {
                    result.setRatingAndReview(Integer.parseInt(ratingText), review);
                }
            } catch (NumberFormatException ex) {
                result = null;
            } catch (IllegalArgumentException ex) {
                result = null;
            }
        }

        return result;
    }

    /**
     * Converts a MediaEntry into the single delimited line that represents it
     * in the save file. The concrete subclass is identified with instanceof so
     * that its type-specific attributes can be written after the shared ones.
     * <p>
     * <b>Precondition:</b> entry is not null <br>
     * <b>Postcondition:</b> the entry is unchanged
     * </p>
     *
     * @param entry the MediaEntry to convert
     * @return the delimited line representing the entry
     */
    public String formatEntry(MediaEntry entry) {
        StringBuilder line = new StringBuilder();
        Integer rating = entry.getRating();

        line.append(entry.getMediaType()).append(DELIMITER);
        line.append(clean(entry.getTitle())).append(DELIMITER);
        line.append(clean(entry.getGenre())).append(DELIMITER);
        line.append(entry.getStatus().name()).append(DELIMITER);

        if (rating != null) {
            line.append(rating);
        }
        line.append(DELIMITER);

        line.append(clean(entry.getReview())).append(DELIMITER);

        if (entry instanceof Movie) {
            Movie m = (Movie) entry;
            line.append(clean(m.getDirector())).append(DELIMITER);
            line.append(m.getDurationMinutes()).append(DELIMITER);
            line.append(m.getReleaseYear());
        } else if (entry instanceof TVSeries) {
            TVSeries t = (TVSeries) entry;
            line.append(t.getTotalEpisodes()).append(DELIMITER);
            line.append(t.getWatchedEpisodes()).append(DELIMITER);
            line.append(t.getSeasonCount());
        } else if (entry instanceof VideoGame) {
            VideoGame g = (VideoGame) entry;
            line.append(clean(g.getPlatform())).append(DELIMITER);
            line.append(clean(g.getRequiredSpecs())).append(DELIMITER);
            line.append(clean(g.getDeveloper())).append(DELIMITER);
            line.append(g.getHoursPlayed());
        }

        return line.toString();
    }

    /**
     * Strips out any delimiter character or line break from a piece of user
     * text so that it cannot corrupt the structure of the save file. A null
     * value becomes an empty String.
     *
     * @param text the raw text to make safe for writing
     * @return the text with delimiters and line breaks replaced by spaces
     */
    private String clean(String text) {
        String safe;

        if (text == null) {
            safe = "";
        } else {
            safe = text.replace(DELIMITER, " ").replace("\n", " ").replace("\r", " ");
        }

        return safe;
    }
}