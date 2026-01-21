package pl.controller;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

import dal.LemmaFrequencyDAO;
import dal.RootFrequencyDAO;
import dal.SegmentFrequencyDAO;
import dal.TokenFrequencyDAO;
import dto.FrequencyDTO;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class FrequencyController {

    @FXML
    private ComboBox<String> analysisTypeCombo;
    @FXML
    private Spinner<Integer> topNSpinner;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button exportBtn;
    @FXML
    private Button chartToggleBtn;
    @FXML
    private Label chartTitle;
    @FXML
    private StackPane chartContainer;
    @FXML
    private BarChart<String, Number> frequencyChart;
    @FXML
    private TableView<FrequencyData> frequencyTable;
    @FXML
    private TableColumn<FrequencyData, String> wordCol;
    @FXML
    private TableColumn<FrequencyData, Integer> freqCol;
    @FXML
    private TableColumn<FrequencyData, String> percentageCol;
    @FXML
    private TableColumn<FrequencyData, String> cumulativeCol;
    @FXML
    private Label statsLabel;

    private ObservableList<FrequencyData> data = FXCollections.observableArrayList();
    private ChartType currentChartType = ChartType.BAR;
    private List<FrequencyDTO> currentFrequencies;
    private String currentAnalysisType;

    private enum ChartType {
        BAR, LINE, PIE
    }

    private bl.IBLFacade facade;

    public void setFacade(bl.IBLFacade facade) {
        this.facade = facade;
        // Load analysis types and initial data now that facade is available
        loadFrequencyTypes();
        refreshData();
    }

    private bl.IBLFacade getFacade() {
        if (facade == null) {
            return pl.Main.getBLFacade();
        }
        return facade;
    }

    @FXML
    public void initialize() {
        setupUI();
        // Defer loading data until facade is injected via setFacade
    }

    private void setupUI() {
        // Setup spinner
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 1000, 50,
                10);
        topNSpinner.setValueFactory(valueFactory);

        // Setup table
        wordCol.setCellValueFactory(cellData -> cellData.getValue().wordProperty());
        freqCol.setCellValueFactory(cellData -> cellData.getValue().frequencyProperty().asObject());
        percentageCol.setCellValueFactory(cellData -> cellData.getValue().percentageProperty());
        cumulativeCol.setCellValueFactory(cellData -> cellData.getValue().cumulativeProperty());

        // Setup chart
        frequencyChart.setAnimated(true);
        frequencyChart.setLegendVisible(false);

        // Button actions
        refreshBtn.setOnAction(e -> refreshData());
        exportBtn.setOnAction(e -> exportToCSV());
        chartToggleBtn.setOnAction(e -> toggleChartType());

        // Combo box listener
        analysisTypeCombo.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> refreshData());
    }

    private void loadFrequencyTypes() {
        analysisTypeCombo.getItems().addAll(
                "Tokens",
                "Lemmas",
                "Segments",
                "Roots");
        analysisTypeCombo.getSelectionModel().selectFirst();
    }

    private void refreshData() {
        if (getFacade() == null)
            return;

        String selectedType = analysisTypeCombo.getSelectionModel().getSelectedItem();
        int topN = topNSpinner.getValue();

        currentFrequencies = getFrequencies(selectedType, topN);
        currentAnalysisType = selectedType;

        if (currentFrequencies != null) {
            updateChart(currentFrequencies, selectedType);
            updateTable(currentFrequencies);
            updateStats(currentFrequencies);
        }
    }

    private List<FrequencyDTO> getFrequencies(String type, int limit) {
        if (getFacade() == null)
            return java.util.Collections.emptyList();

        List<FrequencyDTO> allFreqs;
        switch (type) {
            case "Tokens":
                allFreqs = getFacade().getTokenFrequencies();
                break;
            case "Lemmas":
                allFreqs = getFacade().getLemmaFrequencies();
                break;
            case "Segments":
                allFreqs = getFacade().getSegmentFrequencies();
                break;
            case "Roots":
                allFreqs = getFacade().getRootFrequencies();
                break;
            default:
                return java.util.Collections.emptyList();
        }

        return allFreqs.stream()
                .limit(limit)
                .toList();
    }

    private void updateChart(List<FrequencyDTO> frequencies, String type) {
        switch (currentChartType) {
            case BAR:
                updateBarChart(frequencies, type);
                break;
            case LINE:
                updateLineChart(frequencies, type);
                break;
            case PIE:
                updatePieChart(frequencies, type);
                break;
        }
    }

    private void updateBarChart(List<FrequencyDTO> frequencies, String type) {
        frequencyChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type);

        for (int i = 0; i < Math.min(frequencies.size(), 15); i++) {
            FrequencyDTO dto = frequencies.get(i);
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(dto.getWord(), dto.getFrequency());
            series.getData().add(dataPoint);
        }

        frequencyChart.getData().add(series);
        chartTitle.setText(type + " Frequency Distribution - Bar Chart (Top 15)");

        // Add tooltips
        for (XYChart.Data<String, Number> chartData : series.getData()) {
            Tooltip tooltip = new Tooltip(chartData.getXValue() + ": " + chartData.getYValue());
            if (chartData.getNode() != null) {
                Tooltip.install(chartData.getNode(), tooltip);
            }
        }
    }

    private void updateLineChart(List<FrequencyDTO> frequencies, String type) {
        // Remove current chart if it's not a LineChart
        if (chartContainer.getChildren().isEmpty() || !(chartContainer.getChildren().get(0) instanceof LineChart)) {
            chartContainer.getChildren().clear();

            CategoryAxis xAxis = new CategoryAxis();
            xAxis.setLabel("Word");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Frequency");

            LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
            lineChart.setTitle(type + " Frequency Distribution - Line Chart");
            lineChart.setAnimated(true);
            lineChart.setLegendVisible(false);
            lineChart.setStyle("-fx-background-color: transparent;");

            chartContainer.getChildren().add(lineChart);
        }

        @SuppressWarnings("unchecked")
        LineChart<String, Number> lineChart = (LineChart<String, Number>) chartContainer.getChildren().get(0);
        lineChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName(type);

        for (int i = 0; i < Math.min(frequencies.size(), 15); i++) {
            FrequencyDTO dto = frequencies.get(i);
            XYChart.Data<String, Number> dataPoint = new XYChart.Data<>(dto.getWord(), dto.getFrequency());
            series.getData().add(dataPoint);
        }

        lineChart.getData().add(series);
        chartTitle.setText(type + " Frequency Distribution - Line Chart (Top 15)");

        // Add tooltips
        for (XYChart.Data<String, Number> chartData : series.getData()) {
            Tooltip tooltip = new Tooltip(chartData.getXValue() + ": " + chartData.getYValue());
            if (chartData.getNode() != null) {
                Tooltip.install(chartData.getNode(), tooltip);
            }
        }
    }

    private void updatePieChart(List<FrequencyDTO> frequencies, String type) {
        // Remove current chart if it's not a PieChart
        if (chartContainer.getChildren().isEmpty() || !(chartContainer.getChildren().get(0) instanceof PieChart)) {
            chartContainer.getChildren().clear();

            PieChart pieChart = new PieChart();
            pieChart.setStyle("-fx-background-color: transparent;");

            chartContainer.getChildren().add(pieChart);
        }

        @SuppressWarnings("unchecked")
        PieChart pieChart = (PieChart) chartContainer.getChildren().get(0);
        pieChart.getData().clear();

        int total = frequencies.stream().mapToInt(FrequencyDTO::getFrequency).sum();

        for (int i = 0; i < Math.min(frequencies.size(), 10); i++) { // Show top 10 for Pie Chart
            FrequencyDTO dto = frequencies.get(i);

            PieChart.Data slice = new PieChart.Data(
                    dto.getWord() + " (" + dto.getFrequency() + ")",
                    dto.getFrequency());

            pieChart.getData().add(slice);
        }

        // Add tooltips after chart is rendered
        javafx.application.Platform.runLater(() -> {
            for (int i = 0; i < Math.min(pieChart.getData().size(), frequencies.size()); i++) {
                PieChart.Data slice = pieChart.getData().get(i);
                FrequencyDTO dto = frequencies.get(i);
                double percentage = total > 0 ? (double) dto.getFrequency() / total * 100 : 0;

                Node node = slice.getNode();
                if (node != null) {
                    Tooltip tooltip = new Tooltip(String.format("%s\nFrequency: %d (%.1f%%)",
                            dto.getWord(), dto.getFrequency(), percentage));
                    Tooltip.install(node, tooltip);
                }
            }
        });

        pieChart.setTitle(type + " Frequency Distribution - Pie Chart");
        chartTitle.setText(type + " Frequency Distribution - Pie Chart (Top 10)");
    }

    private void updateTable(List<FrequencyDTO> frequencies) {
        data.clear();

        int total = frequencies.stream().mapToInt(FrequencyDTO::getFrequency).sum();
        int cumulative = 0;

        for (FrequencyDTO dto : frequencies) {
            cumulative += dto.getFrequency();
            double percentage = total > 0 ? (double) dto.getFrequency() / total * 100 : 0;
            double cumulativePercentage = total > 0 ? (double) cumulative / total * 100 : 0;

            data.add(new FrequencyData(
                    dto.getWord(),
                    dto.getFrequency(),
                    percentage,
                    cumulativePercentage));
        }

        frequencyTable.setItems(data);
    }

    private void updateStats(List<FrequencyDTO> frequencies) {
        int total = frequencies.stream().mapToInt(FrequencyDTO::getFrequency).sum();
        int unique = frequencies.size();

        statsLabel.setText(String.format(
                "Total: %d occurrences | Unique: %d items",
                total, unique));
    }

    private void exportToCSV() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Frequency Data to CSV");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            fileChooser.setInitialFileName("frequency_analysis_" +
                    analysisTypeCombo.getSelectionModel().getSelectedItem().toLowerCase() + ".csv");

            File file = fileChooser.showSaveDialog(exportBtn.getScene().getWindow());

            if (file != null) {
                try (PrintWriter writer = new PrintWriter(file)) {
                    // Write CSV header
                    writer.println("Word,Frequency,Percentage,Cumulative Percentage");

                    // Write data rows
                    for (FrequencyData dataItem : data) {
                        writer.printf("%s,%d,%.2f%%,%.2f%%%n",
                                dataItem.wordProperty().get(),
                                dataItem.getFrequency(),
                                dataItem.percentage,
                                dataItem.cumulativePercentage);
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Export Complete");
                    alert.setHeaderText("Data exported successfully");
                    alert.setContentText("Frequency data has been exported to:\n" + file.getAbsolutePath());
                    alert.show();
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Export Error");
            alert.setHeaderText("Failed to export data");
            alert.setContentText("Error: " + e.getMessage());
            alert.show();
        }
    }

    private void toggleChartType() {
        // Cycle through chart types
        switch (currentChartType) {
            case BAR:
                currentChartType = ChartType.LINE;
                chartToggleBtn.setText("📊 Switch to Pie Chart");
                break;
            case LINE:
                currentChartType = ChartType.PIE;
                chartToggleBtn.setText("📈 Switch to Bar Chart");
                break;
            case PIE:
                currentChartType = ChartType.BAR;
                chartToggleBtn.setText("📉 Switch to Line Chart");
                break;
        }

        // Restore BarChart if switching back from other chart types
        if (currentChartType == ChartType.BAR &&
                (chartContainer.getChildren().isEmpty()
                        || !(chartContainer.getChildren().get(0) instanceof BarChart))) {
            chartContainer.getChildren().clear();
            chartContainer.getChildren().add(frequencyChart);
        }

        // Refresh the chart with current data
        if (currentFrequencies != null && currentAnalysisType != null) {
            updateChart(currentFrequencies, currentAnalysisType);
        }

        // Update button tooltip
        chartToggleBtn.setTooltip(new Tooltip("Current chart type: " +
                currentChartType.toString().toLowerCase() + " chart"));
    }

    // Inner class for table data
    public static class FrequencyData {
        private final SimpleStringProperty word;
        private final SimpleIntegerProperty frequency;
        private final Double percentage;
        private final Double cumulativePercentage;

        public FrequencyData(String word, int frequency, double percentage, double cumulativePercentage) {
            this.word = new SimpleStringProperty(word);
            this.frequency = new SimpleIntegerProperty(frequency);
            this.percentage = percentage;
            this.cumulativePercentage = cumulativePercentage;
        }

        public SimpleStringProperty wordProperty() {
            return word;
        }

        public SimpleIntegerProperty frequencyProperty() {
            return frequency;
        }

        public Integer getFrequency() {
            return frequency.get();
        }

        public SimpleStringProperty percentageProperty() {
            return new SimpleStringProperty(String.format("%.2f%%", percentage));
        }

        public SimpleStringProperty cumulativeProperty() {
            return new SimpleStringProperty(String.format("%.2f%%", cumulativePercentage));
        }
    }
}