package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.BookDTO;

@ExtendWith(MockitoExtension.class)
public class BookDAOTest {

    private BookDAO bookDAO;

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    @BeforeEach
    public void setUp() {
        bookDAO = new BookDAO(dbConnectionMock);
    }

    @Test
    public void testCreateBook_Success() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);

        when(connectionMock.prepareStatement(anyString(), eq(PreparedStatement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatementMock);

        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        when(preparedStatementMock.getGeneratedKeys()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(1)).thenReturn(123);

        BookDTO book = new BookDTO(0, "Test Title", "Test Compiler", new Date(System.currentTimeMillis()));
        bookDAO.createBook(book);

        assertEquals(123, book.getBookId());
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testGetBook_Found() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("book_id")).thenReturn(1);
        when(resultSetMock.getString("title")).thenReturn("Found Title");
        when(resultSetMock.getString("compiler")).thenReturn("Found Compiler");
        when(resultSetMock.getDate("era")).thenReturn(new Date(System.currentTimeMillis()));

        BookDTO result = bookDAO.getBook("Found Title");

        assertNotNull(result);
        assertEquals("Found Title", result.getTitle());
    }

    @Test
    public void testGetBook_NotFound() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(false);

        BookDTO result = bookDAO.getBook("Unknown Title");

        assertNull(result);
    }

    @Test
    public void testGetAllBooks() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);

        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getInt("book_id")).thenReturn(1, 2);
        when(resultSetMock.getString("title")).thenReturn("Book 1", "Book 2");
        when(resultSetMock.getString("compiler")).thenReturn("Compiler 1");

        List<BookDTO> books = bookDAO.getAllBooks();

        assertEquals(2, books.size());
    }

    @Test
    public void testUpdateBook() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        BookDTO updateData = new BookDTO(1, "New Title", "New Compiler", null);
        bookDAO.updateBook("Old Title", updateData);

        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeleteBook() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);

        bookDAO.deleteBook("Delete Me");

        verify(preparedStatementMock).executeUpdate();
    }
}
