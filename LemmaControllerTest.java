package pl.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.testfx.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import dto.LemmaDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class LemmaControllerTest extends BaseControllerTest {

    private LemmaController controller;
    private LemmaDTO arabicLemma;

    @Start
    public void start(Stage stage) throws Exception {
        super.start(stage);
        controller = loadView("LemmaView.fxml", "Lemma View Test", 1200, 750);
        injectMockFacade(controller);
    }

    @BeforeEach
    void setupMocks() {
        arabicLemma = new LemmaDTO();
        arabicLemma.setLemmaId(1);
        arabicLemma.setVerseId(10);
        arabicLemma.setLemma(ARABIC_LEMMA);
        arabicLemma.setPositionInVerse(2);

        when(mockFacade.getAllLemmasFromDatabase()).thenReturn(Arrays.asList(arabicLemma));
        when(mockFacade.lemmatizeAllVerses()).thenReturn(Arrays.asList(arabicLemma));
        when(mockFacade.lemmatizeAndSaveAllVerses()).thenReturn(1);
        when(mockFacade.getLemmasByVerseId(10)).thenReturn(Arrays.asList(arabicLemma));
        doNothing().when(mockFacade).deleteAllLemmas(); // void method
    }

    @Test
    void testLemmaTableExists(FxRobot robot) {
        TableView<?> lemmaTable = robot.lookup("#lemmasTable").queryAs(TableView.class);
        assertNotNull(lemmaTable, "Lemma table should exist");
    }

    @Test
    void testStatusLabelExists(FxRobot robot) {
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testVerseIdFieldExists(FxRobot robot) {
        TextField verseIdField = robot.lookup("#verseIdField").queryAs(TextField.class);
        assertNotNull(verseIdField, "Verse ID field should exist");
    }

    @Test
    void testAllButtonsExist(FxRobot robot) {
        Button btnLemmatizeAll = robot.lookup("#btnLemmatizeAll").queryAs(Button.class);
        Button btnLemmatizeSave = robot.lookup("#btnLemmatizeSave").queryAs(Button.class);
        Button btnLoadAll = robot.lookup("#btnLoadAll").queryAs(Button.class);
        Button btnDeleteAll = robot.lookup("#btnDeleteAll").queryAs(Button.class);
        Button btnLoadByVerseId = robot.lookup("#btnLoadByVerseId").queryAs(Button.class);

        assertNotNull(btnLemmatizeAll, "Lemmatize All button should exist");
        assertNotNull(btnLemmatizeSave, "Lemmatize & Save All button should exist");
        assertNotNull(btnLoadAll, "Load All Lemmas button should exist");
        assertNotNull(btnDeleteAll, "Delete All Lemmas button should exist");
        assertNotNull(btnLoadByVerseId, "Load by Verse ID button should exist");
    }

    @Test
    void testAllButtonsEnabled(FxRobot robot) {
        assertThat(robot.lookup("#btnLemmatizeAll").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#btnLemmatizeSave").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#btnLoadAll").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#btnDeleteAll").queryAs(Button.class)).isEnabled();
        assertThat(robot.lookup("#btnLoadByVerseId").queryAs(Button.class)).isEnabled();
    }
}
