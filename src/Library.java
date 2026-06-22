import java.util.ArrayList;

/**
 * Manages the user's collection of media entries, organized into three
 * separate lists by media type: Movie, TVSeries, and VideoGame.
 * Provides operations to add, remove, find, display, filter, and
 * summarize the contents of the library.
 *
 * @author Tobias Raian M. Edep
 */
public class Library {

 private ArrayList<Movie> movies;
 private ArrayList<TVSeries> tvSeriesList;
 private ArrayList<VideoGame> videoGames;

 /**
  * Constructs an empty Library with no movies, TV series, or
  * video games.
  *
  * @pre none
  * @post movies, tvSeriesList, and videoGames are each initialized
  * as empty ArrayLists.
  */
 public Library() {
  movies = new ArrayList<Movie>();
  tvSeriesList = new ArrayList<TVSeries>();
  videoGames = new ArrayList<VideoGame>();
 }

 /**
  * Adds a Movie to the library's movie list.
  *
  * @param movie the Movie object to add
  * @pre movie is not null
  * @post movie is appended to the end of the movies list
  */
 public void addMovie(Movie movie) {
  this.movies.add(movie);
 }

 /**
  * Adds a TVSeries to the library's TV series list.
  *
  * @param series the TVSeries object to add
  * @pre series is not null
  * @post series is appended to the end of the tvSeriesList
  */
 public void addTVSeries(TVSeries series) {
  this.tvSeriesList.add(series);
 }

 /**
  * Adds a VideoGame to the library's video game list.
  *
  * @param videoGame the VideoGame object to add
  * @pre videoGame is not null
  * @post videoGame is appended to the end of the videoGames list
  */
 public void addVideoGame(VideoGame videoGame) {
  this.videoGames.add(videoGame);
 }
 /**
  * Searches the movie list for a Movie whose title matches the
  * given title, ignoring case.
  *
  * @param title the title to search for
  * @return the matching Movie, or null if no Movie with that
  *         title exists in the library
  * @pre  title is not null
  * @post the movies list is unchanged
  */
 public Movie findMovie(String title) {
  for (Movie m : movies) {
   if (m.getEntry().getTitle().equalsIgnoreCase(title))
    return m;
  }
  return null;
 }

 /**
  * Searches the TV series list for a TVSeries whose title matches
  * the given title, ignoring case.
  *
  * @param title the title to search for
  * @return the matching TVSeries, or null if no TVSeries with
  *         that title exists in the library
  * @pre  title is not null
  * @post the tvSeriesList is unchanged
  */
 public TVSeries findTVSeries(String title) {
  for (TVSeries t : tvSeriesList) {
   if (t.getEntry().getTitle().equalsIgnoreCase(title))
    return t;
  }
  return null;
 }

 /**
  * Searches the video game list for a VideoGame whose title
  * matches the given title, ignoring case.
  *
  * @param title the title to search for
  * @return the matching VideoGame, or null if no VideoGame with
  *         that title exists in the library
  * @pre  title is not null
  * @post the videoGames list is unchanged
  */
 public VideoGame findVideoGame(String title) {
  for (VideoGame v : videoGames) {
   if (v.getEntry().getTitle().equalsIgnoreCase(title))
    return v;
  }
  return null;
 }

 /**
  * Removes the entry with the given title from the list matching
  * the given media type. The media type is matched case-insensitively
  * against "Movie", "TVSeries", or "VideoGame".
  *
  * @param title     the title of the entry to remove
  * @param mediaType the type of media to search ("Movie", "TVSeries",
  *                  or "VideoGame")
  * @return true if an entry was found and removed, false if no
  *         matching entry was found or mediaType was not recognized
  * @pre  title and mediaType are not null
  * @post if a match is found, it is removed from the corresponding
  *       list; otherwise no list is modified
  */
 public boolean removeEntry(String title, String mediaType) {
  if (mediaType.equalsIgnoreCase("Movie")) {
   Movie m = findMovie(title);
   if (m != null) {
    movies.remove(m);
    return true;
   }
  } else if (mediaType.equalsIgnoreCase("TVSeries")) {
   TVSeries t = findTVSeries(title);
   if (t != null) {
    tvSeriesList.remove(t);
    return true;
   }
  } else if (mediaType.equalsIgnoreCase("VideoGame")) {
   VideoGame g = findVideoGame(title);
   if (g != null) {
    videoGames.remove(g);
    return true;
   }
  }
  return false;
 }


}


