package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
public class LemmaFrequencyDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private LemmaFrequencyDAO lemmaFrequencyDAO;

    @BeforeEach
    public void setUp() {
        lemmaFrequencyDAO = new LemmaFrequencyDAO(dbConnectionMock);
    }

    @Test
    public void testGetLemmaFrequency() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        // Mock one row
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString("root")).thenReturn("testLemma");
        when(resultSetMock.getInt("freq")).thenReturn(4);

        List<FrequencyDTO> result = lemmaFrequencyDAO.getLemmaFrequency();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testLemma", result.get(0).getWord());
        assertEquals(4, result.get(0).getFrequency());

        // Verify query execution
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }
}
