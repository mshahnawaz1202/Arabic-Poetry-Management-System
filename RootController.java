package pl.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import bl.IBLFacade;
import dto.RootDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class RootController implements Initializable {

    private IBLFacade facade;

    public void setFacade(IBLFacade facade) {
        this.facade = facade;
        loadAllRoots();
    }

    @FXML private TableView<RootDTO> rootsTable;
    @FXML private TableColumn<RootDTO, Integer> idColumn;
    @FXML private TableColumn<RootDTO, Integer> verseIdColumn;
    @FXML private TableColumn<RootDTO, String> rootColumn;
    @FXML private TableColumn<RootDTO, Integer> positionColumn;

    @FXML private Label statusLabel;
    @FXML private TextField verseIdField;

    @FXML private Button loadAllRootsBtn;
    @FXML private Button deleteAllRootsBtn;
    @FXML private Button extractAllBtn;
    @FXML private Button extractAndSaveBtn;
    @FXML private Button loadByVerseBtn;

    private final ObservableList<RootDTO> rootData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("rootId"));
        verseIdColumn.setCellValueFactory(new PropertyValueFactory<>("verseId"));
        rootColumn.setCellValueFactory(new PropertyValueFactory<>("root"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("positionInVerse"));

        rootsTable.setItems(rootData);
    }

    private void loadAllRoots() {
        if (facade == null) return;
        List<RootDTO> roots = facade.getAllRootsFromDatabase();
        rootData.setAll(roots);
        statusLabel.setText("Loaded " + roots.size() + " roots.");
    }

    @FXML
    private void handleLoadAllRoots() { loadAllRoots(); }

    @FXML
    private void handleExtractAll() {
        List<RootDTO> roots = facade.extractRootsAllVerses();
        rootData.setAll(roots);
        statusLabel.setText("Extracted roots from all verses (preview).");
    }

    @FXML
    private void handleExtractAndSave() {
        int count = facade.extractAndSaveAllRoots();
        loadAllRoots();
        statusLabel.setText("Extracted & saved " + count + " roots.");
    }

    @FXML
    private void handleLoadByVerseId() {
        try {
            int vid = Integer.parseInt(verseIdField.getText());
            List<RootDTO> roots = facade.getRootsByVerseId(vid);
            rootData.setAll(roots);
            statusLabel.setText("Loaded " + roots.size() + " roots for verse " + vid);
        } catch (Exception ex) {
            statusLabel.setText("Invalid verse ID.");
        }
    }

    @FXML
    private void handleDeleteAll() {
        facade.deleteAllRoots();
        rootData.clear();
        statusLabel.setText("All roots deleted.");
    }
}
