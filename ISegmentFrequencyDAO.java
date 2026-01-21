package dal;

import java.util.List;
import dto.FrequencyDTO;

public interface ISegmentFrequencyDAO {
    List<FrequencyDTO> getSegmentFrequency();
}
