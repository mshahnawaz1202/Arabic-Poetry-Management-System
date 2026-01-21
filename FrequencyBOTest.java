package bl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dal.ILemmaFrequencyDAO;
import dal.IRootFrequencyDAO;
import dal.ISegmentFrequencyDAO;
import dal.ITokenFrequencyDAO;	
import dto.FrequencyDTO;

public class FrequencyBOTest {

    @Mock
    private ILemmaFrequencyDAO lemmaDAOMock;
    @Mock
    private IRootFrequencyDAO rootDAOMock;
    @Mock
    private ISegmentFrequencyDAO segmentDAOMock;
    @Mock
    private ITokenFrequencyDAO tokenDAOMock;

    private FrequencyBO frequencyBO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        frequencyBO = new FrequencyBO(lemmaDAOMock, rootDAOMock, segmentDAOMock, tokenDAOMock);
    }

    @Test
    public void testGetLemmaFrequencies() {
        List<FrequencyDTO> expected = Arrays.asList(new FrequencyDTO());
        when(lemmaDAOMock.getLemmaFrequency()).thenReturn(expected);

        List<FrequencyDTO> result = frequencyBO.getLemmaFrequencies();

        assertEquals(expected, result);
        verify(lemmaDAOMock, times(1)).getLemmaFrequency();
    }

    @Test
    public void testGetRootFrequencies() {
        List<FrequencyDTO> expected = Arrays.asList(new FrequencyDTO());
        when(rootDAOMock.getRootFrequency()).thenReturn(expected);

        List<FrequencyDTO> result = frequencyBO.getRootFrequencies();

        assertEquals(expected, result);
        verify(rootDAOMock, times(1)).getRootFrequency();
    }

    @Test
    public void testGetSegmentFrequencies() {
        List<FrequencyDTO> expected = Arrays.asList(new FrequencyDTO());
        when(segmentDAOMock.getSegmentFrequency()).thenReturn(expected);

        List<FrequencyDTO> result = frequencyBO.getSegmentFrequencies();

        assertEquals(expected, result);
        verify(segmentDAOMock, times(1)).getSegmentFrequency();
    }

    @Test
    public void testGetTokenFrequencies() {
        List<FrequencyDTO> expected = Arrays.asList(new FrequencyDTO());
        when(tokenDAOMock.getTokenFrequency()).thenReturn(expected);

        List<FrequencyDTO> result = frequencyBO.getTokenFrequencies();

        assertEquals(expected, result);
        verify(tokenDAOMock, times(1)).getTokenFrequency();
    }
}
