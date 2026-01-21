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

import dto.LemmaDTO;

@ExtendWith(MockitoExtension.class)
public class LemmaDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private LemmaDAO lemmaDAO;

    @BeforeEach
    public void setUp() {
        lemmaDAO = new LemmaDAO(dbConnectionMock);
    }

    @Test
    public void testCreateLemma() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatementMock);

        LemmaDTO lemma = new LemmaDTO(0, 1, "Word", "Lemma", 5);
        lemmaDAO.createLemma(lemma);

        verify(preparedStatementMock, times(1)).setInt(1, 1);
        verify(preparedStatementMock, times(1)).setString(2, "Word");
        verify(preparedStatementMock, times(1)).setString(3, "Lemma");
        verify(preparedStatementMock, times(1)).setInt(4, 5);
        verify(preparedStatementMock, times(1)).executeUpdate();
    }

    @Test
    public void testGetLemmaById_Found() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("lemma_id")).thenReturn(1);
        when(resultSetMock.getString("lemma")).thenReturn("Lemma");

        LemmaDTO result = lemmaDAO.getLemmaById(1);

        assertNotNull(result);
        assertEquals(1, result.getLemmaId());
        assertEquals("Lemma", result.getLemma());

        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).next();
    }

    @Test
    public void testGetLemmaById_NotFound() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);

        when(resultSetMock.next()).thenReturn(false);

        LemmaDTO result = lemmaDAO.getLemmaById(999);

        assertNotNull(result);
        assertEquals(0, result.getLemmaId());

        verify(preparedStatementMock, times(1)).executeQuery();
        verify(resultSetMock, times(1)).next();
    }
}
