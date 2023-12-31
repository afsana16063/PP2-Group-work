import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.List;


public class MovieGUI extends JFrame {
    private static MovieDatabase movieDatabase;
    private User currentUser;

    private JTextArea movieDetailsTextArea;
    private JTextField searchTextField;

    public MovieGUI(User user, MovieDatabase movieDatabase) {
        this.currentUser = user;
        this.movieDatabase = movieDatabase;

        currentUser.loadWatchlist();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Movie App");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
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

        updateMovieDetails();
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

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchTextField = new JTextField(20);
        JButton searchButton = new JButton("Search Movie");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = searchTextField.getText();
                try{
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
                try{
                    Movie movie = movieDatabase.getMovieDetails(title);
                    currentUser.addToWatchlist(movie);
                    JOptionPane.showMessageDialog(MovieGUI.this, "Movie added to watchlist: " + movie.getTitle());
                } catch(MovieNotFoundException ex) {
                    JOptionPane.showMessageDialog(MovieGUI.this, "Movie not found.");
                }
            }
        });

        removeFromWatchlistButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                String title = searchTextField.getText();
                Movie movie = movieDatabase.getMovieDetails(title);
                movieDetailsTextArea.setText(movie.toString());
                }catch (MovieNotFoundException ex) {
                    movieDetailsTextArea.setText("Movie not found.");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttonPanel.add(addToWatchlistButton);
        buttonPanel.add(removeFromWatchlistButton);

        return buttonPanel;
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
