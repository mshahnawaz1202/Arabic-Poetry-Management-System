package bl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dal.IDALFacade;
import dto.BookDTO;
import dto.ImportResultDTO;
import dto.PoemDTO;

public class ImportBookBOTest {

    @Mock
    private IDALFacade dalFacadeMock;

    private ImportBookBO importBookBO;
    private File tempFile;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        importBookBO = new ImportBookBO(dalFacadeMock);
    }

    @AfterEach
    public void tearDown() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    private File createTempFile(String content) throws IOException {
        tempFile = Files.createTempFile("test_import", ".txt").toFile();
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write(content);
        }
        return tempFile;
    }

    @Test
    public void testImportBook_NullFile() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            importBookBO.importBook(null);
        });
        assertEquals("File cannot be null", ex.getMessage());
    }

    @Test
    public void testImportBook_NonExistentFile() {
        File nonExistent = new File("non_existent_file_12345.txt");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            importBookBO.importBook(nonExistent);
        });
        assertTrue(ex.getMessage().contains("File does not exist"));
    }

    @Test
    public void testImportBook_Success() throws IOException {
        String content = "Some header content\n" +
                "الكتاب : Test Book Title\n" +
                "[Poem 1]\n" +
                "(Verse 1 of Poem 1)\n" +
                "(Verse 2 of Poem 1)\n" +
                "[Poem 2]\n" +
                "(Verse 1 of Poem 2)";

        createTempFile(content);

        // We can simulate success of DB calls by doing nothing (void methods) or
        // returning values if needed.
        // IDALFacade.createBook returns boolean or void? Let's check or assume void for
        // now based on previous files.
        // It seems void/boolean. Mockito mocks void methods by doing nothing by
        // default.

        ImportResultDTO result = importBookBO.importBook(tempFile);

        assertTrue(result.getMessage().contains("Imported 2 poems successfully"));
        assertEquals("Test Book Title", result.getBookTitle());
        assertEquals(2, result.getPoemsImported());

        verify(dalFacadeMock, times(1)).createBook(any(BookDTO.class));
        verify(dalFacadeMock, times(2)).savePoem(any(PoemDTO.class), anyList(), any(BookDTO.class));
    }

    @Test
    public void testImportBook_EmptyTitleDefaultsUntitled() throws IOException {
        String content = "No title marker here\n" +
                "[Poem 1]\n(Verse 1)";
        createTempFile(content);

        ImportResultDTO result = importBookBO.importBook(tempFile);

        assertEquals("Untitled", result.getBookTitle()); // Although logic says createBook is called with "Untitled"
        verify(dalFacadeMock, times(1)).createBook(argThat(book -> book.getTitle().equals("Untitled")));
    }
}
