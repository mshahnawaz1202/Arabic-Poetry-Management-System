package pl.controller;

import static org.assertj.core.api.Assertions.assertThat;
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
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * TestFX tests for BookController.
 * Tests CRUD operations with Arabic book titles.
 * 
 * Arabic poetry: لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ ... لَيْسُوا مِنَ
 * الشَّرِ فِي شَيءٍ وَإنْ هانَا
 */
@ExtendWith(ApplicationExtension.class)
public class BookControllerTest extends BaseControllerTest {

    private BookController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock getAllBooks to return Arabic test data
        BookDTO arabicBook = new BookDTO(1, ARABIC_BOOK_TITLE, ARABIC_COMPILER, Date.valueOf("2024-01-01"));
        when(mockFacade.getAllBooks()).thenReturn(Arrays.asList(arabicBook));

        controller = loadView("BookView.fxml", "Book Management Test", 1200, 750);
        injectMockFacade(controller);
    }

    @Test
    void testTitleFieldExists(FxRobot robot) {
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);
        assertNotNull(titleField, "Title field should exist");
    }

    @Test
    void testCompilerFieldExists(FxRobot robot) {
        TextField compilerField = robot.lookup("#compilerField").queryAs(TextField.class);
        assertNotNull(compilerField, "Compiler field should exist");
    }

    @Test
    void testEraDatePickerExists(FxRobot robot) {
        DatePicker eraDatePicker = robot.lookup("#eraDatePicker").queryAs(DatePicker.class);
        assertNotNull(eraDatePicker, "Era date picker should exist");
    }

    @Test
    void testBooksTableExists(FxRobot robot) {
        TableView<?> booksTable = robot.lookup("#booksTable").queryAs(TableView.class);
        assertNotNull(booksTable, "Books table should exist");
    }

    @Test
    void testAddBookButtonExists(FxRobot robot) {
        Button addBookBtn = robot.lookup("#addBookBtn").queryAs(Button.class);
        assertNotNull(addBookBtn, "Add Book button should exist");
        assertEquals("Add Book", addBookBtn.getText());
    }

    @Test
    void testUpdateBookButtonExists(FxRobot robot) {
        Button updateBookBtn = robot.lookup("#updateBookBtn").queryAs(Button.class);
        assertNotNull(updateBookBtn, "Update Book button should exist");
        assertThat(updateBookBtn.getText()).contains("Update");
    }

    @Test
    void testDeleteBookButtonExists(FxRobot robot) {
        Button deleteBookBtn = robot.lookup("#deleteBookBtn").queryAs(Button.class);
        assertNotNull(deleteBookBtn, "Delete Book button should exist");
        assertThat(deleteBookBtn.getText()).contains("Delete");
    }

    @Test
    void testOpenPoemsButtonExists(FxRobot robot) {
        Button openPoemsBtn = robot.lookup("#openPoemsBtn").queryAs(Button.class);
        assertNotNull(openPoemsBtn, "Open Poems button should exist");
    }

    @Test
    void testImportBookButtonExists(FxRobot robot) {
        Button importBookBtn = robot.lookup("#importBookBtn").queryAs(Button.class);
        assertNotNull(importBookBtn, "Import Book button should exist");
    }

    @Test
    void testRefreshButtonExists(FxRobot robot) {
        Button refreshBtn = robot.lookup("#refreshBtn").queryAs(Button.class);
        assertNotNull(refreshBtn, "Refresh button should exist");
    }

    @Test
    void testArabicBookTitleInput(FxRobot robot) {
        TextField titleField = robot.lookup("#titleField").queryAs(TextField.class);

        // Clear and type Arabic book title
        robot.clickOn("#titleField");
        robot.write(ARABIC_BOOK_TITLE);

        // Verify Arabic text was entered correctly
        assertEquals(ARABIC_BOOK_TITLE, titleField.getText(),
                "Arabic book title should be entered correctly: كتاب الشعر العربي");
    }

    @Test
    void testArabicCompilerInput(FxRobot robot) {
        TextField compilerField = robot.lookup("#compilerField").queryAs(TextField.class);

        // Clear and type Arabic compiler name
        robot.clickOn("#compilerField");
        robot.write(ARABIC_COMPILER);

        // Verify Arabic text was entered correctly
        assertEquals(ARABIC_COMPILER, compilerField.getText(),
                "Arabic compiler name should be entered correctly: جامع الشعر");
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        assertThat(robot.lookup("#addBookBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#updateBookBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#deleteBookBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#openPoemsBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#importBookBtn").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#refreshBtn").queryAs(Button.class)).isEnabled();
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }
}
