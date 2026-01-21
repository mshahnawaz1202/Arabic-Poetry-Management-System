package pl.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.PoemDTO;
import dto.VerseDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class VerseControllerTest extends BaseControllerTest {

    private VerseController controller;

    private static final String ARABIC_VERSE_LINE1 = "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ؟";
    private static final String ARABIC_VERSE_LINE2 = "أَلَمْ نَشْرَحْ لَكَ صَدْرَكَ بِالضَّرُوبِ؟";
    private static final String ARABIC_FULL_VERSE = ARABIC_VERSE_LINE1 + " " + ARABIC_VERSE_LINE2;
    private static final String ARABIC_POEM_TITLE = "الشرح";

    @Start
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock Arabic verse
        VerseDTO arabicVerse = new VerseDTO(1, 1, 1, ARABIC_VERSE_LINE1,
                ARABIC_VERSE_LINE2, "Translation", "Notes");
        when(mockFacade.getVersesByPoemId(anyInt())).thenReturn(Arrays.asList(arabicVerse));

        controller = loadView("VerseView.fxml", "Verse Management Test", 1200, 750);
        injectMockFacade(controller);

        PoemDTO poem = new PoemDTO();
        poem.setPoemId(1);
        poem.setTitle(ARABIC_POEM_TITLE);
        controller.setPoem(poem);
    }

    @Test
    void testUIComponentsExist(FxRobot robot) {
        assertNotNull(robot.lookup("#verseNoField").queryAs(TextField.class));
        assertNotNull(robot.lookup("#textArea").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#diacritizedArea").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#translationArea").queryAs(TextArea.class));
        assertNotNull(robot.lookup("#notesArea").queryAs(TextArea.class));

        assertNotNull(robot.lookup("#versesTable").queryAs(TableView.class));

        assertNotNull(robot.lookup("#addVerseBtn").queryAs(Button.class));
        assertNotNull(robot.lookup("#updateVerseBtn").queryAs(Button.class));
        assertNotNull(robot.lookup("#deleteVerseBtn").queryAs(Button.class));
        assertNotNull(robot.lookup("#refreshBtn").queryAs(Button.class));

        assertNotNull(robot.lookup("#poemTitleLabel").queryAs(Label.class));
        assertNotNull(robot.lookup("#statusLabel").queryAs(Label.class));
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        robot.interact(() -> {
            assertThat(robot.lookup("#addVerseBtn").queryAs(Button.class)).isEnabled();
            assertThat(robot.lookup("#updateVerseBtn").queryAs(Button.class)).isEnabled();
            assertThat(robot.lookup("#deleteVerseBtn").queryAs(Button.class)).isEnabled();
            assertThat(robot.lookup("#refreshBtn").queryAs(Button.class)).isEnabled();
        });
    }

    @Test
    void testArabicVerseTextInput(FxRobot robot) {
        TextArea textArea = robot.lookup("#textArea").queryAs(TextArea.class);
        robot.interact(() -> textArea.setText(ARABIC_VERSE_LINE1));
        assertEquals(ARABIC_VERSE_LINE1, textArea.getText());
    }

    @Test
    void testArabicDiacritizedTextInput(FxRobot robot) {
        TextArea diacritizedArea = robot.lookup("#diacritizedArea").queryAs(TextArea.class);
        robot.interact(() -> diacritizedArea.setText(ARABIC_VERSE_LINE2));
        assertEquals(ARABIC_VERSE_LINE2, diacritizedArea.getText());
    }

    @Test
    void testFullArabicVerseInput(FxRobot robot) {
        TextArea textArea = robot.lookup("#textArea").queryAs(TextArea.class);
        robot.interact(() -> textArea.setText(ARABIC_FULL_VERSE));
        assertEquals(ARABIC_FULL_VERSE, textArea.getText());
    }
}
