package bl;

import java.io.File;
import java.util.List;

import bl.services.ILemmaBO;
import bl.services.INGram;
import bl.services.IRootBO;
import bl.services.ISegmentBO;
import bl.services.ITokenBO;
import bl.services.IFrequencyBO;
import bl.services.NGram.NGramResult;
import dal.IDALFacade;
import dto.BookDTO;
import dto.BrowseResultDTO;
import dto.ImportResultDTO;
import dto.IndexResultDTO;
import dto.LemmaDTO;
import dto.PoemDTO;
import dto.PoetDTO;
import dto.RootDTO;
import dto.SegmentDTO;
import dto.TokenDTO;
import dto.VerseDTO;
import net.oujda_nlp_team.entity.Result;

public class BLFacade implements IBLFacade {
	private IBookBO books;
	private IPoetBO poets;
	private IPoemBO poems;
	private IVerseBO verses;
	private IImportBookBO importer;
	private IBrowseBO browseBO;
	private ITokenBO tokens;
	private ILemmaBO lemmas;
	private IRootBO roots;
	private ISegmentBO segments;
	private INGram ngrams;
	private IIndexBO indexer;
	private IDALFacade dalfacade;
	private IFrequencyBO frequencies;

	// New constructor: accept importer BO
	public BLFacade(IBookBO books, IPoetBO poets, IPoemBO poems, IVerseBO verses, IImportBookBO importer,
			IBrowseBO browseBO, ITokenBO tokens, ILemmaBO lemmas, IRootBO roots, ISegmentBO segments, INGram ngrams,
			IIndexBO indexer, IFrequencyBO frequencies, IDALFacade dalfacade) {
		this.books = books;
		this.poets = poets;
		this.poems = poems;
		this.verses = verses;
		this.importer = importer;
		this.browseBO = browseBO;
		this.tokens = tokens;
		this.lemmas = lemmas;
		this.roots = roots;
		this.segments = segments;
		this.ngrams = ngrams;
		this.indexer = indexer;
		this.frequencies = frequencies;
		this.dalfacade = dalfacade;
	}

	public BLFacade(IDALFacade dalFacade) {
		this.dalfacade = dalFacade;
	}

	public IDALFacade getDalFacade() {
		return getDalfacade();
	}

