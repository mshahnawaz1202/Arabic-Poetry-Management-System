package bl;

import java.util.List;
import dto.VerseDTO;

public interface IVerseBO {
    void createVerse(VerseDTO verse);

    List<VerseDTO> getVersesByPoemId(int poemId);

    void updateVerse(VerseDTO verse);

    void deleteVerse(int verseId);

    List<VerseDTO> searchExactString(String text);

    List<VerseDTO> searchRegexPattern(String pattern);
}
