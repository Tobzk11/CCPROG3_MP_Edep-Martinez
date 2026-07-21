/**
 * Represents the data shared by every media type in MediaVault:
 * title, genre, status, and an optional rating/review pair that may
 * only be set once the entry is marked COMPLETED.
 *
 * @author Tobias Raian M. Edep
 */
public abstract class MediaEntry {

    protected final String TITLE;
    protected final String GENRE;
    protected MediaStatus status;
    protected Integer rating;
    protected String review;

    /**
     * Constructs a new MediaEntry with the given title and genre.
     * The entry starts with no rating or review, since both may
     * only be set once the entry is COMPLETED.
     *
     * @param title  the title of the media item
     * @param genre  the genre of the media item
     * @param status the initial status (PLANNED or IN_PROGRESS)
     */
    public MediaEntry(String title, String genre, MediaStatus status){
        this.TITLE = title;
        this.GENRE = genre;
        this.status = status;
        this.rating = null;
        this.review = null;
    }

    /**
     * Updates the status of this entry.
     *
     * @param newStatus the new status to apply
     */
    public void updateStatus(MediaStatus newStatus){

        this.status = newStatus;
    }

    /**
     * Attempts to set a rating and review for this entry. This will
     * only succeed if the entry's status is COMPLETED, enforcing the
     * rule that ratings may only be attached to completed media.
     * <p>
     * <b>Precondition:</b> rating is an integer from 1 to 10 <br>
     * <b>Postcondition:</b> rating and review attributes are set <br>
     * </p>
     * @param rating the rating to assign (e.g. 1-10)
     * @param review a short review/comment
     * @return true if the rating and review were set, false if the
     *         entry's status is not COMPLETED
     */
    public boolean setRatingAndReview(int rating, String review) {
        boolean ratingSet = false;
        if (!isCompleted())
            System.out.println("Cannot rate: entry is not completed.");
        else {
            this.rating = rating;
            this.review = review;
            ratingSet = true;
        }
        return ratingSet;   
    }

    /**
     * Checks whether this entry's status is COMPLETED.
     *
     * @return true if status is COMPLETED, false otherwise
     */
    public boolean isCompleted(){

        return this.status == MediaStatus.COMPLETED;
    }

    /**
     * Returns the title of this entry.
     *
     * @return the title
     */
    public String getTitle(){

        return this.TITLE;
    }

    /**
     * Returns the current status of this entry.
     *
     * @return the MediaStatus
     */
    public MediaStatus getStatus() {

        return this.status;
    }
    
    /**
     * Returns the rating of this entry, or null if it has not been
     * rated yet.
     *
     * @return the rating as an Integer, or null if unrated
     */
    public Integer getRating() {

        return this.rating;
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
     * Returns the review of this entry.
     *
     * @return the review
     */
    public String getReview() {
        return this.review;
    }

    public abstract String getMediaType();

    public abstract String getDetails();
}
