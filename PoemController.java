package pl.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pl.Main;
import dto.BookDTO;
import dto.PoemDTO;
import dto.PoetDTO;

import java.util.List;
import java.util.Optional;

public class PoemController {

    @FXML
    private Label bookTitleLabel;
    @FXML
    private TextField poemTitleField;
    @FXML
    private ComboBox<PoetDTO> poetComboBox;

    @FXML
    private Button addPoemBtn;
    @FXML
    private Button updatePoemBtn;
    @FXML
    private Button deletePoemBtn;
    @FXML
    private Button viewVersesBtn;
    @FXML
    private Button managePoetsBtn;
    @FXML
    private Button refreshBtn;

    @FXML
    private TableView<PoemDTO> poemsTable;
    @FXML
    private TableColumn<PoemDTO, Integer> poemIdColumn;
    @FXML
    private TableColumn<PoemDTO, String> poemTitleColumn;
    @FXML
    private TableColumn<PoemDTO, Integer> poetIdColumn;
    @FXML
    private TableColumn<PoemDTO, Integer> bookIdColumn;

    @FXML
    private Label statusLabel;

    private ObservableList<PoemDTO> poemsList = FXCollections.observableArrayList();
    private ObservableList<PoetDTO> poetsList = FXCollections.observableArrayList();
    private BookDTO currentBook;

    private bl.IBLFacade facade;

    public void setFacade(bl.IBLFacade facade) {
        this.facade = facade;
        // Load poets now that facade is available
        loadPoets();
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
        poemIdColumn.setCellValueFactory(new PropertyValueFactory<>("poemId"));
        poemTitleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        poetIdColumn.setCellValueFactory(new PropertyValueFactory<>("poetId"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        // Add selection listener
        poemsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
    }

    public void setBook(BookDTO book) {
        this.currentBook = book;
        bookTitleLabel.setText("Poems in: " + book.getTitle());
        loadPoems();
    }

    private void loadPoets() {
        try {
            List<PoetDTO> poets = getFacade().getAllPoets();
            poetsList.clear();
            poetsList.addAll(poets);
            poetComboBox.setItems(poetsList);

            // Select first poet by default
            if (!poetsList.isEmpty()) {
                poetComboBox.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            showStatus("Error loading poets: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void loadPoems() {
        try {
            if (currentBook == null) {
                showStatus("No book selected", true);
                return;
            }

            // Get all poems and filter by book ID
            List<PoemDTO> allPoems = getFacade().getAllPoems();
            poemsList.clear();

            int bookId = getFacade().getBookID(currentBook);
            for (PoemDTO poem : allPoems) {
                if (poem.getBookId() == bookId) {
                    poemsList.add(poem);
                }
            }

            poemsTable.setItems(poemsList);
            showStatus("Poems loaded successfully", false);
        } catch (Exception e) {
            showStatus("Error loading poems: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void populateFields(PoemDTO poem) {
        poemTitleField.setText(poem.getTitle());

        // Select the poet in combo box
        for (PoetDTO poet : poetsList) {
            if (poet.getPoetId() == poem.getPoetId()) {
                poetComboBox.getSelectionModel().select(poet);
                break;
            }
        }
    }

    private void clearFields() {
        poemTitleField.clear();
        if (!poetsList.isEmpty()) {
            poetComboBox.getSelectionModel().selectFirst();
        }
        poemsTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAddPoem() {
        try {
            String title = poemTitleField.getText().trim();
            PoetDTO selectedPoet = poetComboBox.getSelectionModel().getSelectedItem();

            if (title.isEmpty()) {
                showStatus("Please enter a poem title", true);
                return;
            }

            if (selectedPoet == null) {
                showStatus("Please select a poet", true);
                return;
            }

            int bookId = getFacade().getBookID(currentBook);

            PoemDTO poem = new PoemDTO();
            poem.setTitle(title);
            poem.setPoetId(selectedPoet.getPoetId());
            poem.setBookId(bookId);

            getFacade().createPoem(poem);

            clearFields();
            loadPoems();
            showStatus("Poem added successfully!", false);

        } catch (Exception e) {
            showStatus("Error adding poem: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdatePoem() {
        try {
            PoemDTO selectedPoem = poemsTable.getSelectionModel().getSelectedItem();
            if (selectedPoem == null) {
                showStatus("Please select a poem to update", true);
                return;
            }

            String newTitle = poemTitleField.getText().trim();
            PoetDTO selectedPoet = poetComboBox.getSelectionModel().getSelectedItem();

            if (newTitle.isEmpty()) {
                showStatus("Please enter a poem title", true);
                return;
            }

            selectedPoem.setTitle(newTitle);
            if (selectedPoet != null) {
                selectedPoem.setPoetId(selectedPoet.getPoetId());
            }

            getFacade().updatePoem(selectedPoem);

            clearFields();
            loadPoems();
            showStatus("Poem updated successfully!", false);

        } catch (Exception e) {
            showStatus("Error updating poem: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePoem() {
        try {
            PoemDTO selectedPoem = poemsTable.getSelectionModel().getSelectedItem();
            if (selectedPoem == null) {
                showStatus("Please select a poem to delete", true);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Poem");
            alert.setContentText("Are you sure you want to delete '" + selectedPoem.getTitle()
                    + "'?\nThis will also delete all associated verses.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                getFacade().deletePoem(selectedPoem.getPoemId());
                clearFields();
                loadPoems();
                showStatus("Poem deleted successfully!", false);
            }

        } catch (Exception e) {
            showStatus("Error deleting poem: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleViewVerses() {
        try {
            PoemDTO selectedPoem = poemsTable.getSelectionModel().getSelectedItem();
            if (selectedPoem == null) {
                showStatus("Please select a poem first", true);
                return;
            }

            // Create a new FXMLLoader instance
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/view/VerseView.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Get the controller AFTER loading
            VerseController controller = loader.getController();

            // Set the poem in the controller
            controller.setPoem(selectedPoem);

            // Create and show the stage
            Stage stage = new Stage();
            stage.setTitle("Verses - " + selectedPoem.getTitle());
            stage.setScene(new Scene(root, 1000, 700));
            stage.show();

        } catch (Exception e) {
            showStatus("Error opening verses: " + e.getMessage(), true);
            System.err.println("Full error details:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManagePoets() {
        try {
            // Create a new FXMLLoader instance
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/pl/view/PoetView.fxml"));

            // Load the FXML
            Parent root = loader.load();

            // Create and show the stage
            Stage stage = new Stage();
            stage.setTitle("Manage Poets");
            stage.setScene(new Scene(root, 800, 600));
            stage.show();

            // Refresh poets after closing
            stage.setOnHidden(e -> loadPoets());

        } catch (Exception e) {
            showStatus("Error opening poets manager: " + e.getMessage(), true);
            System.err.println("Full error details:");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        clearFields();
        loadPoets();
        loadPoems();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
    }
}