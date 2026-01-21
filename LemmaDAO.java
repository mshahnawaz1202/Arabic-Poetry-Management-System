package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dto.LemmaDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LemmaDAO implements ILemmaDAO {
    private static final Logger logger = LogManager.getLogger(LemmaDAO.class);

    private final IDatabaseConnection dbConnection;

    public LemmaDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public LemmaDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createLemma(LemmaDTO lemma) {
        String sql = "INSERT INTO lemma (verse_id, word, lemma, position_in_verse) VALUES (?, ?, ?, ?)";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, lemma.getVerseId());
            ps.setString(2, lemma.getWord());
            ps.setString(3, lemma.getLemma());
            ps.setInt(4, lemma.getPositionInVerse());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    lemma.setLemmaId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating lemma: {}", e.getMessage(), e);
        }
    }

    @Override
    public void createLemmaBatch(List<LemmaDTO> lemmas) {
        String sql = "INSERT INTO lemma (verse_id, word, lemma, position_in_verse) VALUES (?, ?, ?, ?)";

        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (LemmaDTO lemma : lemmas) {
                    ps.setInt(1, lemma.getVerseId());
                    ps.setString(2, lemma.getWord());
                    ps.setString(3, lemma.getLemma());
                    ps.setInt(4, lemma.getPositionInVerse());
                    ps.addBatch();
                }

                ps.executeBatch();
                con.commit();
                con.commit();
                logger.info("Batch insert successful: {} lemmas saved.", lemmas.size());
            } catch (SQLException e) {
                con.rollback();
                logger.error("Error in batch insert, rolled back: {}", e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage(), e);
        }
    }

    @Override
    public LemmaDTO getLemmaById(int lemmaId) {
        String sql = "SELECT * FROM lemma WHERE lemma_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lemmaId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToLemma(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving lemma: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<LemmaDTO> getLemmasByVerseId(int verseId) {
        List<LemmaDTO> lemmas = new ArrayList<>();
        String sql = "SELECT * FROM lemma WHERE verse_id = ? ORDER BY position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                lemmas.add(mapResultSetToLemma(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving lemmas by verse: {}", e.getMessage(), e);
        }
        return lemmas;
    }

    @Override
    public List<LemmaDTO> getAllLemmas() {
        List<LemmaDTO> lemmas = new ArrayList<>();
        String sql = "SELECT * FROM lemma ORDER BY verse_id, position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lemmas.add(mapResultSetToLemma(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all lemmas: {}", e.getMessage(), e);
        }
        return lemmas;
    }

    @Override
    public void updateLemma(LemmaDTO lemma) {
        String sql = "UPDATE lemma SET verse_id=?, word=?, lemma=?, position_in_verse=? WHERE lemma_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lemma.getVerseId());
            ps.setString(2, lemma.getWord());
            ps.setString(3, lemma.getLemma());
            ps.setInt(4, lemma.getPositionInVerse());
            ps.setInt(5, lemma.getLemmaId());

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating lemma: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteLemma(int lemmaId) {
        String sql = "DELETE FROM lemma WHERE lemma_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, lemmaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting lemma: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteLemmasByVerseId(int verseId) {
        String sql = "DELETE FROM lemma WHERE verse_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ps.setInt(1, verseId);
            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted {} lemmas for verse ID: {}", rowsAffected, verseId);
        } catch (SQLException e) {
            logger.error("Error deleting lemmas by verse: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllLemmas() {
        String sql = "DELETE FROM lemma";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted all lemmas: {} rows affected.", rowsAffected);
        } catch (SQLException e) {
            logger.error("Error deleting all lemmas: {}", e.getMessage(), e);
        }
    }

    @Override
    public int getLemmaCount() {
        String sql = "SELECT COUNT(*) as count FROM lemma";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting lemma count: {}", e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public boolean lemmaExists(int verseId) {
        String sql = "SELECT COUNT(*) as count FROM lemma WHERE verse_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking lemma existence: {}", e.getMessage(), e);
        }
        return false;
    }

    private LemmaDTO mapResultSetToLemma(ResultSet rs) throws SQLException {
        return new LemmaDTO(
                rs.getInt("lemma_id"),
                rs.getInt("verse_id"),
                rs.getString("word"),
                rs.getString("lemma"),
                rs.getInt("position_in_verse"));
    }
}