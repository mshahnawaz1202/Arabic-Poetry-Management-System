package dal;

import java.util.List;

import dto.IndexResultDTO;

public interface IIndexDAO {
    List<String> getBookTokens(int bookId);
    List<String> getBookLemmas(int bookId);
    List<String> getBookRoots(int bookId);
    List<IndexResultDTO> getTokenOccurrences(int bookId, String token);
    List<IndexResultDTO> getLemmaOccurrences(int bookId, String lemma);
    List<IndexResultDTO> getRootOccurrences(int bookId, String root);
    
    // Add hierarchical filtering methods
    List<String> getLemmasByRoot(int bookId, String root);
    List<String> getTokensByLemma(int bookId, String lemma);
}