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
public class SegmentFrequencyDAO implements ISegmentFrequencyDAO {
    private static final Logger logger = LogManager.getLogger(SegmentFrequencyDAO.class);
    private static final String CLASS_NAME = SegmentFrequencyDAO.class.getSimpleName();

    private final IDatabaseConnection dbConnection;

    public SegmentFrequencyDAO() {
        this.dbConnection = new DatabaseConnection();
    }

    public SegmentFrequencyDAO(IDatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public List<FrequencyDTO> getSegmentFrequency() {
        final String METHOD_NAME = "getSegmentFrequency";
        logger.info("{} - Starting to fetch segment frequency data", METHOD_NAME);

        List<FrequencyDTO> frequencies = new ArrayList<>();
        String query = "SELECT CONCAT(prefix, stem, suffix) AS word, COUNT(*) AS freq " +
                "FROM segment " +
                "GROUP BY word " +
                "ORDER BY freq DESC";

        logger.debug("{} - SQL Query: {}", METHOD_NAME, query);

        long startTime = System.currentTimeMillis();

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            logger.debug("{} - Database connection established successfully", METHOD_NAME);

            int rowCount = 0;
            int totalSegments = 0;
            while (rs.next()) {
                String word = rs.getString("word");
                int freq = rs.getInt("freq");

                // Sanitize word for logging (might contain sensitive data)
                String sanitizedWord = (word != null && word.length() > 20)
                        ? word.substring(0, 20) + "..."
                        : word;

                logger.trace("{} - Retrieved segment: '{}', frequency={}",
                        METHOD_NAME, sanitizedWord, freq);

                frequencies.add(new FrequencyDTO(word, freq));
                rowCount++;
                totalSegments += freq;
            }

            long executionTime = System.currentTimeMillis() - startTime;

            if (rowCount > 0) {
                logger.info("{} - Successfully retrieved {} unique segment records ({} total segments) in {} ms",
                        METHOD_NAME, rowCount, totalSegments, executionTime);

                // Log segment composition statistics
                if (logger.isDebugEnabled()) {
                    int emptyPrefixCount = 0;
                    int emptySuffixCount = 0;

                    for (FrequencyDTO dto : frequencies) {
                        String word = dto.getWord();
                        if (word != null) {
                            // Analyze segment composition
                            if (word.startsWith("-")) {
                                emptyPrefixCount += dto.getFrequency();
                            }
                            if (word.endsWith("-")) {
                                emptySuffixCount += dto.getFrequency();
                            }
                        }
                    }

                    logger.debug("{} - Segment composition: {} segments with no prefix, {} with no suffix",
                            METHOD_NAME, emptyPrefixCount, emptySuffixCount);
                }

            } else {
                logger.warn("{} - No segment frequency data found in database", METHOD_NAME);
            }

        } catch (SQLException e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - SQL Error while fetching segment frequency data after {} ms",
                    METHOD_NAME, executionTime, e);
            logger.error("{} - SQL State: {}, Error Code: {}, Message: {}",
                    METHOD_NAME, e.getSQLState(), e.getErrorCode(), e.getMessage());

        } catch (Exception e) {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("{} - Unexpected error while fetching segment frequency data after {} ms",
                    METHOD_NAME, executionTime, e);
        }

        logger.info("{} - Returning {} segment frequency records", METHOD_NAME, frequencies.size());
        return frequencies;
    }

    /**
     * Get segment frequency by individual components
     */
    public List<FrequencyDTO> getSegmentFrequencyByComponent(String component) {
        final String METHOD_NAME = "getSegmentFrequencyByComponent";
        logger.info("{} - Starting to fetch {} frequency", METHOD_NAME, component);

        if (!component.equals("prefix") && !component.equals("stem") && !component.equals("suffix")) {
            logger.error("{} - Invalid component type: {}", METHOD_NAME, component);
            throw new IllegalArgumentException("Component must be 'prefix', 'stem', or 'suffix'");
        }

        List<FrequencyDTO> frequencies = new ArrayList<>();
        String query = String.format("SELECT %s AS word, COUNT(*) AS freq FROM segment GROUP BY %s ORDER BY freq DESC",
                component, component);

        logger.debug("{} - Component-specific SQL Query: {}", METHOD_NAME, query);

        long startTime = System.currentTimeMillis();

        try (Connection conn = dbConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            int rowCount = 0;
            while (rs.next()) {
                frequencies.add(new FrequencyDTO(
                        rs.getString("word"),
                        rs.getInt("freq")));
                rowCount++;
            }

            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("{} - Retrieved {} {} frequency records in {} ms",
                    METHOD_NAME, rowCount, component, executionTime);

        } catch (SQLException e) {
            logger.error("{} - Error fetching {} frequency data", METHOD_NAME, component, e);
        }

        return frequencies;
    }
}