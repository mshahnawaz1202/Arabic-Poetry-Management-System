package pl.controller;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import bl.IBLFacade;
import dto.BookDTO;
import dto.IndexResultDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class IndexController implements Initializable {

    private IBLFacade facade;

    @FXML
    private ListView<BookDTO> booksList;
    @FXML
    private ListView<String> rootsList;
    @FXML
    private ListView<String> lemmasList;
    @FXML
    private ListView<String> tokensList;

    @FXML
    private TableView<IndexResultDTO> resultsTable;
    @FXML
    private TableColumn<IndexResultDTO, Integer> verseIdCol;
    @FXML
    private TableColumn<IndexResultDTO, Integer> verseNoCol;
    @FXML
    private TableColumn<IndexResultDTO, String> verseTextCol;
    @FXML
    private TableColumn<IndexResultDTO, String> poemTitleCol;

    @FXML
    private Label statusLabel;

    public void setFacade(IBLFacade facade) {
        this.facade = facade;
        loadBooks();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Table Columns
        verseIdCol.setCellValueFactory(new PropertyValueFactory<>("verseId"));
        verseNoCol.setCellValueFactory(new PropertyValueFactory<>("verseNo"));
        verseTextCol.setCellValueFactory(new PropertyValueFactory<>("verseText"));
        poemTitleCol.setCellValueFactory(new PropertyValueFactory<>("poemTitle"));

        // Setup Selection Listeners
        booksList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Clear subsequent lists immediately
                rootsList.getItems().clear();
                lemmasList.getItems().clear();
                tokensList.getItems().clear();
                resultsTable.getItems().clear();

                loadRootsForBook(newVal);
            }
        });

        rootsList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                lemmasList.getItems().clear();
                tokensList.getItems().clear();
                resultsTable.getItems().clear();

                loadLemmasForRoot(newVal);
            }
        });

        lemmasList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                tokensList.getItems().clear();
                resultsTable.getItems().clear();

                loadTokensForLemma(newVal);
            }
        });

        tokensList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadVersesForToken(newVal);
            }
        });
    }

    // Helper to run background tasks
    private <T> void runTask(Task<List<T>> task, String loadingMessage) {
        statusLabel.setText(loadingMessage);

        task.setOnFailed(e -> {
            statusLabel.setText("Error: " + task.getException().getMessage());
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    // Helper to extract just the word part from "word (count occurrences)"
    private String extractWord(String wordWithCount) {
        String result = wordWithCount;
        if (wordWithCount != null && wordWithCount.contains(" (")) {
            result = wordWithCount.substring(0, wordWithCount.lastIndexOf(" ("));
        }
        System.out.println("ExtractWord: Input='" + wordWithCount + "' -> Output='" + result + "'");
        return result != null ? result.trim() : null;
    }

    private void loadBooks() {
        if (facade == null)
            return;
        System.out.println("Loading books...");

        Task<List<BookDTO>> task = new Task<>() {
            @Override
            protected List<BookDTO> call() throws Exception {
                return facade.getAllBooks();
            }
        };

        task.setOnSucceeded(e -> {
            List<BookDTO> res = task.getValue();
            System.out.println("Loaded " + (res != null ? res.size() : 0) + " books.");
            booksList.setItems(FXCollections.observableArrayList(res));
            statusLabel.setText("Select a book to begin.");
        });

        runTask(task, "Loading books...");
    }

    private void loadRootsForBook(BookDTO book) {
        if (facade == null)
            return;
        System.out.println("Loading roots for book: " + (book != null ? book.getTitle() : "null"));

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                if (book == null)
                    return Collections.emptyList();
                int bookId = facade.getBookID(book);
                System.out.println("Book ID for " + book.getTitle() + ": " + bookId);
                return facade.getBookRoots(bookId);
            }
        };

        task.setOnSucceeded(e -> {
            List<String> res = task.getValue();
            System.out.println("Loaded " + (res != null ? res.size() : 0) + " roots.");
            rootsList.setItems(FXCollections.observableArrayList(res));
            statusLabel.setText("Loaded roots for: " + book.getTitle());
        });

        runTask(task, "Loading roots for " + book.getTitle() + "...");
    }

    private void loadLemmasForRoot(String root) {
        if (facade == null)
            return;
        BookDTO selectedBook = booksList.getSelectionModel().getSelectedItem();
        if (selectedBook == null)
            return;

        String cleanRoot = extractWord(root);
        System.out.println("Loading lemmas for root: " + cleanRoot);

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                int bookId = facade.getBookID(selectedBook);
                return facade.getLemmasByRoot(bookId, cleanRoot);
            }
        };

        task.setOnSucceeded(e -> {
            List<String> res = task.getValue();
            System.out.println("Loaded " + (res != null ? res.size() : 0) + " lemmas for root " + cleanRoot);
            lemmasList.setItems(FXCollections.observableArrayList(res));
            statusLabel.setText("Loaded lemmas for root: " + cleanRoot);
        });

        runTask(task, "Loading lemmas for " + cleanRoot + "...");
    }

    private void loadTokensForLemma(String lemma) {
        if (facade == null)
            return;
        BookDTO selectedBook = booksList.getSelectionModel().getSelectedItem();
        if (selectedBook == null)
            return;

        String cleanLemma = extractWord(lemma);
        System.out.println("Loading tokens for lemma: " + cleanLemma);

        Task<List<String>> task = new Task<>() {
            @Override
            protected List<String> call() throws Exception {
                int bookId = facade.getBookID(selectedBook);
                return facade.getTokensByLemma(bookId, cleanLemma);
            }
        };

        task.setOnSucceeded(e -> {
            List<String> res = task.getValue();
            System.out.println("Loaded " + (res != null ? res.size() : 0) + " tokens for lemma " + cleanLemma);
            tokensList.setItems(FXCollections.observableArrayList(res));
            statusLabel.setText("Loaded tokens for lemma: " + cleanLemma);
        });

        runTask(task, "Loading tokens for " + cleanLemma + "...");
    }

    private void loadVersesForToken(String token) {
        if (facade == null)
            return;
        BookDTO selectedBook = booksList.getSelectionModel().getSelectedItem();
        if (selectedBook == null)
            return;

        String cleanToken = extractWord(token);
        System.out.println("Loading verses for token: " + cleanToken);

        Task<List<IndexResultDTO>> task = new Task<>() {
            @Override
            protected List<IndexResultDTO> call() throws Exception {
                int bookId = facade.getBookID(selectedBook);
                return facade.getTokenOccurrences(bookId, cleanToken);
            }
        };

        task.setOnSucceeded(e -> {
            List<IndexResultDTO> results = task.getValue();
            System.out.println("Found " + (results != null ? results.size() : 0) + " verses for token " + cleanToken);
            resultsTable.setItems(FXCollections.observableArrayList(results));
            statusLabel.setText("Found " + results.size() + " occurrences of: " + cleanToken);
        });

        runTask(task, "Finding verses for " + cleanToken + "...");
    }
}