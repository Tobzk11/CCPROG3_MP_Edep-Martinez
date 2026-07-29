import java.io.IOException;
import java.util.ArrayList;

/**
 * Coordinates every interaction between the MediaVault model and its JavaFX
 * view. The controller is the only class that holds both sides at once: it
 * pulls raw text out of the view, validates it, drives the model, then pushes
 * finished Strings back to the view for display.
 * <p>
 * The view is never given a MediaEntry, a Library, or a MediaStatus. Anything
 * the view shows has already been converted to a String here, which keeps the
 * presentation layer completely free of model types.
 * </p>
 */
public class MediaVaultController {

    private final Library LIBRARY;
    private final MediaVaultView VIEW;
    private final FileManager FILE_MANAGER;

    private String buildErrorMessage;

    /**
     * Constructs a controller bound to the given model, view, and file manager.
     * <p>
     * <b>Precondition:</b> library, view, and fileManager are not null <br>
     * <b>Postcondition:</b> the controller holds all three collaborators but
     * has not yet registered any event handlers
     * </p>
     *
     * @param library     the Library holding all media entries
     * @param view        the MediaVaultView the user interacts with
     * @param fileManager the FileManager used to save and load the library
     */
    public MediaVaultController(Library library, MediaVaultView view,
                                FileManager fileManager) {
        this.LIBRARY = library;
        this.VIEW = view;
        this.FILE_MANAGER = fileManager;
        this.buildErrorMessage = "";
    }

    /**
     * Registers every event handler with the view and performs the first
     * refresh so the interface opens showing the current library contents.
     * <p>
     * <b>Postcondition:</b> all view controls are wired to their handlers and
     * the entry list reflects the library
     * </p>
     */
    public void initController() {
        this.VIEW.setEnterHandler(e -> handleLogin());
        this.VIEW.setAddEntryHandler(e -> handleAddEntry());
        this.VIEW.setRemoveEntryHandler(e -> handleRemoveEntry());
        this.VIEW.setUpdateStatusHandler(e -> handleUpdateStatus());
        this.VIEW.setRateAndReviewHandler(e -> handleRateAndReview());
        this.VIEW.setFilterHandler(e -> handleFilter());
        this.VIEW.setSearchHandler(e -> handleSearch());
        this.VIEW.setSaveHandler(e -> handleSave());
        // this.VIEW.setLoadHandler(e -> handleLoad());

        refreshView();
    }

    public void handleLogin() {
        String username = this.VIEW.getLoginUsername().trim();
        if (username.isEmpty()) {
            this.VIEW.setLoginStatus("Please enter a username.");
        } else {
            // Transition to main app view
            this.VIEW.switchToMainApp();
            this.VIEW.showMessage("Welcome, " + username + "!");
        }
    }

    /**
     * Reads the entry fields from the view, validates them, and adds a new
     * entry of the selected media type to the library. Duplicate titles, blank
     * required fields, and non-numeric values in numeric fields are all
     * rejected with a message rather than allowed through.
     * <p>
     * <b>Postcondition:</b> a new entry is added only when every field is
     * valid; the view is refreshed and told the outcome either way
     * </p>
     */
    public void handleAddEntry() {
        String message;
        String title = safeTrim(this.VIEW.getTitleInput());
        String genre = safeTrim(this.VIEW.getGenreInput());
        MediaStatus status = parseStatus(this.VIEW.getSelectedStatus());

        if (title.length() == 0) {
            message = "Title cannot be empty.";
        } else if (genre.length() == 0) {
            message = "Genre cannot be empty.";
        } else if (status == null) {
            message = "Please select a valid status.";
        } else if (this.LIBRARY.findEntry(title) != null) {
            message = "An entry titled \"" + title + "\" already exists.";
        } else {
            MediaEntry newEntry = buildEntry(safeTrim(this.VIEW.getSelectedType()),
                    title, genre, status);

            if (newEntry == null) {
                message = this.buildErrorMessage;
            } else {
                this.LIBRARY.addEntry(newEntry);
                this.VIEW.clearInputFields();
                message = "Added " + newEntry.getMediaType() + ": " + newEntry.getTitle();
            }
        }

        this.VIEW.showMessage(message);
        refreshView();
    }

