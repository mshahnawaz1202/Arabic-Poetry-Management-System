package bl.services;

import java.util.ArrayList;
import java.util.List;

import dal.ILemmaDAO;
import dal.IVerseDAO;
import dto.LemmaDTO;
import dto.VerseDTO;
import net.oujda_nlp_team.AlKhalil2Analyzer;
import net.oujda_nlp_team.entity.Result;
import net.oujda_nlp_team.entity.ResultList;

public class Lemmatization implements ILemmaBO{

    private final IVerseDAO verseDAO;
    private final ILemmaDAO lemmaDAO;
    
    public Lemmatization(IVerseDAO verseDAO, ILemmaDAO lemmaDAO) {
    			this.verseDAO = verseDAO;
    			this.lemmaDAO = lemmaDAO;
    }
    @Override
    public List<LemmaDTO> lemmatizeAllVerses() {
        List<LemmaDTO> lemmas = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();

        System.out.println("Starting Preview Lemmatization");
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


                        String lemma = extractLemma(r, word);

                        LemmaDTO lemmaDTO = new LemmaDTO(verse.getVerseId(), word, lemma);
                        lemmaDTO.setVerseText(txt);
                        lemmaDTO.setPositionInVerse(position);

                        lemmas.add(lemmaDTO);

                        System.out.println("Word: " + word + " | Lemma: " + lemma);
                    } else {
                        System.out.println("No analysis found for: " + word);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }

                position++;
            }
        }


        System.out.println("Total lemmas generated: " + lemmas.size());

        return lemmas;
    }
    @Override
    public int lemmatizeAndSaveAllVerses() {
        List<LemmaDTO> lemmas = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();

        System.out.println("Starting lemmatization for " + verses.size() + " verses...");

        for (VerseDTO verse : verses) {

            if (isVerseLemmatized(verse.getVerseId())) {
                System.out.println("Verse " + verse.getVerseId() + " already lemmatized, skipping...");
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

                        String lemma = extractLemma(r, word);

                        LemmaDTO lemmaDTO = new LemmaDTO(verse.getVerseId(), word, lemma);
                        lemmaDTO.setPositionInVerse(position);
                        lemmas.add(lemmaDTO);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }
                position++;
            }
        }

        if (!lemmas.isEmpty()) {
            lemmaDAO.createLemmaBatch(lemmas);
            System.out.println("Lemmatization complete: " + lemmas.size() + " lemmas saved to database.");
        }

        return lemmas.size();
    }
    @Override
    public List<LemmaDTO> getAllLemmasFromDatabase() {
        List<LemmaDTO> lemmas = lemmaDAO.getAllLemmas();
        List<VerseDTO> allVerses = verseDAO.getAllVerses();
        
        for (LemmaDTO lemma : lemmas) {
            try {
                for (VerseDTO verse : allVerses) {
                    if (verse.getVerseId() == lemma.getVerseId()) {
                        lemma.setVerseText(verse.getText());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting verse text: " + e.getMessage());
            }
        }
        
        return lemmas;
    }
    @Override
    public List<LemmaDTO> getLemmasByVerseId(int verseId) {
        return lemmaDAO.getLemmasByVerseId(verseId);
    }
    @Override
    public void deleteAllLemmas() {
        lemmaDAO.deleteAllLemmas();
    }
    @Override
    public int getLemmaCount() {
        return lemmaDAO.getLemmaCount();
    }
	@Override
    public boolean isVerseLemmatized(int verseId) {
        return lemmaDAO.lemmaExists(verseId);
    }
    @Override
    public String extractLemma(Result r, String originalWord) {
        try {

            String lemma = r.getLemma();
            if (lemma != null && !lemma.trim().isEmpty()) {
                return lemma;
            }
        } catch (Exception e) {
            System.err.println("getLemma() not available or failed");
        }

        try {

            String voweledForm = r.getVoweledWord();
            if (voweledForm != null && !voweledForm.trim().isEmpty()) {
                return voweledForm;
            }
        } catch (Exception e) {
            System.err.println("getVoweledform() not available or failed");
        }

        try {

            String stem = r.getStem();
            if (stem != null && !stem.trim().isEmpty()) {
                return stem;
            }
        } catch (Exception e) {
            System.err.println("getStem() not available or failed");
        }


        return originalWord;
    }
}