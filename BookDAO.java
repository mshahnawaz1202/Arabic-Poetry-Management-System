package dal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.BookDTO;

public class BookDAO implements IBookDAO {
    private static final Logger logger = LogManager.getLogger(BookDAO.class);
    private final IDatabaseConnection dbConnection;

    public BookDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public BookDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    @Override
    public void createBook(BookDTO book) {
        final String METHOD_NAME = "createBook";

        // Input validation logging
        if (book == null) {
            logger.error("{} - Cannot create book: BookDTO is null", METHOD_NAME);
            return;
        }

        logger.info("{} - Creating book with title: '{}'", METHOD_NAME, book.getTitle());

        String query = "INSERT INTO book (title, compiler, era) VALUES (?, ?, ?)";

        long startTime = System.currentTimeMillis();

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            logger.debug("{} - SQL Query: {}", METHOD_NAME, query);
            logger.debug("{} - Book details - Title: '{}', Compiler: '{}', Era: {}",
                    METHOD_NAME, book.getTitle(), book.getCompiler(), book.getEra());

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getCompiler());

            // Handle null era properly
            if (book.getEra() != null) {
                stmt.setDate(3, book.getEra());
                logger.trace("{} - Setting era parameter: {}", METHOD_NAME, book.getEra());
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
                logger.trace("{} - Era is null, setting as NULL", METHOD_NAME);
            }

            int rowsAffected = stmt.executeUpdate();
            long executionTime = System.currentTimeMillis() - startTime;

