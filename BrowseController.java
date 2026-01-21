package pl.controller;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import bl.IBLFacade;
import dto.BrowseResultDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class BrowseController implements Initializable {

    private IBLFacade facade;

    @FXML
    private ComboBox<String> categoryCombo;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> itemList;
    @FXML
    private Label statusLabel;

    @FXML
    private TableView<BrowseResultDTO> resultsTable;
    @FXML
    private TableColumn<BrowseResultDTO, Integer> verseIdCol;
    @FXML
    private TableColumn<BrowseResultDTO, Integer> verseNoCol;
    @FXML
    private TableColumn<BrowseResultDTO, String> verseTextCol;
    @FXML
    private TableColumn<BrowseResultDTO, String> poemTitleCol;
    @FXML
    private TableColumn<BrowseResultDTO, String> poetNameCol;
    @FXML
    private TableColumn<BrowseResultDTO, String> bookTitleCol;

    private ObservableList<String> allItems = FXCollections.observableArrayList();
    private FilteredList<String> filteredItems;

    public void setFacade(IBLFacade facade) {
        this.facade = facade;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize Table Columns
        verseIdCol.setCellValueFactory(new PropertyValueFactory<>("verseId"));
        verseNoCol.setCellValueFactory(new PropertyValueFactory<>("verseNo"));
        verseTextCol.setCellValueFactory(new PropertyValueFactory<>("text"));
        poemTitleCol.setCellValueFactory(new PropertyValueFactory<>("poemTitle"));
        poetNameCol.setCellValueFactory(new PropertyValueFactory<>("poetName"));
        bookTitleCol.setCellValueFactory(new PropertyValueFactory<>("bookName"));

        // Initialize Category ComboBox
        categoryCombo.setItems(FXCollections.observableArrayList("Token", "Root", "Lemma", "Segment"));
        categoryCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadItemsForCategory(newVal);
            }
        });

        // Initialize Filtering for Item List
        filteredItems = new FilteredList<>(allItems, p -> true);
        itemList.setItems(filteredItems);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return item.toLowerCase().contains(newValue.toLowerCase());
            });
        });

        // Handle Item Selection
        itemList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadVersesForItem(newVal);
            }
        });
    }

    private void loadItemsForCategory(String category) {
        if (facade == null)
            return;

        List<String> items = Collections.emptyList();
        try {
            switch (category) {
                case "Token":
                    items = facade.getDistinctToken();
                    break;
                case "Root":
                    items = facade.getDistinctRoot();
                    break;
                case "Lemma":
                    items = facade.getDistinctLemma();
                    break;
                case "Segment":
                    items = facade.getDistinctSegment();
                    break;
            }
            allItems.setAll(items);
            statusLabel.setText("Loaded " + items.size() + " " + category.toLowerCase() + "s.");
            resultsTable.getItems().clear(); // Clear previous results
        } catch (Exception e) {
            statusLabel.setText("Error loading items: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadVersesForItem(String item) {
        if (facade == null)
            return;

        String category = categoryCombo.getSelectionModel().getSelectedItem();
        if (category == null)
            return;

        List<BrowseResultDTO> results = Collections.emptyList();
        try {
            switch (category) {
                case "Token":
                    results = facade.browseByToken(item);
                    break;
                case "Root":
                    results = facade.browseByRoot(item);
                    break;
                case "Lemma":
                    results = facade.browseByLemma(item);
                    break;
                case "Segment":
                    results = facade.browseBySegment(item);
                    break;
            }
            resultsTable.setItems(FXCollections.observableArrayList(results));
            statusLabel.setText("Found " + results.size() + " verses for '" + item + "'.");
        } catch (Exception e) {
            statusLabel.setText("Error loading verses: " + e.getMessage());
            e.printStackTrace();
        }
    }
}