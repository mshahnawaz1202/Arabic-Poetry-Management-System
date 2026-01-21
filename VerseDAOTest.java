package dal;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

import dto.VerseDTO;

@ExtendWith(MockitoExtension.class)
public class VerseDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private VerseDAO verseDAO;

    @BeforeEach
    public void setUp() throws SQLException {
        verseDAO = new VerseDAO(dbConnectionMock);
    }

    @Test
    public void testCreateVerse() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        VerseDTO verse = new VerseDTO(0, 1, 1, "Text", "Diacritized", "Trans", "Notes");
        verseDAO.createVerse(verse);

        verify(preparedStatementMock).setInt(1, 1); // poem_id
        verify(preparedStatementMock).setInt(2, 1); // verse_no
        verify(preparedStatementMock).setString(3, "Text");
        verify(preparedStatementMock).setString(4, "Diacritized");
        verify(preparedStatementMock).setString(5, "Trans");
        verify(preparedStatementMock).setString(6, "Notes");
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testGetAllVerses() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getInt("verse_id")).thenReturn(1);
        when(resultSetMock.getString("txt")).thenReturn("Verse Text");

        List<VerseDTO> results = verseDAO.getAllVerses();

        assertEquals(1, results.size());
        assertEquals("Verse Text", results.get(0).getText());

        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }

    @Test
    public void testGetVersesByPoemId() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, false);
        when(resultSetMock.getInt("verse_no")).thenReturn(1);

        List<VerseDTO> results = verseDAO.getVersesByPoemId(1);

        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getVerseNo());

        verify(preparedStatementMock).executeQuery();
        verify(resultSetMock, times(2)).next();
    }

    @Test
    public void testUpdateVerse() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        VerseDTO verse = new VerseDTO(1, 1, 1, "New Text", "Dia", "Trans", "Notes");
        verseDAO.updateVerse(verse);

        verify(preparedStatementMock).setInt(1, 1); // poem_id
        verify(preparedStatementMock).setInt(2, 1); // verse_no
        verify(preparedStatementMock).setString(3, "New Text");
        verify(preparedStatementMock).setString(4, "Dia");
        verify(preparedStatementMock).setString(5, "Trans");
        verify(preparedStatementMock).setString(6, "Notes");
        verify(preparedStatementMock).setInt(7, 1); // WHERE verse_id
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeleteVerse() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        verseDAO.deleteVerse(1);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).executeUpdate();
    }
}
