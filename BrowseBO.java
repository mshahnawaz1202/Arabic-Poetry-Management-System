package bl;

import java.util.List;
import dal.IBrowseDAO;
import dto.BrowseResultDTO;

public class BrowseBO implements IBrowseBO {
    private IBrowseDAO browseDAO;

    public BrowseBO(IBrowseDAO browseDAO) {
        this.browseDAO = browseDAO;
    }

    @Override
    public List<BrowseResultDTO> browseByLemma(String lemma) {
        if (lemma == null || lemma.trim().isEmpty()) {
            throw new IllegalArgumentException("Lemma cannot be null or empty");
        }
        return browseDAO.browseByLemma(lemma.trim());
    }

    @Override
    public List<BrowseResultDTO> browseByRoot(String root) {
        if (root == null || root.trim().isEmpty()) {
            throw new IllegalArgumentException("Root cannot be null or empty");
        }
        return browseDAO.browseByRoot(root.trim());
    }

    @Override
    public List<BrowseResultDTO> browseBySegment(String segment) {
        if (segment == null || segment.trim().isEmpty()) {
            throw new IllegalArgumentException("Segment cannot be null or empty");
        }
        return browseDAO.browseBySegment(segment.trim());
    }

    @Override
    public List<BrowseResultDTO> browseByToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        return browseDAO.browseByToken(token.trim());
    }

    @Override
    public List<String> getDistinctLemma() {
        return browseDAO.getDistinctLemma();
    }

    @Override
    public List<String> getDistinctRoot() {
        return browseDAO.getDistinctRoot();
    }

    @Override
    public List<String> getDistinctSegment() {
        return browseDAO.getDistinctSegment();
    }

    @Override
    public List<String> getDistinctToken() {
        return browseDAO.getDistinctToken();
    }
}