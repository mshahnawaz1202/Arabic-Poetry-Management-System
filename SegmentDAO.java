package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.SegmentDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SegmentDAO implements ISegmentDAO {
    private static final Logger logger = LogManager.getLogger(SegmentDAO.class);

    private final IDatabaseConnection dbConnection;

    public SegmentDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public SegmentDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createSegment(SegmentDTO segment) {
        String sql = "INSERT INTO segment (verse_id, word, prefix, stem, suffix, position_in_verse) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, segment.getVerseId());
            ps.setString(2, segment.getWord());
            ps.setString(3, segment.getPrefix());
            ps.setString(4, segment.getStem());
            ps.setString(5, segment.getSuffix());
            ps.setInt(6, segment.getPositionInVerse());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    segment.setSegmentId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating segment: {}", e.getMessage(), e);
        }
    }

    @Override
    public void createSegmentBatch(List<SegmentDTO> segments) {
        String sql = "INSERT INTO segment (verse_id, word, prefix, stem, suffix, position_in_verse) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = dbConnection.getConnection();) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (SegmentDTO segment : segments) {
                    ps.setInt(1, segment.getVerseId());
                    ps.setString(2, segment.getWord());
                    ps.setString(3, segment.getPrefix());
                    ps.setString(4, segment.getStem());
                    ps.setString(5, segment.getSuffix());
                    ps.setInt(6, segment.getPositionInVerse());
                    ps.addBatch();
                }

                ps.executeBatch();
                con.commit();
                con.commit();
                logger.info("Batch insert successful: {} segments saved.", segments.size());
            } catch (SQLException e) {
                con.rollback();
                logger.error("Error in batch insert, rolled back: {}", e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage(), e);
        }
    }

    @Override
    public SegmentDTO getSegmentById(int segmentId) {
        String sql = "SELECT * FROM segment WHERE segment_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, segmentId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToSegment(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving segment: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<SegmentDTO> getSegmentsByVerseId(int verseId) {
        List<SegmentDTO> segments = new ArrayList<>();
        String sql = "SELECT * FROM segment WHERE verse_id = ? ORDER BY position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                segments.add(mapResultSetToSegment(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving segments by verse: {}", e.getMessage(), e);
        }
        return segments;
    }

    @Override
    public List<SegmentDTO> getAllSegments() {
        List<SegmentDTO> segments = new ArrayList<>();
        String sql = "SELECT * FROM segment ORDER BY verse_id, position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                segments.add(mapResultSetToSegment(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all segments: {}", e.getMessage(), e);
        }
        return segments;
    }

    @Override
    public void updateSegment(SegmentDTO segment) {
        String sql = "UPDATE segment SET verse_id=?, word=?, prefix=?, stem=?, suffix=?, position_in_verse=? WHERE segment_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, segment.getVerseId());
            ps.setString(2, segment.getWord());
            ps.setString(3, segment.getPrefix());
            ps.setString(4, segment.getStem());
            ps.setString(5, segment.getSuffix());
            ps.setInt(6, segment.getPositionInVerse());
            ps.setInt(7, segment.getSegmentId());

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating segment: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteSegment(int segmentId) {
        String sql = "DELETE FROM segment WHERE segment_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, segmentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting segment: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteSegmentsByVerseId(int verseId) {
        String sql = "DELETE FROM segment WHERE verse_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted {} segments for verse ID: {}", rowsAffected, verseId);
        } catch (SQLException e) {
            logger.error("Error deleting segments by verse: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllSegments() {
        String sql = "DELETE FROM segment";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted all segments: {} rows affected.", rowsAffected);
        } catch (SQLException e) {
            logger.error("Error deleting all segments: {}", e.getMessage(), e);
        }
    }

    @Override
    public int getSegmentCount() {
        String sql = "SELECT COUNT(*) as count FROM segment";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting segment count: {}", e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public boolean segmentExists(int verseId) {
        String sql = "SELECT COUNT(*) as count FROM segment WHERE verse_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking segment existence: {}", e.getMessage(), e);
        }
        return false;
    }

    private SegmentDTO mapResultSetToSegment(ResultSet rs) throws SQLException {
        return new SegmentDTO(
                rs.getInt("segment_id"),
                rs.getInt("verse_id"),
                rs.getString("word"),
                rs.getString("prefix"),
                rs.getString("stem"),
                rs.getString("suffix"),
                rs.getInt("position_in_verse"));
    }
}