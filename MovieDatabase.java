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

public class MovieDatabase { 
    private String databaseFileName;
    private Map<String,Movie> movies;

    public MovieDatabase(String databaseFileName) {
        this.databaseFileName = databaseFileName;
        this.movies = new HashMap<>();
        loadMoviesFromDatabase();
    }

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

    
    public List<Movie> getMoviesSortedByReleaseYear() {
        return movies.values().stream()
                .sorted(Comparator.comparingInt(Movie::getReleaseYear))
                .collect(Collectors.toList());
    }

    public List<Movie> getMoviesByDirector(String director) {
        return movies.values().stream()
                .filter(movie -> movie.getDirector().equalsIgnoreCase(director))
                .collect(Collectors.toList());
    }

    public int getTotalWatchTime() {
        return movies.values().stream()
                .mapToInt(Movie::getRunningTime)
                .sum();
    }

    public void addMovie(Movie movie) {
        if (!movies.containsKey(movie.getTitle())) {
            movies.put(movie.getTitle(), movie);
            System.out.println("Movie added to DB: " + movie.getTitle());
        } else {
            System.out.println("Movie already exists in DB: " + movie.getTitle());
        }
    }

    public void removeMovie(Movie movie) {
        if (movies.containsKey(movie.getTitle())) {
            movies.remove(movie.getTitle());
            System.out.println("Movie removed from DB: " + movie.getTitle());
        } else {
            System.out.println("Movie not found in DB: " + movie.getTitle());
        }
    }

    public List<Movie> getMovies() {
        return new ArrayList<>(movies.values());
    }

    public Movie getMovieDetails(String title) {
        for (Movie movie:movies.values()){
            if(movie.getTitle().equalsIgnoreCase(title)){
            return movie;
        }}
            throw new MovieNotFoundException("Movie not found: " + title);
    }
}

class MovieNotFoundException extends RuntimeException {
    public MovieNotFoundException(String message) {
        super(message);
    }
}

// Snowman