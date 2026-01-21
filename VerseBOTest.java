package bl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import dal.IDALFacade;
import dto.VerseDTO;

public class VerseBOTest {

    @Mock
    private IDALFacade dalFacadeMock;

    private VerseBO verseBO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        verseBO = new VerseBO(dalFacadeMock);
    }

    @Test
    public void testCreateVerse_Success() {
        VerseDTO verse = new VerseDTO();
        verse.setText("Valid Text");

        verseBO.createVerse(verse);

        verify(dalFacadeMock, times(1)).createVerse(verse);
    }

    @Test
    public void testCreateVerse_InvalidText() {
        VerseDTO v1 = new VerseDTO();
        v1.setText(null);
        assertThrows(IllegalArgumentException.class, () -> verseBO.createVerse(v1));

        VerseDTO v2 = new VerseDTO();
        v2.setText("");
        assertThrows(IllegalArgumentException.class, () -> verseBO.createVerse(v2));

        verify(dalFacadeMock, never()).createVerse(any());
    }

    @Test
    public void testGetVersesByPoemId() {
        List<VerseDTO> verses = Arrays.asList(new VerseDTO());
        when(dalFacadeMock.getVersesByPoemId(1)).thenReturn(verses);

        List<VerseDTO> result = verseBO.getVersesByPoemId(1);
        assertEquals(verses, result);
    }

    @Test
    public void testUpdateVerse_Success() {
        VerseDTO v = new VerseDTO();
        v.setVerseId(1);
        verseBO.updateVerse(v);
        verify(dalFacadeMock, times(1)).updateVerse(v);
    }

    @Test
    public void testUpdateVerse_InvalidId() {
        VerseDTO v = new VerseDTO();
        v.setVerseId(0);
        assertThrows(IllegalArgumentException.class, () -> verseBO.updateVerse(v));
        verify(dalFacadeMock, never()).updateVerse(any());
    }

    @Test
    public void testDeleteVerse() {
        verseBO.deleteVerse(10);
        verify(dalFacadeMock, times(1)).deleteVerse(10);
    }

    @Test
    public void testSearchExactString() {
        List<VerseDTO> verses = Arrays.asList(new VerseDTO());
        when(dalFacadeMock.searchExactString("query")).thenReturn(verses);

        List<VerseDTO> result = verseBO.searchExactString("query");
        assertEquals(verses, result);
    }

    @Test
    public void testSearchRegexPattern() {
        List<VerseDTO> verses = Arrays.asList(new VerseDTO());
        when(dalFacadeMock.searchRegexPattern("regex")).thenReturn(verses);

        List<VerseDTO> result = verseBO.searchRegexPattern("regex");
        assertEquals(verses, result);
    }
}
