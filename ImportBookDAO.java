package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import dto.BookDTO;
import dto.PoemDTO;
import dto.VerseDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ImportBookDAO implements IImportBookDAO {
    private static final Logger logger = LogManager.getLogger(ImportBookDAO.class);

    private final IDatabaseConnection dbConnection;

    public ImportBookDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public ImportBookDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void saveImportedBook(BookDTO book, List<PoemDTO> poems, List<VerseDTO> verses) {
        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1. Insert book and get its ID
                int bookId = insertBook(con, book);

                // 2. Insert all poems and collect their IDs
                for (PoemDTO poem : poems) {
                    // Ensure poet exists before inserting poem
                    ensurePoetExists(con, poem.getPoetId());

                    poem.setBookId(bookId);
                    int poemId = insertPoem(con, poem);
                    poem.setPoemId(poemId);
                }

                // 3. Insert all verses
                for (VerseDTO verse : verses) {
                    insertVerse(con, verse);
                }

                con.commit();
                logger.info("Successfully imported book: {}", book.getTitle());
            } catch (SQLException e) {
                con.rollback();
                logger.error("Error importing book, rolled back: {}", e.getMessage(), e);
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void savePoem(PoemDTO poem, List<String> verseTexts, BookDTO book) {
        try (Connection con = dbConnection.getConnection()) {
            con.setAutoCommit(false);

            try {
                // 1. Check if book exists, if not create it
                int bookId = getOrCreateBook(con, book);
                poem.setBookId(bookId);

                // 2. Ensure poet exists (create default if needed)
                int poetId = getOrCreateDefaultPoet(con, poem.getPoetId());
                poem.setPoetId(poetId);

                // 3. Insert poem and get its ID
                int poemId = insertPoem(con, poem);

                // 4. Insert verses
                for (int i = 0; i < verseTexts.size(); i++) {
                    VerseDTO verse = new VerseDTO();
                    verse.setPoemId(poemId);
                    verse.setVerseNo(i + 1);
                    verse.setText(verseTexts.get(i));
                    insertVerse(con, verse);
                }

                con.commit();
                logger.info("Successfully saved poem: {}", poem.getTitle());
            } catch (SQLException e) {
                con.rollback();
                logger.error("Error saving poem, rolled back: {}", e.getMessage(), e);
            }
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private int insertBook(Connection con, BookDTO book) throws SQLException {
        String sql = "INSERT INTO book (title, compiler, era) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getCompiler());
            ps.setDate(3, book.getEra());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to get book ID");
        }
    }

    private int insertPoem(Connection con, PoemDTO poem) throws SQLException {
        String sql = "INSERT INTO poem (title, poet_id, book_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, poem.getTitle());
            ps.setInt(2, poem.getPoetId());
            ps.setInt(3, poem.getBookId());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Failed to get poem ID");
        }
    }

    private void insertVerse(Connection con, VerseDTO verse) throws SQLException {
        String sql = "INSERT INTO verse (poem_id, verse_no, txt, text_diacritized, translation, notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, verse.getPoemId());
            ps.setInt(2, verse.getVerseNo());
            ps.setString(3, verse.getText());
            ps.setString(4, verse.getTextDiacritized());
            ps.setString(5, verse.getTranslation());
            ps.setString(6, verse.getNotes());
            ps.executeUpdate();
        }
    }

    private int getOrCreateBook(Connection con, BookDTO book) throws SQLException {
        // First, try to find existing book by title
        String selectSql = "SELECT book_id FROM book WHERE title = ?";
        try (PreparedStatement ps = con.prepareStatement(selectSql)) {
            ps.setString(1, book.getTitle());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("book_id");
            }
        }

        // If not found, create new book
        return insertBook(con, book);
    }

    private void ensurePoetExists(Connection con, int poetId) throws SQLException {
        // Check if poet exists
        String selectSql = "SELECT poet_id FROM poet WHERE poet_id = ?";
        try (PreparedStatement ps = con.prepareStatement(selectSql)) {
            ps.setInt(1, poetId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return; // Poet exists
            }
        }

        // If poet doesn't exist, create a default one
        String insertSql = "INSERT INTO poet (poet_id, poet_name, biography) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertSql)) {
            ps.setInt(1, poetId);
            ps.setString(2, "Unknown Poet " + poetId);
            ps.setString(3, "Automatically created poet entry");
            ps.executeUpdate();
        }
    }

    private int getOrCreateDefaultPoet(Connection con, int poetId) throws SQLException {
        // If poetId is 0 or invalid, create a default poet
        if (poetId <= 0) {
            String insertSql = "INSERT INTO poet (poet_name, biography) VALUES (?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, "Unknown Poet");
                ps.setString(2, "No biography available");
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Failed to create default poet");
            }
        }

        // Check if poet exists
        String selectSql = "SELECT poet_id FROM poet WHERE poet_id = ?";
        try (PreparedStatement ps = con.prepareStatement(selectSql)) {
            ps.setInt(1, poetId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return poetId; // Poet exists
            }
        }

        // Create the poet if it doesn't exist
        String insertSql = "INSERT INTO poet (poet_id, poet_name, biography) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(insertSql)) {
            ps.setInt(1, poetId);
            ps.setString(2, "Unknown Poet " + poetId);
            ps.setString(3, "Automatically created poet entry");
            ps.executeUpdate();
            return poetId;
        }
    }
}
