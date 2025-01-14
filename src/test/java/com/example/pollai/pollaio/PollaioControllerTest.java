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
        // Crea un utente e un pollaio di test
        Utente utente = new Utente();
        Pollaio pollaio = new Pollaio();
        utente.setPollaio(pollaio);

        when(session.getAttribute("user")).thenReturn(utente);
        when(request.getHeader("Referer")).thenReturn("/");

        String razza = "Leghorn";
        int eta = 5;
        int peso = 2100;

        // Crea una Gallina
        Gallina gallina = new Gallina(razza, eta, peso, pollaio);
        when(pollaioService.addGallina(any(Pollaio.class), any(Gallina.class))).thenReturn(pollaio);

        String result = pollaioController.inserisciGallina(session, razza, eta, peso, request);

        assertEquals("redirect:/", result);
        verify(pollaioService, times(1)).addGallina(any(Pollaio.class), any(Gallina.class));
    }

    // Test del metodo rimuoviGallina (POST)
    @Test
    void testRimuoviGallina_Success() {
        // Crea utente, pollaio e gallina
        Utente utente = new Utente();
        Pollaio pollaio = new Pollaio();
        Gallina gallina = new Gallina();
        gallina.setId(1L);

        //  lista delle galline non sia null
        if (pollaio.getGalline() == null) {
            pollaio.setGalline(new ArrayList<>());
        }
        pollaio.getGalline().add(gallina);
        utente.setPollaio(pollaio);

        when(session.getAttribute("user")).thenReturn(utente);
        when(request.getHeader("Referer")).thenReturn("/");
        when(pollaioService.removeGallina(any(Pollaio.class), any(Gallina.class))).thenReturn(pollaio);

        String result = pollaioController.rimuoviGallina(1L, session, request);

        assertEquals("redirect:/", result);
        verify(pollaioService, times(1)).removeGallina(any(Pollaio.class), any(Gallina.class));
    }
}
