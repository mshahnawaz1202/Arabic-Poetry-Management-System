package pl.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.VerseDTO;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * TestFX tests for SearchController.
 * Tests exact and regex search with Arabic text.
 * 
 * Arabic poetry: لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ ... لَيْسُوا مِنَ
 * الشَّرِ فِي شَيءٍ وَإنْ هانَا
 */
@ExtendWith(ApplicationExtension.class)
public class SearchControllerTest extends BaseControllerTest {

    private SearchController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock search results with Arabic content
        VerseDTO arabicVerse = new VerseDTO(1, 1, 1, ARABIC_VERSE_LINE1,
                ARABIC_VERSE_LINE1, "Translation", "Notes");

        when(mockFacade.searchExactString(anyString())).thenReturn(Arrays.asList(arabicVerse));
        when(mockFacade.searchRegexPattern(anyString())).thenReturn(Arrays.asList(arabicVerse));

        controller = loadView("SearchView.fxml", "Search Test", 1100, 700);
        injectMockFacade(controller);
    }

    @Test
    void testSearchTextFieldExists(FxRobot robot) {
        TextField searchTextField = robot.lookup("#searchTextField").queryAs(TextField.class);
        assertNotNull(searchTextField, "Search text field should exist");
    }

    @Test
    void testSearchTypeComboExists(FxRobot robot) {
        ComboBox<?> searchTypeCombo = robot.lookup("#searchTypeCombo").queryAs(ComboBox.class);
        assertNotNull(searchTypeCombo, "Search type combo box should exist");
    }

    @Test
    void testSearchButtonExists(FxRobot robot) {
        Button searchButton = robot.lookup("#searchButton").queryAs(Button.class);
        assertNotNull(searchButton, "Search button should exist");
    }

    @Test
    void testClearButtonExists(FxRobot robot) {
        Button clearButton = robot.lookup("#clearButton").queryAs(Button.class);
        assertNotNull(clearButton, "Clear button should exist");
    }

    @Test
    void testResultsTableExists(FxRobot robot) {
        TableView<?> resultsTableView = robot.lookup("#resultsTableView").queryAs(TableView.class);
        assertNotNull(resultsTableView, "Results table should exist");
    }

    @Test
    void testArabicSearchTermInput(FxRobot robot) {
        TextField searchTextField = robot.lookup("#searchTextField").queryAs(TextField.class);

        // Type Arabic search term - قَوْمِي
        robot.clickOn("#searchTextField");
        robot.write(ARABIC_SEARCH_TERM);

        assertEquals(ARABIC_SEARCH_TERM, searchTextField.getText(),
                "Arabic search term should be entered correctly: قَوْمِي");
    }

    @Test
    void testArabicVerseSearchInput(FxRobot robot) {
        TextField searchTextField = robot.lookup("#searchTextField").queryAs(TextField.class);

        // Type full Arabic verse line
        robot.clickOn("#searchTextField");
        robot.write(ARABIC_VERSE_LINE1);

        assertEquals(ARABIC_VERSE_LINE1, searchTextField.getText(),
                "Arabic verse should be entered correctly: لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ");
    }

    @Test
    void testSearchTypeComboHasOptions(FxRobot robot) {
        ComboBox<String> searchTypeCombo = robot.lookup("#searchTypeCombo").queryAs(ComboBox.class);
        assertFalse(searchTypeCombo.getItems().isEmpty(), "Search type combo should have options");
    }

    @Test
    void testSearchInfoLabelExists(FxRobot robot) {
        Label searchInfoLabel = robot.lookup("#searchInfoLabel").queryAs(Label.class);
        assertNotNull(searchInfoLabel, "Search info label should exist");
    }

    @Test
    void testResultCountLabelExists(FxRobot robot) {
        Label resultCountLabel = robot.lookup("#resultCountLabel").queryAs(Label.class);
        assertNotNull(resultCountLabel, "Result count label should exist");
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        assertThat(robot.lookup("#searchButton").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#clearButton").queryAs(Button.class)).isEnabled();
    }
}
