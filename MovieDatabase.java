import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.stream.Collectors;

public class MovieDatabase {
    private Map<String,Movie> movies;

    public MovieDatabase() {
        this.movies = new HashMap<>();
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

class Movie {
    private String title;
    private int releaseYear;
    private String director;
    private int runningTime;
    public Movie(String title, String director, int releaseYear, int runningTime) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.director = director;
        this.runningTime= runningTime;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear(){
        return releaseYear;
    }
    
    public String getDirector(){
        return director;
    }

    public int getRunningTime() {
    return runningTime;
    }

    
    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase();
        Movie fightclub = new Movie("FightClub", "Tyler Durden", 2004, 120);
        movieDatabase.addMovie(fightclub);
    }
}

// Snowman