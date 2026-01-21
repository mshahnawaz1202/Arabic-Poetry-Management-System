package dal;

import java.util.List;

import dto.VerseDTO;

public interface IVerseDAO {
    void createVerse(VerseDTO verse);
    List<VerseDTO> getVersesByPoemId(int poemId);
    void updateVerse(VerseDTO verse);
    void deleteVerse(int verseId);
    public List<VerseDTO> getAllVerses();
    /***
     * @author shahn
     */
//    List<VerseDTO>searchExactString(String text);
//    List<VerseDTO>searchRegexPattern(String patt);
	List<VerseDTO> searchExactString(String searchText);
	List<VerseDTO> searchRegexPattern(String pattern);
}
