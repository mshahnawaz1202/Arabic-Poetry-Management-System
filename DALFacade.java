package dal;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dto.BookDTO;
import dto.BrowseResultDTO;
import dto.IndexResultDTO;
import dto.LemmaDTO;
import dto.PoemDTO;
import dto.PoetDTO;
import dto.RootDTO;
import dto.SegmentDTO;
import dto.TokenDTO;
import dto.VerseDTO;

public class DALFacade implements IDALFacade {
	private static final Logger logger = LogManager.getLogger(DALFacade.class);
	private IBookDAO books;
	private IPoetDAO poets;
	private IPoemDAO poems;
	private IVerseDAO verses;
	private IImportBookDAO importer;
	private IBrowseDAO browseDAO;
	private ITokenDAO tokens;
	private ILemmaDAO lemmas;
	private IRootDAO roots;
	private ISegmentDAO segments;
	private IIndexDAO indexer;

	public DALFacade(IBookDAO books, IPoetDAO poets, IPoemDAO poems, IVerseDAO verses, IImportBookDAO importer,
			IBrowseDAO browseDAO, ITokenDAO tokens, ILemmaDAO lemmas, IRootDAO roots, ISegmentDAO segments,
			IIndexDAO indexer) {
		this.books = books;
		this.poets = poets;
		this.poems = poems;
		this.verses = verses;
		this.importer = importer;
		this.browseDAO = browseDAO;
		this.tokens = tokens;
		this.lemmas = lemmas;
		this.roots = roots;
		this.segments = segments;
		this.indexer = indexer;
	}

	public DALFacade() {
		// TODO Auto-generated constructor stub
	}

	// --- Book methods ---
	@Override
	public void createBook(BookDTO book) {
		books.createBook(book);
	}

	@Override
	public BookDTO getBook(String title) {
		return books.getBook(title);
	}

	@Override
	public List<BookDTO> getAllBooks() {
		return books.getAllBooks();
	}

	@Override
	public void updateBook(String cr, BookDTO book) {
		books.updateBook(cr, book);
	}

	@Override
	public void deleteBook(String title) {
		books.deleteBook(title);
	}

	@Override
	public int getBookID(BookDTO book) {
		return books.getBookID(book);
	}

	// --- Poet methods ---
	@Override
	public void createPoet(PoetDTO poet) {
		poets.createPoet(poet);
	}

	@Override
	public PoetDTO getPoetById(int poetId) {
		return poets.getPoetById(poetId);
	}

	@Override
	public List<PoetDTO> getAllPoets() {
		return poets.getAllPoets();
	}

	@Override
	public void updatePoet(PoetDTO poet) {
		poets.updatePoet(poet);
	}

	@Override
	public void deletePoet(int poetId) {
		poets.deletePoet(poetId);
	}

	// --- Poem methods ---
	@Override
	public void createPoem(PoemDTO poem) {
		poems.createPoem(poem);
	}

	@Override
	public PoemDTO getPoemById(int poemId) {
		return poems.getPoemById(poemId);
	}

	@Override
	public List<PoemDTO> getAllPoems() {
		return poems.getAllPoems();
	}

	@Override
	public void updatePoem(PoemDTO poem) {
		poems.updatePoem(poem);
	}

	@Override
	public void deletePoem(int poemId) {
		poems.deletePoem(poemId);
	}

	// --- Verse methods ---
	@Override
	public void createVerse(VerseDTO verse) {
		verses.createVerse(verse);
	}

	@Override
	public List<VerseDTO> getVersesByPoemId(int poemId) {
		return verses.getVersesByPoemId(poemId);
	}

	@Override
	public void updateVerse(VerseDTO verse) {
		verses.updateVerse(verse);
	}

	@Override
	public void deleteVerse(int verseId) {
		verses.deleteVerse(verseId);
	}

