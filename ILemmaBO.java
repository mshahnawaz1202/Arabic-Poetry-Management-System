package bl.services;

import java.util.List;

import dto.LemmaDTO;
import net.oujda_nlp_team.entity.Result;

public interface ILemmaBO {

	List<LemmaDTO> lemmatizeAllVerses();

	int lemmatizeAndSaveAllVerses();

	List<LemmaDTO> getAllLemmasFromDatabase();

	List<LemmaDTO> getLemmasByVerseId(int verseId);

	void deleteAllLemmas();

	int getLemmaCount();
	
	boolean isVerseLemmatized(int verseId);

	String extractLemma(Result r, String originalWord);
}
