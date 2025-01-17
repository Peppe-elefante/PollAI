package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class PollaioServiceTest {

    @InjectMocks
    private PollaioService pollaioService;
    private Pollaio pollaio;
    private Gallina gallina;

    @Mock
    private PollaioDAO pollaioDAO;

    @Mock
    private GallinaDAO gallinaDAO;

    @Mock
    private UtenteService utenteService;

    @Mock
    private Utente utente;

    @BeforeEach
    void setUp() {
        utente = new Utente();
        utente.setId(1L);
        MockitoAnnotations.openMocks(this);

        pollaio = new Pollaio();
        gallina = new Gallina();
    }

    @Test
    void testCreatePollaio() {
        Pollaio pollaio = new Pollaio();
        pollaio.setUtente(utente);
        utente.setPollaio(pollaio);

        when(pollaioDAO.save(any(Pollaio.class))).thenReturn(pollaio);

        Pollaio result = pollaioService.createPollaio(utente);

        assertNotNull(result);
        assertEquals(utente, result.getUtente());
        assertEquals(pollaio, result);

        verify(pollaioDAO, times(1)).save(any(Pollaio.class));
    }


    @Test
    void testCreatePollaioWithNullUtente() {
        Pollaio pollaio = new Pollaio();
        Utente utente = null;

        pollaio.setUtente(utente);

        assertThrows(NullPointerException.class, () -> {
            pollaioService.createPollaio(pollaio.getUtente());
        });
    }


    @Test
    void testFindPollaioID_Success() {
        long id = 1L;
        Pollaio expectedPollaio = new Pollaio();
        expectedPollaio.setId(id);

        when(pollaioDAO.findById(id)).thenReturn(Optional.of(expectedPollaio));

        Optional<Pollaio> result = pollaioService.findPollaioID(id);

        assertTrue(result.isPresent(), "Il risultato non dovrebbe essere vuoto");
        assertEquals(expectedPollaio, result.get(), "Il pollaio restituito dovrebbe corrispondere a quello aspettato");
    }


    @Test
    void testFindPollaioIDNotFound() {
        long id = 999L;
        when(pollaioDAO.findById(id)).thenReturn(Optional.empty());

        Optional<Pollaio> result = pollaioService.findPollaioID(id);

        assertFalse(result.isPresent(), "Il risultato dovrebbe essere vuoto");
    }


    @Test
    void testAddGallina_Success_ps() {
        Pollaio pollaio = new Pollaio();
        pollaio.setQuantity(10);
        Gallina gallina = new Gallina();

        PollaioDAO pollaioDAOMock = mock(PollaioDAO.class);
        GallinaDAO gallinaDAOMock = mock(GallinaDAO.class);
        PollaioService pollaioService = new PollaioService(pollaioDAOMock, gallinaDAOMock);

        when(pollaioDAOMock.save(pollaio)).thenReturn(pollaio);
        when(gallinaDAOMock.save(gallina)).thenReturn(gallina);

        Pollaio result = pollaioService.addGallina(pollaio, gallina);

        verify(gallinaDAOMock, times(1)).save(gallina);
        verify(pollaioDAOMock, times(1)).save(pollaio);
    }


    @Test
    void testAddGallina() {
        Pollaio pollaio = new Pollaio(0, new ArrayList<>(), new Utente());
        Gallina gallina = new Gallina("Razza1", 2, 3, null);

        pollaio.addGallina(gallina);

        System.out.println("Galline nel pollaio: " + pollaio.getGalline());
        System.out.println("Quantità di galline: " + pollaio.getQuantity());
        System.out.println("Pollaio associato alla gallina: " + gallina.getPollaio());

        assertEquals(1, pollaio.getQuantity(), "La quantità di galline dovrebbe essere 1 dopo l'aggiunta");
        assertTrue(pollaio.getGalline().contains(gallina), "La gallina dovrebbe essere aggiunta alla lista di galline");
        assertEquals(pollaio, gallina.getPollaio(), "Il pollaio della gallina dovrebbe essere correttamente associato");
    }


    @Test
    void testAddGallina_FullPollaio() {
        Pollaio pollaio = new Pollaio();
        List<Gallina> galline = new ArrayList<>(Collections.nCopies(15, new Gallina()));
        pollaio.setGalline(galline);
        pollaio.setQuantity(15);

        assertEquals(15, pollaio.getQuantity(), "Il pollaio deve essere pieno con 15 galline");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pollaioService.addGallina(pollaio, new Gallina());
        });

        assertEquals("Il pollaio è pieno!", exception.getMessage());
    }


    @Test
    void testRemoveGallina_Success_ps() {
        Pollaio pollaio = new Pollaio();
        pollaio.setQuantity(10);
        Gallina gallina = new Gallina();

        when(pollaioDAO.save(any(Pollaio.class))).thenReturn(pollaio);
        doNothing().when(gallinaDAO).delete(gallina);

        Pollaio result = pollaioService.removeGallina(pollaio, gallina);

        assertEquals(9, result.getQuantity(), "La quantità di galline dovrebbe diminuire di 1");
        verify(gallinaDAO, times(1)).delete(gallina);
        verify(pollaioDAO, times(1)).save(pollaio);
    }


    @Test
    void testRemoveGallina_GallinaNotFound() {
        Pollaio pollaio = new Pollaio();
        Gallina gallina = new Gallina();

        when(gallinaDAO.findById(anyLong())).thenReturn(Optional.empty());

        pollaioService.removeGallina(pollaio, gallina);

        verify(gallinaDAO, never()).delete(gallina);
        verify(pollaioDAO, never()).save(pollaio);
    }

    @Test
    void testRemoveGallina_PollaioNotFound() {
        Gallina gallina = new Gallina();

        when(pollaioDAO.findById(anyLong())).thenReturn(Optional.empty());

        pollaioService.removeGallina(new Pollaio(), gallina);

        verify(gallinaDAO, never()).delete(gallina);
        verify(pollaioDAO, never()).save(any(Pollaio.class));
    }
}
