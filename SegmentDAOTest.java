package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dto.SegmentDTO;

@ExtendWith(MockitoExtension.class)
public class SegmentDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private SegmentDAO segmentDAO;

    @BeforeEach
    public void setUp() {
        segmentDAO = new SegmentDAO(dbConnectionMock);
    }

    @Test
    public void testCreateSegment() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatementMock);

        SegmentDTO segment = new SegmentDTO(0, 1, "Word", "Pre", "Stem", "Suf", 1);
        segmentDAO.createSegment(segment);

        // Verify all parameter mappings
        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).setString(2, "Word");
        verify(preparedStatementMock).setString(3, "Pre");
        verify(preparedStatementMock).setString(4, "Stem");
        verify(preparedStatementMock).setString(5, "Suf");
        verify(preparedStatementMock).setInt(6, 1);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testGetSegmentById_Found() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("segment_id")).thenReturn(1);
        when(resultSetMock.getInt("verse_id")).thenReturn(1);
        when(resultSetMock.getString("word")).thenReturn("Word");
        when(resultSetMock.getString("prefix")).thenReturn("Pre");
        when(resultSetMock.getString("stem")).thenReturn("StemVal");
        when(resultSetMock.getString("suffix")).thenReturn("Suf");
        when(resultSetMock.getInt("position_in_verse")).thenReturn(1);

        SegmentDTO result = segmentDAO.getSegmentById(1);

        assertNotNull(result);
        assertEquals(1, result.getSegmentId());
        assertEquals("StemVal", result.getStem());

        // Verify query execution
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }

    @Test
    public void testUpdateSegment() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        SegmentDTO segment = new SegmentDTO(1, 1, "Word", "Pre", "NewStem", "Suf", 2);
        segmentDAO.updateSegment(segment);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).setString(2, "Word");
        verify(preparedStatementMock).setString(3, "Pre");
        verify(preparedStatementMock).setString(4, "NewStem");
        verify(preparedStatementMock).setString(5, "Suf");
        verify(preparedStatementMock).setInt(6, 2);
        verify(preparedStatementMock).setInt(7, 1); // WHERE segment_id = ?
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeleteSegment() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        segmentDAO.deleteSegment(1);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).executeUpdate();
    }
}
