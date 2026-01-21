package pl.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import bl.IBLFacade;
import dto.SegmentDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SegmentController implements Initializable {

    private IBLFacade facade;

    public void setFacade(IBLFacade facade) {
        this.facade = facade;
        loadAllSegments();
    }

    @FXML
    private TableView<SegmentDTO> segmentsTable;
    @FXML
    private TableColumn<SegmentDTO, Integer> idColumn;
    @FXML
    private TableColumn<SegmentDTO, Integer> verseIdColumn;
    @FXML
    private TableColumn<SegmentDTO, String> segmentColumn;
    @FXML
    private TableColumn<SegmentDTO, Integer> positionColumn;

    @FXML
    private Label statusLabel;
    @FXML
    private TextField verseIdField;

    private final ObservableList<SegmentDTO> segmentData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("segmentId"));
        verseIdColumn.setCellValueFactory(new PropertyValueFactory<>("verseId"));
        segmentColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("positionInVerse"));

        segmentsTable.setItems(segmentData);
    }

    private void loadAllSegments() {
        if (facade == null)
            return;
        List<SegmentDTO> segments = facade.getAllSegmentsFromDatabase();
        segmentData.setAll(segments);
        statusLabel.setText("Loaded " + segments.size() + " segments.");
    }

    @FXML
    private void handleLoadAllSegments() {
        loadAllSegments();
    }

    @FXML
    private javafx.scene.control.Button segmentAllBtn;
    @FXML
    private javafx.scene.control.Button segmentAndSaveBtn;
    @FXML
    private javafx.scene.control.Button refreshBtn;
    @FXML
    private javafx.scene.control.Button loadByVerseIdBtn;
    @FXML
    private javafx.scene.control.Button deleteBtn;

    @FXML
    private void handleSegmentAll() {
        List<SegmentDTO> segments = facade.segmentAllVerses();
        segmentData.setAll(segments);
        statusLabel.setText("Segmented all verses (preview).");
    }

    @FXML
    private void handleSegmentAndSave() {
        int count = facade.segmentAndSaveAllVerses();
        loadAllSegments();
        statusLabel.setText("Segmented & saved " + count + " segments.");
    }

    @FXML
    private void handleLoadByVerseId() {
        try {
            int vid = Integer.parseInt(verseIdField.getText());
            List<SegmentDTO> segments = facade.getSegmentsByVerseId(vid);
            segmentData.setAll(segments);
            statusLabel.setText("Loaded " + segments.size() + " segments for verse " + vid);
        } catch (Exception ex) {
            statusLabel.setText("Invalid verse ID.");
        }
    }

    @FXML
    private void handleDeleteAll() {
        facade.deleteAllSegments();
        segmentData.clear();
        statusLabel.setText("All segments deleted.");
    }
}
