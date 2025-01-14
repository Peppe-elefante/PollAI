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

import java.util.ArrayList;
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

    // Test del metodo Pollaio (GET) - Utente con pollaio esistente
    @Test
    void testPollaio_UtenteConPollaio() {
        // Crea un utente con ID e un pollaio associato
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

    // Test del metodo Pollaio (GET) - Utente senza pollaio
    @Test
    void testPollaio_UtenteSenzaPollaio() {
        // Crea un utente senza pollaio
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

    // Test del metodo inserisciGallina (POST)
    @Test
    void testInserisciGallina_Success() {
        // Mock dell'utente con un pollaio
        Utente utente = new Utente();
        utente.setId(1L);
        Pollaio pollaio = new Pollaio();
        utente.setPollaio(pollaio);

        // Mock dei metodi di sessione e request
        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));
        when(request.getHeader("Referer")).thenReturn("http://example.com");

        // Mock del servizio per aggiungere una gallina
        Gallina gallina = new Gallina("Livornese", 2, 3, pollaio);
        when(pollaioService.addGallina(any(Pollaio.class), any(Gallina.class))).thenReturn(pollaio);

        // Chiamata al metodo
        String viewName = pollaioController.inserisciGallina(session, "Livornese", 2, 3, request);

        // Verifiche
        assertEquals("redirect:http://example.com", viewName); // Controlla il redirect
        verify(pollaioService, times(1)).addGallina(eq(pollaio), any(Gallina.class)); // Verifica l'aggiunta della gallina
        verify(session, times(1)).setAttribute("user", utente); // Verifica l'aggiornamento della sessione
    }

    // Test del metodo rimuoviGallina (POST)
    @Test
        void testRimuoviGallina_Success() {
            // Configura i mock e i dati necessari
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

            // Chiama il metodo da testare
            String viewName = pollaioController.rimuoviGallina(1L, session, request);

            // Verifica il risultato
            assertEquals("redirect:http://example.com", viewName);

            // Verifica che il servizio sia stato chiamato correttamente
            verify(pollaioService, times(1)).removeGallina(eq(pollaio), eq(gallina));
            verify(session, times(1)).setAttribute("user", utente);
        }
}
