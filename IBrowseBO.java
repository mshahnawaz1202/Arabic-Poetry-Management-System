package bl;

import java.util.List;
import dto.BrowseResultDTO;

public interface IBrowseBO {
    List<BrowseResultDTO> browseByLemma(String lemma);
    List<BrowseResultDTO> browseByRoot(String root);
    List<BrowseResultDTO> browseBySegment(String segment);
    List<BrowseResultDTO> browseByToken(String token);
    List<String> getDistinctLemma();
    List<String> getDistinctRoot();
    List<String> getDistinctSegment();
    List<String> getDistinctToken();
}