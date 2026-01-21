package dal;

import java.sql.*;
import java.util.*;
import dto.PoetDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PoetDAO implements IPoetDAO {
    private static final Logger logger = LogManager.getLogger(PoetDAO.class);

    private final IDatabaseConnection dbConnection;

    public PoetDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public PoetDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createPoet(PoetDTO poet) {
        String sql = "INSERT INTO poet (poet_name, biography) VALUES (?, ?)";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, poet.getPoetName());
            ps.setString(2, poet.getBiography());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error creating poet: {}", e.getMessage(), e);
        }
    }

    @Override
    public PoetDTO getPoetById(int poetId) {
        String sql = "SELECT * FROM poet WHERE poet_id = ?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, poetId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return map(rs);
        } catch (SQLException e) {
            logger.error("Error getting poet: {}", e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<PoetDTO> getAllPoets() {
        List<PoetDTO> poets = new ArrayList<>();
        String sql = "SELECT * FROM poet";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next())
                poets.add(map(rs));
        } catch (SQLException e) {
            logger.error("Error retrieving poets: {}", e.getMessage(), e);
        }
        return poets;
    }

    @Override
    public void updatePoet(PoetDTO poet) {
        String sql = "UPDATE poet SET poet_name=?, biography=? WHERE poet_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, poet.getPoetName());
            ps.setString(2, poet.getBiography());
            ps.setInt(3, poet.getPoetId());
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating poet: {}", e.getMessage(), e);
        }
    }

    @Override
    public void deletePoet(int poetId) {
        String sql = "DELETE FROM poet WHERE poet_id=?";
        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, poetId);
            ps.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error deleting poet: {}", e.getMessage(), e);
        }
    }

    private PoetDTO map(ResultSet rs) throws SQLException {
        return new PoetDTO(rs.getInt("poet_id"), rs.getString("poet_name"), rs.getString("biography"));
    }
}
