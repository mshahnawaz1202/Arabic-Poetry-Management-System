package dal;

import java.util.List;
import dto.SegmentDTO;

public interface ISegmentDAO {
    void createSegment(SegmentDTO segment);
    void createSegmentBatch(List<SegmentDTO> segments);
    SegmentDTO getSegmentById(int segmentId);
    List<SegmentDTO> getSegmentsByVerseId(int verseId);
    List<SegmentDTO> getAllSegments();
    void updateSegment(SegmentDTO segment);
    void deleteSegment(int segmentId);
    void deleteSegmentsByVerseId(int verseId);
    void deleteAllSegments();
    int getSegmentCount();
    boolean segmentExists(int verseId);
}