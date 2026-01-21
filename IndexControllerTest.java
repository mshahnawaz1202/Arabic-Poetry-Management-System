package pl.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.BookDTO;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class IndexControllerTest extends BaseControllerTest {

    private IndexController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock book data
        BookDTO arabicBook = new BookDTO(1, ARABIC_BOOK_TITLE, ARABIC_COMPILER, Date.valueOf("2024-01-01"));
        when(mockFacade.getAllBooks()).thenReturn(Arrays.asList(arabicBook));

        controller = loadView("IndexView.fxml", "Index View Test", 1100, 700);
        injectMockFacade(controller);
    }

    @Test
    void testBooksListExists(FxRobot robot) {
        ListView<?> booksList = robot.lookup("#booksList").queryAs(ListView.class);
        assertNotNull(booksList, "Books list should exist");
    }

    @Test
    void testRootsListExists(FxRobot robot) {
        ListView<?> rootsList = robot.lookup("#rootsList").queryAs(ListView.class);
        assertNotNull(rootsList, "Roots list should exist");
    }

    @Test
    void testLemmasListExists(FxRobot robot) {
        ListView<?> lemmasList = robot.lookup("#lemmasList").queryAs(ListView.class);
        assertNotNull(lemmasList, "Lemmas list should exist");
    }

    @Test
    void testTokensListExists(FxRobot robot) {
        ListView<?> tokensList = robot.lookup("#tokensList").queryAs(ListView.class);
        assertNotNull(tokensList, "Tokens list should exist");
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
}
