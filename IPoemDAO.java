package dal;

import java.util.List;
import dto.PoemDTO;

public interface IPoemDAO {
    void createPoem(PoemDTO poem);
    PoemDTO getPoemById(int poemId);
    List<PoemDTO> getAllPoems();
    void updatePoem(PoemDTO poem);
    void deletePoem(int poemId);
}
