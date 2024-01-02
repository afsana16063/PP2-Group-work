import java.io.Serializable;
import java.util.List;

import javax.swing.SwingUtilities;

public class Movie implements Serializable {
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
        return title.toLowerCase();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getRunningTime() {
        return runningTime;
    }

    public void setRunningTime(int runningTime) {
        this.runningTime = runningTime;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", director='" + director + '\'' +
                ", releaseYear=" + releaseYear +
                ", runningTime=" + runningTime +
                '}';
    }

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