import java.util.ArrayList;

/**
 * Manages the user's collection of media entries in a single polymorphic
 * list of MediaEntry objects. Provides operations to add, remove, find,
 * filter, search, and summarize the contents of the library.
 *
 * @author Tobias Raian M. Edep
 */
public class Library {

 private final ArrayList<MediaEntry> ENTRIES;

 /**
  * Constructs an empty Library with no media entries.
  *
  *
  */
 public Library() {
  ENTRIES = new ArrayList<MediaEntry>();
 }

 /**
  * Adds a MediaEntry to the library. Accepts any subclass of
  * MediaEntry (Movie, TVSeries, or VideoGame).
  * <p>
  * <b>Precondition:</b> entry is not null <br>
  * <b>Postcondition:</b> the entry is added to the arraylist
  * </p>
  * @param entry the MediaEntry object to add
  *
  */
 public void addEntry(MediaEntry entry) {
  this.ENTRIES.add(entry);
 }

 /**
  * Removes the entry with the given title from the library,
  * ignoring case.
  * <p>
  * <b>Precondition:</b> title is not null <br>
  * <b>Postcondition:</b> the matching entry is removed from the arraylist
  * </p>
  * @param title the title of the entry to remove
  * @return true if an entry was found and removed, false if no
  *         matching entry was found
  *
  */
 public boolean removeEntry(String title) {
  boolean removed = false;
  MediaEntry found = findEntry(title);
  if (found != null) {
   ENTRIES.remove(found);
   removed = true;
  }
  return removed;
 }

 /**
  * Searches the library for an entry whose title matches the
  * given title, ignoring case.
  *
  * @param title the title to search for
  * @return the matching MediaEntry, or null if no entry with that
  *         title exists in the library
  *
  */
 public MediaEntry findEntry(String title) {
  MediaEntry result = null;
  for (MediaEntry e : ENTRIES)
   if (e.getTitle().equalsIgnoreCase(title))
    result = e;
  return result;
 }

 /**
  * Builds a list of every entry whose status matches the given
  * MediaStatus.
  * <p>
  * <b>Precondition:</b> status is not null <br>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  * @param status the MediaStatus to filter by (PLANNED, IN_PROGRESS,
  *               or COMPLETED)
  * @return a list of entries matching the given status
  *
  */
 public ArrayList<MediaEntry> filterByStatus(MediaStatus status) {
  ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();
  for (MediaEntry e : ENTRIES) {
   if (e.getStatus() == status) {
    result.add(e);
   }
  }
  return result;
 }

 /**
  * Builds a list of every entry belonging to the given media type.
  * The media type is matched case-insensitively against "Movie",
  * "TVSeries", or "VideoGame".
  * <p>
  * <b>Precondition:</b> type is not null <br>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  * @param type the type of media to filter by ("Movie", "TVSeries",
  *             or "VideoGame")
  * @return a list of entries matching the given media type
  *
  */
 public ArrayList<MediaEntry> filterByType(String type) {
  ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();
  for (MediaEntry e : ENTRIES) {
   if (e.getMediaType().equalsIgnoreCase(type)) {
    result.add(e);
   }
  }
  return result;
 }

 /**
  * Builds a list of every entry whose title or genre contains the
  * given keyword, ignoring case.
  * <p>
  * <b>Precondition:</b> keyword is not null <br>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  * @param keyword the keyword to search for in titles and genres
  * @return a list of entries matching the keyword
  *
  */
 public ArrayList<MediaEntry> searchEntries(String keyword) {
  ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();
  for (MediaEntry e : ENTRIES) {
   if (e.getTitle().toLowerCase().contains(keyword.toLowerCase())
           || e.getGenre().toLowerCase().contains(keyword.toLowerCase())) {
    result.add(e);
   }
  }
  return result;
 }

 /**
  * Returns the complete list of entries in the library.
  *
  * @return the list of all MediaEntry objects
  *
  */
 public ArrayList<MediaEntry> getAllEntries() {
  return this.ENTRIES;
 }

 /**
  * Builds a summary string reporting the total number of entries
  * in the library, the count of each media type, and the average
  * rating across all Completed entries that have been rated.
  * <p>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  * @return a String summarizing total entries, movie/TV/game counts,
  *         and the average rating of completed entries
  *
  */
 public String getSummary() {
  int total = ENTRIES.size();
  int movieCount = 0;
  int seriesCount = 0;
  int gameCount = 0;
  int ratingSum = 0;
  int ratedCount = 0;

  for (MediaEntry e : ENTRIES) {
   String type = e.getMediaType();
   if (type.equals("Movie")) {
    movieCount++;
   } else if (type.equals("TVSeries")) {
    seriesCount++;
   } else if (type.equals("VideoGame")) {
    gameCount++;
   }

   Integer r = e.getRating();
   if (r != null) {
    ratingSum += r;
    ratedCount++;
   }
  }

  String avgRatingText;
  if (ratedCount == 0) {
   avgRatingText = "No completed entries rated yet";
  } else {
   double average = (double) ratingSum / ratedCount;
   avgRatingText = String.format("%.2f", average);
  }

  return "Total entries: " + total
          + " | Movies: " + movieCount
          + " | TV Series: " + seriesCount
          + " | Video Games: " + gameCount
          + " | Average rating (completed): " + avgRatingText;
 }
}