package pl.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.RootDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class RootControllerTest extends BaseControllerTest {

    private RootController controller;

    private static final String ARABIC_ROOT = "قَوْم";

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Prepare mock data
        RootDTO arabicRoot = new RootDTO();
        arabicRoot.setRootId(1);
        arabicRoot.setRoot(ARABIC_ROOT);

        when(mockFacade.getAllRootsFromDatabase()).thenReturn(Arrays.asList(arabicRoot));

        // Load FXML and inject mock facade
        controller = loadView("RootView.fxml", "Root View Test", 1100, 700);
        injectMockFacade(controller);
    }

    @Test
    void testUIComponentsExist(FxRobot robot) {
        // Table
        TableView<?> rootsTable = robot.lookup("#rootsTable").queryAs(TableView.class);
        assertNotNull(rootsTable, "Roots table should exist");

        // Buttons
        assertNotNull(robot.lookup("#loadAllRootsBtn").queryAs(Button.class), "Load All Roots button should exist");
        assertNotNull(robot.lookup("#deleteAllRootsBtn").queryAs(Button.class), "Delete All Roots button should exist");
        assertNotNull(robot.lookup("#extractAllBtn").queryAs(Button.class), "Extract All button should exist");
        assertNotNull(robot.lookup("#extractAndSaveBtn").queryAs(Button.class), "Extract & Save button should exist");
        assertNotNull(robot.lookup("#loadByVerseBtn").queryAs(Button.class), "Load by Verse button should exist");

        // Labels
        assertNotNull(robot.lookup("#statusLabel").queryAs(Label.class), "Status label should exist");

        // TextField
        assertNotNull(robot.lookup("#verseIdField").queryAs(TextField.class), "Verse ID field should exist");
    }

    @Test
    void testButtonsEnabled(FxRobot robot) {
        Button loadAllBtn = robot.lookup("#loadAllRootsBtn").queryAs(Button.class);
        Button deleteAllBtn = robot.lookup("#deleteAllRootsBtn").queryAs(Button.class);
        Button extractAllBtn = robot.lookup("#extractAllBtn").queryAs(Button.class);
        Button extractAndSaveBtn = robot.lookup("#extractAndSaveBtn").queryAs(Button.class);
        Button loadByVerseBtn = robot.lookup("#loadByVerseBtn").queryAs(Button.class);

        // Check they are not disabled
        assertNotNull(loadAllBtn);
        assertNotNull(deleteAllBtn);
        assertNotNull(extractAllBtn);
        assertNotNull(extractAndSaveBtn);
        assertNotNull(loadByVerseBtn);

        assertThat(!loadAllBtn.isDisabled()).isTrue();
        assertThat(!deleteAllBtn.isDisabled()).isTrue();
        assertThat(!extractAllBtn.isDisabled()).isTrue();
        assertThat(!extractAndSaveBtn.isDisabled()).isTrue();
        assertThat(!loadByVerseBtn.isDisabled()).isTrue();
    }


    @Test
    void testArabicRootLoaded(FxRobot robot) {
        TableView<RootDTO> rootsTable = robot.lookup("#rootsTable").queryAs(TableView.class);
        assertThat(rootsTable.getItems()).isNotEmpty();
        assertThat(rootsTable.getItems().get(0).getRoot()).isEqualTo(ARABIC_ROOT);
    }
}
