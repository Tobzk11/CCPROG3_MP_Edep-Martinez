import java.io.IOException;
import java.util.ArrayList;

/**
 * Coordinates every interaction between the MediaVault model and its JavaFX
 * view. The controller is the only class that holds both sides at once: it
 * pulls raw text out of the view, validates it, drives the model, then pushes
 * finished Strings back to the view for display.
 * <p>
 * The view is never given a MediaEntry, a Library, a UserProfile, or a
 * MediaStatus. Anything the view shows has already been converted to a String
 * here, which keeps the presentation layer completely free of model types.
 * Equally, the controller never touches a JavaFX node; page switching is asked
 * for through named view methods such as mainPage and swapFront.
 * </p>
 * <p>
 * Each account owns its own save file, named after the user. Logging in reads
 * that file into the working library; signing up creates it.
 * </p>
 */
public class MediaVaultController {

    private final Library LIBRARY;
    private final MediaVaultView VIEW;
    private final FileManager FILE_MANAGER;

    private UserProfile currentProfile;
    private String buildErrorMessage;

    /**
     * Constructs a controller bound to the given model, view, and file manager.
     * <p>
     * <b>Precondition:</b> library, view, and fileManager are not null <br>
     * <b>Postcondition:</b> the controller holds all three collaborators, with
     * no user logged in and no event handlers registered yet
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
        this.currentProfile = null;
        this.buildErrorMessage = "";
    }

    /**
     * Registers every event handler with the view and shows the login page, so
     * the program opens asking who is using it.
     * <p>
     * <b>Postcondition:</b> all view controls are wired to their handlers and
     * the login page is showing
     * </p>
     */
    public void initController() {
        this.VIEW.setLoginHandler(e -> handleLogin());
        this.VIEW.setSignupHandler(e -> handleSignup());
        this.VIEW.swapLoginSignup(e -> handleSwapFront());
        this.VIEW.swapSignupLogin(e -> handleSwapFront());
        this.VIEW.setAddEntryHandler(e -> handleAddEntry());
        this.VIEW.setRemoveEntryHandler(e -> handleRemoveEntry());
        this.VIEW.setUpdateStatusHandler(e -> handleUpdateStatus());
        this.VIEW.setRateAndReviewHandler(e -> handleRateAndReview());
        this.VIEW.setFilterHandler(e -> handleFilter());
        this.VIEW.setSearchHandler(e -> handleSearch());
        this.VIEW.setSaveHandler(e -> handleSave());
        this.VIEW.setLoadHandler(e -> handleLoad());
        this.VIEW.setEntrySelectedHandler(title -> showDetailsFor(title));

        this.VIEW.loginPage();
    }

