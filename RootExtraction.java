package bl.services;

import java.util.ArrayList;
import java.util.List;

import dal.IRootDAO;
import dal.IVerseDAO;
import dto.RootDTO;
import dto.VerseDTO;
import net.oujda_nlp_team.AlKhalil2Analyzer;
import net.oujda_nlp_team.entity.Result;
import net.oujda_nlp_team.entity.ResultList;

public class RootExtraction implements IRootBO{

    private final IVerseDAO verseDAO;
    private final IRootDAO rootDAO;
    
    public RootExtraction(IVerseDAO verseDAO, IRootDAO rootDAO) {
		this.verseDAO = verseDAO;
		this.rootDAO = rootDAO;
	}
    @Override
    public List<RootDTO> extractRootsAllVerses() {
        List<RootDTO> roots = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();

        System.out.println("Starting Preview Root Extraction");
        System.out.println("Total verses: " + verses.size());

        for (VerseDTO verse : verses) {
            String txt = verse.getText();

            System.out.println("Verse ID: " + verse.getVerseId());
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
                    ResultList results = AlKhalil2Analyzer.getInstance().processToken(word);

                    if (results != null && !results.getAllResults().isEmpty()) {
                        Result r = results.getAllResults().get(0);

                        String root = r.getRoot() != null ? r.getRoot() : "";

                        RootDTO rootDTO = new RootDTO(verse.getVerseId(), word, root);
                        rootDTO.setVerseText(txt);
                        rootDTO.setPositionInVerse(position);

                        roots.add(rootDTO);

                        System.out.println("Word: " + word + " | Root: " + root);
                    } else {
                        System.out.println("No analysis found for: " + word);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }

                position++;
            }
        }

        System.out.println("Total roots generated: " + roots.size());

        return roots;
    }
    @Override
    public int extractAndSaveAllRoots() {
        List<RootDTO> roots = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();

        System.out.println("Starting root extraction for " + verses.size() + " verses...");

        for (VerseDTO verse : verses) {
            if (rootDAO.rootExists(verse.getVerseId())) {
                System.out.println("Verse " + verse.getVerseId() + " already processed, skipping...");
                continue;
            }

            String txt = verse.getText();
            if (txt == null || txt.trim().isEmpty()) continue;

            String[] words = txt.replaceAll("[^\\p{IsArabic}\\s]", "").split("\\s+");
            int position = 1;

            for (String word : words) {
                if (word.trim().isEmpty()) continue;

                try {
                    ResultList results = AlKhalil2Analyzer.getInstance().processToken(word);

                    if (results != null && results.getAllResults() != null && !results.getAllResults().isEmpty()) {
                        Result r = results.getAllResults().get(0);

                        String root = r.getRoot() != null ? r.getRoot() : "";

                        RootDTO rootDTO = new RootDTO(verse.getVerseId(), word, root);
                        rootDTO.setPositionInVerse(position);
                        roots.add(rootDTO);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }
                position++;
            }
        }

        if (!roots.isEmpty()) {
            rootDAO.createRootBatch(roots);
            System.out.println("Root extraction complete: " + roots.size() + " roots saved to database.");
        }

        return roots.size();
    }
    @Override
    public List<RootDTO> getAllRootsFromDatabase() {
        List<RootDTO> roots = rootDAO.getAllRoots();
        List<VerseDTO> allVerses = verseDAO.getAllVerses();
        
        for (RootDTO root : roots) {
            try {
                for (VerseDTO verse : allVerses) {
                    if (verse.getVerseId() == root.getVerseId()) {
                        root.setVerseText(verse.getText());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting verse text: " + e.getMessage());
            }
        }
        
        return roots;
    }
    @Override
    public List<RootDTO> getRootsByVerseId(int verseId) {
        return rootDAO.getRootsByVerseId(verseId);
    }
    @Override
    public void deleteAllRoots() {
        rootDAO.deleteAllRoots();
    }
    @Override
    public int getRootCount() {
        return rootDAO.getRootCount();
    }
}