package bl.services;

import dto.FrequencyDTO;
import java.util.List;

public interface IFrequencyBO {
    List<FrequencyDTO> getLemmaFrequencies();

    List<FrequencyDTO> getRootFrequencies();

    List<FrequencyDTO> getSegmentFrequencies();

    List<FrequencyDTO> getTokenFrequencies();
}
