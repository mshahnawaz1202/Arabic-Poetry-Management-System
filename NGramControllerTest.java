package pl.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class NGramControllerTest extends BaseControllerTest {

    private NGramController controller;

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock facade to return empty results for simplicity
        when(mockFacade.findSimilarVerses(anyString(), anyInt(), anyDouble()))
                .thenReturn(Collections.emptyList());

        // Load your updated FXML
        controller = loadView("NGramView.fxml", "N-Gram Similarity Test", 1400, 800);
        injectMockFacade(controller);
    }

    @Test
    void testUIComponentsExist(FxRobot robot) {
        // Spinners
        Spinner<?> nSpinner = robot.lookup("#nValueSpinner").queryAs(Spinner.class);
        Spinner<?> minSimSpinner = robot.lookup("#minSimilaritySpinner").queryAs(Spinner.class);
        assertNotNull(nSpinner, "N-value spinner should exist");
        assertNotNull(minSimSpinner, "Min similarity spinner should exist");

        // Buttons
        Button findBtn = robot.lookup("#findSimilarButton").queryAs(Button.class);
        Button clearBtn = robot.lookup("#clearButton").queryAs(Button.class);
        assertNotNull(findBtn, "Find Similar button should exist");
        assertNotNull(clearBtn, "Clear button should exist");

        // TextArea
        TextArea inputArea = robot.lookup("#inputVerseArea").queryAs(TextArea.class);
        assertNotNull(inputArea, "Input text area should exist");

        // Results Table
        TableView<?> resultsTable = robot.lookup("#resultsTable").queryAs(TableView.class);
        assertNotNull(resultsTable, "Results table should exist");

        // Status Label
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testArabicTextInput(FxRobot robot) {
        TextArea inputArea = robot.lookup("#inputVerseArea").queryAs(TextArea.class);

        String arabicLine = "لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ";
        robot.clickOn(inputArea).write(arabicLine);

        assertEquals(arabicLine, inputArea.getText(), "Arabic text should be entered correctly");
    }

    @Test
    void testButtonsEnabled(FxRobot robot) {
        assertThat(robot.lookup("#findSimilarButton").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#clearButton").queryAs(Button.class)).isEnabled();
    }
}
