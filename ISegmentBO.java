package bl.services;

import java.util.List;

import dto.SegmentDTO;

public interface ISegmentBO {

	List<SegmentDTO> segmentAllVerses();

	int segmentAndSaveAllVerses();

	List<SegmentDTO> getAllSegmentsFromDatabase();

	List<SegmentDTO> getSegmentsByVerseId(int verseId);

	void deleteAllSegments();

	int getSegmentCount();

	boolean isVerseSegmented(int verseId);

}