    /**
     * Signs an existing user in. The username must not be blank and must have
     * a save file already, which is what distinguishes an existing account
     * from a new one. On success the user's saved entries are read into the
     * working library and the main page is shown.
     * <p>
     * <b>Postcondition:</b> on success a profile is active, the library holds
     * that user's saved entries, and the main page is showing; otherwise a
     * message is placed on the login page and nothing else changes
     * </p>
     */
    public void handleLogin() {
        String username = safeTrim(this.VIEW.getLoginUsername());

        if (username.isEmpty()) {
            this.VIEW.setLoginStatus("Please enter a username.");
        } else {
            this.FILE_MANAGER.setFileName(FileManager.buildFileNameFor(username));

            if (!this.FILE_MANAGER.fileExists()) {
                this.VIEW.setLoginStatus("Username does not exist. Try signing up.");
            } else {
                try {
                    Library loaded = this.FILE_MANAGER.loadLibrary();
                    replaceLibraryContents(loaded);

                    this.currentProfile = new UserProfile(username, this.LIBRARY);

                    String message = "Welcome back, " + username + "! Loaded "
                            + this.LIBRARY.getAllEntries().size() + " entries.";

                    if (this.FILE_MANAGER.getSkippedLineCount() > 0) {
                        message = message + " Skipped "
                                + this.FILE_MANAGER.getSkippedLineCount()
                                + " unreadable line(s).";
                    }

                    enterMainPage(message);
                } catch (IOException ex) {
                    this.VIEW.setLoginStatus("Could not open your library: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Creates a new account. The username must not be blank and must not
     * already have a save file. On success an empty save file is written so
     * that the account exists on disk, and the main page is shown.
     * <p>
     * <b>Postcondition:</b> on success a new empty library and save file exist
     * for the user and the main page is showing; otherwise a message is placed
     * on the sign-up page and nothing else changes
     * </p>
     */
    public void handleSignup() {
        String username = safeTrim(this.VIEW.getSignupUsername());
        if (username.isEmpty()) {
            this.VIEW.setSignupStatus("Please enter a username.");
        } else {
            this.FILE_MANAGER.setFileName(FileManager.buildFileNameFor(username));

            if (this.FILE_MANAGER.fileExists()) {
                this.VIEW.setSignupStatus("That username is already taken.");
            } else {
                this.LIBRARY.getAllEntries().clear();

                try {
                    this.FILE_MANAGER.saveLibrary(this.LIBRARY);
                    this.currentProfile = new UserProfile(username, this.LIBRARY);
                    enterMainPage("Welcome, " + username + "! Your library is empty.");
                } catch (IOException ex) {
                    this.VIEW.setSignupStatus("Could not create your library: " + ex.getMessage());
                }
            }
        }
    }

    /**
     * Switches the front page between login and sign-up. The decision of which
     * page is currently showing belongs to the view, so this simply asks it to
     * swap.
     * <p>
     * <b>Postcondition:</b> the login and sign-up pages have swapped
     * </p>
     */
    public void handleSwapFront() {
        this.VIEW.swapFront();
    }

    /**
     * Moves the interface to the main page and refreshes everything on it.
     * Shared by the login and sign-up paths.
     *
     * @param message the greeting to show in the message area
     */
    private void enterMainPage(String message) {
        this.VIEW.clearLoginFields();
        this.VIEW.clearInputFields();
        this.VIEW.showEntryDetails("");
        this.VIEW.mainPage();
        refreshView();
        this.VIEW.showMessage(message);
    }

    /**
     * Empties the working library and refills it from the given one. The
     * library object itself is kept rather than swapped out, so every other
     * reference to it stays valid.
     * <p>
     * <b>Precondition:</b> source is not null <br>
     * <b>Postcondition:</b> the working library holds exactly the entries of
     * the source library
     * </p>
     *
     * @param source the library whose entries should replace the current ones
     */
    private void replaceLibraryContents(Library source) {
        this.LIBRARY.getAllEntries().clear();

        for (MediaEntry e : source.getAllEntries()) {
            this.LIBRARY.addEntry(e);
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

        if (title.isEmpty()) {
            message = "Title cannot be empty.";
        } else if (genre.isEmpty()) {
            message = "Genre cannot be empty.";
        } else if (status == null) {
            message = "Please select a valid status.";
        } else if (this.LIBRARY.findEntry(title) != null) {
            message = "An entry titled \"" + title + "\" already exists.";
        } else {
            MediaEntry newEntry = buildEntry(normalizeType(this.VIEW.getSelectedType()),
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
     * <b>Postcondition:</b> the selected entry is removed when one is selected
     * and found; the view is refreshed and told the outcome
     * </p>
     */
    public void handleRemoveEntry() {
        String message;
        String title = safeTrim(this.VIEW.getSelectedEntryTitle());

        if (title.isEmpty()) {
            message = "Select an entry to remove.";
        } else if (this.LIBRARY.removeEntry(title)) {
            message = "Removed: " + title;
            this.VIEW.showEntryDetails("");
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
        int watchedEps;
        double hrsPlayed;

        if (title.isEmpty())
            message = "Select an entry to update.";
        else if (newStatus == null)
            message = "Please select a valid status.";
        else {
            MediaEntry entry = this.LIBRARY.findEntry(title);

            if (entry == null)
                message = "No entry titled \"" + title + "\" was found.";
            else {
                if (entry.updateStatus(newStatus)) {
                    message = title + " is now " + newStatus + ".";
                    if (entry instanceof TVSeries tv && !this.VIEW.getWatchedEpisodesInput().isEmpty()) {
                        watchedEps = Integer.parseInt(this.VIEW.getWatchedEpisodesInput());
                        if (tv.updateWatchedEpisodes(watchedEps)) {
                            message += " Updated watched episodes.";
                            this.LIBRARY.removeEntry(title);
                            this.LIBRARY.addEntry(tv);
                        } else
                            message += " Invalid watched episodes.";
                    } else if (entry instanceof VideoGame vg && !this.VIEW.getHoursPlayedInput().isEmpty()) {
                        hrsPlayed = Double.parseDouble(this.VIEW.getHoursPlayedInput());
                        if (vg.updateHoursPlayed(hrsPlayed)) {
                            message += " Updated hours played.";
                            this.LIBRARY.removeEntry(title);
                            this.LIBRARY.addEntry(vg);
                        } else
                            message += " Invalid hours played.";
                    }
                } else
                    message = "Invalid status change.";

                this.VIEW.showEntryDetails(entry.getDetails());
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

        if (title.isEmpty())
            message = "Select an entry to rate.";
        else {
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
                        this.VIEW.showEntryDetails(entry.getDetails());
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
     * in the view. Either selector may be left on its "None" option, in which
     * case that dimension is not narrowed.
     * <p>
     * <b>Postcondition:</b> the view shows only entries matching the chosen
     * filters; the library itself is unchanged
     * </p>
     */
    public void handleFilter() {
        String typeChoice = normalizeType(this.VIEW.getFilterType());
        MediaStatus status = parseStatus(this.VIEW.getFilterStatus());

        ArrayList<MediaEntry> filtered;

        if (status != null) {
            filtered = this.LIBRARY.filterByStatus(status);
        } else {
            filtered = this.LIBRARY.getAllEntries();
        }

        ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();
        boolean filterByType = !typeChoice.isEmpty();

        for (MediaEntry e : filtered) {
            if (!filterByType || e.getMediaType().equals(typeChoice)) {
                result.add(e);
            }
        }

        this.VIEW.displayEntries(toDisplayList(result));
        this.VIEW.showMessage("Showing " + result.size() + " of "
                + this.LIBRARY.getAllEntries().size() + " entries.");
        this.VIEW.showStatistics(buildStatistics());
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

        if (keyword.isEmpty()) {
            results = this.LIBRARY.getAllEntries();
            message = "Showing all entries.";
        } else {
            results = this.LIBRARY.searchEntries(keyword);
            message = results.size() + " result(s) for \"" + keyword + "\".";
        }

        this.VIEW.displayEntries(toDisplayList(results));
        this.VIEW.showMessage(message);
        this.VIEW.showStatistics(buildStatistics());
    }

    /**
     * Writes the current library to the logged-in user's save file and then
     * logs them out, returning the interface to the login page. Any input or
     * output failure is reported to the user instead of escaping.
     * <p>
     * <b>Postcondition:</b> on success the save file reflects the library and
     * no user is logged in; on failure the user stays logged in and is told
     * what went wrong
     * </p>
     */
    public void handleSave() {
        if (this.currentProfile == null) {
            this.VIEW.showMessage("No user is logged in.");
        } else {
            try {
                this.FILE_MANAGER.saveLibrary(this.LIBRARY);
                this.LIBRARY.getAllEntries().clear();
                this.currentProfile = null;
                this.VIEW.clearInputFields();
                this.VIEW.clearLoginFields();
                this.VIEW.showEntryDetails("");
                this.VIEW.displayEntries(new ArrayList<String>());
                this.VIEW.loginPage();
            } catch (IOException ex) {
                this.VIEW.showMessage("Could not save to "
                        + this.FILE_MANAGER.getFileName() + ": " + ex.getMessage());
            }
        }
    }

    /**
     * Re-reads the logged-in user's save file, discarding any unsaved changes
     * made since the last save.
     * <p>
     * <b>Postcondition:</b> the library holds exactly the entries read from
     * file, or is left untouched if the read failed
     * </p>
     */
    public void handleLoad() {
        String message;

        if (this.currentProfile == null) {
            message = "No user is logged in.";
        } else {
            try {
                Library loaded = this.FILE_MANAGER.loadLibrary();
                replaceLibraryContents(loaded);
                message = "Reloaded " + this.LIBRARY.getAllEntries().size()
                        + " entries from " + this.FILE_MANAGER.getFileName() + ".";
                if (this.FILE_MANAGER.getSkippedLineCount() > 0) {
                    message = message + " Skipped "
                            + this.FILE_MANAGER.getSkippedLineCount()
                            + " unreadable line(s).";
                }
                this.VIEW.showEntryDetails("");
            } catch (IOException ex) {
                message = "Could not load from " + this.FILE_MANAGER.getFileName()
                        + ": " + ex.getMessage();
            }
        }

        this.VIEW.showMessage(message);
        refreshView();
    }

    /**
     * Looks up the entry with the given title and sends its full details to
     * the view. Called whenever the user highlights a row in the list, which
     * is where the polymorphic getDetails of each subclass becomes visible.
     * <p>
     * <b>Postcondition:</b> the view's detail area shows the entry's details,
     * or is cleared when nothing is selected
     * </p>
     *
     * @param title the title of the entry whose details should be shown
     */
    public void showDetailsFor(String title) {
        String cleanTitle = safeTrim(title);

        if (cleanTitle.isEmpty()) {
            this.VIEW.showEntryDetails("");
        } else {
            MediaEntry entry = this.LIBRARY.findEntry(cleanTitle);

            if (entry == null) {
                this.VIEW.showEntryDetails("No entry selected.");
            } else {
                this.VIEW.showEntryDetails(entry.getDetails());
            }
        }
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
        this.VIEW.showStatistics(buildStatistics());
    }
    /**
     * Builds the statistics text shown at the bottom of the main page. When a
     * user is logged in this is their personal greeting and summary from
     * UserProfile; otherwise it is the plain library summary.
     * @return the statistics text to display
     */
    private String buildStatistics() {
        String text;

        if (this.currentProfile == null)
            text = this.LIBRARY.getSummary();
        else
            text = this.currentProfile.viewSummary();

        return text;
    }
    //
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
            String line;

            if (e.getRating() == null) {
                ratingText = "UNRATED";
            } else {
                ratingText = e.getRating() + "/10";
            }

            line = e.getTitle() + " [" + e.getStatus() + " - " + ratingText + "]";

            lines.add(line);
        }

        return lines;
    }

    /**
     * Converts a status label chosen in the view into a MediaStatus constant.
     * Spaces are accepted in place of underscores so that a control offering
     * "In Progress" still resolves correctly. Any label that is not a status,
     * including the "None" option in the filter bar, yields null, which the
     * callers read as "no status chosen".
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
     * Converts a media type label chosen in the view into the exact label that
     * getMediaType returns on the model side. The dropdowns read "TV Series"
     * and "Video Game" for the user's benefit, while the model uses "TVSeries"
     * and "VideoGame", so the spaces are removed before matching. Any label
     * that is not one of the three types, including the "None" filter option,
     * yields an empty String, which the callers read as "no type chosen".
     *
     * @param text the media type label taken from the view
     * @return the canonical media type label, or an empty String if the label
     *         matches none
     */
    private String normalizeType(String text) {
        String result = "";

        if (text != null) {
            String compact = text.trim().replace(" ", "");

            if (compact.equalsIgnoreCase("Movie")) {
                result = "Movie";
            } else if (compact.equalsIgnoreCase("TVSeries")) {
                result = "TVSeries";
            } else if (compact.equalsIgnoreCase("VideoGame")) {
                result = "VideoGame";
            }
        }

        return result;
    }

    /**
     * Builds a new MediaEntry of the requested type from the type-specific
     * fields currently in the view. When a field cannot be read, the reason is
     * stored in buildErrorMessage and null is returned.
     *
     * @param type   the canonical media type label from normalizeType
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
            switch (type) {
                case "Movie" -> {
                    String director = safeTrim(this.VIEW.getDirectorInput());
                    int duration = Integer.parseInt(safeTrim(this.VIEW.getDurationInput()));
                    int year = Integer.parseInt(safeTrim(this.VIEW.getReleaseYearInput()));

                    if (director.isEmpty()) {
                        this.buildErrorMessage = "Director cannot be empty.";
                    } else if (duration <= 0) {
                        this.buildErrorMessage = "Duration must be greater than zero.";
                    } else {
                        entry = new Movie(title, genre, status, director, duration, year);
                    }
                }
                case "TVSeries" -> {
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
                }
                case "VideoGame" -> {
                    String platform = safeTrim(this.VIEW.getPlatformInput());
                    String specs = safeTrim(this.VIEW.getRequiredSpecsInput());
                    String developer = safeTrim(this.VIEW.getDeveloperInput());
                    double hours = Double.parseDouble(safeTrim(this.VIEW.getHoursPlayedInput()));

                    if (platform.isEmpty()) {
                        this.buildErrorMessage = "Platform cannot be empty.";
                    } else if (developer.isEmpty()) {
                        this.buildErrorMessage = "Developer cannot be empty.";
                    } else if (hours < 0) {
                        this.buildErrorMessage = "Hours played cannot be negative.";
                    } else {
                        entry = new VideoGame(title, genre, status, platform, specs,
                                developer, hours);
                    }
                }
                default -> this.buildErrorMessage = "Please select a media type.";
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