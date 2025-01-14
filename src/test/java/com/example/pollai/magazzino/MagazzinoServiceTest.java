package com.example.pollai.magazzino;

import com.example.pollai.utente.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MagazzinoServiceTest {

    @InjectMocks
    private MagazzinoService magazzinoService;

    @Mock
    private MagazzinoDAO magazzinoDAO;

    @Mock
    private FarmacoDAO farmacoDAO;

    @Mock
    private CiboDAO ciboDAO;

    private Magazzino magazzino;
    private Utente utente;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        magazzino = new Magazzino();
        utente = new Utente();
        utente.setMagazzino(magazzino);
        magazzino.setUtente(utente);
    }

    @Test
    void testCreateMagazzino() {
        when(magazzinoDAO.save(any(Magazzino.class))).thenReturn(magazzino);

        Magazzino result = magazzinoService.createMagazzino(utente);

        assertNotNull(result);
        assertEquals(utente, result.getUtente());
        verify(magazzinoDAO, times(1)).save(any(Magazzino.class));
    }

    @Test
    void testAddFarmaco() {
        Farmaco farmaco = new Farmaco();
        when(magazzinoDAO.save(any(Magazzino.class))).thenReturn(magazzino);

        Magazzino result = magazzinoService.addFarmaco(magazzino, farmaco);

        assertTrue(result.getFarmaci().contains(farmaco));
        verify(magazzinoDAO, times(1)).save(magazzino);
    }

    @Test
    void testRemoveFarmaco() {
        Magazzino magazzino = new Magazzino();
        Farmaco farmaco = new Farmaco("Antibiotico", 10, magazzino);
        farmaco.setId(1L);
        magazzino.addFarmaco(farmaco);

        when(farmacoDAO.findById(1L)).thenReturn(Optional.of(farmaco));

        when(magazzinoDAO.save(any(Magazzino.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Magazzino result = magazzinoService.removeFarmaco(magazzino, 1L);

        assertNotNull(result);
        assertEquals(0, result.getFarmaci().size());
        verify(farmacoDAO).delete(farmaco);
    }

    @Test
    void testRemoveFarmacoException() {
        Magazzino magazzino = new Magazzino();
        Farmaco farmaco = new Farmaco("Antibiotico", 10, magazzino);
        farmaco.setId(1L);
        magazzino.addFarmaco(farmaco);

        when(farmacoDAO.findById(1L)).thenReturn(Optional.of(farmaco));

        doThrow(new RuntimeException("Errore simulato")).when(farmacoDAO).delete(farmaco);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> magazzinoService.removeFarmaco(magazzino, 1L)
        );

        assertTrue(exception.getMessage().contains("Errore durante la rimozione del farmaco"));

        verify(farmacoDAO).delete(farmaco);

        verify(magazzinoDAO, never()).save(any(Magazzino.class));
    }


    @Test
    void testModificaFarmaco() {
        Farmaco farmaco = new Farmaco();
        farmaco.setId(1L);
        farmaco.setQuantita(10);

        when(farmacoDAO.findById(1L)).thenReturn(java.util.Optional.of(farmaco));
        when(magazzinoDAO.save(magazzino)).thenReturn(magazzino);

        Magazzino result = magazzinoService.modificaFarmaco(magazzino, 1L, 5);

        assertEquals(15, farmaco.getQuantita());
        verify(farmacoDAO, times(1)).save(farmaco);
        verify(magazzinoDAO, times(1)).save(magazzino);
    }

    @Test
    void testModificaFarmacoWithNegativeQuantity() {
        Farmaco farmaco = new Farmaco();
        farmaco.setId(1L);
        farmaco.setQuantita(10);

        when(farmacoDAO.findById(1L)).thenReturn(java.util.Optional.of(farmaco));

        Magazzino result = magazzinoService.modificaFarmaco(magazzino, 1L, -15);

        assertEquals(10, farmaco.getQuantita()); // No change
        verify(farmacoDAO, never()).save(farmaco);
        verify(magazzinoDAO, never()).save(magazzino);
    }

    @Test
    void testModificaFarmacoException() {
        Farmaco farmaco = new Farmaco();
        farmaco.setId(1L);
        farmaco.setQuantita(10);

        when(farmacoDAO.findById(1L)).thenReturn(java.util.Optional.of(farmaco));

        doThrow(new RuntimeException("Errore simulato")).when(farmacoDAO).save(farmaco);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> magazzinoService.modificaFarmaco(magazzino, 1L, 5)
        );

        assertTrue(exception.getMessage().contains("Errore durante la modifica del farmaco"));

        verify(farmacoDAO, times(1)).save(farmaco);
        verify(magazzinoDAO, never()).save(magazzino);
    }

    @Test
    void testAddCibo() {
        Cibo cibo = new Cibo();
        when(magazzinoDAO.save(any(Magazzino.class))).thenReturn(magazzino);

        Magazzino result = magazzinoService.addCibo(magazzino, cibo);

        assertTrue(result.getCibo().contains(cibo));
        verify(magazzinoDAO, times(1)).save(magazzino);
    }

    @Test
    void testRemoveCibo() {
        Magazzino magazzino = new Magazzino();
        Cibo cibo = new Cibo("Grano", 10, magazzino);
        cibo.setId(1L);
        magazzino.addCibo(cibo);

        when(ciboDAO.findById(1L)).thenReturn(Optional.of(cibo));

        when(magazzinoDAO.save(any(Magazzino.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Magazzino result = magazzinoService.removeCibo(magazzino, 1L);

        assertNotNull(result);
        assertEquals(0, result.getCibo().size());
        verify(ciboDAO).delete(cibo);
    }

    @Test
    void testRemoveCiboException() {
        Magazzino magazzino = new Magazzino();
        Cibo cibo = new Cibo("Grano", 10, magazzino);
        cibo.setId(1L);
        magazzino.addCibo(cibo);

        when(ciboDAO.findById(1L)).thenReturn(Optional.of(cibo));

        doThrow(new RuntimeException("Errore simulato")).when(ciboDAO).delete(cibo);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> magazzinoService.removeCibo(magazzino, 1L)
        );

        assertTrue(exception.getMessage().contains("Errore durante la rimozione del cibo"));

        verify(ciboDAO).delete(cibo);

        verify(magazzinoDAO, never()).save(any(Magazzino.class));
    }

    @Test
    void testModificaCibo() {
        Cibo cibo = new Cibo();
        cibo.setId(1L);
        cibo.setQuantita(10);

        when(ciboDAO.findById(1L)).thenReturn(java.util.Optional.of(cibo));
        when(magazzinoDAO.save(magazzino)).thenReturn(magazzino);

        Magazzino result = magazzinoService.modificaCibo(magazzino, 1L, 5);

        assertEquals(15, cibo.getQuantita());
        verify(ciboDAO, times(1)).save(cibo);
        verify(magazzinoDAO, times(1)).save(magazzino);
    }

    @Test
    void testModificaCiboWithNegativeQuantity() {
        Cibo cibo = new Cibo();
        cibo.setId(1L);
        cibo.setQuantita(10);

        when(ciboDAO.findById(1L)).thenReturn(java.util.Optional.of(cibo));

        Magazzino result = magazzinoService.modificaCibo(magazzino, 1L, -15);

        assertEquals(10, cibo.getQuantita()); // No change
        verify(ciboDAO, never()).save(cibo);
        verify(magazzinoDAO, never()).save(magazzino);
    }

    @Test
    void testModificaCiboException() {
        Cibo cibo = new Cibo();
        cibo.setId(1L);
        cibo.setQuantita(10);

        when(ciboDAO.findById(1L)).thenReturn(java.util.Optional.of(cibo));

        doThrow(new RuntimeException("Errore simulato")).when(ciboDAO).save(cibo);
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> magazzinoService.modificaCibo(magazzino, 1L, 5)
        );

        assertTrue(exception.getMessage().contains("Errore durante la modifica del cibo"));

        verify(ciboDAO, times(1)).save(cibo);
        verify(magazzinoDAO, never()).save(magazzino);
    }
}
