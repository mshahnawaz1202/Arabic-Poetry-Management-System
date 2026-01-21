package pl.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.Main;
import dto.PoetDTO;

import java.util.List;
import java.util.Optional;

public class PoetController {

    @FXML
    private TextField poetNameField;
    @FXML
    private TextArea biographyArea;

    @FXML
    private Button addPoetBtn;
    @FXML
    private Button updatePoetBtn;
    @FXML
    private Button deletePoetBtn;
    @FXML
    private Button refreshBtn;

    @FXML
    private TableView<PoetDTO> poetsTable;
    @FXML
    private TableColumn<PoetDTO, Integer> poetIdColumn;
    @FXML
    private TableColumn<PoetDTO, String> poetNameColumn;
    @FXML
    private TableColumn<PoetDTO, String> biographyColumn;

    @FXML
    private Label statusLabel;

    private ObservableList<PoetDTO> poetsList = FXCollections.observableArrayList();

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
        poetIdColumn.setCellValueFactory(new PropertyValueFactory<>("poetId"));
        poetNameColumn.setCellValueFactory(new PropertyValueFactory<>("poetName"));
        biographyColumn.setCellValueFactory(new PropertyValueFactory<>("biography"));

        // Add selection listener
        poetsTable.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> {
                    if (newSelection != null) {
                        populateFields(newSelection);
                    }
                });
    }

    private void loadPoets() {
        try {
            List<PoetDTO> poets = getFacade().getAllPoets();
            poetsList.clear();
            poetsList.addAll(poets);
            poetsTable.setItems(poetsList);
            showStatus("Poets loaded successfully", false);
        } catch (Exception e) {
            showStatus("Error loading poets: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    private void populateFields(PoetDTO poet) {
        poetNameField.setText(poet.getPoetName());
        biographyArea.setText(poet.getBiography());
    }

    private void clearFields() {
        poetNameField.clear();
        biographyArea.clear();
        poetsTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAddPoet() {
        try {
            String name = poetNameField.getText().trim();
            String biography = biographyArea.getText().trim();

            if (name.isEmpty()) {
                showStatus("Please enter a poet name", true);
                return;
            }

            PoetDTO poet = new PoetDTO();
            poet.setPoetName(name);
            poet.setBiography(biography.isEmpty() ? "No biography available" : biography);

            getFacade().createPoet(poet);

            clearFields();
            loadPoets();
            showStatus("Poet added successfully!", false);

        } catch (Exception e) {
            showStatus("Error adding poet: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUpdatePoet() {
        try {
            PoetDTO selectedPoet = poetsTable.getSelectionModel().getSelectedItem();
            if (selectedPoet == null) {
                showStatus("Please select a poet to update", true);
                return;
            }

            String name = poetNameField.getText().trim();
            String biography = biographyArea.getText().trim();

            if (name.isEmpty()) {
                showStatus("Please enter a poet name", true);
                return;
            }

            selectedPoet.setPoetName(name);
            selectedPoet.setBiography(biography.isEmpty() ? "No biography available" : biography);

            getFacade().updatePoet(selectedPoet);

            clearFields();
            loadPoets();
            showStatus("Poet updated successfully!", false);

        } catch (Exception e) {
            showStatus("Error updating poet: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePoet() {
        try {
            PoetDTO selectedPoet = poetsTable.getSelectionModel().getSelectedItem();
            if (selectedPoet == null) {
                showStatus("Please select a poet to delete", true);
                return;
            }

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Delete Poet");
            alert.setContentText("Are you sure you want to delete '" + selectedPoet.getPoetName()
                    + "'?\nThis may affect associated poems.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                getFacade().deletePoet(selectedPoet.getPoetId());
                clearFields();
                loadPoets();
                showStatus("Poet deleted successfully!", false);
            }

        } catch (Exception e) {
            showStatus("Error deleting poet: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        clearFields();
        loadPoets();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
    }
}