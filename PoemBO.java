package bl;

import java.util.List;
import dal.IDALFacade;
import dto.PoemDTO;

public class PoemBO implements IPoemBO {
    private IDALFacade dal;

    public PoemBO(IDALFacade dal) { this.dal = dal; }

    @Override
    public void createPoem(PoemDTO poem) {
        if (poem == null || poem.getTitle() == null || poem.getTitle().trim().isEmpty())
            throw new IllegalArgumentException("Poem title cannot be empty");
        if (poem.getTitle().length() > 100)
            throw new IllegalArgumentException("Poem title cannot exceed 100 characters");
        dal.createPoem(poem);
    }

    @Override
    public PoemDTO getPoemById(int id) {
        PoemDTO p = dal.getPoemById(id);
        if (p == null) throw new IllegalArgumentException("Poem with id " + id + " not found");
        return p;
    }

    @Override
    public List<PoemDTO> getAllPoems() {
        return dal.getAllPoems();
    }

    @Override
    public void updatePoem(PoemDTO poem) {
        if (poem == null || poem.getPoemId() <= 0) throw new IllegalArgumentException("Invalid poem id");
        dal.updatePoem(poem);
    }

    @Override
    public void deletePoem(int id) {
        dal.deletePoem(id);
    }
}
