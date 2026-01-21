package pl.controller;

import bl.BLFacade;
import bl.IBLFacade;
import dal.IDALFacade;
import dto.VerseDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class SearchController {

    private IBLFacade facade;
    private IDALFacade dalFacade; // FIXED: Should be interface type

    @FXML
    private ComboBox<String> searchTypeCombo;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearButton;
    @FXML
    private Label searchInfoLabel;
    @FXML
    private Label resultCountLabel;
    @FXML
    private TableView<VerseDTO> resultsTableView;
    @FXML
    private TableColumn<VerseDTO, Integer> verseIdCol;
    @FXML
    private TableColumn<VerseDTO, Integer> poemIdCol;
    @FXML
    private TableColumn<VerseDTO, Integer> verseNoCol;
    @FXML
    private TableColumn<VerseDTO, String> textCol;
    @FXML
    private TableColumn<VerseDTO, String> textDiacritizedCol;

    @FXML
    private VBox verseDetailBox;
    @FXML
    private Label detailTextLabel;
    @FXML
    private Label detailDiacritizedLabel;
    @FXML
    private Label detailTranslationLabel;
    @FXML
    private TextArea detailNotesTextArea;

    @FXML
    private Label statusLabel;

    private final ObservableList<VerseDTO> searchResults = FXCollections.observableArrayList();

    public void setFacade(IBLFacade facade) {
        this.facade = facade;
    }

    @FXML
    public void initialize() {
        searchTypeCombo.getItems().addAll("Exact Search", "Regex Search");
        searchTypeCombo.getSelectionModel().selectFirst();
        updateSearchInfo();

        // Bind table columns
        verseIdCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getVerseId()).asObject());
        poemIdCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPoemId()).asObject());
        verseNoCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getVerseNo()).asObject());
        textCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getText()));
        textDiacritizedCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTextDiacritized()));

        resultsTableView.setItems(searchResults);

        verseDetailBox.setVisible(false);

        // Details on selecting a row
        resultsTableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> showVerseDetails(newVal));

        searchTypeCombo.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> updateSearchInfo());
    }

    @FXML
    private void handleSearch() {
        String searchText = searchTextField.getText().trim();
        String type = searchTypeCombo.getValue();

        if (searchText.isEmpty()) {
            showAlert("Please enter text to search.");
            return;
        }

        if (facade == null) {
            showAlert("Facade is not connected. Check initialization.");
            statusLabel.setText("Error: Facade unavailable");
            return;
        }

        try {
            statusLabel.setText("Searching...");
            searchResults.clear();
            verseDetailBox.setVisible(false);

            if (type.equals("Exact Search")) {
                searchResults.addAll(facade.searchExactString(searchText));
            } else {
                searchResults.addAll(facade.searchRegexPattern(searchText));
            }

            resultCountLabel.setText("Found " + searchResults.size() + " verses");

            if (searchResults.isEmpty()) {
                statusLabel.setText("No results found.");
            } else {
                statusLabel.setText("Search completed.");
                resultsTableView.getSelectionModel().selectFirst();
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Search failed: " + e.getMessage());
            statusLabel.setText("Search error");
        }
    }

    @FXML
    private void handleClear() {
        searchTextField.clear();
        searchResults.clear();
        resultCountLabel.setText("Found 0 verses");
        verseDetailBox.setVisible(false);
        statusLabel.setText("Cleared");
    }

    private void updateSearchInfo() {
        String type = searchTypeCombo.getValue();
        if (type.equals("Exact Search")) {
            searchInfoLabel.setText("Search for verses containing specific text (case-insensitive)");
            searchTextField.setPromptText("Enter exact text...");
        } else {
            searchInfoLabel.setText("Use regular expressions for flexible matching");
            searchTextField.setPromptText("Enter regex pattern...");
        }
    }

    private void showVerseDetails(VerseDTO verse) {
        if (verse == null) {
            verseDetailBox.setVisible(false);
            return;
        }

        verseDetailBox.setVisible(true);
        detailTextLabel.setText(nonNull(verse.getText()));
        detailDiacritizedLabel.setText(nonNull(verse.getTextDiacritized()));
        detailTranslationLabel.setText(nonNull(verse.getTranslation(), "No translation available"));
        detailNotesTextArea.setText(nonNull(verse.getNotes(), "No notes available"));
    }

    private String nonNull(String s) {
        return (s == null || s.trim().isEmpty()) ? "N/A" : s;
    }

    private String nonNull(String s, String alt) {
        return (s == null || s.trim().isEmpty()) ? alt : s;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    // OPTIONAL for Dashboard auto-fill
    public void setSearchText(String text) {
        searchTextField.setText(text);
    }

    public void setSearchType(String type) {
        searchTypeCombo.setValue(type);
    }
}
