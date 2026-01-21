package bl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dal.IBrowseDAO;
import dto.BrowseResultDTO;

public class BrowseBOTest {

    @Mock
    private IBrowseDAO browseDAOMock;

    private BrowseBO browseBO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        browseBO = new BrowseBO(browseDAOMock);
    }

    @Test
    public void testBrowseByLemma_Success() {
        String lemma = "testLemma";
        List<BrowseResultDTO> expected = Arrays.asList(new BrowseResultDTO());
        when(browseDAOMock.browseByLemma(lemma)).thenReturn(expected);

        List<BrowseResultDTO> result = browseBO.browseByLemma(lemma);

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).browseByLemma(lemma);
    }

    @Test
    public void testBrowseByLemma_NullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseByLemma(null));
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseByLemma(""));
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseByLemma("   "));
        verify(browseDAOMock, never()).browseByLemma(anyString());
    }

    @Test
    public void testBrowseByRoot_Success() {
        String root = "calcRoot";
        List<BrowseResultDTO> expected = Arrays.asList(new BrowseResultDTO());
        when(browseDAOMock.browseByRoot(root)).thenReturn(expected);

        List<BrowseResultDTO> result = browseBO.browseByRoot(root);

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).browseByRoot(root);
    }

    @Test
    public void testBrowseByRoot_NullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseByRoot(null));
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseByRoot(""));
        verify(browseDAOMock, never()).browseByRoot(anyString());
    }

    @Test
    public void testBrowseBySegment_Success() {
        String segment = "seg";
        List<BrowseResultDTO> expected = Arrays.asList(new BrowseResultDTO());
        when(browseDAOMock.browseBySegment(segment)).thenReturn(expected);

        List<BrowseResultDTO> result = browseBO.browseBySegment(segment);

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).browseBySegment(segment);
    }

    @Test
    public void testBrowseBySegment_NullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseBySegment(null));
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseBySegment(""));
        verify(browseDAOMock, never()).browseBySegment(anyString());
    }

    @Test
    public void testBrowseByToken_Success() {
        String token = "tok";
        List<BrowseResultDTO> expected = Arrays.asList(new BrowseResultDTO());
        when(browseDAOMock.browseByToken(token)).thenReturn(expected);

        List<BrowseResultDTO> result = browseBO.browseByToken(token);

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).browseByToken(token);
    }

    @Test
    public void testBrowseByToken_NullOrEmpty() {
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseByToken(null));
        assertThrows(IllegalArgumentException.class, () -> browseBO.browseByToken(""));
        verify(browseDAOMock, never()).browseByToken(anyString());
    }

    @Test
    public void testGetDistinctLemma() {
        List<String> expected = Arrays.asList("l1", "l2");
        when(browseDAOMock.getDistinctLemma()).thenReturn(expected);

        List<String> result = browseBO.getDistinctLemma();

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).getDistinctLemma();
    }

    @Test
    public void testGetDistinctRoot() {
        List<String> expected = Arrays.asList("r1", "r2");
        when(browseDAOMock.getDistinctRoot()).thenReturn(expected);

        List<String> result = browseBO.getDistinctRoot();

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).getDistinctRoot();
    }

    @Test
    public void testGetDistinctSegment() {
        List<String> expected = Arrays.asList("s1", "s2");
        when(browseDAOMock.getDistinctSegment()).thenReturn(expected);

        List<String> result = browseBO.getDistinctSegment();

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).getDistinctSegment();
    }

    @Test
    public void testGetDistinctToken() {
        List<String> expected = Arrays.asList("t1", "t2");
        when(browseDAOMock.getDistinctToken()).thenReturn(expected);

        List<String> result = browseBO.getDistinctToken();

        assertEquals(expected, result);
        verify(browseDAOMock, times(1)).getDistinctToken();
    }
}
