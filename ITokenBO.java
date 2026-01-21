package bl.services;

import java.util.List;

import dto.TokenDTO;

public interface ITokenBO {

	List<TokenDTO> tokenizeAllVerses();

	int tokenizeAndSaveAllVerses();

	List<TokenDTO> getAllTokensFromDatabase();

	List<TokenDTO> getTokensByVerseId(int verseId);

	void deleteAllTokens();

	int getTokenCount();

}
