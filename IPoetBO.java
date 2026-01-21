package bl;

import java.util.List;
import dto.PoetDTO;

public interface IPoetBO {
    void createPoet(PoetDTO poet);
    PoetDTO getPoetById(int id);
    List<PoetDTO> getAllPoets();
    void updatePoet(PoetDTO poet);
    void deletePoet(int id);
}
