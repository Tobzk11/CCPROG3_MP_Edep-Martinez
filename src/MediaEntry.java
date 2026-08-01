/**
 * Represents the data shared by every media type in MediaVault:
 * title, genre, status, and an optional rating/review pair that may
 * only be set once the entry is marked COMPLETED.
 * <p>
 * This class is abstract and cannot be instantiated on its own. Each
 * concrete subclass supplies its own media type label, formatted detail
 * string, and progress description. This lets the rest of the program hold
 * and operate on every kind of media through this single common type.
 * </p>
 */
public abstract class MediaEntry {

    /** The lowest rating that may be assigned to a completed entry. */
    public static final int MIN_RATING = 1;

    /** The highest rating that may be assigned to a completed entry. */
    public static final int MAX_RATING = 10;

    protected final String TITLE;
    protected final String GENRE;
    protected MediaStatus status;
    protected Integer rating;
    protected String review;

    /**
     * Constructs a new MediaEntry with the given title and genre.
     * The entry starts with no rating and no review, since both may
     * only be set once the entry is COMPLETED.
     * <p>
     * <b>Precondition:</b> title, genre, and status are not null <br>
     * <b>Postcondition:</b> the entry is created with a null rating and review
     * </p>
     *
     * @param title  the title of the media item
     * @param genre  the genre of the media item
     * @param status the initial status of the media item
     */
    public MediaEntry(String title, String genre, MediaStatus status) {
        this.TITLE = title;
        this.GENRE = genre;
        this.status = status;
        this.rating = null;
        this.review = null;
    }

    /**
     * Advances the status of this entry. Progress through a media item only
     * ever moves forward, so a status earlier in the sequence PLANNED,
     * IN_PROGRESS, COMPLETED than the current one is rejected and the entry is
     * left untouched. Re-selecting the status the entry already holds is
     * accepted and changes nothing.
     * <p>
     * The comparison relies on the constants of MediaStatus being declared in
     * order of progress, so that comparing them orders them the same way.
     * </p>
     * <p>
     * <b>Precondition:</b> newStatus is not null <br>
     * <b>Postcondition:</b> the status attribute reflects newStatus only when
     * newStatus is the same as, or later than, the current status; otherwise
     * the entry is unchanged
     * </p>
     *
     * @param newStatus the new status to apply
     * @return true if the status was applied, false if it would have moved the
     *         entry backward
     */
    public boolean updateStatus(MediaStatus newStatus) {
        int compare = newStatus.compareTo(this.status);
        boolean upd = true;

        if (compare < 0)
            upd = false;
        else
            this.status = newStatus;

        return upd;
    }

    /**
     * Attempts to set a rating and review for this entry. This succeeds only
     * when the entry's status is COMPLETED and the rating falls within the
     * valid range, enforcing the rule that ratings may only be attached to
     * finished media.
     * <p>
     * <b>Precondition:</b> review is not null <br>
     * <b>Postcondition:</b> the rating and review attributes are set only if
     * the entry is COMPLETED and the rating is valid; otherwise the entry is
     * left unchanged
     * </p>
     *
     * @param rating the rating to assign, from MIN_RATING to MAX_RATING
     * @param review a short review or comment on the entry
     * @return true if the rating and review were set, false if the entry is
     *         not COMPLETED or the rating is out of range
     */
    public boolean setRatingAndReview(int rating, String review) {
        boolean ratingSet = false;

        if (isCompleted() && rating >= MIN_RATING && rating <= MAX_RATING) {
            this.rating = rating;
            this.review = review;
            ratingSet = true;
        }

        return ratingSet;
    }

    /**
     * Checks whether this entry's status is COMPLETED.
     *
     * @return true if the status is COMPLETED, false otherwise
     */
    public boolean isCompleted() {
        return this.status == MediaStatus.COMPLETED;
    }

    /**
     * Returns the title of this entry.
     *
     * @return the title
     */
    public String getTitle() {
        return this.TITLE;
    }

    /**
     * Returns the genre of this entry.
     *
     * @return the genre
     */
    public String getGenre() {
        return this.GENRE;
    }

    /**
     * Returns the current status of this entry.
     *
     * @return the MediaStatus of this entry
     */
    public MediaStatus getStatus() {
        return this.status;
    }

    /**
     * Returns the rating of this entry, or null if it has not been rated yet.
     *
     * @return the rating as an Integer, or null when unrated
     */
    public Integer getRating() {
        return this.rating;
    }

    /**
     * Returns the review of this entry, or null if it has not been reviewed.
     *
     * @return the review text, or null when unreviewed
     */
    public String getReview() {
        return this.review;
    }

    /**
     * Returns the label identifying which kind of media this entry is.
     * Each subclass returns its own fixed label.
     *
     * @return the media type label of this entry
     */
    public abstract String getMediaType();

    /**
     * Builds a complete, human-readable description of this entry including
     * every attribute specific to its media type.
     *
     * @return a formatted multi-line description of this entry
     */
    public abstract String getDetails();

    /**
     * Builds a short description of how far along this entry is, phrased in
     * terms that suit its media type.
     *
     * @return a one-line progress description for this entry
     */
    public abstract String getProgressDescription();
}