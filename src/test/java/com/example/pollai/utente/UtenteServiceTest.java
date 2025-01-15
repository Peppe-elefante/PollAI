package com.example.pollai.utente;

import com.example.pollai.utente.UtenteDAO;
import com.example.pollai.utente.UtenteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UtenteServiceTest {

    @Mock
    private UtenteDAO utenteDAO;

    @InjectMocks
    private UtenteService utenteService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveUtente() {
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setEmail("mario@example.com");

        String password = "Sup3rm@n!";

        when(utenteDAO.save(any(Utente.class))).thenReturn(utente);

        Utente savedUtente = utenteService.saveUtente(utente, password);

        assertNotEquals(password, savedUtente.getPassword());
        assertTrue(savedUtente.getPassword().startsWith("$2a$"));

        verify(utenteDAO, times(1)).save(utente);
    }

    @Test
    void testGetAllUtenti() {
        List<Utente> utenti = List.of(new Utente(), new Utente());

        when(utenteDAO.findAll()).thenReturn(utenti);

        List<Utente> result = utenteService.getAllUtenti();

        assertEquals(2, result.size());
        verify(utenteDAO, times(1)).findAll();
    }

    @Test
    void testGetUtenteByID(){
        Utente utente = new Utente();
        utente.setId(1L);

        when(utenteDAO.findById(1L)).thenReturn(Optional.of(utente));

        Optional<Utente> result = utenteService.getUtenteById(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(utenteDAO, times(1)).findById(1L);
    }

    @Test
    void testAutenticazione(){
        Utente utente = new Utente();
        utente.setEmail("mario.rossi@example.com");
        utente.setPassword(UtenteService.hashPassword("Sup3rm@n!"));

        when(utenteDAO.findByEmail("mario.rossi@example.com")).thenReturn(Optional.of(utente));

        Optional<Utente> result = utenteService.autenticazione(utente.getEmail(), "Sup3rm@n!");

        assertTrue(result.isPresent());
        assertEquals("mario.rossi@example.com", result.get().getEmail());

        verify(utenteDAO, times(1)).findByEmail("mario.rossi@example.com");
    }

    @Test
    void testAutenticazionePasswordErrata(){
        Utente utente= new Utente();
        utente.setEmail("mario.rossi@example.com");
        utente.setPassword(UtenteService.hashPassword("Sup3rm@n!"));

        when(utenteDAO.findByEmail("mario.rossi@example.com")).thenReturn(Optional.of(utente));

        Optional<Utente> result = utenteService.autenticazione(utente.getEmail(), "PasswordSbagliata!");

        assertTrue(result.isEmpty());
        verify(utenteDAO, times(1)).findByEmail("mario.rossi@example.com");
    }

    @Test
    public void testDeleteUtente() {
        utenteService.deleteUtente(1L);

        // Verifica che il metodo deleteById sia stato chiamato
        verify(utenteDAO, times(1)).deleteById(1L);
    }


}
