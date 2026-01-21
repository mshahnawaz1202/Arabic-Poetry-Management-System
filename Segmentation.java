package bl.services;

import java.util.ArrayList;
import java.util.List;

import dal.ISegmentDAO;
import dal.IVerseDAO;
import dto.SegmentDTO;
import dto.VerseDTO;
import net.oujda_nlp_team.ADATAnalyzer;
public class Segmentation implements ISegmentBO {

    private final IVerseDAO verseDAO;
    private final ISegmentDAO segmentDAO;
    public Segmentation(IVerseDAO verseDAO, ISegmentDAO segmentDAO) {
		this.verseDAO = verseDAO;
		this.segmentDAO = segmentDAO;
	}
    

    @Override
    public List<SegmentDTO> segmentAllVerses() {
        List<SegmentDTO> segments = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();

        System.out.println("Starting Preview Segmentation");
        System.out.println("Total verses: " + verses.size());

        for (VerseDTO verse : verses) {
            String txt = verse.getText();

            System.out.println("\nVerse ID: " + verse.getVerseId());
            System.out.println("Verse No: " + verse.getVerseNo());
            System.out.println("Text: " + txt);

            if (txt == null || txt.trim().isEmpty()) {
                System.out.println("Skipping empty verse.\n");
                continue;
            }

            String[] words = txt.replaceAll("[^\\p{IsArabic}\\s]", "").split("\\s+");
            int position = 1;

            for (String word : words) {
                if (word.trim().isEmpty()) continue;

                try {
                    //ADAT segmentation
                    String segmentString =
                            ADATAnalyzer.getInstance().processADATTokenizedString(word);

                    SegmentDTO segment = new SegmentDTO(
                            verse.getVerseId(),
                            word,
                            segmentString, 
                            "",             
                            ""             
                    );

                    segment.setVerseText(txt);
                    segment.setPositionInVerse(position);

                    segments.add(segment);

                    System.out.println("Word: " + word + " | Segmentation: " + segmentString);

                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }

                position++;
            }
        }

        System.out.println("\n=== Preview Complete ===");
        System.out.println("Total segments generated: " + segments.size());

        return segments;
    }
    @Override
    public int segmentAndSaveAllVerses() {
        List<SegmentDTO> segments = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();

        System.out.println("Starting segmentation for " + verses.size() + " verses...");

        for (VerseDTO verse : verses) {

            // Skip if already segmented
            if (isVerseSegmented(verse.getVerseId())) {
                System.out.println("Verse " + verse.getVerseId() + " already segmented, skipping...");
                continue;
            }

            String txt = verse.getText();
            if (txt == null || txt.trim().isEmpty()) continue;

            String[] words = txt.replaceAll("[^\\p{IsArabic}\\s]", "").split("\\s+");
            int position = 1;

            for (String word : words) {
                if (word.trim().isEmpty()) continue;

                try {
                    //ADAT segmentation
                    String segmentString = ADATAnalyzer.getInstance().processADATTokenizedString(word);

                    SegmentDTO segment = new SegmentDTO(
                            verse.getVerseId(),
                            word,
                            segmentString,  
                            "",
                            ""
                    );

                    segment.setPositionInVerse(position);
                    segments.add(segment);

                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }
                position++;
            }
        }


        if (!segments.isEmpty()) {
            segmentDAO.createSegmentBatch(segments);
            System.out.println("Segmentation complete: " + segments.size() + " segments saved to database.");
        }

        return segments.size();
    }
    @Override
    public List<SegmentDTO> getAllSegmentsFromDatabase() {
        List<SegmentDTO> segments = segmentDAO.getAllSegments();
        List<VerseDTO> allVerses = verseDAO.getAllVerses();

        for (SegmentDTO segment : segments) {
            try {
                for (VerseDTO verse : allVerses) {
                    if (verse.getVerseId() == segment.getVerseId()) {
                        segment.setVerseText(verse.getText());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting verse text: " + e.getMessage());
            }
        }

        return segments;
    }
    @Override
    public List<SegmentDTO> getSegmentsByVerseId(int verseId) {
        return segmentDAO.getSegmentsByVerseId(verseId);
    }
    @Override
    public void deleteAllSegments() {
        segmentDAO.deleteAllSegments();
    }
    @Override
    public int getSegmentCount() {
        return segmentDAO.getSegmentCount();
    }
    @Override
    public boolean isVerseSegmented(int verseId) {
        return segmentDAO.getSegmentsByVerseId(verseId).size() > 0;
    }
}