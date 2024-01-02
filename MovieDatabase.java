import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * The MovieDatabase class represents a database of movies.
 * It includes methods to load movies from a file, perform various queries,
 * and manage the collection of movies.
 */

public class MovieDatabase { 
    private String databaseFileName;
    private Map<String,Movie> movies;

    /**
     * Constructs a MovieDatabase with the specified database file name.
     *
     * @param databaseFileName The name of the file containing movie data.
     */

    public MovieDatabase(String databaseFileName) {
        this.databaseFileName = databaseFileName;
        this.movies = new HashMap<>();
        loadMoviesFromDatabase();
    }

    /**
     * Main method for testing MovieDatabase functionality.
     *
     * @param args Command-line arguments (not used).
     */

    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase("movie_db.txt");
    }

    private void loadMoviesFromDatabase() {
        try 
        {
            movies = Files.lines(Paths.get(databaseFileName))
                    .map(line -> {
                        String[] parts = line.split(",");
                        String title = parts[0].trim();
                        String director = parts[1].trim();
                        int releaseYear = Integer.parseInt(parts[2].trim());
                        int runningTime = Integer.parseInt(parts[3].trim());
                        return new Movie(title, director, releaseYear, runningTime);
                    })
                    .collect(Collectors.toMap(movie -> movie.getTitle().toLowerCase(), movie -> movie));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a list of movies sorted by release year.
     *
     * @return A list of movies sorted by release year.
     */
    
    public List<Movie> getMoviesSortedByReleaseYear() {
        return movies.values().stream()
                .sorted(Comparator.comparingInt(Movie::getReleaseYear))
                .collect(Collectors.toList());
    }

    /**
     * Gets a list of movies by a specified director.
     *
     * @param director The director's name.
     * @return A list of movies directed by the specified director.
     */

    public List<Movie> getMoviesByDirector(String director) {
        return movies.values().stream()
                .filter(movie -> movie.getDirector().equalsIgnoreCase(director))
                .collect(Collectors.toList());
    }

    /**
     * Gets the total watch time of all movies in the database.
     *
     * @return The total watch time in minutes.
     */

    public int getTotalWatchTime() {
        return movies.values().stream()
                .mapToInt(Movie::getRunningTime)
                .sum();
    }

    /**
     * Adds a movie to the database.
     *
     * @param movie The movie to be added.
     */

    public void addMovie(Movie movie) {
        if (!movies.containsKey(movie.getTitle())) {
            movies.put(movie.getTitle(), movie);
            System.out.println("Movie added to DB: " + movie.getTitle());
        } else {
            System.out.println("Movie already exists in DB: " + movie.getTitle());
        }
    }

    /**
     * Removes a movie from the database.
     *
     * @param movie The movie to be removed.
     */

    public void removeMovie(Movie movie) {
        if (movies.containsKey(movie.getTitle())) {
            movies.remove(movie.getTitle());
            System.out.println("Movie removed from DB: " + movie.getTitle());
        } else {
            System.out.println("Movie not found in DB: " + movie.getTitle());
        }
    }

    /**
     * Gets a list of all movies in the database.
     *
     * @return A list of all movies.
     */

    public List<Movie> getMovies() {
        return new ArrayList<>(movies.values());
    }

    /**
     * Gets the details of a specific movie by title.
     *
     * @param title The title of the movie to retrieve.
     * @return The details of the specified movie.
     * @throws MovieNotFoundException If the movie is not found.
     */

    public Movie getMovieDetails(String title) {
        for (Movie movie:movies.values()){
            if(movie.getTitle().equalsIgnoreCase(title)){
            return movie;
        }}
            throw new MovieNotFoundException("Movie not found: " + title);
    }
}

/**
 * Custom exception class for signaling that a movie is not found.
 */

class MovieNotFoundException extends RuntimeException {
    /**
     * Constructs a MovieNotFoundException with the specified error message.
     *
     * @param message The error message.
     */
    
    public MovieNotFoundException(String message) {
        super(message);
    }
}

// Snowman