	// --- Import methods ---
	@Override
	public void saveImportedBook(BookDTO book, List<PoemDTO> poemsList, List<VerseDTO> verses) {
		if (importer == null) {
			logger.error("IImportBookDAO (importer) not initialized in DALFacade");
			throw new IllegalStateException("IImportBookDAO (importer) not initialized in DALFacade");
		}
		importer.saveImportedBook(book, poemsList, verses);
	}

	/**
	 * Saves a single poem: delegate to importer implementation which is
	 * responsible for book/poem/verse insertion details.
	 */
	@Override
	public void savePoem(PoemDTO poem, List<String> verseTexts, BookDTO book) {
		if (importer == null) {
			logger.error("IImportBookDAO (importer) not initialized in DALFacade");
			throw new IllegalStateException("IImportBookDAO (importer) not initialized in DALFacade");
		}
		importer.savePoem(poem, verseTexts, book);
	}

	@Override
	public List<VerseDTO> getAllVerses() {
		return verses.getAllVerses();
	}

	// --- Browse methods ---
	@Override
	public List<BrowseResultDTO> browseByLemma(String lemma) {
		return browseDAO.browseByLemma(lemma);
	}

	@Override
	public List<BrowseResultDTO> browseByRoot(String root) {
		return browseDAO.browseByRoot(root);
	}

	@Override
	public List<BrowseResultDTO> browseBySegment(String segment) {
		return browseDAO.browseBySegment(segment);
	}

