/**
 * Displays the status of the media type based on completion.
 * 
 * @author Tobias Raian M. Edep
 */

public enum MediaStatus {
    /**
     * Planning to watch/play, still considered as incomplete.
     */
    PLANNED,
    /**
     * Currently watching/playing, still considered as incomplete.
     */
    IN_PROGRESS,
    /**
     * Already watched/played. Marked as complete.
     */
    COMPLETED
}