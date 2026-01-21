package dal;

import java.util.List;
import dto.RootDTO;

public interface IRootDAO {
    void createRoot(RootDTO root);
    void createRootBatch(List<RootDTO> roots);
    RootDTO getRootById(int rootId);
    List<RootDTO> getRootsByVerseId(int verseId);
    List<RootDTO> getAllRoots();
    void updateRoot(RootDTO root);
    void deleteRoot(int rootId);
    void deleteRootsByVerseId(int verseId);
    void deleteAllRoots();
    int getRootCount();
    boolean rootExists(int verseId);
}