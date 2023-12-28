import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MovieGUI extends JFrame {
    private MovieDatabase movieDatabase;
    private User currentUser;

    private JTextArea movieDetailsTextArea;
    private JTextField searchTextField;

    public MovieGUI(User user, MovieDatabase movieDatabase) {
        this.currentUser = user;
        this.movieDatabase = movieDatabase;

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
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchTextField = new JTextField(20);
        JButton searchButton = new JButton("Search Movie");

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = searchTextField.getText();
                Movie movie = movieDatabase.getMovieDetails(title);
                if (movie != null) {
                    movieDetailsTextArea.setText(movie.toString());
                } else {
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
                Movie movie = movieDatabase.getMovieDetails(title);
                if (movie != null) {
                    currentUser.addToWatchlist(movie);
                    JOptionPane.showMessageDialog(MovieGUI.this, "Movie added to watchlist: " + movie.getTitle());
                } else {
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

    public static void main(String[] args) {
        MovieDatabase movieDatabase = new MovieDatabase();
        User user = new User("User", "password");

        MovieGUI MovieGUI = new MovieGUI(user, movieDatabase);
        SwingUtilities.invokeLater(() -> {
            MovieGUI.setVisible(true);
        });
    }
}
