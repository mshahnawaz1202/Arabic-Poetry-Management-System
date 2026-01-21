package bl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dal.IDALFacade;
import dto.BookDTO;

public class BookBOTest {

    @Mock
    private IDALFacade dalFacadeMock;

    private BookBO bookBO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookBO = new BookBO(dalFacadeMock);
    }

    @Test
    public void testCreateBook_Success() {
        BookDTO book = new BookDTO(0, "Test Book", "Test Compiler", null);

        when(dalFacadeMock.getBookID(book)).thenReturn(0);

        bookBO.createBook(book);

        verify(dalFacadeMock, times(1)).createBook(book);
    }

    @Test
    public void testCreateBook_AlreadyExists() {
        BookDTO book = new BookDTO(0, "Existing Book", "Compiler", null);

        when(dalFacadeMock.getBookID(book)).thenReturn(1);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bookBO.createBook(book);
        });

        assertEquals("Book with title 'Existing Book' already exists", exception.getMessage());
        verify(dalFacadeMock, never()).createBook(any());
    }

    @Test
    public void testGetBook_Success() {
        BookDTO book = new BookDTO(1, "Some Book", "Compiler", null);
        when(dalFacadeMock.getBook("Some Book")).thenReturn(book);

        BookDTO result = bookBO.getBook("Some Book");

        assertNotNull(result);
        assertEquals("Some Book", result.getTitle());
    }

    @Test
    public void testGetBook_NotFound() {
        when(dalFacadeMock.getBook("Unknown Book")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookBO.getBook("Unknown Book");
        });

        assertEquals("Book with title Unknown Book does not exist", exception.getMessage());
    }

    @Test
    public void testGetAllBooks_Success() {
        List<BookDTO> books = Arrays.asList(
                new BookDTO(1, "Book1", "Compiler1", null),
                new BookDTO(2, "Book2", "Compiler2", null));
        when(dalFacadeMock.getAllBooks()).thenReturn(books);

        List<BookDTO> result = bookBO.getAllBooks();

        assertEquals(2, result.size());
        verify(dalFacadeMock, times(1)).getAllBooks();
    }

    @Test
    public void testGetAllBooks_Empty() {
        when(dalFacadeMock.getAllBooks()).thenReturn(Collections.emptyList());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            bookBO.getAllBooks();
        });

        assertEquals("No books found in the database", exception.getMessage());
    }

    @Test
    public void testDeleteBook() {
        bookBO.deleteBook("Some Book");
        verify(dalFacadeMock, times(1)).deleteBook("Some Book");
    }
}
