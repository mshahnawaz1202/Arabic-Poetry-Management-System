package dal;

import java.util.List;
import dto.FrequencyDTO;

public interface ITokenFrequencyDAO {
    List<FrequencyDTO> getTokenFrequency();
}
