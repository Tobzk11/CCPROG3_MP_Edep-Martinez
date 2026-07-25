import java.util.ArrayList;

/**
 * Manages the user's collection of media entries in a single polymorphic
 * list of MediaEntry objects. Because the list is declared to hold
 * MediaEntry, it can store Movie, TVSeries, and VideoGame objects side by
 * side and operate on all of them through the shared base type.
 * <p>
 * Provides operations to add, remove, find, filter, search, and summarize
 * the contents of the library.
 * </p>
 */
public class Library {

 private final ArrayList<MediaEntry> ENTRIES;

 /**
  * Constructs an empty Library with no media entries.
  * <p>
  * <b>Postcondition:</b> the library contains zero entries
  * </p>
  */
 public Library() {
  this.ENTRIES = new ArrayList<MediaEntry>();
 }

 /**
  * Adds a MediaEntry to the library. Accepts any subclass of MediaEntry,
  * namely Movie, TVSeries, or VideoGame.
  * <p>
  * <b>Precondition:</b> entry is not null <br>
  * <b>Postcondition:</b> the entry is appended to the list
  * </p>
  *
  * @param entry the MediaEntry object to add
  */
 public void addEntry(MediaEntry entry) {
  this.ENTRIES.add(entry);
 }

 /**
  * Removes the entry with the given title from the library, ignoring case.
  * <p>
  * <b>Precondition:</b> title is not null <br>
  * <b>Postcondition:</b> the matching entry is removed from the list
  * </p>
  *
  * @param title the title of the entry to remove
  * @return true if an entry was found and removed, false if no matching
  *         entry was found
  */
 public boolean removeEntry(String title) {
  boolean removed = false;
  MediaEntry found = findEntry(title);

  if (found != null) {
   this.ENTRIES.remove(found);
   removed = true;
  }

  return removed;
 }

 /**
  * Searches the library for the first entry whose title matches the given
  * title, ignoring case.
  * <p>
  * <b>Precondition:</b> title is not null <br>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  *
  * @param title the title to search for
  * @return the matching MediaEntry, or null if no entry with that title
  *         exists in the library
  */
 public MediaEntry findEntry(String title) {
  MediaEntry result = null;

  for (MediaEntry e : this.ENTRIES) {
   if (result == null && e.getTitle().equalsIgnoreCase(title)) {
    result = e;
   }
  }

  return result;
 }

 /**
  * Builds a list of every entry whose status matches the given MediaStatus.
  * <p>
  * <b>Precondition:</b> status is not null <br>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  *
  * @param status the MediaStatus to filter by
  * @return a list of entries matching the given status
  */
 public ArrayList<MediaEntry> filterByStatus(MediaStatus status) {
  ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();

  for (MediaEntry e : this.ENTRIES) {
   if (e.getStatus() == status) {
    result.add(e);
   }
  }

  return result;
 }

 /**
  * Builds a list of every entry belonging to the given media type. The
  * media type is matched case-insensitively against the labels returned by
  * getMediaType, namely "Movie", "TVSeries", and "VideoGame".
  * <p>
  * <b>Precondition:</b> type is not null <br>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  *
  * @param type the type of media to filter by
  * @return a list of entries matching the given media type
  */
 public ArrayList<MediaEntry> filterByType(String type) {
  ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();

  for (MediaEntry e : this.ENTRIES) {
   if (e.getMediaType().equalsIgnoreCase(type)) {
    result.add(e);
   }
  }

  return result;
 }

 /**
  * Builds a list of every entry whose title or genre contains the given
  * keyword, ignoring case.
  * <p>
  * <b>Precondition:</b> keyword is not null <br>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  *
  * @param keyword the keyword to search for in titles and genres
  * @return a list of entries matching the keyword
  */
 public ArrayList<MediaEntry> searchEntries(String keyword) {
  ArrayList<MediaEntry> result = new ArrayList<MediaEntry>();
  String lowerKeyword = keyword.toLowerCase();

  for (MediaEntry e : this.ENTRIES) {
   if (e.getTitle().toLowerCase().contains(lowerKeyword)
           || e.getGenre().toLowerCase().contains(lowerKeyword)) {
    result.add(e);
   }
  }

  return result;
 }

 /**
  * Returns the complete list of entries in the library.
  *
  * @return the list of all MediaEntry objects
  */
 public ArrayList<MediaEntry> getAllEntries() {
  return this.ENTRIES;
 }

 /**
  * Builds a summary string reporting the total number of entries in the
  * library, the count of each media type, and the average rating across
  * every entry that has been rated. Since a rating can only be attached to
  * a COMPLETED entry, every rated entry is necessarily a completed one.
  * <p>
  * <b>Postcondition:</b> the library contents are unchanged
  * </p>
  *
  * @return a String summarizing total entries, per-type counts, and the
  *         average rating of completed entries
  */
 public String getSummary() {
  int total = this.ENTRIES.size();
  int movieCount = 0;
  int seriesCount = 0;
  int gameCount = 0;
  int ratingSum = 0;
  int ratedCount = 0;

  for (MediaEntry e : this.ENTRIES) {
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