    /**
     * Removes the entry currently selected in the view from the library.
     * <p>
     * <b>Postcondition:</b> the selected entry is removed when one is
     * selected and found; the view is refreshed and told the outcome
     * </p>
     */
    public void handleRemoveEntry() {
        String message;
        String title = safeTrim(this.VIEW.getSelectedEntryTitle());

        if (title.length() == 0) {
            message = "Select an entry to remove.";
        } else if (this.LIBRARY.removeEntry(title)) {
            message = "Removed: " + title;
        } else {
            message = "No entry titled \"" + title + "\" was found.";
        }

        this.VIEW.showMessage(message);
        refreshView();
    }

    /**
     * Applies the status currently chosen in the view to the entry currently
     * selected in the view.
     * <p>
     * <b>Postcondition:</b> the selected entry's status is updated when both a
     * valid entry and a valid status are chosen
     * </p>
     */
    public void handleUpdateStatus() {
        String message;
        String title = safeTrim(this.VIEW.getSelectedEntryTitle());
        MediaStatus newStatus = parseStatus(this.VIEW.getSelectedStatus());

        if (title.length() == 0) {
            message = "Select an entry to update.";
        } else if (newStatus == null) {
            message = "Please select a valid status.";
        } else {
            MediaEntry entry = this.LIBRARY.findEntry(title);

            if (entry == null) {
                message = "No entry titled \"" + title + "\" was found.";
            } else {
                entry.updateStatus(newStatus);
                message = title + " is now " + newStatus + ".";
            }
        }

        this.VIEW.showMessage(message);
        refreshView();
    }

    /**
     * Attaches a rating and review to the entry currently selected in the view.
     * The completion rule is checked here before prompting the model, and the
     * model enforces it again as a safety net.
     * <p>
     * <b>Postcondition:</b> the rating and review are stored only when the
     * entry is COMPLETED and the rating is within the valid range
     * </p>
     */
    public void handleRateAndReview() {
        String message;
        String title = safeTrim(this.VIEW.getSelectedEntryTitle());
        String ratingText = safeTrim(this.VIEW.getRatingInput());
        String review = safeTrim(this.VIEW.getReviewInput());

        if (title.length() == 0) {
            message = "Select an entry to rate.";
        } else {
            MediaEntry entry = this.LIBRARY.findEntry(title);

            if (entry == null) {
                message = "No entry titled \"" + title + "\" was found.";
            } else if (!entry.isCompleted()) {
                message = "Only completed entries can be rated. Mark \""
                        + title + "\" as COMPLETED first.";
            } else {
                try {
                    int rating = Integer.parseInt(ratingText);

                    if (entry.setRatingAndReview(rating, review)) {
                        this.VIEW.clearInputFields();
                        message = "Rated " + title + ": " + rating + "/10";
                    } else {
                        message = "Rating must be a whole number from "
                                + MediaEntry.MIN_RATING + " to "
                                + MediaEntry.MAX_RATING + ".";
                    }
                } catch (NumberFormatException ex) {
                    message = "Rating must be a whole number from "
                            + MediaEntry.MIN_RATING + " to "
                            + MediaEntry.MAX_RATING + ".";
                }
            }
        }

        this.VIEW.showMessage(message);
        refreshView();
    }

    /**
     * Filters the displayed list by the media type and status currently chosen
     * in the view. Either selector may be left on its "All" option, in which
     * case that dimension is not narrowed.
     * <p>
     * <b>Postcondition:</b> the view shows only entries matching the chosen
     * filters; the library itself is unchanged
     * </p>
     */
    public void handleFilter() {
        String typeChoice = safeTrim(this.VIEW.getFilterType());
        String statusChoice = safeTrim(this.VIEW.getFilterStatus());
        MediaStatus status = parseStatus(statusChoice);

        ArrayList<MediaEntry> filtered;

        if (status != null) {
            filtered = this.LIBRARY.filterByStatus(status);
        } else {
            filtered = this.LIBRARY.getAllEntries();
        }

        ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();
        boolean filterByType = typeChoice.length() > 0
                && !typeChoice.equalsIgnoreCase("All");

        for (MediaEntry e : filtered) {
            if (!filterByType || e.getMediaType().equalsIgnoreCase(typeChoice)) {
                result.add(e);
            }
        }

        this.VIEW.displayEntries(toDisplayList(result));
        this.VIEW.showMessage("Showing " + result.size() + " of "
                + this.LIBRARY.getAllEntries().size() + " entries.");
        this.VIEW.showStatistics(this.LIBRARY.getSummary());
    }

