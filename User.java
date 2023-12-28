import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Movie> watchlist;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.watchlist = new ArrayList<>();
    }

    public static void register(String username, String password) {
        User user = new User(username, password);
        user.saveToDatabase();
    }

    public static boolean login(String username, String password) {
        List<User> users = loadFromDatabase();

        for (User user : users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                return true;
            }
        }

        return false;
    }

    public void addToWatchlist(Movie movie) {
        watchlist.add(movie);
    }

    public void removeFromWatchlist(Movie movie) {
        watchlist.remove(movie);
    }

    private void saveToDatabase() {
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

    private static List<User> loadFromDatabase() {
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

// Snowman