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
public class RootFrequencyDAO implements IRootFrequencyDAO {
    private static final Logger logger = LogManager.getLogger(RootFrequencyDAO.class);
    private static final String CLASS_NAME = RootFrequencyDAO.class.getSimpleName();

    private final IDatabaseConnection dbConnection;

    public RootFrequencyDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public RootFrequencyDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<FrequencyDTO> getRootFrequency() {
        final String METHOD_NAME = "getRootFrequency";
        logger.info("{} - Starting to fetch root frequency data", METHOD_NAME);

        List<FrequencyDTO> frequencies = new ArrayList<>();
        String query = "SELECT root AS word, COUNT(*) AS freq FROM root GROUP BY root ORDER BY freq DESC";

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

                logger.trace("{} - Retrieved: root='{}', frequency={}", METHOD_NAME, word, freq);

                frequencies.add(new FrequencyDTO(word, freq));
                rowCount++;
            }

            long executionTime = System.currentTimeMillis() - startTime;

            if (rowCount > 0) {
                logger.info("{} - Successfully retrieved {} root frequency records in {} ms",
                        METHOD_NAME, rowCount, executionTime);

                // Log summary statistics at debug level
                if (logger.isDebugEnabled()) {
                    int totalFrequency = frequencies.stream().mapToInt(FrequencyDTO::getFrequency).sum();
                    logger.debug("{} - Total root occurrences: {}", METHOD_NAME, totalFrequency);

                    if (!frequencies.isEmpty()) {
                        FrequencyDTO maxFreq = frequencies.get(0);
                        logger.debug("{} - Most frequent root: '{}' ({} occurrences)",
                                METHOD_NAME, maxFreq.getWord(), maxFreq.getFrequency());
                    }
                }

            } else {
                logger.warn("{} - No root frequency data found in database", METHOD_NAME);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error while fetching root frequency data after {} ms",
                    METHOD_NAME, executionTime, e);
            logger.error("{} - SQL State: {}, Error Code: {}, Message: {}",
                    METHOD_NAME, e.getSQLState(), e.getErrorCode(), e.getMessage());

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error while fetching root frequency data after {} ms",
                    METHOD_NAME, executionTime, e);
        }

        logger.info("{} - Returning {} root frequency records", METHOD_NAME, frequencies.size());
        return frequencies;
    }

    /**
     * Get root frequency with custom minimum frequency threshold
     */
    public List<FrequencyDTO> getRootFrequency(int minFrequency) {
        final String METHOD_NAME = "getRootFrequency(minFrequency)";
        logger.info("{} - Starting to fetch root frequency with minFrequency={}",
                METHOD_NAME, minFrequency);

        List<FrequencyDTO> frequencies = new ArrayList<>();
        String query = "SELECT root AS word, COUNT(*) AS freq " +
                "FROM root " +
                "GROUP BY root " +
                "HAVING COUNT(*) >= ? " +
                "ORDER BY freq DESC";

        logger.debug("{} - SQL Query with filter: {}", METHOD_NAME, query);
        logger.debug("{} - Minimum frequency threshold: {}", METHOD_NAME, minFrequency);

        long startTime = System.currentTimeMillis();

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, minFrequency);

            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    frequencies.add(new FrequencyDTO(
                            rs.getString("word"),
                            rs.getInt("freq")));
                    rowCount++;
                }

                long executionTime = System.currentTimeMillis() - startTime;
                logger.info("{} - Retrieved {} filtered root records (freq >= {}) in {} ms",
                        METHOD_NAME, rowCount, minFrequency, executionTime);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Failed to execute filtered root query after {} ms",
                    METHOD_NAME, executionTime, e);
        }

        return frequencies;
    }
}