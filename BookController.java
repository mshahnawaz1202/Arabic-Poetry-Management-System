package pl.controller;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import dto.BookDTO;
import dto.ImportResultDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.Main;

public class BookController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField compilerField;
    @FXML
    private DatePicker eraDatePicker;

    @FXML
    private Button addBookBtn;
    @FXML
    private Button updateBookBtn;
    @FXML
    private Button deleteBookBtn;
    @FXML
    private Button openPoemsBtn;
    @FXML
    private Button importBookBtn;
    @FXML
    private Button refreshBtn;

    @FXML
    private TableView<BookDTO> booksTable;
    @FXML
    private TableColumn<BookDTO, Integer> bookIdColumn;
    @FXML
    private TableColumn<BookDTO, String> titleColumn;
    @FXML
    private TableColumn<BookDTO, String> compilerColumn;
    @FXML
    private TableColumn<BookDTO, Date> eraColumn;

    @FXML
    private Label statusLabel;

    private ObservableList<BookDTO> booksList = FXCollections.observableArrayList();

    private bl.IBLFacade facade;

    public void setFacade(bl.IBLFacade facade) {
        this.facade = facade;
        // Load books now that facade is available
        loadBooks();
    }

    private bl.IBLFacade getFacade() {
        if (facade == null) {
            return Main.getBLFacade();
        }
        return facade;
    }

    @FXML
    public void initialize() {
        // Set up table columns
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        compilerColumn.setCellValueFactory(new PropertyValueFactory<>("compiler"));
        eraColumn.setCellValueFactory(new PropertyValueFactory<>("era"));

        // Add selection listener
        booksTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
    }

    private void loadBooks() {
        try {
            List<BookDTO> books = getFacade().getAllBooks();
            booksList.clear();
            booksList.addAll(books);
            booksTable.setItems(booksList);
            showStatus("Books loaded successfully", false);
        } catch (Exception e) {
            showStatus("Error loading books: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void populateFields(BookDTO book) {
        titleField.setText(book.getTitle());
        compilerField.setText(book.getCompiler());
        if (book.getEra() != null) {
            eraDatePicker.setValue(book.getEra().toLocalDate());
        }
    }

    private void clearFields() {
        titleField.clear();
        compilerField.clear();
        eraDatePicker.setValue(null);
        booksTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAddBook() {
        try {
            String title = titleField.getText().trim();
            String compiler = compilerField.getText().trim();
            LocalDate eraDate = eraDatePicker.getValue();

            if (title.isEmpty()) {
                showStatus("Please enter a book title", true);
                return;
            }

            BookDTO book = new BookDTO();
            book.setTitle(title);
            book.setCompiler(compiler.isEmpty() ? "Unknown" : compiler);
            book.setEra(eraDate != null ? Date.valueOf(eraDate) : new Date(System.currentTimeMillis()));

            getFacade().createBook(book);

            clearFields();
            loadBooks();
            showStatus("Book added successfully!", false);

        } catch (Exception e) {
            showStatus("Error adding book: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateBook() {
        try {
            BookDTO selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                showStatus("Please select a book to update", true);
                return;
            }

            String newTitle = titleField.getText().trim();
            if (newTitle.isEmpty()) {
                showStatus("Please enter a book title", true);
                return;
            }

            String currentTitle = selectedBook.getTitle();
            selectedBook.setTitle(newTitle); // Update the DTO with new title
            getFacade().updateBook(currentTitle, selectedBook);

            clearFields();
            loadBooks();
            showStatus("Book updated successfully!", false);

        } catch (Exception e) {
            showStatus("Error updating book: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteBook() {
        try {
            BookDTO selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                showStatus("Please select a book to delete", true);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Book");
            alert.setContentText("Are you sure you want to delete '" + selectedBook.getTitle()
                    + "'?\nThis will also delete all associated poems and verses.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                getFacade().deleteBook(selectedBook.getTitle());
                clearFields();
                loadBooks();
                showStatus("Book deleted successfully!", false);
            }

        } catch (Exception e) {
            showStatus("Error deleting book: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOpenPoems() {
        try {
            BookDTO selectedBook = booksTable.getSelectionModel().getSelectedItem();
            if (selectedBook == null) {
                showStatus("Please select a book first", true);
                return;
            }

            // Create a new FXMLLoader instance
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/view/PoemView.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller AFTER loading
            PoemController controller = loader.getController();

            // Set the book in the controller
            controller.setBook(selectedBook);

            // Create and show the stage
            Stage stage = new Stage();
            stage.setTitle("Poems - " + selectedBook.getTitle());
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

        } catch (Exception e) {
            showStatus("Error opening poems: " + e.getMessage(), true);
            System.err.println("Full error details:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleImportBook() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Book File to Import");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));

            File selectedFile = fileChooser.showOpenDialog(importBookBtn.getScene().getWindow());

            if (selectedFile != null) {
                ImportResultDTO result = getFacade().importBook(selectedFile);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Import Result");
                alert.setHeaderText("Book Import Complete");
                alert.setContentText(result.getMessage());
                alert.showAndWait();

                loadBooks();
            }

        } catch (Exception e) {
            showStatus("Error importing book: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        clearFields();
        loadBooks();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
    }
}