	public BLFacade() {
		// default empty constructor
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
	public void updateBook(String currentTitle, BookDTO book) {
		books.updateBook(currentTitle, book);
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
	public PoetDTO getPoetById(int id) {
		return poets.getPoetById(id);
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
	public void deletePoet(int id) {
		poets.deletePoet(id);
	}

	// --- Poem methods ---
	@Override
	public void createPoem(PoemDTO poem) {
		poems.createPoem(poem);
	}

	@Override
	public PoemDTO getPoemById(int id) {
		return poems.getPoemById(id);
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
	public void deletePoem(int id) {
		poems.deletePoem(id);
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

	@Override
	public List<VerseDTO> searchExactString(String text) {
		return verses.searchExactString(text);
	}

	@Override
	public List<VerseDTO> searchRegexPattern(String pattern) {
		return verses.searchRegexPattern(pattern);
	}

	// --- Import methods ---
	@Override
	public ImportResultDTO importBook(File file) {
		if (importer == null) {
			throw new IllegalStateException("ImportBookBO (importer) not initialized in BLFacade");
		}
		return importer.importBook(file);
	}

	// --- Browse methods ---
	@Override
	public List<BrowseResultDTO> browseByLemma(String lemma) {
		return browseBO.browseByLemma(lemma);
	}

	@Override
	public List<BrowseResultDTO> browseByRoot(String root) {
		return browseBO.browseByRoot(root);
	}

	@Override
	public List<BrowseResultDTO> browseBySegment(String segment) {
		return browseBO.browseBySegment(segment);
	}

	@Override
	public List<BrowseResultDTO> browseByToken(String token) {
		return browseBO.browseByToken(token);
	}

	@Override
	public List<String> getDistinctLemma() {
		return browseBO.getDistinctLemma();
	}

	@Override
	public List<String> getDistinctRoot() {
		return browseBO.getDistinctRoot();
	}

	@Override
	public List<String> getDistinctSegment() {
		return browseBO.getDistinctSegment();
	}

	@Override
	public List<String> getDistinctToken() {
		return browseBO.getDistinctToken();
	}

	@Override
	public List<TokenDTO> tokenizeAllVerses() {
		return tokens.tokenizeAllVerses();
	}

	@Override
	public int tokenizeAndSaveAllVerses() {
		return tokens.tokenizeAndSaveAllVerses();
	}

	@Override
	public List<TokenDTO> getAllTokensFromDatabase() {
		return tokens.getAllTokensFromDatabase();
	}

	@Override
	public List<TokenDTO> getTokensByVerseId(int verseId) {
		return tokens.getTokensByVerseId(verseId);
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
	public List<LemmaDTO> lemmatizeAllVerses() {
		return lemmas.lemmatizeAllVerses();
	}

	@Override
	public int lemmatizeAndSaveAllVerses() {
		return lemmas.lemmatizeAndSaveAllVerses();
	}

	@Override
	public List<LemmaDTO> getAllLemmasFromDatabase() {
		return lemmas.getAllLemmasFromDatabase();
	}

	@Override
	public List<LemmaDTO> getLemmasByVerseId(int verseId) {
		return lemmas.getLemmasByVerseId(verseId);
	}

	@Override
	public void deleteAllLemmas() {
		lemmas.deleteAllLemmas();

	}

	@Override
	public int getLemmaCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isVerseLemmatized(int verseId) {
		return lemmas.isVerseLemmatized(verseId);
	}

	@Override
	public String extractLemma(Result r, String originalWord) {
		return lemmas.extractLemma(r, originalWord);
	}

	@Override
	public List<RootDTO> extractRootsAllVerses() {
		return roots.extractRootsAllVerses();
	}

	@Override
	public int extractAndSaveAllRoots() {
		return roots.extractAndSaveAllRoots();
	}

	@Override
	public List<RootDTO> getAllRootsFromDatabase() {
		return roots.getAllRootsFromDatabase();
	}

	@Override
	public List<RootDTO> getRootsByVerseId(int verseId) {
		return roots.getRootsByVerseId(verseId);
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
	public List<SegmentDTO> segmentAllVerses() {
		return segments.segmentAllVerses();
	}

	@Override
	public int segmentAndSaveAllVerses() {
		return segments.segmentAndSaveAllVerses();
	}

	@Override
	public List<SegmentDTO> getAllSegmentsFromDatabase() {
		return segments.getAllSegmentsFromDatabase();
	}

	@Override
	public List<SegmentDTO> getSegmentsByVerseId(int verseId) {
		return segments.getSegmentsByVerseId(verseId);
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
	public boolean isVerseSegmented(int verseId) {
		return segments.isVerseSegmented(verseId);
	}

	@Override
	public List<String> generateNGrams(String text, int n) {
		return ngrams.generateNGrams(text, n);
	}

	@Override
	public int intersectionCount(List<String> gramsA, List<String> gramsB) {
		return ngrams.intersectionCount(gramsA, gramsB);
	}

	@Override
	public List<String> splitWords(String text) {
		return ngrams.splitWords(text);
	}

	@Override
	public int countCommonWords(List<String> wordsA, List<String> wordsB) {
		return ngrams.countCommonWords(wordsA, wordsB);
	}

	@Override
	public double calculateSimilarity(String text1, String text2, int n) {
		return ngrams.calculateSimilarity(text1, text2, n);
	}

	@Override
	public List<NGramResult> findSimilarVerses(String targetText, int n, double minSimilarity) {
		return ngrams.findSimilarVerses(targetText, n, minSimilarity);
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

	public IDALFacade getDalfacade() {
		return dalfacade;
	}

	public void setDalfacade(IDALFacade dalfacade) {
		this.dalfacade = dalfacade;
	}

	@Override
	public List<dto.FrequencyDTO> getLemmaFrequencies() {
		return frequencies.getLemmaFrequencies();
	}

	@Override
	public List<dto.FrequencyDTO> getRootFrequencies() {
		return frequencies.getRootFrequencies();
	}

	@Override
	public List<dto.FrequencyDTO> getSegmentFrequencies() {
		return frequencies.getSegmentFrequencies();
	}

	@Override
	public List<dto.FrequencyDTO> getTokenFrequencies() {
		return frequencies.getTokenFrequencies();
	}
}