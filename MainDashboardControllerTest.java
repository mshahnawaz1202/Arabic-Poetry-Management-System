package pl.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.control.Button;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class MainDashboardControllerTest extends BaseControllerTest {

    private MainDashboardController controller;

    @Start
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Load FXML and inject mock facade
        controller = loadView("MainDashboardView.fxml", "Main Dashboard Test", 1000, 700);
        injectMockFacade(controller);
    }

    @Test
    void testManageBooksButtonExists(FxRobot robot) {
        Button manageBooksBtn = robot.lookup("#manageBooksBtn").queryAs(Button.class);
        assertNotNull(manageBooksBtn, "Manage Books button should exist");
        assertEquals("Manage Books", manageBooksBtn.getText());
    }

    @Test
    void testTokenizeButtonExists(FxRobot robot) {
        Button tokenizeBtn = robot.lookup("#tokenizeBtn").queryAs(Button.class);
        assertNotNull(tokenizeBtn, "Tokenize button should exist");
        assertEquals("Tokenize", tokenizeBtn.getText());
    }

    @Test
    void testLemmatizeButtonExists(FxRobot robot) {
        Button lemmatizeBtn = robot.lookup("#lemmatizeBtn").queryAs(Button.class);
        assertNotNull(lemmatizeBtn, "Lemmatize button should exist");
        assertEquals("Lemmatize", lemmatizeBtn.getText());
    }

    @Test
    void testSegmentizeButtonExists(FxRobot robot) {
        Button segmentizeBtn = robot.lookup("#segmentizeBtn").queryAs(Button.class);
        assertNotNull(segmentizeBtn, "Segmentize button should exist");
        assertEquals("Segmentize", segmentizeBtn.getText());
    }

    @Test
    void testRootExtractButtonExists(FxRobot robot) {
        Button rootExtractBtn = robot.lookup("#rootExtractBtn").queryAs(Button.class);
        assertNotNull(rootExtractBtn, "Root Extract button should exist");
        assertEquals("Root Extract", rootExtractBtn.getText());
    }

    @Test
    void testNGramSimilarityButtonExists(FxRobot robot) {
        Button ngramBtn = robot.lookup("#ngramBtn").queryAs(Button.class);
        assertNotNull(ngramBtn, "N-Gram Similarity button should exist");
        assertEquals("N-Gram Similarity", ngramBtn.getText());
    }

    @Test
    void testViewBookIndexButtonExists(FxRobot robot) {
        Button viewIndexBtn = robot.lookup("#indexBtn").queryAs(Button.class);
        assertNotNull(viewIndexBtn, "View Book Index button should exist");
        assertEquals("View Book Index", viewIndexBtn.getText());
    }

    @Test
    void testFrequencyAnalysisButtonExists(FxRobot robot) {
        Button freqBtn = robot.lookup("#frequencyBtn").queryAs(Button.class);
        assertNotNull(freqBtn, "Frequency Analysis button should exist");
        assertEquals("Frequency Analysis", freqBtn.getText());
    }

    @Test
    void testSearchVersesButtonExists(FxRobot robot) {
        Button searchBtn = robot.lookup("#searchBtn").queryAs(Button.class);
        assertNotNull(searchBtn, "Search Verses button should exist");
        assertEquals("Search Verses", searchBtn.getText());
    }

    @Test
    void testBrowsePoetryButtonExists(FxRobot robot) {
        Button browseBtn = robot.lookup("#browseBtn").queryAs(Button.class);
        assertNotNull(browseBtn, "Browse Poetry button should exist");
        assertEquals("Browse Poetry", browseBtn.getText());
    }

    @Test
    void testExitButtonExists(FxRobot robot) {
        Button exitBtn = robot.lookup("#exitBtn").queryAs(Button.class);
        assertNotNull(exitBtn, "Exit button should exist");
        assertEquals("Exit Application", exitBtn.getText());
    }

    @Test
    void testAllNavigationButtonsEnabled(FxRobot robot) {
        // All buttons should exist and be enabled
        String[] buttonIds = {
            "#manageBooksBtn", "#tokenizeBtn", "#lemmatizeBtn", "#segmentizeBtn",
            "#rootExtractBtn", "#ngramBtn", "#indexBtn", "#frequencyBtn",
            "#searchBtn", "#browseBtn", "#exitBtn"
        };

        for (String id : buttonIds) {
            Button btn = robot.lookup(id).queryAs(Button.class);
            assertNotNull(btn, "Button " + id + " should exist");
            assertFalse(btn.isDisabled(), "Button " + id + " should be enabled");
        }
    }
}
