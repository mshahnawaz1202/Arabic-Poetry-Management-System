package bl;

import java.util.List;
import dal.IDALFacade;
import dto.VerseDTO;

public class VerseBO implements IVerseBO {
    private IDALFacade dal;

    public VerseBO(IDALFacade dal) {
        this.dal = dal;
    }

    @Override
    public void createVerse(VerseDTO verse) {
        if (verse == null || verse.getText() == null || verse.getText().trim().isEmpty())
            throw new IllegalArgumentException("Verse text cannot be empty");
        dal.createVerse(verse);
    }

    @Override
    public List<VerseDTO> getVersesByPoemId(int poemId) {
        return dal.getVersesByPoemId(poemId);
    }

    @Override
    public void updateVerse(VerseDTO verse) {
        if (verse == null || verse.getVerseId() <= 0)
            throw new IllegalArgumentException("Invalid verse id");
        dal.updateVerse(verse);
    }

    @Override
    public void deleteVerse(int verseId) {
        dal.deleteVerse(verseId);
    }

    @Override
    public List<VerseDTO> searchExactString(String text) {
        return dal.searchExactString(text);
    }

    @Override
    public List<VerseDTO> searchRegexPattern(String pattern) {
        return dal.searchRegexPattern(pattern);
    }
}
