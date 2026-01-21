package bl;

import dto.ImportResultDTO;
import java.io.File;


/***
 * @author shahn
 */
public interface IImportBookBO {
    ImportResultDTO importBook(File file);
}