            if (rowsAffected > 0) {
                // Retrieve generated book_id
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        book.setBookId(generatedId); // Update DTO with generated ID
                        logger.info("{} - Book '{}' created successfully with ID: {} ({} ms, {} rows affected)",
                                METHOD_NAME, book.getTitle(), generatedId, executionTime, rowsAffected);
                    } else {
                        logger.warn("{} - Book '{}' created but no generated ID returned ({} ms)",
                                METHOD_NAME, book.getTitle(), executionTime);
                    }
                }
            } else {
                logger.warn("{} - No rows affected when creating book '{}' ({} ms)",
                        METHOD_NAME, book.getTitle(), executionTime);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error creating book '{}' after {} ms. SQL State: {}, Error Code: {}, Message: {}",
                    METHOD_NAME, book.getTitle(), executionTime, e.getSQLState(), e.getErrorCode(), e.getMessage(), e);

            // Check for duplicate entry
            if (e.getErrorCode() == 1062 || e.getSQLState().equals("23000")) {
                logger.warn("{} - Duplicate book entry detected for title '{}'", METHOD_NAME, book.getTitle());
            }

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error creating book '{}' after {} ms: {}",
                    METHOD_NAME, book.getTitle(), executionTime, e.getMessage(), e);
        }
    }

    @Override
    public BookDTO getBook(String title) {
        final String METHOD_NAME = "getBook";

        if (title == null || title.trim().isEmpty()) {
            logger.error("{} - Cannot retrieve book: Title is null or empty", METHOD_NAME);
            return null;
        }

        logger.info("{} - Retrieving book with title: '{}'", METHOD_NAME, title);

        String query = "SELECT * FROM book WHERE title = ?";

        long startTime = System.currentTimeMillis();

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            logger.debug("{} - SQL Query: {}", METHOD_NAME, query);
            stmt.setString(1, title);
            logger.trace("{} - Setting title parameter: '{}'", METHOD_NAME, title);

            try (ResultSet rs = stmt.executeQuery()) {
                long executionTime = System.currentTimeMillis() - startTime;

                if (rs.next()) {
                    BookDTO book = mapResultSetToBookDTO(rs);
                    logger.info("{} - Book '{}' retrieved successfully (ID: {}, {} ms)",
                            METHOD_NAME, title, book.getBookId(), executionTime);
                    logger.debug("{} - Book details: ID={}, Title='{}', Compiler='{}', Era={}",
                            METHOD_NAME, book.getBookId(), book.getTitle(), book.getCompiler(), book.getEra());
                    return book;
                } else {
                    logger.warn("{} - No book found with title '{}' ({} ms)", METHOD_NAME, title, executionTime);
                    return null;
                }
            }
        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error retrieving book '{}' after {} ms: {}",
                    METHOD_NAME, title, executionTime, e.getMessage(), e);
            return null;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error retrieving book '{}' after {} ms: {}",
                    METHOD_NAME, title, executionTime, e.getMessage(), e);
            return null;
        }
    }

    @Override
    public List<BookDTO> getAllBooks() {
        final String METHOD_NAME = "getAllBooks";
        logger.info("{} - Retrieving all books", METHOD_NAME);

        String query = "SELECT * FROM book ORDER BY title";
        List<BookDTO> books = new ArrayList<>();

        long startTime = System.currentTimeMillis();

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            logger.debug("{} - SQL Query: {}", METHOD_NAME, query);

            int rowCount = 0;
            while (rs.next()) {
                BookDTO book = mapResultSetToBookDTO(rs);
                books.add(book);
                rowCount++;

                // Log first few books at debug level
                if (rowCount <= 5 && logger.isDebugEnabled()) {
                    logger.debug("{} - Book {}: ID={}, Title='{}'", METHOD_NAME, rowCount, book.getBookId(),
                            book.getTitle());
                }
            }

            long executionTime = System.currentTimeMillis() - startTime;

            if (rowCount > 0) {
                logger.info("{} - Retrieved {} books successfully ({} ms)",
                        METHOD_NAME, rowCount, executionTime);

                // Log some statistics
                if (logger.isDebugEnabled()) {
                    int booksWithEra = (int) books.stream()
                            .filter(b -> b.getEra() != null)
                            .count();
                    logger.debug("{} - Books with era specified: {} out of {}",
                            METHOD_NAME, booksWithEra, rowCount);
                }

            } else {
                logger.warn("{} - No books found in database ({} ms)", METHOD_NAME, executionTime);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error retrieving all books after {} ms: {}",
                    METHOD_NAME, executionTime, e.getMessage(), e);
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error retrieving all books after {} ms: {}",
                    METHOD_NAME, executionTime, e.getMessage(), e);
        }

        return books;
    }

    @Override
    public void updateBook(String currentTitle, BookDTO book) {
        final String METHOD_NAME = "updateBook";

        if (currentTitle == null || currentTitle.trim().isEmpty()) {
            logger.error("{} - Cannot update book: Current title is null or empty", METHOD_NAME);
            return;
        }

        if (book == null) {
            logger.error("{} - Cannot update book '{}': BookDTO is null", METHOD_NAME, currentTitle);
            return;
        }

        logger.info("{} - Updating book from '{}' to '{}'",
                METHOD_NAME, currentTitle, book.getTitle());

        String query = "UPDATE book SET title = ?, compiler = ?, era = ? WHERE title = ?";

        long startTime = System.currentTimeMillis();

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            logger.debug("{} - SQL Query: {}", METHOD_NAME, query);
            logger.debug("{} - Update parameters: New Title='{}', Compiler='{}', Era={}, Current Title='{}'",
                    METHOD_NAME, book.getTitle(), book.getCompiler(), book.getEra(), currentTitle);

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getCompiler());

            if (book.getEra() != null) {
                stmt.setDate(3, book.getEra());
                logger.trace("{} - Setting era parameter: {}", METHOD_NAME, book.getEra());
            } else {
                stmt.setNull(3, java.sql.Types.DATE);
                logger.trace("{} - Era is null, setting as NULL", METHOD_NAME);
            }

            stmt.setString(4, currentTitle);
            logger.trace("{} - Setting current title parameter: '{}'", METHOD_NAME, currentTitle);

            int rowsAffected = stmt.executeUpdate();
            long executionTime = System.currentTimeMillis() - startTime;

            if (rowsAffected > 0) {
                logger.info("{} - Book '{}' updated successfully to '{}' ({} ms, {} rows affected)",
                        METHOD_NAME, currentTitle, book.getTitle(), executionTime, rowsAffected);
            } else {
                logger.warn("{} - No book found to update with title '{}' ({} ms)",
                        METHOD_NAME, currentTitle, executionTime);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error updating book '{}' after {} ms: {}",
                    METHOD_NAME, currentTitle, executionTime, e.getMessage(), e);
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error updating book '{}' after {} ms: {}",
                    METHOD_NAME, currentTitle, executionTime, e.getMessage(), e);
        }
    }

    @Override
    public void deleteBook(String title) {
        final String METHOD_NAME = "deleteBook";

        if (title == null || title.trim().isEmpty()) {
            logger.error("{} - Cannot delete book: Title is null or empty", METHOD_NAME);
            return;
        }

        logger.info("{} - Deleting book with title: '{}'", METHOD_NAME, title);

        String query = "DELETE FROM book WHERE title = ?";

        long startTime = System.currentTimeMillis();

        try (Connection connection = dbConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            logger.debug("{} - SQL Query: {}", METHOD_NAME, query);
            stmt.setString(1, title);
            logger.trace("{} - Setting title parameter: '{}'", METHOD_NAME, title);

            int rowsAffected = stmt.executeUpdate();
            long executionTime = System.currentTimeMillis() - startTime;

            if (rowsAffected > 0) {
                logger.info("{} - Book '{}' deleted successfully ({} ms, {} rows affected)",
                        METHOD_NAME, title, executionTime, rowsAffected);
            } else {
                logger.warn("{} - No book found to delete with title '{}' ({} ms)",
                        METHOD_NAME, title, executionTime);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error deleting book '{}' after {} ms: {}",
                    METHOD_NAME, title, executionTime, e.getMessage(), e);
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error deleting book '{}' after {} ms: {}",
                    METHOD_NAME, title, executionTime, e.getMessage(), e);
        }
    }

    private BookDTO mapResultSetToBookDTO(ResultSet rs) throws SQLException {
        try {
            int bookId = rs.getInt("book_id");
            String title = rs.getString("title");
            String compiler = rs.getString("compiler");
            Date era = rs.getDate("era");

            logger.trace("Mapping ResultSet to BookDTO - ID: {}, Title: '{}', Compiler: '{}', Era: {}",
                    bookId, title, compiler, era);

            return new BookDTO(bookId, title, compiler, era);
        } catch (SQLException e) {
            logger.error("Error mapping ResultSet to BookDTO: {}", e.getMessage(), e);
            throw e;
        }
    }

    public int getBookID(BookDTO book) {
        final String METHOD_NAME = "getBookID";

        if (book == null || book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            logger.error("{} - Cannot get book ID: BookDTO or title is null/empty", METHOD_NAME);
            return 0;
        }

        logger.info("{} - Getting book ID for title: '{}'", METHOD_NAME, book.getTitle());

        String query = "SELECT book_id FROM book WHERE title = ?";

        long startTime = System.currentTimeMillis();

        try (Connection connection = DatabaseConfigure.getConnection();
                PreparedStatement stmt = connection.prepareStatement(query)) {

            logger.debug("{} - SQL Query: {}", METHOD_NAME, query);
            stmt.setString(1, book.getTitle());
            logger.trace("{} - Setting title parameter: '{}'", METHOD_NAME, book.getTitle());

            try (ResultSet rs = stmt.executeQuery()) {
                long executionTime = System.currentTimeMillis() - startTime;

                if (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    logger.info("{} - Found book ID {} for title '{}' ({} ms)",
                            METHOD_NAME, bookId, book.getTitle(), executionTime);
                    return bookId;
                } else {
                    logger.warn("{} - No book found with title '{}' ({} ms)",
                            METHOD_NAME, book.getTitle(), executionTime);
                    return 0;
                }
            }
        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error getting book ID for '{}' after {} ms: {}",
                    METHOD_NAME, book.getTitle(), executionTime, e.getMessage(), e);
            return 0;
        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error getting book ID for '{}' after {} ms: {}",
                    METHOD_NAME, book.getTitle(), executionTime, e.getMessage(), e);
            return 0;
        }
    }
}