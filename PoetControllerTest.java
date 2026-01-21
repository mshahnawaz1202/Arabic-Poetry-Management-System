package pl.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.PoetDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * TestFX tests for PoetController.
 * Tests CRUD operations with Arabic poet names.
 * 
 * Arabic poetry: لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ ... لَيْسُوا مِنَ
 * الشَّرِ فِي شَيءٍ وَإنْ هانَا
 */
@ExtendWith(ApplicationExtension.class)
public class PoetControllerTest extends BaseControllerTest {

    private PoetController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock data with Arabic poet - المتنبي
        PoetDTO arabicPoet = new PoetDTO();
        arabicPoet.setPoetId(1);
        arabicPoet.setPoetName(ARABIC_POET_NAME);
        arabicPoet.setBiography(ARABIC_POET_BIO);

        when(mockFacade.getAllPoets()).thenReturn(Arrays.asList(arabicPoet));

        controller = loadView("PoetView.fxml", "Poet Management Test", 1000, 700);
        injectMockFacade(controller);
    }

    @Test
    void testPoetNameFieldExists(FxRobot robot) {
        TextField poetNameField = robot.lookup("#poetNameField").queryAs(TextField.class);
        assertNotNull(poetNameField, "Poet name field should exist");
    }

    @Test
    void testBiographyAreaExists(FxRobot robot) {
        TextArea biographyArea = robot.lookup("#biographyArea").queryAs(TextArea.class);
        assertNotNull(biographyArea, "Biography text area should exist");
    }

    @Test
    void testPoetsTableExists(FxRobot robot) {
        TableView<?> poetsTable = robot.lookup("#poetsTable").queryAs(TableView.class);
        assertNotNull(poetsTable, "Poets table should exist");
    }

    @Test
    void testAddPoetButtonExists(FxRobot robot) {
        Button addPoetBtn = robot.lookup("#addPoetBtn").queryAs(Button.class);
        assertNotNull(addPoetBtn, "Add Poet button should exist");
    }

    @Test
    void testUpdatePoetButtonExists(FxRobot robot) {
        Button updatePoetBtn = robot.lookup("#updatePoetBtn").queryAs(Button.class);
        assertNotNull(updatePoetBtn, "Update Poet button should exist");
    }

    @Test
    void testDeletePoetButtonExists(FxRobot robot) {
        Button deletePoetBtn = robot.lookup("#deletePoetBtn").queryAs(Button.class);
        assertNotNull(deletePoetBtn, "Delete Poet button should exist");
    }

    @Test
    void testRefreshButtonExists(FxRobot robot) {
        Button refreshBtn = robot.lookup("#refreshBtn").queryAs(Button.class);
        assertNotNull(refreshBtn, "Refresh button should exist");
    }

    @Test
    void testArabicPoetNameInput(FxRobot robot) {
        TextField poetNameField = robot.lookup("#poetNameField").queryAs(TextField.class);

        // Clear and type Arabic poet name - المتنبي
        robot.clickOn("#poetNameField");
        robot.write(ARABIC_POET_NAME);

        assertEquals(ARABIC_POET_NAME, poetNameField.getText(),
                "Arabic poet name should be entered correctly: المتنبي");
    }

    @Test
    void testArabicBiographyInput(FxRobot robot) {
        TextArea biographyArea = robot.lookup("#biographyArea").queryAs(TextArea.class);

        // Clear and type Arabic biography
        robot.clickOn("#biographyArea");
        robot.write(ARABIC_POET_BIO);

        assertEquals(ARABIC_POET_BIO, biographyArea.getText(),
                "Arabic biography should be entered correctly: أبو الطيب المتنبي، شاعر عربي من العصر العباسي");
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        assertThat(robot.lookup("#addPoetBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#updatePoetBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#deletePoetBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#refreshBtn").queryAs(Button.class)).isEnabled();
    }

    @Test
    void testArabicFullVerseInBiography(FxRobot robot) {
        TextArea biographyArea = robot.lookup("#biographyArea").queryAs(TextArea.class);

        // Clear and type full Arabic verse
        robot.clickOn("#biographyArea");
        robot.write(ARABIC_FULL_VERSE);

        assertEquals(ARABIC_FULL_VERSE, biographyArea.getText(),
                "Full Arabic verse should be entered correctly: لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ ... لَيْسُوا مِنَ الشَّرِ فِي شَيءٍ وَإنْ هانَا");
    }
}
