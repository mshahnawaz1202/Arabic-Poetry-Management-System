package dal;

import java.util.List;
import dto.LemmaDTO;

public interface ILemmaDAO {
    void createLemma(LemmaDTO lemma);
    void createLemmaBatch(List<LemmaDTO> lemmas);
    LemmaDTO getLemmaById(int lemmaId);
    List<LemmaDTO> getLemmasByVerseId(int verseId);
    List<LemmaDTO> getAllLemmas();
    void updateLemma(LemmaDTO lemma);
    void deleteLemma(int lemmaId);
    void deleteLemmasByVerseId(int verseId);
    void deleteAllLemmas();
    int getLemmaCount();
    boolean lemmaExists(int verseId);
}