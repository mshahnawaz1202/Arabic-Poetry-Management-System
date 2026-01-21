package dal;

import java.sql.*;
import java.util.*;
import dto.PoemDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoemDAO implements IPoemDAO {
    private static final Logger logger = LogManager.getLogger(PoemDAO.class);

    private final IDatabaseConnection dbConnection;

    public PoemDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public PoemDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createPoem(PoemDTO poem) {
        String sql = "INSERT INTO poem (title, poet_id, book_id) VALUES (?, ?, ?)";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, poem.getTitle());
            ps.setInt(2, poem.getPoetId());
            ps.setInt(3, poem.getBookId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error creating poem: {}", e.getMessage(), e);
        }
    }

    @Override
    public PoemDTO getPoemById(int poemId) {
        String sql = "SELECT * FROM poem WHERE poem_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, poemId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
        } catch (SQLException e) {
            logger.error("Error retrieving poem: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<PoemDTO> getAllPoems() {
        List<PoemDTO> poems = new ArrayList<>();
        String sql = "SELECT * FROM poem";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                poems.add(map(rs));
        } catch (SQLException e) {
            logger.error("Error retrieving poems: {}", e.getMessage(), e);
        }
        return poems;
    }

    @Override
    public void updatePoem(PoemDTO poem) {
        String sql = "UPDATE poem SET title=?, poet_id=?, book_id=? WHERE poem_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, poem.getTitle());
            ps.setInt(2, poem.getPoetId());
            ps.setInt(3, poem.getBookId());
            ps.setInt(4, poem.getPoemId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating poem: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deletePoem(int poemId) {
        String sql = "DELETE FROM poem WHERE poem_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, poemId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting poem: {}", e.getMessage(), e);
        }
    }

    private PoemDTO map(ResultSet rs) throws SQLException {
        return new PoemDTO(
                rs.getInt("poem_id"),
                rs.getString("title"),
                rs.getInt("poet_id"),
                rs.getInt("book_id"));
    }
}
