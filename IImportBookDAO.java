package dal;

import java.util.List;
import dto.BookDTO;
import dto.PoemDTO;
import dto.VerseDTO;

/***
 * @author shahn
 */

public interface IImportBookDAO {
    void saveImportedBook(BookDTO book, List<PoemDTO> poems, List<VerseDTO> verses);
    void savePoem(PoemDTO poem, List<String> verses, BookDTO book);
}
