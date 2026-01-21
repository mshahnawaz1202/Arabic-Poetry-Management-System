package pl.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import pl.Main;
import bl.services.NGram.NGramResult;
import dto.VerseDTO;

import java.util.List;

public class NGramController {

    @FXML
    private Spinner<Integer> nValueSpinner;
    @FXML
    private Spinner<Double> minSimilaritySpinner;
    @FXML
    private Button findSimilarButton;
    @FXML
    private Button clearButton;
    @FXML
    private TextArea inputVerseArea;

    @FXML
    private TableView<NGramResultRow> resultsTable;
    @FXML
    private TableColumn<NGramResultRow, Integer> verseIdColumn;
    @FXML
    private TableColumn<NGramResultRow, String> similarityColumn;
    @FXML
    private TableColumn<NGramResultRow, String> verseTextColumn;
    @FXML
    private TableColumn<NGramResultRow, Integer> targetGramsColumn;
    @FXML
    private TableColumn<NGramResultRow, Integer> verseGramsColumn;
    @FXML
    private TableColumn<NGramResultRow, Integer> intersectionColumn;
    @FXML
    private TableColumn<NGramResultRow, Integer> commonWordsColumn;

    @FXML
    private Label statusLabel;

    private ObservableList<NGramResultRow> resultsList = FXCollections.observableArrayList();

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
        // Initialize spinners
        SpinnerValueFactory<Integer> nValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, 3);
        nValueSpinner.setValueFactory(nValueFactory);
        nValueSpinner.setEditable(true);

        SpinnerValueFactory<Double> similarityFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 100.0,
                20.0, 5.0);
        minSimilaritySpinner.setValueFactory(similarityFactory);
        minSimilaritySpinner.setEditable(true);

        // Setup table columns
        verseIdColumn.setCellValueFactory(new PropertyValueFactory<>("verseId"));
        similarityColumn.setCellValueFactory(new PropertyValueFactory<>("similarity"));
        verseTextColumn.setCellValueFactory(new PropertyValueFactory<>("verseText"));
        targetGramsColumn.setCellValueFactory(new PropertyValueFactory<>("targetGrams"));
        verseGramsColumn.setCellValueFactory(new PropertyValueFactory<>("verseGrams"));
        intersectionColumn.setCellValueFactory(new PropertyValueFactory<>("intersection"));
        commonWordsColumn.setCellValueFactory(new PropertyValueFactory<>("commonWords"));

        // Style table rows based on similarity
        resultsTable.setRowFactory(tv -> new TableRow<NGramResultRow>() {
            @Override
            protected void updateItem(NGramResultRow item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setStyle("");
                } else {
                    double similarity = item.getSimilarityValue();
                    if (similarity >= 80) {
                        setStyle("-fx-background-color: #d4edda;");
                    } else if (similarity >= 60) {
                        setStyle("-fx-background-color: #fff3cd;");
                    } else if (similarity >= 40) {
                        setStyle("-fx-background-color: #f8d7da;");
                    }
                }
            }
        });

        resultsTable.setItems(resultsList);

        showStatus("Ready. Enter verse text to find similar verses.", false);
    }

    @FXML
    private void handleFindSimilar() {
        try {
            String targetText = inputVerseArea.getText().trim();

            if (targetText.isEmpty()) {
                showStatus("Please enter verse text to analyze", true);
                showAlert("Input Required", "Please enter verse text in the text area.", Alert.AlertType.WARNING);
                return;
            }

            int n = nValueSpinner.getValue();
            double minSimilarity = minSimilaritySpinner.getValue();

            showStatus("Searching for similar verses...", false);

            // Call the business logic facade
            List<NGramResult> results = getFacade().findSimilarVerses(targetText, n, minSimilarity);

            // Clear previous results
            resultsList.clear();

            // Populate table with results
            for (NGramResult result : results) {
                VerseDTO verse = result.getVerse();

                NGramResultRow row = new NGramResultRow(
                        verse.getVerseId(),
                        String.format("%.2f%%", result.getSimilarity()),
                        result.getSimilarity(),
                        truncateText(verse.getText(), 100),
                        result.getTargetGramCount(),
                        result.getVerseGramCount(),
                        result.getIntersection(),
                        result.getCommonWords());

                resultsList.add(row);
            }

            if (results.isEmpty()) {
                showStatus("No similar verses found with the current parameters", false);
                showAlert("No Results",
                        "No similar verses found. Try:\n" +
                                "• Lowering the minimum similarity threshold\n" +
                                "• Adjusting the N-gram size\n" +
                                "• Using different text",
                        Alert.AlertType.INFORMATION);
            } else {
                showStatus(String.format("Found %d similar verse(s)", results.size()), false);
            }

        } catch (Exception e) {
            showStatus("Error during similarity search", true);
            showAlert("Error", "An error occurred during the search:\n" + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClear() {
        inputVerseArea.clear();
        resultsList.clear();
        nValueSpinner.getValueFactory().setValue(3);
        minSimilaritySpinner.getValueFactory().setValue(20.0);
        showStatus("Ready. Enter verse text to find similar verses.", false);
    }

    private String truncateText(String text, int maxLength) {
        if (text == null)
            return "";
        if (text.length() <= maxLength)
            return text;
        return text.substring(0, maxLength) + "...";
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        if (isError) {
            statusLabel.setStyle("-fx-text-fill: #b14545; -fx-font-weight: bold;");
        } else {
            statusLabel.setStyle("-fx-text-fill: #4b2e19;");
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Inner class for table row data
    public static class NGramResultRow {
        private final int verseId;
        private final String similarity;
        private final double similarityValue;
        private final String verseText;
        private final int targetGrams;
        private final int verseGrams;
        private final int intersection;
        private final int commonWords;

        public NGramResultRow(int verseId, String similarity, double similarityValue,
                String verseText, int targetGrams, int verseGrams,
                int intersection, int commonWords) {
            this.verseId = verseId;
            this.similarity = similarity;
            this.similarityValue = similarityValue;
            this.verseText = verseText;
            this.targetGrams = targetGrams;
            this.verseGrams = verseGrams;
            this.intersection = intersection;
            this.commonWords = commonWords;
        }

        public int getVerseId() {
            return verseId;
        }

        public String getSimilarity() {
            return similarity;
        }

        public double getSimilarityValue() {
            return similarityValue;
        }

        public String getVerseText() {
            return verseText;
        }

        public int getTargetGrams() {
            return targetGrams;
        }

        public int getVerseGrams() {
            return verseGrams;
        }

        public int getIntersection() {
            return intersection;
        }

        public int getCommonWords() {
            return commonWords;
        }
    }
}