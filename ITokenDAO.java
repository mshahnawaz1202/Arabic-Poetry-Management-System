package dal;

import java.util.List;
import dto.TokenDTO;

public interface ITokenDAO {
    void createToken(TokenDTO token);
    void createTokenBatch(List<TokenDTO> tokens);
    TokenDTO getTokenById(int tokenId);
    List<TokenDTO> getTokensByVerseId(int verseId);
    List<TokenDTO> getAllTokens();
    void updateToken(TokenDTO token);
    void deleteToken(int tokenId);
    void deleteTokensByVerseId(int verseId);
    void deleteAllTokens();
    int getTokenCount();
    boolean tokenExists(int verseId);
}