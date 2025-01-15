package com.example.pollai.utente;

import com.example.pollai.pollaio.Pollaio;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UtenteControllerTest {

    @Mock
    private UtenteService utenteService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @InjectMocks
    private UtenteController utenteController;

    private Utente testUser;

    @BeforeEach
    void setup() {
        testUser = new Utente();
        testUser.setEmail("mario.rossi@example.com");
        testUser.setPassword("Sup3rm@n!");
        testUser.setNome("Mario");
        testUser.setPollaio(null);

        Pollaio testPollaio = new Pollaio();
        testPollaio.setQuantity(10);
        testUser.setPollaio(testPollaio);
    }

    @Test
    void testLoginSuccess() {
        when(utenteService.autenticazione(eq("mario.rossi@example.com"), eq("Sup3rm@n!")))
                .thenReturn(Optional.of(testUser));

        String result = utenteController.login("mario.rossi@example.com", "Sup3rm@n!",
                new MockHttpSession(),
                new MockHttpServletRequest(),
                null);

        assertEquals("/areautente", result);
        verify(utenteService, times(1)).autenticazione(anyString(), anyString());
    }

    @Test
    void testLoginInvalidCredentials() {
        when(utenteService.autenticazione(eq("mario.rossi@example.com"), eq("wrongpassword")))
                .thenReturn(Optional.empty());

        String result = utenteController.login("mario.rossi@example.com", "wrongpassword",
                new MockHttpSession(),
                new MockHttpServletRequest(),
                model);

        assertEquals("redirect:/", result);
        verify(utenteService, times(1)).autenticazione(anyString(), anyString());
    }

    @Test
    void testRegister() {
        when(utenteService.saveUtente(any(Utente.class), eq("Sup3rm@n!")))
                .thenReturn(testUser);

        String result = utenteController.register(testUser, "Sup3rm@n!", new MockHttpSession());

        assertEquals("configura-pollaio", result);
        verify(utenteService, times(1)).saveUtente(any(Utente.class), eq("Sup3rm@n!"));
    }

    @Test
    void testAreaUtenteWithUserSession() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", testUser);

        String result = utenteController.areaUtente(session, model);

        assertEquals("areautente", result);
    }

    @Test
    void testAreaUtenteWithoutUserSession() {
        String result = utenteController.areaUtente(session, model);

        assertEquals("login", result);
    }

    @Test
    void testGetAllUsers() {
        when(utenteService.getAllUtenti()).thenReturn(Collections.singletonList(testUser));

        String result = utenteController.getUsers(model);

        assertEquals("allusers", result);
        verify(utenteService, times(1)).getAllUtenti();
    }

    @Test
    void testLoginRedirectToConfiguraPollaioWhenNoPollaio() {
        when(utenteService.autenticazione(eq("mario.rossi@example.com"), eq("Sup3rm@n!")))
                .thenReturn(Optional.of(testUser));

        testUser.setPollaio(null);

        String result = utenteController.login("mario.rossi@example.com", "Sup3rm@n!",
                new MockHttpSession(), new MockHttpServletRequest(), null);

        assertEquals("configura-pollaio", result);
    }

    @Test
    void testAreaUtenteRedirectToConfiguraPollaioWhenNoPollaio() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", testUser);

        testUser.setPollaio(null);

        String result = utenteController.areaUtente(session, model);

        assertEquals("configura-pollaio", result);
    }

    @Test
    void testProfilo() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("user", testUser);

        String result = utenteController.profilo(session);

        assertEquals("profilo", result);
    }

    @Test
    void testLogout() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setSession(session);

        String result = utenteController.logout(request);

        assertEquals("login", result);
        verify(session, times(1)).invalidate();
    }

}
