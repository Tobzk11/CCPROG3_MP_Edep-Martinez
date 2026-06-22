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
}

