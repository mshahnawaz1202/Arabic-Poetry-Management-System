package bl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dal.IIndexDAO;
import dto.IndexResultDTO;

public class IndexBOTest {

    @Mock
    private IIndexDAO indexDAOMock;

    private IndexBO indexBO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        indexBO = new IndexBO(indexDAOMock);
    }

    @Test
    public void testGetBookTokens_Success() {
        int bookId = 1;
        List<String> tokens = Arrays.asList("token1", "token2");
        when(indexDAOMock.getBookTokens(bookId)).thenReturn(tokens);

        List<String> result = indexBO.getBookTokens(bookId);

        assertEquals(tokens, result);
        verify(indexDAOMock, times(1)).getBookTokens(bookId);
    }

    @Test
    public void testGetBookTokens_InvalidBookId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            indexBO.getBookTokens(0);
        });
        assertEquals("Invalid book ID", ex.getMessage());
        verify(indexDAOMock, never()).getBookTokens(anyInt());
    }

    @Test
    public void testGetBookLemmas_Success() {
        int bookId = 2;
        List<String> lemmas = Arrays.asList("lemma1", "lemma2");
        when(indexDAOMock.getBookLemmas(bookId)).thenReturn(lemmas);

        List<String> result = indexBO.getBookLemmas(bookId);

        assertEquals(lemmas, result);
        verify(indexDAOMock, times(1)).getBookLemmas(bookId);
    }

    @Test
    public void testGetBookRoots_Success() {
        int bookId = 3;
        List<String> roots = Arrays.asList("root1", "root2");
        when(indexDAOMock.getBookRoots(bookId)).thenReturn(roots);

        List<String> result = indexBO.getBookRoots(bookId);

        assertEquals(roots, result);
        verify(indexDAOMock, times(1)).getBookRoots(bookId);
    }

    @Test
    public void testGetTokenOccurrences_Success() {
        int bookId = 1;
        String token = "hello";
        List<IndexResultDTO> occurrences = Collections.singletonList(new IndexResultDTO());
        when(indexDAOMock.getTokenOccurrences(bookId, token)).thenReturn(occurrences);

        List<IndexResultDTO> result = indexBO.getTokenOccurrences(bookId, token);

        assertEquals(occurrences, result);
        verify(indexDAOMock, times(1)).getTokenOccurrences(bookId, token);
    }

    @Test
    public void testGetTokenOccurrences_InvalidBookId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            indexBO.getTokenOccurrences(0, "token");
        });
        assertEquals("Invalid book ID", ex.getMessage());
        verify(indexDAOMock, never()).getTokenOccurrences(anyInt(), anyString());
    }

    @Test
    public void testGetTokenOccurrences_NullOrEmptyWord() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () -> {
            indexBO.getTokenOccurrences(1, null);
        });
        assertEquals("Word cannot be null or empty", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () -> {
            indexBO.getTokenOccurrences(1, "  ");
        });
        assertEquals("Word cannot be null or empty", ex2.getMessage());
    }

    @Test
    public void testGetLemmaOccurrences_Success() {
        int bookId = 1;
        String lemma = "lemma";
        List<IndexResultDTO> occurrences = Collections.singletonList(new IndexResultDTO());
        when(indexDAOMock.getLemmaOccurrences(bookId, lemma)).thenReturn(occurrences);

        List<IndexResultDTO> result = indexBO.getLemmaOccurrences(bookId, lemma);

        assertEquals(occurrences, result);
        verify(indexDAOMock, times(1)).getLemmaOccurrences(bookId, lemma);
    }

    @Test
    public void testGetRootOccurrences_Success() {
        int bookId = 1;
        String root = "root";
        List<IndexResultDTO> occurrences = Collections.singletonList(new IndexResultDTO());
        when(indexDAOMock.getRootOccurrences(bookId, root)).thenReturn(occurrences);

        List<IndexResultDTO> result = indexBO.getRootOccurrences(bookId, root);

        assertEquals(occurrences, result);
        verify(indexDAOMock, times(1)).getRootOccurrences(bookId, root);
    }

    @Test
    public void testGetLemmasByRoot_Success() {
        int bookId = 1;
        String root = "root";
        List<String> lemmas = Arrays.asList("lemma1", "lemma2");
        when(indexDAOMock.getLemmasByRoot(bookId, root)).thenReturn(lemmas);

        List<String> result = indexBO.getLemmasByRoot(bookId, root);

        assertEquals(lemmas, result);
        verify(indexDAOMock, times(1)).getLemmasByRoot(bookId, root);
    }

    @Test
    public void testGetTokensByLemma_Success() {
        int bookId = 1;
        String lemma = "lemma";
        List<String> tokens = Arrays.asList("token1", "token2");
        when(indexDAOMock.getTokensByLemma(bookId, lemma)).thenReturn(tokens);

        List<String> result = indexBO.getTokensByLemma(bookId, lemma);

        assertEquals(tokens, result);
        verify(indexDAOMock, times(1)).getTokensByLemma(bookId, lemma);
    }
}
