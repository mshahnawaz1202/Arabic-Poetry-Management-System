package bl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import dto.PoemDTO;

public class PoemBOTest {

    @Mock
    private IDALFacade dalFacadeMock;

    private PoemBO poemBO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        poemBO = new PoemBO(dalFacadeMock);
    }

    @Test
    public void testCreatePoem_Success() {
        PoemDTO poem = new PoemDTO();
        poem.setTitle("Valid Title");

        poemBO.createPoem(poem);

        verify(dalFacadeMock, times(1)).createPoem(poem);
    }

    @Test
    public void testCreatePoem_InvalidTitle() {
        PoemDTO p1 = new PoemDTO();
        p1.setTitle(null);
        assertThrows(IllegalArgumentException.class, () -> poemBO.createPoem(p1));

        PoemDTO p2 = new PoemDTO();
        p2.setTitle("");
        assertThrows(IllegalArgumentException.class, () -> poemBO.createPoem(p2));

        PoemDTO p3 = new PoemDTO();
        p3.setTitle("   ");
        assertThrows(IllegalArgumentException.class, () -> poemBO.createPoem(p3));

        verify(dalFacadeMock, never()).createPoem(any());
    }

    @Test
    public void testCreatePoem_TitleTooLong() {
        PoemDTO p = new PoemDTO();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++)
            sb.append("a");
        p.setTitle(sb.toString());

        assertThrows(IllegalArgumentException.class, () -> poemBO.createPoem(p));
        verify(dalFacadeMock, never()).createPoem(any());
    }

    @Test
    public void testGetPoemById_Success() {
        PoemDTO poem = new PoemDTO();
        poem.setPoemId(1);
        when(dalFacadeMock.getPoemById(1)).thenReturn(poem);

        PoemDTO result = poemBO.getPoemById(1);
        assertNotNull(result);
        assertEquals(1, result.getPoemId());
    }

    @Test
    public void testGetPoemById_NotFound() {
        when(dalFacadeMock.getPoemById(99)).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> poemBO.getPoemById(99));
        assertEquals("Poem with id 99 not found", ex.getMessage());
    }

    @Test
    public void testGetAllPoems() {
        List<PoemDTO> poems = Arrays.asList(new PoemDTO(), new PoemDTO());
        when(dalFacadeMock.getAllPoems()).thenReturn(poems);

        List<PoemDTO> result = poemBO.getAllPoems();
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdatePoem_Success() {
        PoemDTO p = new PoemDTO();
        p.setPoemId(1);
        poemBO.updatePoem(p);
        verify(dalFacadeMock, times(1)).updatePoem(p);
    }

    @Test
    public void testUpdatePoem_InvalidId() {
        PoemDTO p = new PoemDTO();
        p.setPoemId(0);
        assertThrows(IllegalArgumentException.class, () -> poemBO.updatePoem(p));
        verify(dalFacadeMock, never()).updatePoem(any());
    }

    @Test
    public void testDeletePoem() {
        poemBO.deletePoem(5);
        verify(dalFacadeMock, times(1)).deletePoem(5);
    }
}
