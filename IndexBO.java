package bl;

import java.util.List;
import dal.IIndexDAO;
import dto.IndexResultDTO;

public class IndexBO implements IIndexBO {
    private IIndexDAO indexDAO;

    public IndexBO(IIndexDAO indexDAO) {
        this.indexDAO = indexDAO;
    }

    @Override
    public List<String> getBookTokens(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        return indexDAO.getBookTokens(bookId);
    }

    @Override
    public List<String> getBookLemmas(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        return indexDAO.getBookLemmas(bookId);
    }

    @Override
    public List<String> getBookRoots(int bookId) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        return indexDAO.getBookRoots(bookId);
    }

    @Override
    public List<IndexResultDTO> getTokenOccurrences(int bookId, String token) {
        validateParameters(bookId, token);
        return indexDAO.getTokenOccurrences(bookId, token);
    }

    @Override
    public List<IndexResultDTO> getLemmaOccurrences(int bookId, String lemma) {
        validateParameters(bookId, lemma);
        return indexDAO.getLemmaOccurrences(bookId, lemma);
    }

    @Override
    public List<IndexResultDTO> getRootOccurrences(int bookId, String root) {
        validateParameters(bookId, root);
        return indexDAO.getRootOccurrences(bookId, root);
    }

    @Override
    public List<String> getLemmasByRoot(int bookId, String root) {
        validateParameters(bookId, root);
        return indexDAO.getLemmasByRoot(bookId, root);
    }
    
    @Override
    public List<String> getTokensByLemma(int bookId, String lemma) {
        validateParameters(bookId, lemma);
        return indexDAO.getTokensByLemma(bookId, lemma);
    }
    
    private void validateParameters(int bookId, String word) {
        if (bookId <= 0) {
            throw new IllegalArgumentException("Invalid book ID");
        }
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Word cannot be null or empty");
        }
    }

    }