	@Override
	public List<BrowseResultDTO> browseByToken(String token) {
		return browseDAO.browseByToken(token);
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

	/*-----------------------------------------------------------------------------------------*/
	/***
	 * @author shahn
	 */
	// @Override
	public List<VerseDTO> searchExactString(String searchText) {
		return verses.searchExactString(searchText);
	}

	// @Override
	public List<VerseDTO> searchRegexPattern(String pattern) {
		return verses.searchRegexPattern(pattern);
	}

	@Override
	public void createToken(TokenDTO token) {
		tokens.createToken(token);

	}

	@Override
	public void createTokenBatch(List<TokenDTO> tokens) {
		this.tokens.createTokenBatch(tokens);
	}

	@Override
	public TokenDTO getTokenById(int tokenId) {
		return tokens.getTokenById(tokenId);
	}

	@Override
	public List<TokenDTO> getTokensByVerseId(int verseId) {
		return tokens.getTokensByVerseId(verseId);
	}

	@Override
	public void updateToken(TokenDTO token) {
		tokens.updateToken(token);

	}

	@Override
	public void deleteToken(int tokenId) {
		tokens.deleteToken(tokenId);

	}

	@Override
	public void deleteTokensByVerseId(int verseId) {
		tokens.deleteTokensByVerseId(verseId);

	}

	@Override
	public void deleteAllTokens() {
		tokens.deleteAllTokens();

	}

	@Override
	public int getTokenCount() {
		return tokens.getTokenCount();
	}

	@Override
	public boolean tokenExists(int verseId) {
		return tokens.tokenExists(verseId);
	}

	@Override
	public void createSegment(SegmentDTO segment) {
		segments.createSegment(segment);

	}

	@Override
	public void createSegmentBatch(List<SegmentDTO> segments) {
		this.segments.createSegmentBatch(segments);
	}

	@Override
	public SegmentDTO getSegmentById(int segmentId) {
		return segments.getSegmentById(segmentId);
	}

	@Override
	public List<SegmentDTO> getSegmentsByVerseId(int verseId) {
		return segments.getSegmentsByVerseId(verseId);
	}

	@Override
	public void updateSegment(SegmentDTO segment) {
		segments.updateSegment(segment);

	}

	@Override
	public void deleteSegment(int segmentId) {
		segments.deleteSegment(segmentId);

	}

	@Override
	public void deleteSegmentsByVerseId(int verseId) {
		segments.deleteSegmentsByVerseId(verseId);

	}

	@Override
	public void deleteAllSegments() {
		segments.deleteAllSegments();

	}

	@Override
	public int getSegmentCount() {
		return segments.getSegmentCount();
	}

	@Override
	public boolean segmentExists(int verseId) {
		return segments.segmentExists(verseId);
	}

	@Override
	public void createLemma(LemmaDTO lemma) {
		lemmas.createLemma(lemma);

	}

	@Override
	public void createLemmaBatch(List<LemmaDTO> lemmas) {
		this.lemmas.createLemmaBatch(lemmas);
	}

	@Override
	public LemmaDTO getLemmaById(int lemmaId) {
		return lemmas.getLemmaById(lemmaId);
	}

	@Override
	public List<LemmaDTO> getLemmasByVerseId(int verseId) {
		return lemmas.getLemmasByVerseId(verseId);
	}

	@Override
	public void updateLemma(LemmaDTO lemma) {
		lemmas.updateLemma(lemma);

	}

	@Override
	public void deleteLemma(int lemmaId) {
		lemmas.deleteLemma(lemmaId);

	}

	@Override
	public void deleteLemmasByVerseId(int verseId) {
		lemmas.deleteLemmasByVerseId(verseId);

	}

	@Override
	public void deleteAllLemmas() {
		lemmas.deleteAllLemmas();

	}

	@Override
	public int getLemmaCount() {
		return lemmas.getLemmaCount();
	}

	@Override
	public boolean lemmaExists(int verseId) {
		return lemmas.lemmaExists(verseId);
	}

	@Override
	public void createRoot(RootDTO root) {
		roots.createRoot(root);

	}

	@Override
	public void createRootBatch(List<RootDTO> roots) {
		this.roots.createRootBatch(roots);
	}

	@Override
	public RootDTO getRootById(int rootId) {
		return roots.getRootById(rootId);
	}

	@Override
	public List<RootDTO> getRootsByVerseId(int verseId) {
		return roots.getRootsByVerseId(verseId);
	}

	@Override
	public void updateRoot(RootDTO root) {
		roots.updateRoot(root);

	}

	@Override
	public void deleteRoot(int rootId) {
		roots.deleteRoot(rootId);

	}

	@Override
	public void deleteRootsByVerseId(int verseId) {
		roots.deleteRootsByVerseId(verseId);

	}

	@Override
	public void deleteAllRoots() {
		roots.deleteAllRoots();

	}

	@Override
	public int getRootCount() {
		return roots.getRootCount();
	}

	@Override
	public boolean rootExists(int verseId) {
		return roots.rootExists(verseId);
	}

	@Override
	public List<TokenDTO> getAllTokens() {
		return tokens.getAllTokens();
	}

	@Override
	public List<SegmentDTO> getAllSegments() {

		return segments.getAllSegments();
	}

	@Override
	public List<LemmaDTO> getAllLemmas() {
		return lemmas.getAllLemmas();
	}

	@Override
	public List<RootDTO> getAllRoots() {
		return roots.getAllRoots();
	}

	@Override
	public List<String> getBookTokens(int bookId) {
		return indexer.getBookTokens(bookId);
	}

	@Override
	public List<String> getBookLemmas(int bookId) {
		return indexer.getBookLemmas(bookId);
	}

	@Override
	public List<String> getBookRoots(int bookId) {
		return indexer.getBookRoots(bookId);
	}

	@Override
	public List<IndexResultDTO> getTokenOccurrences(int bookId, String token) {
		return indexer.getTokenOccurrences(bookId, token);
	}

	@Override
	public List<IndexResultDTO> getLemmaOccurrences(int bookId, String lemma) {
		return indexer.getLemmaOccurrences(bookId, lemma);
	}

	@Override
	public List<IndexResultDTO> getRootOccurrences(int bookId, String root) {
		return indexer.getRootOccurrences(bookId, root);
	}

	@Override
	public List<String> getLemmasByRoot(int bookId, String root) {
		return indexer.getLemmasByRoot(bookId, root);
	}

	@Override
	public List<String> getTokensByLemma(int bookId, String lemma) {
		return indexer.getTokensByLemma(bookId, lemma);
	}
}
