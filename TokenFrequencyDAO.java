package dal;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import dto.FrequencyDTO;
import java.util.List;
import java.sql.*;

/***
 * @author shahn
 */
public class TokenFrequencyDAO implements ITokenFrequencyDAO {
    private static final Logger logger = LogManager.getLogger(TokenFrequencyDAO.class);
    private static final String CLASS_NAME = TokenFrequencyDAO.class.getSimpleName();

    private final IDatabaseConnection dbConnection;

    public TokenFrequencyDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public TokenFrequencyDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<FrequencyDTO> getTokenFrequency() {
        final String METHOD_NAME = "getTokenFrequency";
        logger.info("{} - Starting to fetch token frequency data", METHOD_NAME);

        List<FrequencyDTO> list = new ArrayList<>();
        String sql = "SELECT word, COUNT(*) AS frequency FROM token GROUP BY word ORDER BY frequency DESC";

        logger.debug("{} - SQL Query: {}", METHOD_NAME, sql);

        long startTime = System.currentTimeMillis();

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            logger.debug("{} - Database connection established successfully", METHOD_NAME);

            int rowCount = 0;
            int totalTokens = 0;
            String mostFrequentToken = null;
            int maxFrequency = 0;

            while (rs.next()) {
                String word = rs.getString("word");
                int frequency = rs.getInt("frequency");

                // Use your FrequencyDTO constructor
                FrequencyDTO dto = new FrequencyDTO(word, frequency);
                list.add(dto);

                // Track statistics
                if (frequency > maxFrequency) {
                    maxFrequency = frequency;
                    mostFrequentToken = word;
                }

                rowCount++;
                totalTokens += frequency;

                // Log high-frequency tokens at debug level
                if (logger.isDebugEnabled() && frequency > 100) {
                    logger.debug("{} - High frequency token: '{}' ({} occurrences)",
                            METHOD_NAME, word, frequency);
                }
            }

            long executionTime = System.currentTimeMillis() - startTime;

            if (rowCount > 0) {
                logger.info("{} - Successfully retrieved {} unique tokens ({} total occurrences) in {} ms",
                        METHOD_NAME, rowCount, totalTokens, executionTime);

                // Log comprehensive statistics
                if (logger.isInfoEnabled()) {
                    logger.info("{} - Most frequent token: '{}' ({} occurrences)",
                            METHOD_NAME, mostFrequentToken, maxFrequency);

                    // Calculate average frequency
                    double avgFrequency = totalTokens / (double) rowCount;
                    logger.info("{} - Average token frequency: {:.2f}",
                            METHOD_NAME, avgFrequency);
                }

                // Analyze token length distribution at debug level
                if (logger.isDebugEnabled() && !list.isEmpty()) {
                    analyzeTokenLengthDistribution(list, METHOD_NAME);
                }

            } else {
                logger.warn("{} - No token frequency data found in database", METHOD_NAME);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error while fetching token frequency data after {} ms",
                    METHOD_NAME, executionTime, e);
            logger.error("{} - SQL State: {}, Error Code: {}, Message: {}",
                    METHOD_NAME, e.getSQLState(), e.getErrorCode(), e.getMessage());

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error while fetching token frequency data after {} ms",
                    METHOD_NAME, executionTime, e);
        }

        logger.info("{} - Returning {} token frequency records", METHOD_NAME, list.size());
        return list;
    }

    /**
     * Get token frequency with word pattern filter
     */
    public List<FrequencyDTO> getTokenFrequency(String pattern) {
        final String METHOD_NAME = "getTokenFrequency(pattern)";
        logger.info("{} - Starting to fetch token frequency with pattern: {}", METHOD_NAME, pattern);

        List<FrequencyDTO> list = new ArrayList<>();
        String sql = "SELECT word, COUNT(*) AS frequency FROM token WHERE word LIKE ? GROUP BY word ORDER BY frequency DESC";

        logger.debug("{} - Pattern SQL Query: {}", METHOD_NAME, sql);
        logger.debug("{} - Search pattern: {}", METHOD_NAME, pattern);

        long startTime = System.currentTimeMillis();

        try (Connection con = dbConnection.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                int rowCount = 0;
                while (rs.next()) {
                    list.add(new FrequencyDTO(
                            rs.getString("word"),
                            rs.getInt("frequency")));
                    rowCount++;
                }

                long executionTime = System.currentTimeMillis() - startTime;
                logger.info("{} - Retrieved {} tokens matching pattern '{}' in {} ms",
                        METHOD_NAME, rowCount, pattern, executionTime);
            }

        } catch (SQLException e) {
            logger.error("{} - Error fetching token frequency with pattern '{}'",
                    METHOD_NAME, pattern, e);
        }

        return list;
    }

    /**
     * Private helper method to analyze token length distribution
     */
    private void analyzeTokenLengthDistribution(List<FrequencyDTO> tokens, String methodName) {
        int[] lengthBuckets = new int[6]; // [1-3], [4-6], [7-9], [10-12], [13-15], [16+]

        for (FrequencyDTO token : tokens) {
            String word = token.getWord();
            if (word != null) {
                int length = word.length();
                int bucketIndex = Math.min((length - 1) / 3, 5);
                lengthBuckets[bucketIndex] += token.getFrequency();
            }
        }

        logger.debug("{} - Token length distribution:", methodName);
        String[] bucketLabels = { "1-3 chars", "4-6 chars", "7-9 chars",
                "10-12 chars", "13-15 chars", "16+ chars" };
        for (int i = 0; i < lengthBuckets.length; i++) {
            if (lengthBuckets[i] > 0) {
                logger.debug("{} -   {}: {} occurrences",
                        methodName, bucketLabels[i], lengthBuckets[i]);
            }
        }
    }
}