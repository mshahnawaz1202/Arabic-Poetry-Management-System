package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
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

import dto.BrowseResultDTO;

@ExtendWith(MockitoExtension.class)
public class BrowseDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private BrowseDAO browseDAO;

    @BeforeEach
    public void setUp() {
        browseDAO = new BrowseDAO(dbConnectionMock);
    }

    @Test
    public void testBrowseByLemma() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Mock result set
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getInt("verse_id")).thenReturn(1);
        when(resultSetMock.getString("txt")).thenReturn("Verse Text");
        when(resultSetMock.getString("poem_title")).thenReturn("Poem Title");
        when(resultSetMock.getString("poet_name")).thenReturn("Poet Name");
        when(resultSetMock.getString("book_name")).thenReturn("Book Title");
        when(resultSetMock.getInt("verse_no")).thenReturn(10);

        List<BrowseResultDTO> results = browseDAO.browseByLemma("testLemma");

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Verse Text", results.get(0).getText());

        // Optional: verify correct method calls
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }

    @Test
    public void testGetDistinctLemma() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Mock result set
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString("lemma")).thenReturn("testLemma");

        List<String> results = browseDAO.getDistinctLemma();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("testLemma", results.get(0));

        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }
}
