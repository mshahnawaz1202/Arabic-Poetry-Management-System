package dal;

import dto.FrequencyDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/***
 * @author shahn
 */
public class LemmaFrequencyDAO implements ILemmaFrequencyDAO {
    private static final Logger logger = LogManager.getLogger(LemmaFrequencyDAO.class);
    private static final String CLASS_NAME = LemmaFrequencyDAO.class.getSimpleName();

    private final IDatabaseConnection dbConnection;

    public LemmaFrequencyDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public LemmaFrequencyDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<FrequencyDTO> getLemmaFrequency() {
        final String METHOD_NAME = "getLemmaFrequency";
        logger.info("{} - Starting to fetch lemma frequency data", METHOD_NAME);

        List<FrequencyDTO> frequencies = new ArrayList<>();
        String query = "SELECT lemma AS word, COUNT(*) AS freq FROM lemma GROUP BY lemma ORDER BY freq DESC";

        // Log the SQL query (at debug level for security)
        logger.debug("{} - SQL Query: {}", METHOD_NAME, query);

        long startTime = System.currentTimeMillis();

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            logger.debug("{} - Database connection established successfully", METHOD_NAME);

            int rowCount = 0;
            while (rs.next()) {
                String word = rs.getString("word");
                int freq = rs.getInt("freq");

                // Log each row at trace level for detailed debugging
                logger.trace("{} - Retrieved: word='{}', frequency={}", METHOD_NAME, word, freq);

                frequencies.add(new FrequencyDTO(word, freq));
                rowCount++;
            }

            long executionTime = System.currentTimeMillis() - startTime;

            if (rowCount > 0) {
                logger.info("{} - Successfully retrieved {} lemma frequency records in {} ms",
                        METHOD_NAME, rowCount, executionTime);
            } else {
                logger.warn("{} - No lemma frequency data found in database", METHOD_NAME);
            }

            // Log sample data for debugging (first 5 records)
            if (logger.isDebugEnabled() && !frequencies.isEmpty()) {
                logger.debug("{} - Sample data (first 5 records):", METHOD_NAME);
                frequencies.stream().limit(5)
                        .forEach(freqDTO -> logger.debug("  - {}: {}", freqDTO.getWord(), freqDTO.getFrequency()));
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error while fetching lemma frequency data after {} ms",
                    METHOD_NAME, executionTime, e);

            // Log specific SQL state and error code for better debugging
            logger.error("{} - SQL State: {}, Error Code: {}, Message: {}",
                    METHOD_NAME, e.getSQLState(), e.getErrorCode(), e.getMessage());

            // You could rethrow as a custom application exception
            // throw new DataAccessException("Failed to fetch lemma frequencies", e);

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error while fetching lemma frequency data after {} ms",
                    METHOD_NAME, executionTime, e);
        }

        logger.info("{} - Returning {} frequency records", METHOD_NAME, frequencies.size());
        return frequencies;
    }

    /**
     * Overloaded method with additional filtering capabilities
     */
    public List<FrequencyDTO> getLemmaFrequencyWithFilter(int minFrequency, int limit) {
        final String METHOD_NAME = "getLemmaFrequencyWithFilter";
        logger.info("{} - Starting to fetch lemma frequency with filter: minFrequency={}, limit={}",
                METHOD_NAME, minFrequency, limit);

        List<FrequencyDTO> frequencies = new ArrayList<>();
        String query = "SELECT lemma AS word, COUNT(*) AS freq " +
                "FROM lemma " +
                "GROUP BY lemma " +
                "HAVING COUNT(*) >= ? " +
                "ORDER BY freq DESC " +
                "LIMIT ?";

        logger.debug("{} - Parameterized SQL Query with filters", METHOD_NAME);
        logger.debug("{} - Query parameters: minFrequency={}, limit={}",
                METHOD_NAME, minFrequency, limit);

        long startTime = System.currentTimeMillis();

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, minFrequency);
            ps.setInt(2, limit);

            logger.debug("{} - Executing parameterized query", METHOD_NAME);

            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    frequencies.add(new FrequencyDTO(
                            rs.getString("word"),
                            rs.getInt("freq")));
                    rowCount++;
                }

                long executionTime = System.currentTimeMillis() - startTime;
                logger.info("{} - Retrieved {} filtered lemma records in {} ms",
                        METHOD_NAME, rowCount, executionTime);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Failed to execute filtered query after {} ms. " +
                    "Parameters: minFrequency={}, limit={}",
                    METHOD_NAME, executionTime, minFrequency, limit, e);
        }

        return frequencies;
    }
}