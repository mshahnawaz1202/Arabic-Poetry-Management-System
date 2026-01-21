package pl.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import bl.IBLFacade;
import dto.TokenDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class TokenController implements Initializable {

    private IBLFacade facade;  // injected from MainDashboardController

    public void setFacade(IBLFacade facade) {
        this.facade = facade;
        loadAllTokens();  // safe because facade is initialized
    }

    @FXML private TableView<TokenDTO> tokensTable;

    @FXML private TableColumn<TokenDTO, Integer> idColumn;
    @FXML private TableColumn<TokenDTO, Integer> verseIdColumn;
    @FXML private TableColumn<TokenDTO, String> tokenColumn;
    @FXML private TableColumn<TokenDTO, Integer> positionColumn;

    @FXML private Label statusLabel;
    @FXML private TextField verseIdField;

    private final ObservableList<TokenDTO> tokenData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Map TableColumns to TokenDTO getters
        idColumn.setCellValueFactory(new PropertyValueFactory<>("tokenId"));
        verseIdColumn.setCellValueFactory(new PropertyValueFactory<>("verseId"));
        tokenColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("positionInVerse"));

        tokensTable.setItems(tokenData);
    }

    private void loadAllTokens() {
        if (facade == null) return;  // safety check
        List<TokenDTO> tokens = facade.getAllTokensFromDatabase();
        tokenData.setAll(tokens);
        statusLabel.setText("Loaded " + tokens.size() + " tokens.");
    }

    @FXML
    private void handleLoadAllTokens() {
        loadAllTokens();
    }

    @FXML
    private void handleTokenizeAll() {
        List<TokenDTO> tokens = facade.tokenizeAllVerses();
        tokenData.setAll(tokens);
        statusLabel.setText("Tokenized all verses (preview mode).");
    }

    @FXML
    private void handleTokenizeAndSave() {
        int count = facade.tokenizeAndSaveAllVerses();
        loadAllTokens();
        statusLabel.setText("Tokenized & saved " + count + " tokens.");
    }

    @FXML
    private void handleLoadByVerseId() {
        try {
            int vid = Integer.parseInt(verseIdField.getText());
            List<TokenDTO> tokens = facade.getTokensByVerseId(vid);
            tokenData.setAll(tokens);
            statusLabel.setText("Loaded " + tokens.size() + " tokens for verse " + vid);
        } catch (Exception ex) {
            statusLabel.setText("Invalid verse ID.");
        }
    }

    @FXML
    private void handleDeleteAll() {
        facade.deleteAllTokens();
        tokenData.clear();
        statusLabel.setText("All tokens deleted.");
    }
}
