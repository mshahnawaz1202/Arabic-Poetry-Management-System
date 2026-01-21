package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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

import dto.PoetDTO;

@ExtendWith(MockitoExtension.class)
public class PoetDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private PoetDAO poetDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        poetDAO = new PoetDAO(dbConnectionMock);
    }

    @Test
    public void testCreatePoet_Success() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        PoetDTO poet = new PoetDTO(0, "Poet Name", "Biography");
        poetDAO.createPoet(poet);

        verify(preparedStatementMock).setString(1, "Poet Name");
        verify(preparedStatementMock).setString(2, "Biography");
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testGetPoetById_Found() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("poet_id")).thenReturn(1);
        when(resultSetMock.getString("poet_name")).thenReturn("Test Poet");
        when(resultSetMock.getString("biography")).thenReturn("Test Bio");

        PoetDTO result = poetDAO.getPoetById(1);

        assertNotNull(result);
        assertEquals(1, result.getPoetId());
        assertEquals("Test Poet", result.getPoetName());
        assertEquals("Test Bio", result.getBiography());
    }

    @Test
    public void testGetPoetById_NotFound() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(false);

        PoetDTO result = poetDAO.getPoetById(1);

        assertNull(result);
    }

    @Test
    public void testGetAllPoets() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getInt("poet_id")).thenReturn(1, 2);
        when(resultSetMock.getString("poet_name")).thenReturn("Poet 1", "Poet 2");
        when(resultSetMock.getString("biography")).thenReturn("Bio 1", "Bio 2");

        List<PoetDTO> results = poetDAO.getAllPoets();

        assertEquals(2, results.size());
        assertEquals("Poet 1", results.get(0).getPoetName());
        assertEquals("Poet 2", results.get(1).getPoetName());
    }

    @Test
    public void testUpdatePoet() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        PoetDTO poet = new PoetDTO(1, "Updated Name", "Updated Bio");
        poetDAO.updatePoet(poet);

        verify(preparedStatementMock).setString(1, "Updated Name");
        verify(preparedStatementMock).setString(2, "Updated Bio");
        verify(preparedStatementMock).setInt(3, 1);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeletePoet() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        poetDAO.deletePoet(1);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).executeUpdate();
    }
}
