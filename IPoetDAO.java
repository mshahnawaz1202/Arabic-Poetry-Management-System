package dal;

import java.util.List;
import dto.PoetDTO;

public interface IPoetDAO {
    void createPoet(PoetDTO poet);
    PoetDTO getPoetById(int poetId);
    List<PoetDTO> getAllPoets();
    void updatePoet(PoetDTO poet);
    void deletePoet(int poetId);
}
