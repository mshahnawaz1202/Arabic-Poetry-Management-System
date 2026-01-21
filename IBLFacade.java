package bl;

import bl.services.ILemmaBO;
import bl.services.INGram;
import bl.services.IRootBO;
import bl.services.ISegmentBO;
import bl.services.ITokenBO;
import bl.services.IFrequencyBO;
import dto.BookDTO;

public interface IBLFacade extends IBookBO, IPoetBO, IPoemBO, IVerseBO, IImportBookBO, IBrowseBO, ITokenBO, ILemmaBO,
        IRootBO, ISegmentBO, INGram, IIndexBO, IFrequencyBO {
    int getBookID(BookDTO book);
}