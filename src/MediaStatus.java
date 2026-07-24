/**
 * Represents the three possible progress states a media entry may occupy
 * in a MediaVault library.
 * <p>
 * Using an enumeration instead of integer flags or free-form Strings makes
 * the set of valid states closed and checked at compile time, which removes
 * any possibility of an entry holding a meaningless status value.
 * </p>
 */
public enum MediaStatus {

    /** The entry has been added to the library but not started. */
    PLANNED,

    /** The entry is currently being watched or played. */
    IN_PROGRESS,

    /** The entry has been finished and may now be rated and reviewed. */
    COMPLETED
}