    /**
     * Searches the library for entries whose title or genre contains the
     * keyword currently typed in the view. An empty keyword restores the full
     * list rather than matching nothing.
     * <p>
     * <b>Postcondition:</b> the view shows the matching entries; the library
     * itself is unchanged
     * </p>
     */
    public void handleSearch() {
        String keyword = safeTrim(this.VIEW.getSearchKeyword());
        ArrayList<MediaEntry> results;
        String message;

        if (keyword.length() == 0) {
            results = this.LIBRARY.getAllEntries();
            message = "Showing all entries.";
        } else {
            results = this.LIBRARY.searchEntries(keyword);
            message = results.size() + " result(s) for \"" + keyword + "\".";
        }

        this.VIEW.displayEntries(toDisplayList(results));
        this.VIEW.showMessage(message);
        this.VIEW.showStatistics(this.LIBRARY.getSummary());
    }

    /**
     * Writes the current library to disk through the FileManager, reporting
     * any input or output failure to the user instead of letting it escape.
     * <p>
     * <b>Postcondition:</b> the save file reflects the library, or the view is
     * told why the write failed
     * </p>
     */
    public void handleSave() {
        String message;

        try {
            this.FILE_MANAGER.saveLibrary(this.LIBRARY);
            message = "Saved " + this.LIBRARY.getAllEntries().size()
                    + " entries to " + this.FILE_MANAGER.getFileName() + ".";
        } catch (IOException ex) {
            message = "Could not save to " + this.FILE_MANAGER.getFileName()
                    + ": " + ex.getMessage();
        }

        this.VIEW.showMessage(message);
        this.VIEW.switchToLogin();
    }

    /**
     * Reads the save file through the FileManager and replaces the contents of
     * the current library with what was read. The library object itself is
     * kept rather than swapped out, so every other reference to it stays valid.
     * <p>
     * <b>Postcondition:</b> the library holds exactly the entries read from
     * file, or is left untouched if the read failed
     * </p>
     */
    public void handleLoad() {
        String message;

        try {
            Library loaded = this.FILE_MANAGER.loadLibrary();

            this.LIBRARY.getAllEntries().clear();
            for (MediaEntry e : loaded.getAllEntries()) {
                this.LIBRARY.addEntry(e);
            }

            message = "Loaded " + this.LIBRARY.getAllEntries().size()
                    + " entries from " + this.FILE_MANAGER.getFileName() + ".";

            if (this.FILE_MANAGER.getSkippedLineCount() > 0) {
                message = message + " Skipped "
                        + this.FILE_MANAGER.getSkippedLineCount()
                        + " unreadable line(s).";
            }
        } catch (IOException ex) {
            message = "Could not load from " + this.FILE_MANAGER.getFileName()
                    + ": " + ex.getMessage();
        }

        this.VIEW.showMessage(message);
        refreshView();
    }

    /**
     * Rebuilds everything the view displays from the current state of the
     * library. Each MediaEntry is converted to a String here so that the view
     * never touches a model object.
     * <p>
     * <b>Postcondition:</b> the view's list and statistics match the library
     * </p>
     */
    public void refreshView() {
        this.VIEW.displayEntries(toDisplayList(this.LIBRARY.getAllEntries()));
        this.VIEW.showStatistics(this.LIBRARY.getSummary());
    }

    /**
     * Looks up the entry with the given title and sends its full details to
     * the view. Used when the user selects an entry from the list.
     * <p>
     * <b>Postcondition:</b> the view's detail area shows the entry's details,
     * or a notice that it could not be found
     * </p>
     *
     * @param title the title of the entry whose details should be shown
     */
    /* public void showDetailsFor(String title) {
        MediaEntry entry = this.LIBRARY.findEntry(safeTrim(title));

        if (entry == null) {
            this.VIEW.showEntryDetails("No entry selected.");
        } else {
            this.VIEW.showEntryDetails(entry.getDetails());
        }
    } */

    /**
     * Converts a list of MediaEntry objects into the one-line summary Strings
     * the view displays in its list control.
     *
     * @param entries the entries to convert
     * @return a list of display Strings, one per entry
     */
    private ArrayList<String> toDisplayList(ArrayList<MediaEntry> entries) {
        ArrayList<String> lines = new ArrayList<String>();

        for (MediaEntry e : entries) {
            String ratingText;

            if (e.getRating() == null) {
                ratingText = "unrated";
            } else {
                ratingText = e.getRating() + "/10";
            }

            lines.add("[" + e.getMediaType() + "] " + e.getTitle()
                    + " - " + e.getStatus() + " - " + ratingText);
        }

        return lines;
    }

