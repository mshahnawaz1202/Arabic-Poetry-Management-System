package bl;

import dal.ILemmaFrequencyDAO;
import dal.IRootFrequencyDAO;
import dal.ISegmentFrequencyDAO;
import dal.ITokenFrequencyDAO;
import dal.LemmaFrequencyDAO;
import dal.RootFrequencyDAO;
import dal.SegmentFrequencyDAO;
import dal.TokenFrequencyDAO;
import dto.FrequencyDTO;

import java.util.List;

/***
 * @author shahn
 */
public class FrequencyBO implements bl.services.IFrequencyBO {

    private final ILemmaFrequencyDAO lemmaDAO;
    private final IRootFrequencyDAO rootDAO;
    private final ISegmentFrequencyDAO segmentDAO;
    private final ITokenFrequencyDAO tokenDAO;

    public FrequencyBO() {
        this.lemmaDAO = new LemmaFrequencyDAO();
        this.rootDAO = new RootFrequencyDAO();
        this.segmentDAO = new SegmentFrequencyDAO();
        this.tokenDAO = new TokenFrequencyDAO();
    }

    public FrequencyBO(ILemmaFrequencyDAO lemmaDAO, IRootFrequencyDAO rootDAO, ISegmentFrequencyDAO segmentDAO,
            ITokenFrequencyDAO tokenDAO) {
        this.lemmaDAO = lemmaDAO;
        this.rootDAO = rootDAO;
        this.segmentDAO = segmentDAO;
        this.tokenDAO = tokenDAO;
    }

    /**
     * Lemma frequency list
     */
    public List<FrequencyDTO> getLemmaFrequencies() {
        return lemmaDAO.getLemmaFrequency();
    }

    /**
     * Root frequency list
     */
    public List<FrequencyDTO> getRootFrequencies() {
        return rootDAO.getRootFrequency();
    }

    /**
     * Segment frequency list
     */
    public List<FrequencyDTO> getSegmentFrequencies() {
        return segmentDAO.getSegmentFrequency();
    }

    /**
     * Token frequency list
     */
    public List<FrequencyDTO> getTokenFrequencies() {
        return tokenDAO.getTokenFrequency();
    }
}
