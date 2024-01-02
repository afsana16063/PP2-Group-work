import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The User class represents a user of the movie application.
 * It includes methods for user registration, login, managing a watchlist,
 * and interaction with the user database.
 */

public class User {
    private String username;
    private String password;
    private List<Movie> watchlist;

    /**
     * Constructs a User with the specified username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     */

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.watchlist = new ArrayList<>();
    }

    /**
     * Registers a new user with the given username and password.
     *
     * @param username The desired username for registration.
     * @param password The desired password for registration.
     * @return The newly registered user.
     */

    public static User register(String username, String password) {
        User user = new User(username, password);
        user.saveToDatabase();
        return user;  //change no 1
    }

    /**
     * Attempts to log in a user with the provided username and password.
     *
     * @param username The username for login.
     * @param password The password for login.
     * @return An Optional containing the logged-in user if successful, empty otherwise.
     */

    public static Optional<User> login(String username, String password) {
        List<User> users = loadFromDatabase();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return Optional.of(user);
            }
        }

        return Optional.empty();
    }

    /**
     * Saves the user's watchlist to a serialized file.
     */

    public void saveWatchlist() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(username + "_wl.ser"))) {
            oos.writeObject(watchlist);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the user's watchlist from a serialized file.
     *
     * @return The loaded watchlist.
     */

    @SuppressWarnings("unchecked")
    public List<Movie> loadWatchlist() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(username + "_wl.ser"))) {
            watchlist = (List<Movie>) ois.readObject();
            System.out.println("Watchlist content: " + watchlist);
            return watchlist;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Retrieves the user's password.
     *
     * @return The user's password.
     */

    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user's username.
     *
     * @return The user's username.
     */

    public String getUsername() {
        return username;
    }

    /**
     * Adds a movie to the user's watchlist.
     *
     * @param movie The movie to be added to the watchlist.
     */

    public void addToWatchlist(Movie movie) {
        watchlist.add(movie);
        saveWatchlist();
    }

    /**
     * Removes a movie from the user's watchlist.
     *
     * @param movie The movie to be removed from the watchlist.
     */

    public void removeFromWatchlist(Movie movie) {
        watchlist.removeIf(m -> m.getTitle().equalsIgnoreCase(movie.getTitle()));
        saveWatchlist();
    }

    /**
     * Saves the user's information to the user database.
     */

    public void saveToDatabase() {
        try (BufferedReader reader = new BufferedReader(new FileReader("user_db.txt"))) {
            List<User> users = loadFromDatabase();
            users.add(this);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("user_db.txt"))) {
                for (User user : users) {
                    writer.write(user.username + "," + user.password);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads user information from the user database.
     *
     * @return A list of users loaded from the database.
     */

    public static List<User> loadFromDatabase() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("user_db.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                users.add(new User(parts[0], parts[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return users;
    }
}