package com.example.pollai.salute;

import com.example.pollai.magazzino.Magazzino;
import com.example.pollai.magazzino.MagazzinoController;
import com.example.pollai.magazzino.MagazzinoService;
import com.example.pollai.pollaio.Gallina;
import com.example.pollai.pollaio.Pollaio;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SaluteControllerTest {

    @InjectMocks
    private SaluteController saluteController;

    @Mock
    private UtenteService utenteService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;


    private Utente utente;

    private Pollaio pollaio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        pollaio = new Pollaio();
        utente = new Utente();
        utente.setPollaio(pollaio);
        pollaio.setUtente(utente);
    }

    @Test
    void testSalute() {
        Utente utente = new Utente();
        Pollaio pollaio = new Pollaio();
        utente.setId(1L);
        utente.setNome("User");
        pollaio.setQuantity(1);
        utente.setPollaio(pollaio);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        doNothing().when(session).setAttribute("user", utente);

        String viewName = saluteController.salute(session);

        assertEquals("gestione-salute", viewName);

        verify(session, times(1)).setAttribute("user", utente);
    }


    @Test
    void testSaluteWithUserNotLogged() {
        when(session.getAttribute("user")).thenReturn(null);

        String viewName = saluteController.salute(session);

        assertEquals("login", viewName);
        verify(session, never()).setAttribute(eq("user"), any());
    }


    @Test
    void testSaluteWithUserWithoutHens() {
        Utente utente = new Utente();
        Pollaio pollaio = new Pollaio();
        utente.setId(1L);
        utente.setNome("User");
        pollaio.setQuantity(0);
        utente.setPollaio(pollaio);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        String viewName = saluteController.salute(session);

        assertEquals("configura-pollaio", viewName);

    }

    @Test
    void testSaluteWithUserWithoutPollaio() {
        Utente utente = new Utente();

        utente.setId(1L);
        utente.setNome("User");

        utente.setPollaio(null);

        when(session.getAttribute("user")).thenReturn(utente);
        when(utenteService.getUtenteById(anyLong())).thenReturn(Optional.of(utente));

        String viewName = saluteController.salute(session);

        assertEquals("configura-pollaio", viewName);

    }

    @Test
    void testRichiediConsiglioHealthyWeight(){
        Utente utente = new Utente();
        Pollaio pollaio = mock(Pollaio.class);
        utente.setId(1L);
        utente.setNome("User");
        utente.setPollaio(pollaio);
        pollaio.setGalline(new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(utente);

        Gallina gallina = new Gallina();
        gallina.setPeso(2000);
        when(pollaio.getGallinaByid(1)).thenReturn(Optional.of(gallina));

        ResponseEntity<Map<String, String>> viewName = saluteController.richiediConsiglio("1", session);

        assertEquals(viewName.getBody().get("message") , "The hen is in good health.");

    }

    @Test
    void testRichiediConsiglioUnhealthyWeight(){
        Utente utente = new Utente();
        Pollaio pollaio = mock(Pollaio.class);
        utente.setId(1L);
        utente.setNome("User");
        utente.setPollaio(pollaio);
        pollaio.setGalline(new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(utente);

        Gallina gallina = new Gallina();
        gallina.setPeso(1000);
        when(pollaio.getGallinaByid(1)).thenReturn(Optional.of(gallina));

        ResponseEntity<Map<String, String>> viewName = saluteController.richiediConsiglio("1", session);

        assertEquals(viewName.getBody().get("message") , "The hen is not in good health.");

    }

    @Test
    void testRichiediConsiglioOverweight(){
        Utente utente = new Utente();
        Pollaio pollaio = mock(Pollaio.class);
        utente.setId(1L);
        utente.setNome("User");
        utente.setPollaio(pollaio);
        pollaio.setGalline(new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(utente);

        Gallina gallina = new Gallina();
        gallina.setPeso(3000);
        when(pollaio.getGallinaByid(1)).thenReturn(Optional.of(gallina));

        ResponseEntity<Map<String, String>> viewName = saluteController.richiediConsiglio("1", session);

        assertEquals(viewName.getBody().get("message") , "The hen is not in good health.");

    }

    @Test
    void testRichiediConsiglioNullHen(){
        Utente utente = new Utente();
        Pollaio pollaio = mock(Pollaio.class);
        utente.setId(1L);
        utente.setNome("User");
        utente.setPollaio(pollaio);
        pollaio.setGalline(new ArrayList<>());

        when(session.getAttribute("user")).thenReturn(utente);


        when(pollaio.getGallinaByid(1)).thenReturn(Optional.empty());

        ResponseEntity<Map<String, String>> viewName = saluteController.richiediConsiglio("1", session);

        assertEquals(viewName.getBody().get("message") , "Hen with ID " + "1" + " not found.");

    }
}