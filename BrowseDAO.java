package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import dto.BrowseResultDTO;

public class BrowseDAO implements IBrowseDAO {
    private static final Logger logger = LogManager.getLogger(BrowseDAO.class);

    private final IDatabaseConnection dbConnection;

    public BrowseDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public BrowseDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<BrowseResultDTO> browseByLemma(String lemma) {
        List<BrowseResultDTO> results = new ArrayList<>();
        String sql = "SELECT DISTINCT v.verse_id, v.txt, v.verse_no, p.title as poem_title, " +
                "poet.poet_name, b.title as book_name " +
                "FROM verse v " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "JOIN poet poet ON p.poet_id = poet.poet_id " +
                "JOIN book b ON p.book_id = b.book_id " +
                "JOIN lemma l ON v.verse_id = l.verse_id " +
                "WHERE l.lemma = ? " +
                "ORDER BY b.title, p.title, v.verse_no";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, lemma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToBrowseResult(rs));
            }
        } catch (SQLException e) {
            logger.error("Error browsing by lemma: {}", e.getMessage(), e);
        }
        return results;
    }

    @Override
    public List<BrowseResultDTO> browseByRoot(String root) {
        List<BrowseResultDTO> results = new ArrayList<>();
        String sql = "SELECT DISTINCT v.verse_id, v.txt, v.verse_no, p.title as poem_title, " +
                "poet.poet_name, b.title as book_name " +
                "FROM verse v " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "JOIN poet poet ON p.poet_id = poet.poet_id " +
                "JOIN book b ON p.book_id = b.book_id " +
                "JOIN root r ON v.verse_id = r.verse_id " +
                "WHERE r.root = ? " +
                "ORDER BY b.title, p.title, v.verse_no";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, root);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToBrowseResult(rs));
            }
        } catch (SQLException e) {
            logger.error("Error browsing by root: {}", e.getMessage(), e);
        }
        return results;
    }

    @Override
    public List<BrowseResultDTO> browseBySegment(String segment) {
        List<BrowseResultDTO> results = new ArrayList<>();
        String sql = "SELECT DISTINCT v.verse_id, v.txt, v.verse_no, p.title as poem_title, " +
                "poet.poet_name, b.title as book_name " +
                "FROM verse v " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "JOIN poet poet ON p.poet_id = poet.poet_id " +
                "JOIN book b ON p.book_id = b.book_id " +
                "JOIN segment s ON v.verse_id = s.verse_id " +
                "WHERE s.stem = ? OR s.prefix = ? OR s.suffix = ? " +
                "ORDER BY b.title, p.title, v.verse_no";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, segment);
            ps.setString(2, segment);
            ps.setString(3, segment);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToBrowseResult(rs));
            }
        } catch (SQLException e) {
            logger.error("Error browsing by segment: {}", e.getMessage(), e);
        }
        return results;
    }

    @Override
    public List<BrowseResultDTO> browseByToken(String token) {
        List<BrowseResultDTO> results = new ArrayList<>();
        String sql = "SELECT DISTINCT v.verse_id, v.txt, v.verse_no, p.title as poem_title, " +
                "poet.poet_name, b.title as book_name " +
                "FROM verse v " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "JOIN poet poet ON p.poet_id = poet.poet_id " +
                "JOIN book b ON p.book_id = b.book_id " +
                "JOIN token t ON v.verse_id = t.verse_id " +
                "WHERE t.word = ? " +
                "ORDER BY b.title, p.title, v.verse_no";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                results.add(mapResultSetToBrowseResult(rs));
            }
        } catch (SQLException e) {
            logger.error("Error browsing by token: {}", e.getMessage(), e);
        }
        return results;
    }

    @Override
    public List<String> getDistinctLemma() {
        List<String> lemmas = new ArrayList<>();
        String sql = "SELECT DISTINCT lemma FROM lemma WHERE lemma IS NOT NULL AND lemma != '' ORDER BY lemma";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lemmas.add(rs.getString("lemma"));
            }
        } catch (SQLException e) {
            logger.error("Error getting lemmas: {}", e.getMessage(), e);
        }
        return lemmas;
    }

    @Override
    public List<String> getDistinctRoot() {
        List<String> roots = new ArrayList<>();
        String sql = "SELECT DISTINCT root FROM root WHERE root IS NOT NULL AND root != '' ORDER BY root";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                roots.add(rs.getString("root"));
            }
        } catch (SQLException e) {
            logger.error("Error getting roots: {}", e.getMessage(), e);
        }
        return roots;
    }

    @Override
    public List<String> getDistinctSegment() {
        List<String> segments = new ArrayList<>();
        String sql = "SELECT DISTINCT stem FROM segment WHERE stem IS NOT NULL AND stem != '' " +
                "UNION " +
                "SELECT DISTINCT prefix FROM segment WHERE prefix IS NOT NULL AND prefix != '' " +
                "UNION " +
                "SELECT DISTINCT suffix FROM segment WHERE suffix IS NOT NULL AND suffix != '' " +
                "ORDER BY stem";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String segment = rs.getString("stem");
                if (segment != null && !segment.trim().isEmpty()) {
                    segments.add(segment);
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting segments: {}", e.getMessage(), e);
        }
        return segments;
    }

    @Override
    public List<String> getDistinctToken() {
        List<String> tokens = new ArrayList<>();
        String sql = "SELECT DISTINCT word FROM token WHERE word IS NOT NULL AND word != '' ORDER BY word";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                tokens.add(rs.getString("word"));
            }
        } catch (SQLException e) {
            logger.error("Error getting tokens: {}", e.getMessage(), e);
        }
        return tokens;
    }

    private BrowseResultDTO mapResultSetToBrowseResult(ResultSet rs) throws SQLException {
        return new BrowseResultDTO(
                rs.getInt("verse_id"),
                rs.getString("txt"),
                rs.getString("poem_title"),
                rs.getString("poet_name"),
                rs.getString("book_name"),
                rs.getInt("verse_no"));
    }
}