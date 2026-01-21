package pl.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.sql.Date;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.BookDTO;
import dto.PoemDTO;
import dto.PoetDTO;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * TestFX tests for PoemController.
 * Tests CRUD operations with Arabic poem titles.
 * 
 * Arabic poetry: لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ ... لَيْسُوا مِنَ
 * الشَّرِ فِي شَيءٍ وَإنْ هانَا
 */
@ExtendWith(ApplicationExtension.class)
public class PoemControllerTest extends BaseControllerTest {

    private PoemController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock data with Arabic content
        PoetDTO arabicPoet = new PoetDTO();
        arabicPoet.setPoetId(1);
        arabicPoet.setPoetName(ARABIC_POET_NAME);
        arabicPoet.setBiography(ARABIC_POET_BIO);

        PoemDTO arabicPoem = new PoemDTO();
        arabicPoem.setPoemId(1);
        arabicPoem.setTitle(ARABIC_POEM_TITLE);
        arabicPoem.setBookId(1);
        arabicPoem.setPoetId(1);

        when(mockFacade.getAllPoets()).thenReturn(Arrays.asList(arabicPoet));
        when(mockFacade.getAllPoems()).thenReturn(Arrays.asList(arabicPoem));

        controller = loadView("PoemView.fxml", "Poem Management Test", 1200, 750);
        injectMockFacade(controller);

        // Set a book context
        BookDTO book = new BookDTO(1, ARABIC_BOOK_TITLE, ARABIC_COMPILER, Date.valueOf("2024-01-01"));
        controller.setBook(book);
    }

    @Test
    void testPoemTitleFieldExists(FxRobot robot) {
        TextField poemTitleField = robot.lookup("#poemTitleField").queryAs(TextField.class);
        assertNotNull(poemTitleField, "Poem title field should exist");
    }

    @Test
    void testPoetComboBoxExists(FxRobot robot) {
        ComboBox<?> poetComboBox = robot.lookup("#poetComboBox").queryAs(ComboBox.class);
        assertNotNull(poetComboBox, "Poet combo box should exist");
    }

    @Test
    void testPoemsTableExists(FxRobot robot) {
        TableView<?> poemsTable = robot.lookup("#poemsTable").queryAs(TableView.class);
        assertNotNull(poemsTable, "Poems table should exist");
    }

    @Test
    void testAddPoemButtonExists(FxRobot robot) {
        Button addPoemBtn = robot.lookup("#addPoemBtn").queryAs(Button.class);
        assertNotNull(addPoemBtn, "Add Poem button should exist");
    }

    @Test
    void testUpdatePoemButtonExists(FxRobot robot) {
        Button updatePoemBtn = robot.lookup("#updatePoemBtn").queryAs(Button.class);
        assertNotNull(updatePoemBtn, "Update Poem button should exist");
    }

    @Test
    void testDeletePoemButtonExists(FxRobot robot) {
        Button deletePoemBtn = robot.lookup("#deletePoemBtn").queryAs(Button.class);
        assertNotNull(deletePoemBtn, "Delete Poem button should exist");
    }

    @Test
    void testViewVersesButtonExists(FxRobot robot) {
        Button viewVersesBtn = robot.lookup("#viewVersesBtn").queryAs(Button.class);
        assertNotNull(viewVersesBtn, "View Verses button should exist");
    }

    @Test
    void testManagePoetsButtonExists(FxRobot robot) {
        Button managePoetsBtn = robot.lookup("#managePoetsBtn").queryAs(Button.class);
        assertNotNull(managePoetsBtn, "Manage Poets button should exist");
    }

    @Test
    void testRefreshButtonExists(FxRobot robot) {
        Button refreshBtn = robot.lookup("#refreshBtn").queryAs(Button.class);
        assertNotNull(refreshBtn, "Refresh button should exist");
    }

    @Test
    void testArabicPoemTitleInput(FxRobot robot) {
        TextField poemTitleField = robot.lookup("#poemTitleField").queryAs(TextField.class);

        // Clear and type Arabic poem title
        robot.clickOn("#poemTitleField");
        robot.write(ARABIC_POEM_TITLE);

        // Verify Arabic text - قصيدة الحماسة
        assertEquals(ARABIC_POEM_TITLE, poemTitleField.getText(),
                "Arabic poem title should be entered correctly: قصيدة الحماسة");
    }

    @Test
    void testBookTitleLabelExists(FxRobot robot) {
        Label bookTitleLabel = robot.lookup("#bookTitleLabel").queryAs(Label.class);
        assertNotNull(bookTitleLabel, "Book title label should exist");
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        assertThat(robot.lookup("#addPoemBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#updatePoemBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#deletePoemBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#viewVersesBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#managePoetsBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#refreshBtn").queryAs(Button.class)).isEnabled();
    }
}
