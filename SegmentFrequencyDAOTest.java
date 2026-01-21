package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
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

import dto.FrequencyDTO;

@ExtendWith(MockitoExtension.class)
public class SegmentFrequencyDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private SegmentFrequencyDAO segmentFrequencyDAO;

    @BeforeEach
    public void setUp() {
        segmentFrequencyDAO = new SegmentFrequencyDAO(dbConnectionMock);
    }

    @Test
    public void testGetSegmentFrequency() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getString("word")).thenReturn("testSegment");
        when(resultSetMock.getInt("freq")).thenReturn(7);

        List<FrequencyDTO> result = segmentFrequencyDAO.getSegmentFrequency();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testSegment", result.get(0).getWord());
        assertEquals(7, result.get(0).getFrequency());
    }
}
