package pl.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.Main;
import dto.PoemDTO;
import dto.VerseDTO;

import java.util.List;
import java.util.Optional;

public class VerseController {

    @FXML
    private Label poemTitleLabel;
    @FXML
    private TextField verseNoField;
    @FXML
    private TextArea textArea;
    @FXML
    private TextArea diacritizedArea;
    @FXML
    private TextArea translationArea;
    @FXML
    private TextArea notesArea;

    @FXML
    private Button addVerseBtn;
    @FXML
    private Button updateVerseBtn;
    @FXML
    private Button deleteVerseBtn;
    @FXML
    private Button refreshBtn;

    @FXML
    private TableView<VerseDTO> versesTable;
    @FXML
    private TableColumn<VerseDTO, Integer> verseIdColumn;
    @FXML
    private TableColumn<VerseDTO, Integer> verseNoColumn;
    @FXML
    private TableColumn<VerseDTO, String> textColumn;

    @FXML
    private Label statusLabel;

    private ObservableList<VerseDTO> versesList = FXCollections.observableArrayList();
    private PoemDTO currentPoem;

    private bl.IBLFacade facade;

    public void setFacade(bl.IBLFacade facade) {
        this.facade = facade;
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
        verseIdColumn.setCellValueFactory(new PropertyValueFactory<>("verseId"));
        verseNoColumn.setCellValueFactory(new PropertyValueFactory<>("verseNo"));
        textColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        // Add selection listener
        versesTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
    }

    public void setPoem(PoemDTO poem) {
        this.currentPoem = poem;
        poemTitleLabel.setText("Verses in: " + poem.getTitle());
        loadVerses();
    }

    private void loadVerses() {
        try {
            if (currentPoem == null) {
                showStatus("No poem selected", true);
                return;
            }

            List<VerseDTO> verses = getFacade().getVersesByPoemId(currentPoem.getPoemId());
            versesList.clear();
            versesList.addAll(verses);
            versesTable.setItems(versesList);
            showStatus("Verses loaded successfully", false);
        } catch (Exception e) {
            showStatus("Error loading verses: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void populateFields(VerseDTO verse) {
        verseNoField.setText(String.valueOf(verse.getVerseNo()));
        textArea.setText(verse.getText());
        diacritizedArea.setText(verse.getTextDiacritized() != null ? verse.getTextDiacritized() : "");
        translationArea.setText(verse.getTranslation() != null ? verse.getTranslation() : "");
        notesArea.setText(verse.getNotes() != null ? verse.getNotes() : "");
    }

    private void clearFields() {
        verseNoField.clear();
        textArea.clear();
        diacritizedArea.clear();
        translationArea.clear();
        notesArea.clear();
        versesTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAddVerse() {
        try {
            String verseNoStr = verseNoField.getText().trim();
            String text = textArea.getText().trim();

            if (verseNoStr.isEmpty()) {
                showStatus("Please enter verse number", true);
                return;
            }

            if (text.isEmpty()) {
                showStatus("Please enter verse text", true);
                return;
            }

            int verseNo;
            try {
                verseNo = Integer.parseInt(verseNoStr);
            } catch (NumberFormatException e) {
                showStatus("Verse number must be a valid integer", true);
                return;
            }

            VerseDTO verse = new VerseDTO();
            verse.setPoemId(currentPoem.getPoemId());
            verse.setVerseNo(verseNo);
            verse.setText(text);
            verse.setTextDiacritized(diacritizedArea.getText().trim());
            verse.setTranslation(translationArea.getText().trim());
            verse.setNotes(notesArea.getText().trim());

            getFacade().createVerse(verse);

            clearFields();
            loadVerses();
            showStatus("Verse added successfully!", false);

        } catch (Exception e) {
            showStatus("Error adding verse: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdateVerse() {
        try {
            VerseDTO selectedVerse = versesTable.getSelectionModel().getSelectedItem();
            if (selectedVerse == null) {
                showStatus("Please select a verse to update", true);
                return;
            }

            String verseNoStr = verseNoField.getText().trim();
            String text = textArea.getText().trim();

            if (verseNoStr.isEmpty() || text.isEmpty()) {
                showStatus("Please fill in required fields", true);
                return;
            }

            int verseNo;
            try {
                verseNo = Integer.parseInt(verseNoStr);
            } catch (NumberFormatException e) {
                showStatus("Verse number must be a valid integer", true);
                return;
            }

            selectedVerse.setVerseNo(verseNo);
            selectedVerse.setText(text);
            selectedVerse.setTextDiacritized(diacritizedArea.getText().trim());
            selectedVerse.setTranslation(translationArea.getText().trim());
            selectedVerse.setNotes(notesArea.getText().trim());

            getFacade().updateVerse(selectedVerse);

            clearFields();
            loadVerses();
            showStatus("Verse updated successfully!", false);

        } catch (Exception e) {
            showStatus("Error updating verse: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteVerse() {
        try {
            VerseDTO selectedVerse = versesTable.getSelectionModel().getSelectedItem();
            if (selectedVerse == null) {
                showStatus("Please select a verse to delete", true);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Verse");
            alert.setContentText("Are you sure you want to delete Verse #" + selectedVerse.getVerseNo() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                getFacade().deleteVerse(selectedVerse.getVerseId());
                clearFields();
                loadVerses();
                showStatus("Verse deleted successfully!", false);
            }

        } catch (Exception e) {
            showStatus("Error deleting verse: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        clearFields();
        loadVerses();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
    }
}