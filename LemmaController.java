package pl.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import bl.IBLFacade;
import dto.LemmaDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

public class LemmaController implements Initializable {

    private IBLFacade facade;

    public void setFacade(IBLFacade facade) {
        this.facade = facade;
        loadAllLemmas();
    }

    // FXML fields
    @FXML private TableView<LemmaDTO> lemmasTable;
    @FXML private TableColumn<LemmaDTO, Integer> idColumn;
    @FXML private TableColumn<LemmaDTO, Integer> verseIdColumn;
    @FXML private TableColumn<LemmaDTO, String> lemmaColumn;
    @FXML private TableColumn<LemmaDTO, Integer> positionColumn;

    @FXML private Label statusLabel;
    @FXML private TextField verseIdField;

    @FXML private Button btnLemmatizeAll;
    @FXML private Button btnLemmatizeSave;
    @FXML private Button btnLoadAll;
    @FXML private Button btnDeleteAll;
    @FXML private Button btnLoadByVerseId;

    private final ObservableList<LemmaDTO> lemmaData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getLemmaId()));
        verseIdColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getVerseId()));
        lemmaColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getLemma()));
        positionColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getPositionInVerse()));

        lemmasTable.setItems(lemmaData);
    }

    private void loadAllLemmas() {
        if (facade == null) return;
        List<LemmaDTO> lemmas = facade.getAllLemmasFromDatabase();
        lemmaData.setAll(lemmas);
        statusLabel.setText("Loaded " + lemmas.size() + " lemmas.");
    }

    @FXML
    private void handleLoadAllLemmas() { loadAllLemmas(); }

    @FXML
    private void handleLemmatizeAll() {
        if (facade == null) return;
        List<LemmaDTO> lemmas = facade.lemmatizeAllVerses();
        lemmaData.setAll(lemmas);
        statusLabel.setText("Lemmatized all verses (preview).");
    }

    @FXML
    private void handleLemmatizeAndSave() {
        if (facade == null) return;
        int count = facade.lemmatizeAndSaveAllVerses();
        loadAllLemmas();
        statusLabel.setText("Lemmatized & saved " + count + " lemmas.");
    }

    @FXML
    private void handleLoadByVerseId() {
        if (facade == null) return;
        try {
            int vid = Integer.parseInt(verseIdField.getText());
            List<LemmaDTO> lemmas = facade.getLemmasByVerseId(vid);
            lemmaData.setAll(lemmas);
            statusLabel.setText("Loaded " + lemmas.size() + " lemmas for verse " + vid);
        } catch (Exception ex) {
            statusLabel.setText("Invalid verse ID.");
        }
    }

    @FXML
    private void handleDeleteAll() {
        if (facade == null) return;
        facade.deleteAllLemmas();
        lemmaData.clear();
        statusLabel.setText("All lemmas deleted.");
    }
}
