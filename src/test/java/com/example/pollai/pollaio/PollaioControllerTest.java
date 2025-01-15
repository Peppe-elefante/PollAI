package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PollaioControllerTest {

    @InjectMocks
    private PollaioController pollaioController;

    @Mock
    private PollaioService pollaioService;

    @Mock
    private UtenteService utenteService;

    @Mock
    private HttpSession session;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testPollaio() {
        Utente utente = new Utente();
        utente.setId(1L);
        Pollaio pollaio = new Pollaio();
        utente.setPollaio(pollaio);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(1L)).thenReturn(java.util.Optional.of(utente));

        String result = pollaioController.Pollaio(session);

        assertEquals("pollaio", result);

        verify(session, times(1)).setAttribute("user", utente);
    }


    @Test
    void testPollaioWithUserWithoutPollaio() {
        Utente utente = new Utente();
        utente.setId(1L);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(1L)).thenReturn(java.util.Optional.of(utente));
        when(pollaioService.createPollaio(utente)).thenReturn(new Pollaio());

        String result = pollaioController.Pollaio(session);

        assertEquals("pollaio", result);

        verify(session, times(1)).setAttribute("user", utente);
        verify(pollaioService, times(1)).createPollaio(utente);
    }


    @Test
    void testPollaioWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String result = pollaioController.Pollaio(session);

        assertEquals("login", result);
        verify(pollaioService, never()).createPollaio(any());
    }


    @Test
    void testAddGallina_Success_pc() {
        Utente utente = new Utente();
        utente.setId(1L);
        Pollaio pollaio = new Pollaio();
        utente.setPollaio(pollaio);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        Gallina gallina = new Gallina("Livornese", 2, 3, pollaio);
        when(pollaioService.addGallina(any(Pollaio.class), any(Gallina.class))).thenReturn(pollaio);

        String viewName = pollaioController.inserisciGallina(session, "Livornese", 2, 3, request);

        assertEquals("redirect:http://example.com", viewName);
        verify(pollaioService, times(1)).addGallina(eq(pollaio), any(Gallina.class));
        verify(session, times(1)).setAttribute("user", utente);
    }

    @Test
    void testAddGallinaWithUserWithoutPollaio() {
        Utente utente = new Utente();
        utente.setId(1L);
        utente.setPollaio(null);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(1L)).thenReturn(java.util.Optional.of(utente));
        when(pollaioService.createPollaio(utente)).thenReturn(new Pollaio());

        String view = pollaioController.Pollaio(session);

        assertEquals("pollaio", view);
        verify(pollaioService).createPollaio(utente);
    }


    @Test
    void testAddGallinaWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        String viewName = pollaioController.inserisciGallina(session, "Livornese", 6, 2000, request);

        assertEquals("login", viewName);
        verify(pollaioService, never()).addGallina(any(Pollaio.class), any(Gallina.class));
    }


    @Test
        void testRemoveGallina_Success_pc() {
            Utente utente = new Utente();
            utente.setId(1L);

            Pollaio pollaio = new Pollaio();
            Gallina gallina = new Gallina("Razza1", 2, 3, pollaio);
            gallina.setId(1L);
            pollaio.addGallina(gallina);
            utente.setPollaio(pollaio);

            when(session.getAttribute("user")).thenReturn(utente);
            when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));
            when(request.getHeader("Referer")).thenReturn("http://example.com");
            when(pollaioService.removeGallina(eq(pollaio), eq(gallina))).thenReturn(pollaio);

            String viewName = pollaioController.rimuoviGallina(1L, session, request);

            assertEquals("redirect:http://example.com", viewName);

            verify(pollaioService, times(1)).removeGallina(eq(pollaio), eq(gallina));
            verify(session, times(1)).setAttribute("user", utente);
        }


    @Test
    void testRemoveGallinaWithUserWithoutPollaio() {
        Utente utente = new Utente();
        utente.setId(1L);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        String viewName = pollaioController.rimuoviGallina(1L, session, request);

        assertEquals("pollaio", viewName);
    }


    @Test
    void testRemoveGallinaWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String viewName = pollaioController.rimuoviGallina(1L, session, request);

        assertEquals("login", viewName);
    }

}
