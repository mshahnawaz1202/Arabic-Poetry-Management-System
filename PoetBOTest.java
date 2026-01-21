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
import dto.PoetDTO;

public class PoetBOTest {

    @Mock
    private IDALFacade dalFacadeMock;

    private PoetBO poetBO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        poetBO = new PoetBO(dalFacadeMock);
    }

    @Test
    public void testCreatePoet_Success() {
        PoetDTO poet = new PoetDTO();
        poet.setPoetName("Allama Iqbal");

        poetBO.createPoet(poet);

        verify(dalFacadeMock, times(1)).createPoet(poet);
    }

    @Test
    public void testCreatePoet_InvalidName() {
        PoetDTO p1 = new PoetDTO();
        p1.setPoetName(null);
        assertThrows(IllegalArgumentException.class, () -> poetBO.createPoet(p1));

        PoetDTO p2 = new PoetDTO();
        p2.setPoetName("");
        assertThrows(IllegalArgumentException.class, () -> poetBO.createPoet(p2));

        verify(dalFacadeMock, never()).createPoet(any());
    }

    @Test
    public void testCreatePoet_NameTooLong() {
        PoetDTO p = new PoetDTO();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 101; i++)
            sb.append("a");
        p.setPoetName(sb.toString());

        assertThrows(IllegalArgumentException.class, () -> poetBO.createPoet(p));
        verify(dalFacadeMock, never()).createPoet(any());
    }

    @Test
    public void testGetPoetById_Success() {
        PoetDTO poet = new PoetDTO();
        poet.setPoetId(1);
        when(dalFacadeMock.getPoetById(1)).thenReturn(poet);

        PoetDTO result = poetBO.getPoetById(1);
        assertNotNull(result);
        assertEquals(1, result.getPoetId());
    }

    @Test
    public void testGetPoetById_NotFound() {
        when(dalFacadeMock.getPoetById(99)).thenReturn(null);

        Exception ex = assertThrows(IllegalArgumentException.class, () -> poetBO.getPoetById(99));
        assertEquals("Poet with id 99 not found", ex.getMessage());
    }

    @Test
    public void testGetAllPoets() {
        List<PoetDTO> poets = Arrays.asList(new PoetDTO(), new PoetDTO());
        when(dalFacadeMock.getAllPoets()).thenReturn(poets);

        List<PoetDTO> result = poetBO.getAllPoets();
        assertEquals(2, result.size());
    }

    @Test
    public void testUpdatePoet_Success() {
        PoetDTO p = new PoetDTO();
        p.setPoetId(1);
        poetBO.updatePoet(p);
        verify(dalFacadeMock, times(1)).updatePoet(p);
    }

    @Test
    public void testUpdatePoet_InvalidId() {
        PoetDTO p = new PoetDTO();
        p.setPoetId(0);
        assertThrows(IllegalArgumentException.class, () -> poetBO.updatePoet(p));
        verify(dalFacadeMock, never()).updatePoet(any());
    }

    @Test
    public void testDeletePoet() {
        poetBO.deletePoet(5);
        verify(dalFacadeMock, times(1)).deletePoet(5);
    }
}
