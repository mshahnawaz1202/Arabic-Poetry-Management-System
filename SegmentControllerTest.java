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

import dto.SegmentDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class SegmentControllerTest extends BaseControllerTest {

    private SegmentController controller;
    private static final String ARABIC_SEGMENT = "قَوْمِي";

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock segment data with Arabic content
        SegmentDTO arabicSegment = new SegmentDTO();
        arabicSegment.setSegmentId(1);
        arabicSegment.setWord(ARABIC_SEGMENT);

        when(mockFacade.getAllSegmentsFromDatabase()).thenReturn(Arrays.asList(arabicSegment));

        controller = loadView("SegmentView.fxml", "Segment View Test", 1100, 700);
        injectMockFacade(controller);
    }

    @Test
    void testUIComponentsExist(FxRobot robot) {
        // Table
        TableView<?> segmentTable = robot.lookup("#segmentsTable").queryAs(TableView.class);
        assertNotNull(segmentTable, "Segment table should exist");

        // Buttons
        Button refreshBtn = robot.lookup("#refreshBtn").queryAs(Button.class);
        Button deleteBtn = robot.lookup("#deleteBtn").queryAs(Button.class);
        assertNotNull(refreshBtn, "Refresh button should exist");
        assertNotNull(deleteBtn, "Delete button should exist");

        // Labels
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testButtonsEnabled(FxRobot robot) {
        Button refreshBtn = robot.lookup("#refreshBtn").queryAs(Button.class);
        Button deleteBtn = robot.lookup("#deleteBtn").queryAs(Button.class);

        assertThat(!refreshBtn.isDisabled()).isTrue();
        assertThat(!deleteBtn.isDisabled()).isTrue();
    }

    @Test
    void testArabicSegmentLoaded(FxRobot robot) {
        TableView<?> segmentTable = robot.lookup("#segmentsTable").queryAs(TableView.class);
        assertNotNull(segmentTable, "Segment table should exist");

        // Verify that the mocked Arabic segment is loaded
        assertThat(segmentTable.getItems()).isNotEmpty();
        assertThat(segmentTable.getItems().get(0).toString()).contains(ARABIC_SEGMENT);
    }
}
