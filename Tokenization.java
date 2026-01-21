package bl.services;

import java.util.ArrayList;
import java.util.List;

import dal.ITokenDAO;
import dal.IVerseDAO;
import dal.VerseDAO;
import dto.TokenDTO;
import dto.VerseDTO;
import net.oujda_nlp_team.AlKhalil2Analyzer;
import net.oujda_nlp_team.entity.Result;
import net.oujda_nlp_team.entity.ResultList;

public class Tokenization implements ITokenBO {

	private final IVerseDAO verseDAO;
    private final ITokenDAO tokenDAO;

    public Tokenization(IVerseDAO verseDAO, ITokenDAO tokenDAO) {
        this.verseDAO = verseDAO;
        this.tokenDAO = tokenDAO;
    }
    @Override
    public List<TokenDTO> tokenizeAllVerses() {
        List<TokenDTO> tokens = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();


        System.out.println("Total verses: "+verses.size());

        for (VerseDTO verse : verses) {
            String txt = verse.getText();

            System.out.println(verse.getVerseId());
            System.out.println("/n" + verse.getVerseNo() + "/n");
            System.out.println(txt);

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
                        Result r = results.getAllResults().get(0); // First result

                        TokenDTO token = new TokenDTO(
                            verse.getVerseId(),
                            word,
                            r.getRoot()
                        );
                        token.setVerseText(txt);
                        token.setPositionInVerse(position);

                        tokens.add(token);

                        System.out.println("Token: " + word + " | Root: " + r.getRoot());
                    } else {
                        System.out.println("No analysis found for: " + word);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }

                position++;
            }

        }

        System.out.println("Total tokens generated: " + tokens.size());

        return tokens;
    }


    @Override
    public int tokenizeAndSaveAllVerses() {
        List<TokenDTO> tokens = new ArrayList<>();
        List<VerseDTO> verses = verseDAO.getAllVerses();

        System.out.println("Starting tokenization for " + verses.size() + " verses...");

        for (VerseDTO verse : verses) {

            if (tokenDAO.tokenExists(verse.getVerseId())) {
                System.out.println("Verse " + verse.getVerseId() + " already tokenized, skipping...");
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
                        TokenDTO token = new TokenDTO(verse.getVerseId(), word, r.getRoot());
                        token.setPositionInVerse(position);
                        tokens.add(token);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing word '" + word + "': " + e.getMessage());
                }
                position++;
            }
        }

        if (!tokens.isEmpty()) {
            tokenDAO.createTokenBatch(tokens);
            System.out.println("Tokenization complete: " + tokens.size() + " tokens saved to database.");
        }

        return tokens.size();
    }

    @Override
    public List<TokenDTO> getAllTokensFromDatabase() {
        List<TokenDTO> tokens = tokenDAO.getAllTokens();
        VerseDAO verseDAO = new VerseDAO();
        

        List<VerseDTO> allVerses = verseDAO.getAllVerses();
        
        for (TokenDTO token : tokens) {
            try {

                for (VerseDTO verse : allVerses) {
                    if (verse.getVerseId() == token.getVerseId()) {
                        token.setVerseText(verse.getText());
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error getting verse text: " + e.getMessage());
            }
        }
        
        return tokens;
    }

    @Override
    public List<TokenDTO> getTokensByVerseId(int verseId) {
        return tokenDAO.getTokensByVerseId(verseId);
    }
    @Override
    public void deleteAllTokens() {
        tokenDAO.deleteAllTokens();
    }

    @Override
    public int getTokenCount() {
        return tokenDAO.getTokenCount();
    }
}