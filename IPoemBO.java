package bl;

import java.util.List;
import dto.PoemDTO;

public interface IPoemBO {
    void createPoem(PoemDTO poem);
    PoemDTO getPoemById(int id);
    List<PoemDTO> getAllPoems();
    void updatePoem(PoemDTO poem);
    void deletePoem(int id);
}
