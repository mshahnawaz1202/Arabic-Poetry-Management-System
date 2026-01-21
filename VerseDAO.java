package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.VerseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerseDAO implements IVerseDAO {
    private static final Logger logger = LogManager.getLogger(VerseDAO.class);

    private final IDatabaseConnection dbConnection;

    public VerseDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public VerseDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createVerse(VerseDTO verse) {
        String sql = "INSERT INTO verse (poem_id, verse_no, txt, text_diacritized, translation, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, verse.getPoemId());
            ps.setInt(2, verse.getVerseNo());
            ps.setString(3, verse.getText());
            ps.setString(4, verse.getTextDiacritized());
            ps.setString(5, verse.getTranslation());
            ps.setString(6, verse.getNotes());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error creating verse: {}", e.getMessage(), e);
        }
    }

    @Override
    public List<VerseDTO> getAllVerses() {
        List<VerseDTO> verses = new ArrayList<>();
        String sql = "SELECT * FROM verse ORDER BY poem_id ASC, verse_no ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                verses.add(map(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all verses: {}", e.getMessage(), e);
        }
        return verses;
    }

    @Override
    public List<VerseDTO> getVersesByPoemId(int poemId) {
        List<VerseDTO> verses = new ArrayList<>();
        String sql = "SELECT * FROM verse WHERE poem_id = ? ORDER BY verse_no ASC";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, poemId);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                verses.add(map(rs));
        } catch (SQLException e) {
            logger.error("Error retrieving verses: {}", e.getMessage(), e);
        }
        return verses;
    }

    @Override
    public void updateVerse(VerseDTO verse) {
        String sql = "UPDATE verse SET poem_id=?, verse_no=?, txt=?, text_diacritized=?, translation=?, notes=? WHERE verse_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, verse.getPoemId());
            ps.setInt(2, verse.getVerseNo());
            ps.setString(3, verse.getText());
            ps.setString(4, verse.getTextDiacritized());
            ps.setString(5, verse.getTranslation());
            ps.setString(6, verse.getNotes());
            ps.setInt(7, verse.getVerseId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating verse: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteVerse(int verseId) {
        String sql = "DELETE FROM verse WHERE verse_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, verseId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting verse: {}", e.getMessage(), e);
        }
    }

    private VerseDTO map(ResultSet rs) throws SQLException {
        return new VerseDTO(
                rs.getInt("verse_id"),
                rs.getInt("poem_id"),
                rs.getInt("verse_no"),
                rs.getString("txt"),
                rs.getString("text_diacritized"),
                rs.getString("translation"),
                rs.getString("notes"));
    }

    /*--------------------------------------------------------------------------------------------------------------------*/
    /**
     * @author shahn
     */

    public List<VerseDTO> searchExactString(String text) {
        List<VerseDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM verse WHERE txt LIKE ?";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + text + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(map(rs));
            }
        } catch (SQLException e) {
            logger.error("Error in exact search: {}", e.getMessage(), e);
        }

        return results;
    }

    public List<VerseDTO> searchRegexPattern(String pattern) {
        List<VerseDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM verse WHERE txt REGEXP ?";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pattern);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                results.add(map(rs));
            }
        } catch (SQLException e) {
            logger.error("Error in regex search: {}", e.getMessage(), e);
        }

        return results;
    }
}
