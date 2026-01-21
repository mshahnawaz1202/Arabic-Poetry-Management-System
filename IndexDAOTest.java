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

@ExtendWith(MockitoExtension.class)
public class IndexDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private IndexDAO indexDAO;

    @BeforeEach
    public void setUp() {
        indexDAO = new IndexDAO(dbConnectionMock);
    }

    @Test
    public void testGetBookTokens() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString("word")).thenReturn("testWord");
        when(resultSetMock.getInt("occurrence_count")).thenReturn(5);

        List<String> results = indexDAO.getBookTokens(1);

        assertNotNull(results);
        assertEquals(1, results.size());

        // Stricter assertion: exact expected string
        String expected = "testWord (5 occurrences)";
        assertEquals(expected, results.get(0));

        // Verify executeQuery was called
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }
}