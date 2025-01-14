package com.example.pollai.magazzino;

import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;


import java.util.Optional;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MagazzinoControllerTest {

    @InjectMocks
    private MagazzinoController magazzinoController;

    @Mock
    private MagazzinoService magazzinoService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @Mock
    private UtenteService utenteService;

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
    void testMagazzino() {
        Magazzino magazzino = new Magazzino();
        magazzino.setId(1L);
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setMagazzino(magazzino);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        doNothing().when(session).setAttribute("user", utente);

        String viewName = magazzinoController.Magazzino(session, model);

        assertEquals("magazzino", viewName);
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testMagazzinoWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String viewName = magazzinoController.Magazzino(session, model);

        assertEquals("login", viewName);
        verify(session, never()).setAttribute(eq("user"), any());
    }

    @Test
    void testMagazzinoWithUserWithoutMagazzino() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setMagazzino(null);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        Magazzino magazzino = new Magazzino();
        when(magazzinoService.createMagazzino(utente)).thenReturn(magazzino);

        doNothing().when(session).setAttribute("user", utente);

        String viewName = magazzinoController.Magazzino(session, model);

        assertEquals("magazzino", viewName);
        assertEquals(magazzino, utente.getMagazzino());
        verify(magazzinoService, times(1)).createMagazzino(utente);
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testInserisciFarmaco() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        String tipoFarmaco = "Antibiotico";
        int quantitaFarmaco = 10;
        Farmaco farmaco = new Farmaco(tipoFarmaco, quantitaFarmaco, magazzino);

        when(magazzinoService.addFarmaco(any(Magazzino.class), any(Farmaco.class))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        String viewName = magazzinoController.inserisciFarmaco(session, tipoFarmaco, quantitaFarmaco, request);

        assertEquals("redirect:http://example.com", viewName);
        verify(magazzinoService, times(1)).addFarmaco(any(Magazzino.class), any(Farmaco.class));
        verify(session, times(1)).setAttribute(eq("user"), eq(utente));
    }

    @Test
    void testInserisceFarmacoWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String tipo_f = "Aspirina";
        int quantita_f = 10;

        String viewName = magazzinoController.inserisciFarmaco(session, tipo_f, quantita_f, request);

        assertEquals("login", viewName);
        verify(magazzinoService, never()).addFarmaco(any(), any());
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void testInserisciFarmacoWithNegativeOrZeroQuantity() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setMagazzino(new Magazzino());

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));
        String viewName = magazzinoController.inserisciFarmaco(session, "FarmacoTest", 0, request);

        verify(magazzinoService, never()).addFarmaco(any(), any());
        assertEquals("redirect:/", viewName);

        viewName = magazzinoController.inserisciFarmaco(session, "FarmacoTest", -1, request);

        verify(magazzinoService, never()).addFarmaco(any(), any());
        assertEquals("redirect:/", viewName);
    }

    @Test
    void testInserisciCibo() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        String tipoCibo = "Grano";
        int quantitàCibo = 10;
        Farmaco farmaco = new Farmaco(tipoCibo, quantitàCibo, magazzino);

        when(magazzinoService.addCibo(any(Magazzino.class), any(Cibo.class))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        String viewName = magazzinoController.inserisciCibo(session, tipoCibo, quantitàCibo, request);

        assertEquals("redirect:http://example.com", viewName);
        verify(magazzinoService, times(1)).addCibo(any(Magazzino.class), any(Cibo.class));
        verify(session, times(1)).setAttribute(eq("user"), eq(utente));
    }

    @Test
    void testInserisceCiboWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String tipo_c = "Grano";
        int quantita_c = 10;

        String viewName = magazzinoController.inserisciCibo(session, tipo_c, quantita_c, request);

        assertEquals("login", viewName);
        verify(magazzinoService, never()).addCibo(any(), any());
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void testInserisciCiboWithNegativeOrZeroQuantity() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setMagazzino(new Magazzino());

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        String viewName = magazzinoController.inserisciCibo(session, "Grano", 0, request);

        verify(magazzinoService, never()).addCibo(any(), any());
        assertEquals("redirect:/", viewName);

        viewName = magazzinoController.inserisciCibo(session, "Grano", -1, request);

        verify(magazzinoService, never()).addCibo(any(), any());
        assertEquals("redirect:/", viewName);
    }

    @Test
    void testRimuoviElementoWhenFarmaco() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        Farmaco farmaco = new Farmaco("Aspirina", 10, magazzino);
        when(magazzinoService.removeFarmaco(any(Magazzino.class), eq(1L))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        String viewName = magazzinoController.rimuoviElemento(session, 1L, "farmaco", request);
        
        assertEquals("redirect:http://example.com", viewName);
        verify(magazzinoService, times(1)).removeFarmaco(any(Magazzino.class), eq(1L));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testRimuoviElementoWhenCibo() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        Cibo cibo = new Cibo("Grano", 10, magazzino);
        when(magazzinoService.removeCibo(any(Magazzino.class), eq(1L))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        String viewName = magazzinoController.rimuoviElemento(session, 1L, "cibo", request);

        assertEquals("redirect:http://example.com", viewName);
        verify(magazzinoService, times(1)).removeCibo(any(Magazzino.class), eq(1L));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testRimuoviElementoWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String viewName = magazzinoController.rimuoviElemento(session, 1L, "farmaco", request);

        assertEquals("login", viewName);
        verify(session, never()).setAttribute(eq("user"), any());
    }

    @Test
    void testRimuoviElementoWhenFarmacoWithNoReferer() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        Long farmacoId = 1L;
        when(magazzinoService.removeFarmaco(any(Magazzino.class), eq(farmacoId)))
                .thenReturn(new Magazzino());
        when(request.getHeader("Referer")).thenReturn(null);

        String result = magazzinoController.rimuoviElemento(session, farmacoId, "farmaco", request);

        assertEquals("redirect:/", result);
        verify(magazzinoService, times(1)).removeFarmaco(any(Magazzino.class), eq(farmacoId));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testModificaElementoWhenFarmacoAdd() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        when(magazzinoService.modificaFarmaco(any(Magazzino.class), eq(1L), eq(10))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        String viewName = magazzinoController.modificaElemento(10, "aggiungi", 1L, "farmaco", session, request);

        assertEquals("redirect:http://example.com", viewName);
        verify(magazzinoService, times(1)).modificaFarmaco(any(Magazzino.class), eq(1L), eq(10));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testModificaElementoWhenCiboAdd() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        when(magazzinoService.modificaCibo(any(Magazzino.class), eq(1L), eq(5))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        String viewName = magazzinoController.modificaElemento(5, "aggiungi", 1L, "cibo", session, request);

        assertEquals("redirect:http://example.com", viewName);
        verify(magazzinoService, times(1)).modificaCibo(any(Magazzino.class), eq(1L), eq(5));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testModificaElementoWhenFarmacoRemove() {
        // Setup dell'utente e del magazzino
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);

        // Simuliamo che l'utente sia loggato
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        // Creiamo un oggetto Farmaco con una quantità di 10
        Farmaco farmaco = new Farmaco("Aspirina", 10, magazzino);

        // Simuliamo la rimozione del farmaco
        when(magazzinoService.modificaFarmaco(any(Magazzino.class), eq(1L), eq(-5))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        // Chiamata al controller per rimuovere il farmaco
        String viewName = magazzinoController.modificaElemento( -5, "rimuovi", 1L, "farmaco", session, request);

        // Verifica che il risultato sia il redirect corretto
        assertEquals("redirect:http://example.com", viewName);

        // Verifica che il servizio sia stato chiamato per modificare il farmaco
        verify(magazzinoService, times(1)).modificaFarmaco(any(Magazzino.class), eq(1L), eq(-5));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testModificaElementoWhenCiboRemove() {
        // Setup dell'utente e del magazzino
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);

        // Simuliamo che l'utente sia loggato
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        // Creiamo un oggetto Cibo con una quantità di 10
        Cibo cibo = new Cibo("Grano", 10, magazzino);

        // Simuliamo la rimozione del cibo
        when(magazzinoService.modificaCibo(any(Magazzino.class), eq(1L), eq(-5))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        // Chiamata al controller per rimuovere il cibo
        String viewName = magazzinoController.modificaElemento( -5, "rimuovi", 1L, "cibo", session, request);

        // Verifica che il risultato sia il redirect corretto
        assertEquals("redirect:http://example.com", viewName);

        // Verifica che il servizio sia stato chiamato per modificare il cibo
        verify(magazzinoService, times(1)).modificaCibo(any(Magazzino.class), eq(1L), eq(-5));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testModificaElementoWhenUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String viewName = magazzinoController.modificaElemento(10, "aggiungi", 1L, "farmaco", session, request);

        assertEquals("login", viewName);
        verify(session, never()).setAttribute(eq("user"), any());
    }

    @Test
    void testModificaElementoWhenRefererIsNull() {
        Utente utente = new Utente();
        utente.setId(1L);
        Magazzino magazzino = new Magazzino();
        utente.setMagazzino(magazzino);
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        when(magazzinoService.modificaFarmaco(any(Magazzino.class), eq(1L), eq(10))).thenReturn(magazzino);
        when(request.getHeader("Referer")).thenReturn(null);  // Referer nullo

        String viewName = magazzinoController.modificaElemento(10, "aggiungi", 1L, "farmaco", session, request);

        assertEquals("redirect:/", viewName);  // Redirect alla home
        verify(magazzinoService, times(1)).modificaFarmaco(any(Magazzino.class), eq(1L), eq(10));
        verify(session, times(1)).setAttribute("user", utente);
    }
}