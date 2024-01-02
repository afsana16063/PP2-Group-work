import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class MovieGUI extends JFrame {
    private static MovieDatabase movieDatabase;
    private User currentUser;

    private JTextArea movieDetailsTextArea;
    private JTextField searchTextField;

    private JList<String> watchlistJList;

    public MovieGUI(User user, MovieDatabase movieDatabase) {
        this.currentUser = user;
        MovieGUI.movieDatabase = movieDatabase;
        showLoginOrRegisterScreen();
    }
    
    private void showLoginOrRegisterScreen() {
        JPanel choicePanel = new JPanel(new GridLayout(1, 2));

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choicePanel.setVisible(false);
                showLoginScreen();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                choicePanel.setVisible(false);
                showRegisterScreen();
            }
        });

        choicePanel.add(loginButton);
        choicePanel.add(registerButton);

        getContentPane().add(choicePanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    private void showRegisterScreen() { //change 2
        JPanel registerPanel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
    
        registerPanel.add(new JLabel("Username:"));
        registerPanel.add(usernameField);
        registerPanel.add(new JLabel("Password:"));
        registerPanel.add(passwordField);
    
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                User user =  User.register(username, password);
                user.saveWatchlist();
                getContentPane().removeAll();
                showLoginScreen();
                revalidate();
                repaint();
            }
        });
        registerPanel.add(registerButton);
    
        getContentPane().add(registerPanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void showLoginScreen() {
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                Optional<User> user = User.login(username, password);
                if (user.isPresent()) {
                    currentUser = user.get();
                    getContentPane().removeAll();
                    initializeUI();
                    revalidate();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(MovieGUI.this, "Invalid username or password. Try again.");
                }
            }
        });

        loginPanel.add(loginButton);

        getContentPane().add(loginPanel);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initializeUI() {
        setTitle("Movie App");
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel searchPanel = createSearchPanel();
        add(searchPanel, BorderLayout.NORTH);

        movieDetailsTextArea = new JTextArea();
        movieDetailsTextArea.setEditable(false);
        JScrollPane detailsScrollPane = new JScrollPane(movieDetailsTextArea);
        add(detailsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);

        JPanel sortingPanel = createSortingPanel();
        add(sortingPanel, BorderLayout.WEST);

        JPanel watchlistPanel = createWatchlistPanel();
        add(watchlistPanel, BorderLayout.EAST);

        updateMovieDetails();
        updateWatchlist();
    }

    private JPanel createWatchlistPanel() {
        JPanel watchlistPanel = new JPanel(new BorderLayout());
        watchlistJList = new JList<>();
        JScrollPane watchlistScrollPane = new JScrollPane(watchlistJList);

        watchlistPanel.add(new JLabel("Watchlist"), BorderLayout.NORTH);
        watchlistPanel.add(watchlistScrollPane, BorderLayout.CENTER);

        return watchlistPanel;
    }

    private void updateWatchlist() {
        List<Movie> watchlist = currentUser.loadWatchlist();
        DefaultListModel<String> watchlistModel = new DefaultListModel<>();
        for (Movie movie : watchlist) {
            watchlistModel.addElement(movie.getTitle());
        }
        watchlistJList.setModel(watchlistModel);
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchTextField = new JTextField(20);
        JButton searchButton = new JButton("Search Movie");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = searchTextField.getText();
                try {
                    Movie movie = movieDatabase.getMovieDetails(title);
                    movieDetailsTextArea.setText(movie.toString());
                } catch (MovieNotFoundException ex) {
                    movieDetailsTextArea.setText("Movie not found.");
                }
            }
        });

        searchPanel.add(new JLabel("Enter Movie Title: "));
        searchPanel.add(searchTextField);
        searchPanel.add(searchButton);

        return searchPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton addToWatchlistButton = new JButton("Add to watchlist");
        JButton removeFromWatchlistButton = new JButton("Remove from watchlist");

        addToWatchlistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = searchTextField.getText();
                try {
                    Movie movie = movieDatabase.getMovieDetails(title);
                    currentUser.addToWatchlist(movie);
                    JOptionPane.showMessageDialog(MovieGUI.this, "Movie added to watchlist: " + movie.getTitle());
                } catch (MovieNotFoundException ex) {
                    JOptionPane.showMessageDialog(MovieGUI.this, "Movie not found.");
                }
                updateWatchlist();
            }
        });

        removeFromWatchlistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = watchlistJList.getSelectedIndex();
                if (selectedIndex != -1) {
                String title = watchlistJList.getModel().getElementAt(selectedIndex);
                try {
                    currentUser.removeFromWatchlist(new Movie(title, "", 0, 0));  
                    JOptionPane.showMessageDialog(MovieGUI.this, "Movie removed from watchlist: " + title);
                    updateWatchlist();
                } catch (MovieNotFoundException ex) {
                    JOptionPane.showMessageDialog(MovieGUI.this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(MovieGUI.this, " Select a movie from the watchlist.");
            }
            }
        });

        buttonPanel.add(addToWatchlistButton);
        buttonPanel.add(removeFromWatchlistButton);

        return buttonPanel;
    }

    private JPanel createSortingPanel() {
        JPanel sortingPanel = new JPanel();
        sortingPanel.setLayout(new BoxLayout(sortingPanel, BoxLayout.Y_AXIS));

        JButton sortByTitleButton = new JButton("Sort by Title");
        JButton sortByReleaseYearButton = new JButton("Sort by Release Year");

        sortByTitleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortAndDisplayMovies(Comparator.comparing(Movie::getTitle));
            }
        });

        sortByReleaseYearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sortAndDisplayMovies(Comparator.comparingInt(Movie::getReleaseYear));
            }
        });

        sortingPanel.add(new JLabel("Sort Movies:"));
        sortingPanel.add(sortByTitleButton);
        sortingPanel.add(sortByReleaseYearButton);

        return sortingPanel;
    }

    private void sortAndDisplayMovies(Comparator<Movie> comparator) {
        List<Movie> sortedMovies = movieDatabase.getMovies();
        sortedMovies.sort(comparator);

        displayMovies(sortedMovies);
    }

    private void displayMovies(List<Movie> movies) {
        StringBuilder sb = new StringBuilder();
        for (Movie movie : movies) {
            sb.append(movie.toString()).append("\n\n");
        }
        movieDetailsTextArea.setText(sb.toString());
    }

    private void updateMovieDetails() {
        displayMovies(movieDatabase.getMovies());
    }

    public void setDefaultCloseOperation(int operation) {
        currentUser.saveWatchlist();
        super.setDefaultCloseOperation(operation);
    }

    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase();
        User user = new User("User", "password");

        MovieGUI MovieGUI = new MovieGUI(user, movieDatabase);
        SwingUtilities.invokeLater(() -> {
            MovieGUI.setVisible(true);
        });
    }
}