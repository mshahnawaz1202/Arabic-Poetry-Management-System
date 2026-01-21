package pl.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.FrequencyDTO;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

/**
 * TestFX tests for FrequencyController.
 * Tests frequency analysis with charts and data export.
 * 
 * Arabic poetry: لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ ... لَيْسُوا مِنَ
 * الشَّرِ فِي شَيءٍ وَإنْ هانَا
 */
@ExtendWith(ApplicationExtension.class)
public class FrequencyControllerTest extends BaseControllerTest {

    private FrequencyController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock frequency data with Arabic words
        FrequencyDTO arabicFreq = new FrequencyDTO(ARABIC_TOKEN, 10);
        when(mockFacade.getTokenFrequencies()).thenReturn(Arrays.asList(arabicFreq));
        when(mockFacade.getLemmaFrequencies()).thenReturn(Arrays.asList(arabicFreq));
        when(mockFacade.getRootFrequencies()).thenReturn(Arrays.asList(arabicFreq));
        when(mockFacade.getSegmentFrequencies()).thenReturn(Arrays.asList(arabicFreq));

        controller = loadView("FrequencyView.fxml", "Frequency Analysis Test", 1200, 800);
        injectMockFacade(controller);
    }

    @Test
    void testAnalysisTypeComboExists(FxRobot robot) {
        ComboBox<?> analysisTypeCombo = robot.lookup("#analysisTypeCombo").queryAs(ComboBox.class);
        assertNotNull(analysisTypeCombo, "Analysis type combo box should exist");
    }

    @Test
    void testTopNSpinnerExists(FxRobot robot) {
        Spinner<?> topNSpinner = robot.lookup("#topNSpinner").queryAs(Spinner.class);
        assertNotNull(topNSpinner, "Top N spinner should exist");
    }

    @Test
    void testRefreshButtonExists(FxRobot robot) {
        Button refreshBtn = robot.lookup("#refreshBtn").queryAs(Button.class);
        assertNotNull(refreshBtn, "Refresh button should exist");
    }

    @Test
    void testExportButtonExists(FxRobot robot) {
        Button exportBtn = robot.lookup("#exportBtn").queryAs(Button.class);
        assertNotNull(exportBtn, "Export button should exist");
    }

    @Test
    void testFrequencyTableExists(FxRobot robot) {
        TableView<?> frequencyTable = robot.lookup("#frequencyTable").queryAs(TableView.class);
        assertNotNull(frequencyTable, "Frequency table should exist");
    }

    @Test
    void testAnalysisTypeComboHasOptions(FxRobot robot) {
        ComboBox<String> analysisTypeCombo = robot.lookup("#analysisTypeCombo").queryAs(ComboBox.class);
        assertFalse(analysisTypeCombo.getItems().isEmpty(), "Analysis type combo should have options");
    }

    @Test
    void testChartToggleButtonExists(FxRobot robot) {
        Button chartToggleBtn = robot.lookup("#chartToggleBtn").queryAs(Button.class);
        assertNotNull(chartToggleBtn, "Chart toggle button should exist");
    }

    @Test
    void testStatsLabelExists(FxRobot robot) {
        Label statsLabel = robot.lookup("#statsLabel").queryAs(Label.class);
        assertNotNull(statsLabel, "Stats label should exist");
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        assertThat(robot.lookup("#refreshBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#exportBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#chartToggleBtn").queryAs(Button.class)).isEnabled();
    }
}
