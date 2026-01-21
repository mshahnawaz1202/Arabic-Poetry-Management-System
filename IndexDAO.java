package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.IndexResultDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IndexDAO implements IIndexDAO {
    private static final Logger logger = LogManager.getLogger(IndexDAO.class);

    private final IDatabaseConnection dbConnection;

    public IndexDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public IndexDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public List<String> getBookTokens(int bookId) {
        List<String> tokens = new ArrayList<>();
        String sql = "SELECT DISTINCT t.word, COUNT(*) as occurrence_count " +
                "FROM token t " +
                "JOIN verse v ON t.verse_id = v.verse_id " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "WHERE p.book_id = ? AND t.word IS NOT NULL AND t.word != '' " +
                "GROUP BY t.word " +
                "ORDER BY occurrence_count DESC, t.word";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String word = rs.getString("word");
                int count = rs.getInt("occurrence_count");
                tokens.add(word + " (" + count + " occurrences)");
            }
        } catch (SQLException e) {
            logger.error("Error getting book tokens: {}", e.getMessage(), e);
        }
        return tokens;
    }

    @Override
    public List<String> getBookLemmas(int bookId) {
        List<String> lemmas = new ArrayList<>();
        String sql = "SELECT DISTINCT l.lemma, COUNT(*) as occurrence_count " +
                "FROM lemma l " +
                "JOIN verse v ON l.verse_id = v.verse_id " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "WHERE p.book_id = ? AND l.lemma IS NOT NULL AND l.lemma != '' " +
                "GROUP BY l.lemma " +
                "ORDER BY occurrence_count DESC, l.lemma";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String lemma = rs.getString("lemma");
                int count = rs.getInt("occurrence_count");
                lemmas.add(lemma + " (" + count + " occurrences)");
            }
        } catch (SQLException e) {
            logger.error("Error getting book lemmas: {}", e.getMessage(), e);
        }
        return lemmas;
    }

    @Override
    public List<String> getBookRoots(int bookId) {
        List<String> roots = new ArrayList<>();
        String sql = "SELECT DISTINCT r.root, COUNT(*) as occurrence_count " +
                "FROM root r " +
                "JOIN verse v ON r.verse_id = v.verse_id " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "WHERE p.book_id = ? AND r.root IS NOT NULL AND r.root != '' " +
                "GROUP BY r.root " +
                "ORDER BY occurrence_count DESC, r.root";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String root = rs.getString("root");
                int count = rs.getInt("occurrence_count");
                roots.add(root + " (" + count + " occurrences)");
            }
        } catch (SQLException e) {
            logger.error("Error getting book roots: {}", e.getMessage(), e);
        }
        return roots;
    }

    public List<String> getLemmasByRoot(int bookId, String root) {
        List<String> lemmas = new ArrayList<>();
        String sql = "SELECT DISTINCT l.lemma, COUNT(*) as occurrence_count " +
                "FROM lemma l " +
                "JOIN verse v ON l.verse_id = v.verse_id " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "WHERE p.book_id = ? AND l.lemma IS NOT NULL AND l.lemma != '' " +
                "AND (l.lemma LIKE ? OR EXISTS (" +
                "  SELECT 1 FROM root r2 WHERE r2.verse_id = l.verse_id " +
                "  AND r2.root = ? AND r2.position_in_verse = l.position_in_verse" +
                ")) " +
                "GROUP BY l.lemma " +
                "ORDER BY occurrence_count DESC, l.lemma";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setString(2, "%" + root + "%");
            ps.setString(3, root);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String lemma = rs.getString("lemma");
                int count = rs.getInt("occurrence_count");
                lemmas.add(lemma + " (" + count + " occurrences)");
            }
        } catch (SQLException e) {
            logger.error("Error getting lemmas by root: {}", e.getMessage(), e);
        }
        return lemmas;
    }

    public List<String> getTokensByLemma(int bookId, String lemma) {
        List<String> tokens = new ArrayList<>();
        String sql = "SELECT DISTINCT t.word, COUNT(*) as occurrence_count " +
                "FROM token t " +
                "JOIN verse v ON t.verse_id = v.verse_id " +
                "JOIN poem p ON v.poem_id = p.poem_id " +
                "WHERE p.book_id = ? AND t.word IS NOT NULL AND t.word != '' " +
                "AND (t.word = ? OR EXISTS (" +
                "  SELECT 1 FROM lemma l2 WHERE l2.verse_id = t.verse_id " +
                "  AND l2.lemma = ? AND l2.position_in_verse = t.position_in_verse" +
                ")) " +
                "GROUP BY t.word " +
                "ORDER BY occurrence_count DESC, t.word";

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setString(2, lemma);
            ps.setString(3, lemma);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String word = rs.getString("word");
                int count = rs.getInt("occurrence_count");
                tokens.add(word + " (" + count + " occurrences)");
            }
        } catch (SQLException e) {
            logger.error("Error getting tokens by lemma: {}", e.getMessage(), e);
        }
        return tokens;
    }

    @Override
    public List<IndexResultDTO> getTokenOccurrences(int bookId, String token) {
        // Extract just the word part (remove occurrence count)
        String cleanToken = extractWord(token);
        return getOccurrences(bookId, cleanToken, "token");
    }

    @Override
    public List<IndexResultDTO> getLemmaOccurrences(int bookId, String lemma) {
        // Extract just the word part (remove occurrence count)
        String cleanLemma = extractWord(lemma);
        return getOccurrences(bookId, cleanLemma, "lemma");
    }

    @Override
    public List<IndexResultDTO> getRootOccurrences(int bookId, String root) {
        // Extract just the word part (remove occurrence count)
        String cleanRoot = extractWord(root);
        return getOccurrences(bookId, cleanRoot, "root");
    }

    private List<IndexResultDTO> getOccurrences(int bookId, String word, String type) {
        List<IndexResultDTO> results = new ArrayList<>();
        String sql = "";

        switch (type) {
            case "token":
                sql = "SELECT v.verse_id, v.txt as verse_text, v.verse_no, " +
                        "p.title as poem_title, poet.poet_name, b.title as book_name, t.word " +
                        "FROM token t " +
                        "JOIN verse v ON t.verse_id = v.verse_id " +
                        "JOIN poem p ON v.poem_id = p.poem_id " +
                        "JOIN poet poet ON p.poet_id = poet.poet_id " +
                        "JOIN book b ON p.book_id = b.book_id " +
                        "WHERE p.book_id = ? AND t.word = ? " +
                        "ORDER BY p.title, v.verse_no";
                break;
            case "lemma":
                sql = "SELECT v.verse_id, v.txt as verse_text, v.verse_no, " +
                        "p.title as poem_title, poet.poet_name, b.title as book_name, l.lemma as word " +
                        "FROM lemma l " +
                        "JOIN verse v ON l.verse_id = v.verse_id " +
                        "JOIN poem p ON v.poem_id = p.poem_id " +
                        "JOIN poet poet ON p.poet_id = poet.poet_id " +
                        "JOIN book b ON p.book_id = b.book_id " +
                        "WHERE p.book_id = ? AND l.lemma = ? " +
                        "ORDER BY p.title, v.verse_no";
                break;
            case "root":
                sql = "SELECT v.verse_id, v.txt as verse_text, v.verse_no, " +
                        "p.title as poem_title, poet.poet_name, b.title as book_name, r.root as word " +
                        "FROM root r " +
                        "JOIN verse v ON r.verse_id = v.verse_id " +
                        "JOIN poem p ON v.poem_id = p.poem_id " +
                        "JOIN poet poet ON p.poet_id = poet.poet_id " +
                        "JOIN book b ON p.book_id = b.book_id " +
                        "WHERE p.book_id = ? AND r.root = ? " +
                        "ORDER BY p.title, v.verse_no";
                break;
        }

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, bookId);
            ps.setString(2, word);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                IndexResultDTO result = new IndexResultDTO(
                        rs.getInt("verse_id"),
                        rs.getString("verse_text"),
                        rs.getInt("verse_no"),
                        rs.getString("poem_title"),
                        rs.getString("poet_name"),
                        rs.getString("book_name"),
                        rs.getString("word"),
                        type);
                results.add(result);
            }
        } catch (SQLException e) {
            logger.error("Error getting {} occurrences: {}", type, e.getMessage(), e);
        }
        return results;
    }

    // Helper method to extract just the word part from "word (count occurrences)"
    private String extractWord(String wordWithCount) {
        if (wordWithCount.contains(" (")) {
            return wordWithCount.substring(0, wordWithCount.indexOf(" ("));
        }
        return wordWithCount;
    }

    // Debug method to see total vs unique counts
    public void debugTotalCounts(int bookId) {
        String[] types = { "token", "lemma", "root" };
        String[] tables = { "token", "lemma", "root" };
        String[] columns = { "word", "lemma", "root" };

        logger.debug("=== DEBUG: TOTAL COUNTS FOR BOOK ID {} ===", bookId);

        for (int i = 0; i < types.length; i++) {
            String countSql = "SELECT COUNT(*) as total_count " +
                    "FROM " + tables[i] + " t " +
                    "JOIN verse v ON t.verse_id = v.verse_id " +
                    "JOIN poem p ON v.poem_id = p.poem_id " +
                    "WHERE p.book_id = ?";

            String distinctSql = "SELECT COUNT(DISTINCT t." + columns[i] + ") as distinct_count " +
                    "FROM " + tables[i] + " t " +
                    "JOIN verse v ON t.verse_id = v.verse_id " +
                    "JOIN poem p ON v.poem_id = p.poem_id " +
                    "WHERE p.book_id = ?";

            try (Connection con = dbConnection.getConnection();
                    PreparedStatement countPs = con.prepareStatement(countSql);
                    PreparedStatement distinctPs = con.prepareStatement(distinctSql)) {

                countPs.setInt(1, bookId);
                distinctPs.setInt(1, bookId);

                ResultSet countRs = countPs.executeQuery();
                ResultSet distinctRs = distinctPs.executeQuery();

                if (countRs.next() && distinctRs.next()) {
                    logger.debug("{}: {} total, {} unique", types[i].toUpperCase(), countRs.getInt("total_count"),
                            distinctRs.getInt("distinct_count"));
                }

            } catch (SQLException e) {
                logger.error("Error debugging {} counts: {}", types[i], e.getMessage(), e);
            }
        }
    }
}