    /**
     * Converts a status label chosen in the view into a MediaStatus constant.
     * Spaces are accepted in place of underscores so that a control offering
     * "In Progress" still resolves correctly.
     *
     * @param text the status label taken from the view
     * @return the matching MediaStatus, or null if the label matches none
     */
    private MediaStatus parseStatus(String text) {
        MediaStatus result = null;

        if (text != null) {
            String normalized = text.trim().replace(' ', '_').toUpperCase();

            try {
                result = MediaStatus.valueOf(normalized);
            } catch (IllegalArgumentException ex) {
                result = null;
            }
        }

        return result;
    }

    /**
     * Builds a new MediaEntry of the requested type from the type-specific
     * fields currently in the view. When a field cannot be read, the reason is
     * stored in buildErrorMessage and null is returned.
     *
     * @param type   the media type label selected in the view
     * @param title  the validated title
     * @param genre  the validated genre
     * @param status the validated status
     * @return the newly built MediaEntry, or null if the type-specific fields
     *         were invalid
     */
    private MediaEntry buildEntry(String type, String title, String genre,
                                  MediaStatus status) {
        MediaEntry entry = null;
        this.buildErrorMessage = "";

        try {
            if (type.equalsIgnoreCase("Movie")) {
                String director = safeTrim(this.VIEW.getDirectorInput());
                int duration = Integer.parseInt(safeTrim(this.VIEW.getDurationInput()));
                int year = Integer.parseInt(safeTrim(this.VIEW.getReleaseYearInput()));

                if (director.length() == 0) {
                    this.buildErrorMessage = "Director cannot be empty.";
                } else if (duration <= 0) {
                    this.buildErrorMessage = "Duration must be greater than zero.";
                } else {
                    entry = new Movie(title, genre, status, director, duration, year);
                }
            } else if (type.equalsIgnoreCase("TVSeries")) {
                int totalEps = Integer.parseInt(safeTrim(this.VIEW.getTotalEpisodesInput()));
                int watchedEps = Integer.parseInt(safeTrim(this.VIEW.getWatchedEpisodesInput()));
                int seasons = Integer.parseInt(safeTrim(this.VIEW.getSeasonCountInput()));

                if (totalEps <= 0) {
                    this.buildErrorMessage = "Total episodes must be greater than zero.";
                } else if (watchedEps < 0 || watchedEps > totalEps) {
                    this.buildErrorMessage = "Watched episodes must be between 0 and "
                            + totalEps + ".";
                } else if (seasons <= 0) {
                    this.buildErrorMessage = "Season count must be greater than zero.";
                } else {
                    entry = new TVSeries(title, genre, status, totalEps, watchedEps, seasons);
                }
            } else if (type.equalsIgnoreCase("VideoGame")) {
                String platform = safeTrim(this.VIEW.getPlatformInput());
                String specs = safeTrim(this.VIEW.getRequiredSpecsInput());
                String developer = safeTrim(this.VIEW.getDeveloperInput());
                double hours = Double.parseDouble(safeTrim(this.VIEW.getHoursPlayedInput()));

                if (platform.length() == 0) {
                    this.buildErrorMessage = "Platform cannot be empty.";
                } else if (developer.length() == 0) {
                    this.buildErrorMessage = "Developer cannot be empty.";
                } else if (hours < 0) {
                    this.buildErrorMessage = "Hours played cannot be negative.";
                } else {
                    entry = new VideoGame(title, genre, status, platform, specs,
                            developer, hours);
                }
            } else {
                this.buildErrorMessage = "Please select a media type.";
            }
        } catch (NumberFormatException ex) {
            this.buildErrorMessage = "Numeric fields must contain valid numbers.";
            entry = null;
        }

        return entry;
    }

    /**
     * Trims a String taken from the view, treating null as empty so that the
     * calling code never has to test for it.
     *
     * @param text the text to trim
     * @return the trimmed text, or an empty String if text was null
     */
    private String safeTrim(String text) {
        String result;

        if (text == null) {
            result = "";
        } else {
            result = text.trim();
        }

        return result;
    }
}