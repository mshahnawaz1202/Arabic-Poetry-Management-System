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

import dto.TokenDTO;

@ExtendWith(MockitoExtension.class)
public class TokenDAOTest {

    @Mock
    private IDatabaseConnection dbConnectionMock;

    @Mock
    private Connection connectionMock;

    @Mock
    private PreparedStatement preparedStatementMock;

    @Mock
    private ResultSet resultSetMock;

    private TokenDAO tokenDAO;

    @BeforeEach
    public void setUp() {
        tokenDAO = new TokenDAO(dbConnectionMock);
    }

    @Test
    public void testCreateToken() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString(), anyInt())).thenReturn(preparedStatementMock);

        TokenDTO token = new TokenDTO(0, 1, "Word", "Root", 1);
        tokenDAO.createToken(token);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).setString(2, "Word");
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testGetTokenById_Found() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);
        when(preparedStatementMock.executeQuery()).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("token_id")).thenReturn(1);
        when(resultSetMock.getString("word")).thenReturn("TokenWord");

        TokenDTO result = tokenDAO.getTokenById(1);

        assertNotNull(result);
        assertEquals(1, result.getTokenId());
        assertEquals("TokenWord", result.getWord());
    }

    @Test
    public void testUpdateToken() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        TokenDTO token = new TokenDTO(1, 1, "NewWord", "Root", 2);
        tokenDAO.updateToken(token);

        verify(preparedStatementMock).setString(2, "NewWord");
        verify(preparedStatementMock).setInt(5, 1);
        verify(preparedStatementMock).executeUpdate();
    }

    @Test
    public void testDeleteToken() throws SQLException {
        when(dbConnectionMock.getConnection()).thenReturn(connectionMock);
        when(connectionMock.prepareStatement(anyString())).thenReturn(preparedStatementMock);

        tokenDAO.deleteToken(1);

        verify(preparedStatementMock).setInt(1, 1);
        verify(preparedStatementMock).executeUpdate();
    }
}
