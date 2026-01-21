package bl;

import java.util.List;
import dal.IDALFacade;
import dto.PoetDTO;

public class PoetBO implements IPoetBO {
    private IDALFacade dal;

    public PoetBO(IDALFacade dal) { this.dal = dal; }

    @Override
    public void createPoet(PoetDTO poet) {
        if (poet == null || poet.getPoetName() == null || poet.getPoetName().trim().isEmpty())
            throw new IllegalArgumentException("Poet name cannot be empty");
        if (poet.getPoetName().length() > 100)
            throw new IllegalArgumentException("Poet name cannot exceed 100 characters");
        dal.createPoet(poet);
    }

    @Override
    public PoetDTO getPoetById(int id) {
        PoetDTO p = dal.getPoetById(id);
        if (p == null) throw new IllegalArgumentException("Poet with id " + id + " not found");
        return p;
    }

    @Override
    public List<PoetDTO> getAllPoets() {
        return dal.getAllPoets();
    }

    @Override
    public void updatePoet(PoetDTO poet) {
        if (poet == null || poet.getPoetId() <= 0) throw new IllegalArgumentException("Invalid poet id");
        dal.updatePoet(poet);
    }

    @Override
    public void deletePoet(int id) {
        dal.deletePoet(id);
    }
}
