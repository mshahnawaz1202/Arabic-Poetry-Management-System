package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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

import dto.RootDTO;

@ExtendWith(MockitoExtension.class)
public class RootDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private RootDAO rootDAO;

    @BeforeEach
    public void setUp() {
        rootDAO = new RootDAO(dbConnectionMock);
    }

    @Test
    public void testCreateRoot() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatementMock);

        RootDTO root = new RootDTO(0, 1, "Word", "Root", 1);
        rootDAO.createRoot(root);

        // Verify all relevant parameter mappings
        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).setString(2, "Word");
        verify(preparedStatementMock).setString(3, "Root");
        verify(preparedStatementMock).setInt(4, 1);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testGetRootById_Found() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("root_id")).thenReturn(1);
        when(resultSetMock.getInt("verse_id")).thenReturn(1);
        when(resultSetMock.getString("word")).thenReturn("Word");
        when(resultSetMock.getString("root")).thenReturn("RootVal");
        when(resultSetMock.getInt("position_in_verse")).thenReturn(1);

        RootDTO result = rootDAO.getRootById(1);

        assertNotNull(result);
        assertEquals(1, result.getRootId());
        assertEquals("RootVal", result.getRoot());

        // Verify executeQuery and resultSet.next() calls
        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock).next();
    }

    @Test
    public void testUpdateRoot() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        RootDTO root = new RootDTO(1, 1, "Word", "NewRoot", 2);
        rootDAO.updateRoot(root);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).setString(2, "Word");
        verify(preparedStatementMock).setString(3, "NewRoot");
        verify(preparedStatementMock).setInt(4, 2);
        verify(preparedStatementMock).setInt(5, 1); // WHERE root_id = ?
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeleteRoot() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        rootDAO.deleteRoot(1);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).executeUpdate();
    }
}
