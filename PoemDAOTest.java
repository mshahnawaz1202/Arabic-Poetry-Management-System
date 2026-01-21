package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.PoemDTO;

@ExtendWith(MockitoExtension.class)
public class PoemDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private PoemDAO poemDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        poemDAO = new PoemDAO(dbConnectionMock);
    }

    @Test
    public void testCreatePoem() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        PoemDTO poem = new PoemDTO(0, "Poem Title", 1, 1);
        poemDAO.createPoem(poem);

        verify(preparedStatementMock).setString(1, "Poem Title");
        verify(preparedStatementMock).setInt(2, 1);
        verify(preparedStatementMock).setInt(3, 1);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testGetPoemById_Found() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("poem_id")).thenReturn(1);
        when(resultSetMock.getString("title")).thenReturn("Test Poem");
        when(resultSetMock.getInt("poet_id")).thenReturn(10);
        when(resultSetMock.getInt("book_id")).thenReturn(20);

        PoemDTO result = poemDAO.getPoemById(1);

        assertNotNull(result);
        assertEquals(1, result.getPoemId());
        assertEquals("Test Poem", result.getTitle());
    }

    @Test
    public void testGetAllPoems() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getString("title")).thenReturn("Poem 1", "Poem 2");

        List<PoemDTO> results = poemDAO.getAllPoems();

        assertEquals(2, results.size());
        assertEquals("Poem 1", results.get(0).getTitle());
        assertEquals("Poem 2", results.get(1).getTitle());
    }

    @Test
    public void testUpdatePoem() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        PoemDTO poem = new PoemDTO(1, "Updated Title", 2, 2);
        poemDAO.updatePoem(poem);

        verify(preparedStatementMock).setString(1, "Updated Title");
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeletePoem() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        poemDAO.deletePoem(1);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).executeUpdate();
    }
}
