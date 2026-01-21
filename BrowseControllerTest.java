package pl.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class BrowseControllerTest extends BaseControllerTest {

    private BrowseController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock distinct values with Arabic content
        when(mockFacade.getDistinctLemma()).thenReturn(Arrays.asList(ARABIC_LEMMA));
        when(mockFacade.getDistinctRoot()).thenReturn(Arrays.asList(ARABIC_ROOT));
        when(mockFacade.getDistinctSegment()).thenReturn(Arrays.asList(ARABIC_SEGMENT));
        when(mockFacade.getDistinctToken()).thenReturn(Arrays.asList(ARABIC_TOKEN));

        controller = loadView("BrowseView.fxml", "Browse Test", 1100, 700);
        injectMockFacade(controller);
    }

    @Test
    void testCategoryComboExists(FxRobot robot) {
        ComboBox<?> categoryCombo = robot.lookup("#categoryCombo").queryAs(ComboBox.class);
        assertNotNull(categoryCombo, "Category combo box should exist");
    }

    @Test
    void testSearchFieldExists(FxRobot robot) {
        assertNotNull(robot.lookup("#searchField").queryAs(javafx.scene.control.TextField.class),
                "Search field should exist");
    }

    @Test
    void testItemListExists(FxRobot robot) {
        ListView<?> itemList = robot.lookup("#itemList").queryAs(ListView.class);
        assertNotNull(itemList, "Item list should exist");
    }

    @Test
    void testResultsTableExists(FxRobot robot) {
        TableView<?> resultsTable = robot.lookup("#resultsTable").queryAs(TableView.class);
        assertNotNull(resultsTable, "Results table should exist");
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testCategoryComboHasOptions(FxRobot robot) {
        ComboBox<String> categoryCombo = robot.lookup("#categoryCombo").queryAs(ComboBox.class);
        assertFalse(categoryCombo.getItems().isEmpty(), "Category combo should have options");
    }
}
