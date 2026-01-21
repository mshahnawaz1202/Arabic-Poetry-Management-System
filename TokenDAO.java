package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.TokenDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TokenDAO implements ITokenDAO {
    private static final Logger logger = LogManager.getLogger(TokenDAO.class);

    private final IDatabaseConnection dbConnection;

    public TokenDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public TokenDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createToken(TokenDTO token) {
        String sql = "INSERT INTO token (verse_id, word, root, position_in_verse) VALUES (?, ?, ?, ?)";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, token.getVerseId());
            ps.setString(2, token.getWord());
            ps.setString(3, token.getRoot());
            ps.setInt(4, token.getPositionInVerse());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    token.setTokenId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating token: {}", e.getMessage(), e);
        }
    }

    @Override
    public void createTokenBatch(List<TokenDTO> tokens) {
        String sql = "INSERT INTO token (verse_id, word, root, position_in_verse) VALUES (?, ?, ?, ?)";

        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (TokenDTO token : tokens) {
                    ps.setInt(1, token.getVerseId());
                    ps.setString(2, token.getWord());
                    ps.setString(3, token.getRoot());
                    ps.setInt(4, token.getPositionInVerse());
                    ps.addBatch();
                }

                ps.executeBatch();
                con.commit();
                con.commit();
                logger.info("Batch insert successful: {} tokens saved.", tokens.size());
            } catch (SQLException e) {
                con.rollback();
                logger.error("Error in batch insert, rolled back: {}", e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage(), e);
        }
    }

    @Override
    public TokenDTO getTokenById(int tokenId) {
        String sql = "SELECT * FROM token WHERE token_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tokenId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToToken(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving token: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<TokenDTO> getTokensByVerseId(int verseId) {
        List<TokenDTO> tokens = new ArrayList<>();
        String sql = "SELECT * FROM token WHERE verse_id = ? ORDER BY position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                tokens.add(mapResultSetToToken(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving tokens by verse: {}", e.getMessage(), e);
        }
        return tokens;
    }

    @Override
    public List<TokenDTO> getAllTokens() {
        List<TokenDTO> tokens = new ArrayList<>();
        String sql = "SELECT * FROM token ORDER BY verse_id, position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                tokens.add(mapResultSetToToken(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all tokens: {}", e.getMessage(), e);
        }
        return tokens;
    }

    @Override
    public void updateToken(TokenDTO token) {
        String sql = "UPDATE token SET verse_id=?, word=?, root=?, position_in_verse=? WHERE token_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, token.getVerseId());
            ps.setString(2, token.getWord());
            ps.setString(3, token.getRoot());
            ps.setInt(4, token.getPositionInVerse());
            ps.setInt(5, token.getTokenId());

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating token: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteToken(int tokenId) {
        String sql = "DELETE FROM token WHERE token_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tokenId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting token: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteTokensByVerseId(int verseId) {
        String sql = "DELETE FROM token WHERE verse_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted {} tokens for verse ID: {}", rowsAffected, verseId);
        } catch (SQLException e) {
            logger.error("Error deleting tokens by verse: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllTokens() {
        String sql = "DELETE FROM token";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted all tokens: {} rows affected.", rowsAffected);
        } catch (SQLException e) {
            logger.error("Error deleting all tokens: {}", e.getMessage(), e);
        }
    }

    @Override
    public int getTokenCount() {
        String sql = "SELECT COUNT(*) as count FROM token";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting token count: {}", e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public boolean tokenExists(int verseId) {
        String sql = "SELECT COUNT(*) as count FROM token WHERE verse_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking token existence: {}", e.getMessage(), e);
        }
        return false;
    }

    private TokenDTO mapResultSetToToken(ResultSet rs) throws SQLException {
        return new TokenDTO(
                rs.getInt("token_id"),
                rs.getInt("verse_id"),
                rs.getString("word"),
                rs.getString("root"),
                rs.getInt("position_in_verse"));
    }
}