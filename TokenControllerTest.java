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

import dto.TokenDTO;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

@ExtendWith(ApplicationExtension.class)
public class TokenControllerTest extends BaseControllerTest {

    private TokenController controller;
    private static final String ARABIC_TOKEN = "قَوْمِي";
    private static final String ARABIC_ROOT = "ق و م";

    @Start
    @Override
    public void start(Stage stage) throws Exception {
        super.start(stage);

        // Mock token data with Arabic content
        TokenDTO arabicToken = new TokenDTO(1, ARABIC_TOKEN, ARABIC_ROOT);

        when(mockFacade.getAllTokensFromDatabase()).thenReturn(Arrays.asList(arabicToken));

        controller = loadView("TokenView.fxml", "Token View Test", 1100, 700);
        injectMockFacade(controller);
    }

    @Test
    void testUIComponentsExist(FxRobot robot) {
        // Table
        TableView<?> tokenTable = robot.lookup("#tokensTable").queryAs(TableView.class);
        assertNotNull(tokenTable, "Token table should exist");

        // Buttons
        Button loadTokensBtn = robot.lookup("#loadTokensBtn").queryAs(Button.class);
        Button editBtn = robot.lookup("#editBtn").queryAs(Button.class);
        Button deleteAllBtn = robot.lookup("#deleteAllBtn").queryAs(Button.class);
        assertNotNull(loadTokensBtn, "Load Tokens button should exist");
        assertNotNull(editBtn, "Edit button should exist");
        assertNotNull(deleteAllBtn, "Delete All Tokens button should exist");

        // Labels
        Label statusLabel = robot.lookup("#statusLabel").queryAs(Label.class);
        assertNotNull(statusLabel, "Status label should exist");
    }

    @Test
    void testButtonsEnabled(FxRobot robot) {
        Button loadTokensBtn = robot.lookup("#loadTokensBtn").queryAs(Button.class);
        Button editBtn = robot.lookup("#editBtn").queryAs(Button.class);
        Button deleteAllBtn = robot.lookup("#deleteAllBtn").queryAs(Button.class);

        assertThat(!loadTokensBtn.isDisabled()).isTrue();
        assertThat(!editBtn.isDisabled()).isTrue();
        assertThat(!deleteAllBtn.isDisabled()).isTrue();
    }

    @Test
    void testArabicTokenLoaded(FxRobot robot) {
        TableView<?> tokenTable = robot.lookup("#tokensTable").queryAs(TableView.class);
        assertNotNull(tokenTable, "Token table should exist");

        // Verify that the mocked Arabic token is loaded
        assertThat(tokenTable.getItems()).isNotEmpty();
        assertThat(tokenTable.getItems().get(0).toString()).contains(ARABIC_TOKEN);
    }
}
