package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.RootDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RootDAO implements IRootDAO {
    private static final Logger logger = LogManager.getLogger(RootDAO.class);

    private final IDatabaseConnection dbConnection;

    public RootDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public RootDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createRoot(RootDTO root) {
        String sql = "INSERT INTO root (verse_id, word, root, position_in_verse) VALUES (?, ?, ?, ?)";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, root.getVerseId());
            ps.setString(2, root.getWord());
            ps.setString(3, root.getRoot());
            ps.setInt(4, root.getPositionInVerse());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    root.setRootId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            logger.error("Error creating root: {}", e.getMessage(), e);
        }
    }

    @Override
    public void createRootBatch(List<RootDTO> roots) {
        String sql = "INSERT INTO root (verse_id, word, root, position_in_verse) VALUES (?, ?, ?, ?)";

        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (RootDTO root : roots) {
                    ps.setInt(1, root.getVerseId());
                    ps.setString(2, root.getWord());
                    ps.setString(3, root.getRoot());
                    ps.setInt(4, root.getPositionInVerse());
                    ps.addBatch();
                }

                ps.executeBatch();
                con.commit();
                con.commit();
                logger.info("Batch insert successful: {} roots saved.", roots.size());
            } catch (SQLException e) {
                con.rollback();
                logger.error("Error in batch insert, rolled back: {}", e.getMessage(), e);
            }
        } catch (SQLException e) {
            logger.error("Database connection error: {}", e.getMessage(), e);
        }
    }

    @Override
    public RootDTO getRootById(int rootId) {
        String sql = "SELECT * FROM root WHERE root_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rootId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToRoot(rs);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving root: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<RootDTO> getRootsByVerseId(int verseId) {
        List<RootDTO> roots = new ArrayList<>();
        String sql = "SELECT * FROM root WHERE verse_id = ? ORDER BY position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                roots.add(mapResultSetToRoot(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving roots by verse: {}", e.getMessage(), e);
        }
        return roots;
    }

    @Override
    public List<RootDTO> getAllRoots() {
        List<RootDTO> roots = new ArrayList<>();
        String sql = "SELECT * FROM root ORDER BY verse_id, position_in_verse ASC";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                roots.add(mapResultSetToRoot(rs));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all roots: {}", e.getMessage(), e);
        }
        return roots;
    }

    @Override
    public void updateRoot(RootDTO root) {
        String sql = "UPDATE root SET verse_id=?, word=?, root=?, position_in_verse=? WHERE root_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, root.getVerseId());
            ps.setString(2, root.getWord());
            ps.setString(3, root.getRoot());
            ps.setInt(4, root.getPositionInVerse());
            ps.setInt(5, root.getRootId());

            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating root: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteRoot(int rootId) {
        String sql = "DELETE FROM root WHERE root_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, rootId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting root: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteRootsByVerseId(int verseId) {
        String sql = "DELETE FROM root WHERE verse_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted {} roots for verse ID: {}", rowsAffected, verseId);
        } catch (SQLException e) {
            logger.error("Error deleting roots by verse: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deleteAllRoots() {
        String sql = "DELETE FROM root";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            int rowsAffected = ps.executeUpdate();
            logger.info("Deleted all roots: {} rows affected.", rowsAffected);
        } catch (SQLException e) {
            logger.error("Error deleting all roots: {}", e.getMessage(), e);
        }
    }

    @Override
    public int getRootCount() {
        String sql = "SELECT COUNT(*) as count FROM root";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("count");
            }
        } catch (SQLException e) {
            logger.error("Error getting root count: {}", e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public boolean rootExists(int verseId) {
        String sql = "SELECT COUNT(*) as count FROM root WHERE verse_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, verseId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("count") > 0;
            }
        } catch (SQLException e) {
            logger.error("Error checking root existence: {}", e.getMessage(), e);
        }
        return false;
    }

    private RootDTO mapResultSetToRoot(ResultSet rs) throws SQLException {
        return new RootDTO(
                rs.getInt("root_id"),
                rs.getInt("verse_id"),
                rs.getString("word"),
                rs.getString("root"),
                rs.getInt("position_in_verse"));
    }
}