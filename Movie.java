import java.io.Serializable;

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
        MovieDatabase movieDatabase = new MovieDatabase();
        User user= new User("User","password");
        Movie fightclub = new Movie("FightClub", "Tyler Durden", 2004, 120);
        movieDatabase.addMovie(fightclub);

        MovieGUI movieGUI= new MovieGUI(user, movieDatabase);
        SwingUtilities.invokeLater(()->{
            movieGUI.setVisible(true);
        });
    }
}

// Snowman