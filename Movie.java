import java.io.Serializable;
import java.util.List;

import javax.swing.SwingUtilities;

/**
 * Represents a Movie with title, director, release year, and running time.
 * Implements Serializable to support object serialization.
 */
public class Movie implements Serializable {
    private String title;
    private String director;
    private int releaseYear;
    private int runningTime;

    /**
     * Constructs a Movie with the specified title, director, release year, and running time.
     *
     * @param title       the title of the movie
     * @param director    the director of the movie
     * @param releaseYear the release year of the movie
     * @param runningTime the running time of the movie in minutes
     */
    public Movie(String title, String director, int releaseYear, int runningTime) {
        this.title = title;
        this.director = director;
        this.releaseYear = releaseYear;
        this.runningTime = runningTime;
    }

    /**
     * Gets the title of the movie in lowercase.
     *
     * @return the title of the movie
     */
    public String getTitle() {
        return title.toLowerCase();
    }

    /**
     * Sets the title of the movie.
     *
     * @param title the new title of the movie
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the director of the movie.
     *
     * @return the director of the movie
     */
    public String getDirector() {
        return director;
    }

    /**
     * Sets the director of the movie.
     *
     * @param director the new director of the movie
     */
    public void setDirector(String director) {
        this.director = director;
    }

    /**
     * Gets the release year of the movie.
     *
     * @return the release year of the movie
     */
    public int getReleaseYear() {
        return releaseYear;
    }

    /**
     * Sets the release year of the movie.
     *
     * @param releaseYear the new release year of the movie
     */
    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    /**
     * Gets the running time of the movie in minutes.
     *
     * @return the running time of the movie
     */
    public int getRunningTime() {
        return runningTime;
    }

    /**
     * Sets the running time of the movie.
     *
     * @param runningTime the new running time of the movie
     */
    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    /**
     * Returns a string representation of the movie.
     *
     * @return a string representation of the movie
     */
    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", releaseYear=" + releaseYear +
                ", runningTime=" + runningTime +
                '}';
    }

    /**
     * Main method to demonstrate Movie functionalities.
     * Initializes a MovieDatabase, authenticates a user, and displays the MovieGUI.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase("movie_db.txt");
        User authUser = authUser("User", "password");
        if (authUser != null) {
            MovieGUI movieGUI = new MovieGUI(authUser, movieDatabase);
            SwingUtilities.invokeLater(() -> {
                movieGUI.setVisible(true);
            });
        } else {
            System.out.println("Authentication failed. Exiting.");
        }

        Movie fightclub = new Movie("FightClub", "Tyler Durden", 2004, 120);
        User user = authUser("user", "password");
        movieDatabase.addMovie(fightclub);

        MovieGUI movieGUI = new MovieGUI(user, movieDatabase);
        SwingUtilities.invokeLater(() -> {
            movieGUI.setVisible(true);
        });
    }

    /**
     * Authenticates a user with the given username and password.
     *
     * @param username the username for authentication
     * @param password the password for authentication
     * @return the authenticated user, or null if authentication fails
     */
    private static User authUser(String username, String password) {
        List<User> users = User.loadFromDatabase();

        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }
}

// Snowman
