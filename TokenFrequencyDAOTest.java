package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

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

import dto.FrequencyDTO;

@ExtendWith(MockitoExtension.class)
public class TokenFrequencyDAOTest {

    private TokenFrequencyDAO tokenFrequencyDAO;

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
        tokenFrequencyDAO = new TokenFrequencyDAO(dbConnectionMock);
    }

    @Test
    public void testGetTokenFrequency_Success() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getString("word")).thenReturn("word1", "word2");
        when(resultSetMock.getInt("frequency")).thenReturn(10, 5);

        List<FrequencyDTO> result = tokenFrequencyDAO.getTokenFrequency();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("word1", result.get(0).getWord());
        assertEquals(10, result.get(0).getFrequency());
        assertEquals("word2", result.get(1).getWord());
        assertEquals(5, result.get(1).getFrequency());

        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(3)).next();
    }

    @Test
    public void testGetTokenFrequencyInternalException() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenThrow(new SQLException("Database error"));

        List<FrequencyDTO> result = tokenFrequencyDAO.getTokenFrequency();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetTokenFrequencyByPattern_Success() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString("word")).thenReturn("testWord");
        when(resultSetMock.getInt("frequency")).thenReturn(3);

        List<FrequencyDTO> result = tokenFrequencyDAO.getTokenFrequency("test%");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testWord", result.get(0).getWord());
        assertEquals(3, result.get(0).getFrequency());

        verify(preparedStatementMock).setString(1, "test%");
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }
}
