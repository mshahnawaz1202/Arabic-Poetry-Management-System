package dal;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.BookDTO;
import dto.PoemDTO;
import dto.VerseDTO;

@ExtendWith(MockitoExtension.class)
public class ImportBookDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private ImportBookDAO importBookDAO;

    @BeforeEach
    public void setUp() {
        importBookDAO = new ImportBookDAO(dbConnectionMock);
    }

    @Test
    public void testSaveImportedBook_Success() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeUpdate()).thenReturn(1);
        when(preparedStatementMock.getGeneratedKeys()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(1)).thenReturn(100); // Mock generated ID

        BookDTO book = new BookDTO(0, "Title", "Compiler", new java.sql.Date(System.currentTimeMillis()));
        List<PoemDTO> poems = new ArrayList<>();
        List<VerseDTO> verses = new ArrayList<>();

        importBookDAO.saveImportedBook(book, poems, verses);

        // Verify that commit is called on success
        verify(connectionMock).commit();

        // Verify at least one insert was attempted
        verify(preparedStatementMock, atLeastOnce()).executeUpdate();
        verify(preparedStatementMock, atLeastOnce()).getGeneratedKeys();
    }

    @Test
    public void testSaveImportedBook_Failure() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenThrow(new SQLException("Insert failed"));

        BookDTO book = new BookDTO(0, "Title", "Compiler", new java.sql.Date(System.currentTimeMillis()));
        List<PoemDTO> poems = new ArrayList<>();
        List<VerseDTO> verses = new ArrayList<>();

        importBookDAO.saveImportedBook(book, poems, verses);

        // Verify rollback on failure
        verify(connectionMock).rollback();
    }
}
