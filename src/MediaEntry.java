/**
 * Represents the data shared by every media type in MediaVault:
 * title, genre, status, and an optional rating/review pair that may
 * only be set once the entry is marked COMPLETED.
 *
 * @author Tobias Raian M. Edep
 */
public class MediaEntry {

    private String title;
    private String genre;
    private MediaStatus status;
    private Integer rating;
    private String review;

    /**
     * Constructs a new MediaEntry with the given title and genre.
     * The entry starts with no rating or review, since both may
     * only be set once the entry is COMPLETED.
     *
     * @param title  the title of the media item
     * @param genre  the genre of the media item
     * @param status the initial status (PLANNED or IN_PROGRESS)
     * @pre  title and genre are not null; status is not null
     * @post title, genre, and status are set; rating and review
     *       are both null
     */
    public MediaEntry(String title, String genre, MediaStatus status){
        this.title = title;
        this.genre = genre;
        this.status = status;
        this.rating = null;
        this.review = null;
    }

    /**
     * Updates the status of this entry.
     *
     * @param newStatus the new status to apply
     * @pre  newStatus is not null
     * @post status is updated to newStatus
     */
    public void updateStatus(MediaStatus newStatus){
        this.status = newStatus;
    }

    /**
     * Attempts to set a rating and review for this entry. This will
     * only succeed if the entry's status is COMPLETED, enforcing the
     * rule that ratings may only be attached to completed media.
     *
     * @param rating the rating to assign (e.g. 1-10)
     * @param review a short review/comment
     * @return true if the rating and review were set, false if the
     *         entry's status is not COMPLETED
     * @pre  none
     * @post if status is COMPLETED, rating and review are updated
     *       and true is returned; otherwise neither field changes
     *       and false is returned
     */
    public boolean setRatingAndReview(int rating, String review){
        if (!isCompleted()){
            return false;
        }
        this.rating = rating;
        this.review = review;
        return true;
    }

    /**
     * Checks whether this entry's status is COMPLETED.
     *
     * @return true if status is COMPLETED, false otherwise
     * @pre  none
     * @post no data is modified
     */
    public boolean isCompleted(){
        return this.status==MediaStatus.COMPLETED;
    }

    /**
     * Returns the title of this entry.
     *
     * @return the title
     */
    public String getTitle(){
        return this.title;
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
     * @pre  none
     * @post no data is modified
     */
    public Integer getRating() {
        return this.rating;
    }
}
