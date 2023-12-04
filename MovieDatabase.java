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

    public Movie getMovieDetails(String title) {
        for (Movie movie : movies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null; // Movie not found
    }
}

class Movie {
    private String title;
    private String director;
    private int releaseYear;
    private int runningTime;

    public Movie(String title, String director, int releaseYear, int runningTime) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runningTime = runningTime;
    }

    public String getTitle() {
        return title;
    }
}

// Snowman