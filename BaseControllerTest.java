package pl.controller;

import bl.IBLFacade;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Base class for all PL layer controller tests.
 * Provides common setup, Arabic test data, and helper methods.
 * 
 * Arabic poetry used for testing:
 * لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ ... لَيْسُوا مِنَ الشَّرِ فِي شَيءٍ
 * وَإنْ هانَا
 */
public abstract class BaseControllerTest extends ApplicationTest {

    // ============= ARABIC TEST DATA =============

    /** Full Arabic verse line 1 with diacritics */
    public static final String ARABIC_VERSE_LINE1 = "لَكِنَّ قَوْمِي وَإنْ كانُوا ذَوِي عَدَدٍ";

    /** Full Arabic verse line 2 with diacritics */
    public static final String ARABIC_VERSE_LINE2 = "لَيْسُوا مِنَ الشَّرِ فِي شَيءٍ وَإنْ هانَا";

    /** Complete Arabic verse */
    public static final String ARABIC_FULL_VERSE = ARABIC_VERSE_LINE1 + " ... " + ARABIC_VERSE_LINE2;

    /** Arabic book title */
    public static final String ARABIC_BOOK_TITLE = "كتاب الشعر العربي";

    /** Arabic poem title */
    public static final String ARABIC_POEM_TITLE = "قصيدة الحماسة";

    /** Arabic poet name - Al-Mutanabbi */
    public static final String ARABIC_POET_NAME = "المتنبي";

    /** Arabic poet biography */
    public static final String ARABIC_POET_BIO = "أبو الطيب المتنبي، شاعر عربي من العصر العباسي";

    /** Arabic search term */
    public static final String ARABIC_SEARCH_TERM = "قَوْمِي";

    /** Arabic root */
    public static final String ARABIC_ROOT = "قوم";

    /** Arabic lemma */
    public static final String ARABIC_LEMMA = "قوم";

    /** Arabic token */
    public static final String ARABIC_TOKEN = "قَوْمِي";

    /** Arabic segment */
    public static final String ARABIC_SEGMENT = "قوم";

    /** Arabic compiler name */
    public static final String ARABIC_COMPILER = "جامع الشعر";

    // ============= MOCK FACADE =============

    @Mock
    protected IBLFacade mockFacade;

    protected Stage testStage;

    @BeforeAll
    public static void setupSpec() throws Exception {
        // Ensure JavaFX toolkit is initialized
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "false");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
    }

    /**
     * Initialize Mockito mocks before each test.
     */
    protected void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Load an FXML view and set up the stage.
     * 
     * @param fxmlPath Path to FXML file relative to pl.view package
     * @param title    Window title
     * @param width    Window width
     * @param height   Window height
     * @return The loaded controller
     * @throws IOException if FXML loading fails
     */
    protected <T> T loadView(String fxmlPath, String title, double width, double height) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/pl/view/" + fxmlPath));
        Parent root = loader.load();
        T controller = loader.getController();

        Scene scene = new Scene(root, width, height);
        testStage.setTitle(title);
        testStage.setScene(scene);
        testStage.show();

        return controller;
    }

    /**
     * Inject the mock facade into a controller using reflection.
     * 
     * @param controller The controller instance
     */
    protected void injectMockFacade(Object controller) {
        try {
            Method setFacade = controller.getClass().getMethod("setFacade", IBLFacade.class);
            setFacade.invoke(controller, mockFacade);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock facade", e);
        }
    }

    /**
     * Override this in subclasses to set up specific stage.
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.testStage = stage;
        initMocks();
    }

    /**
     * Helper to safely close dialogs/alerts that may appear.
     */
    protected void closeAlert() {
        try {
            clickOn("OK");
        } catch (Exception ignored) {
            // No alert present, ignore
        }
    }
}
