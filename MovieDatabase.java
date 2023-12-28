import java.util.ArrayList;
import java.util.List;

public class MovieDatabase {
    private List<Movie> movies;

    public MovieDatabase() {
        this.movies = new ArrayList<>();
    }

    public void addMovie(Movie movie) {
        if (!movies.contains(movie)) {
            movies.add(movie);
            System.out.println("Movie added to DB: " + movie.getTitle());
        } else {
            System.out.println("Movie already exists in DB: " + movie.getTitle());
        }
    }

    public void removeMovie(Movie movie) {
        if (movies.contains(movie)) {
            movies.remove(movie);
            System.out.println("Movie removed from DB: " + movie.getTitle());
        } else {
            System.out.println("Movie not found in DB: " + movie.getTitle());
        }
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public Movie getMovieDetails(String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
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
    public Movie(String title, String director, int releaseYear, int runningTime) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}

// Snowman