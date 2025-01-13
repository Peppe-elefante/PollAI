package com.example.pollai.magazzino;

import com.example.pollai.utente.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        // Setup del Magazzino e Farmaco
        Magazzino magazzino = new Magazzino();
        Farmaco farmaco = new Farmaco("Antibiotico", 10, magazzino);
        farmaco.setId(1L);
        magazzino.addFarmaco(farmaco);

        // Mock del comportamento di farmacoDAO.findById
        when(farmacoDAO.findById(1L)).thenReturn(Optional.of(farmaco));

        // Mock del comportamento di magazzinoDAO.save
        when(magazzinoDAO.save(any(Magazzino.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Rimozione del farmaco
        Magazzino result = magazzinoService.removeFarmaco(magazzino, 1L);

        // Verifiche
        assertNotNull(result); // Verifica che il risultato non sia null
        assertEquals(0, result.getFarmaci().size()); // Verifica che il farmaco sia stato rimosso
        verify(farmacoDAO).delete(farmaco); // Verifica che il farmaco sia stato eliminato
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
}
