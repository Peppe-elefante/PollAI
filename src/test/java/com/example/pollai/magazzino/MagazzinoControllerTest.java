package com.example.pollai.magazzino;

import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;


import java.util.Optional;

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

}