package com.example.pollai.utente;

import com.example.pollai.magazzino.Magazzino;
import com.example.pollai.pollaio.Pollaio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtenteTest {
    @Test
    void testGetId(){
        Utente utente = new Utente();
        utente.setId(1L);

        assertEquals(1L, utente.getId());
    }

    @Test
    void testGetNome(){
        Utente utente = new Utente();
        utente.setNome("Mario");

        assertEquals("Mario", utente.getNome());
    }

    @Test
    void testGetCognome(){
        Utente utente = new Utente();
        utente.setCognome("Rossi");

        assertEquals("Rossi", utente.getCognome());
    }

    @Test
    void testGetPhone_number(){
        Utente utente = new Utente();
        utente.setPhone_number("3485126493");

        assertEquals("3485126493", utente.getPhone_number());
    }

    @Test
    void testGetRasvsl(){
        Utente utente = new Utente();
        utente.setRasvsl(true);

        assertEquals(true, utente.getRasvsl());
    }

    @Test
    void testGetPIVA(){
        Utente utente = new Utente();
        utente.setPIVA("1234567890");

        assertEquals("1234567890", utente.getPIVA());
    }

    @Test
    void testGetEmail(){
        Utente utente = new Utente();
        utente.setEmail("mario.rossi@example.com");

        assertEquals("mario.rossi@example.com", utente.getEmail());
    }

    @Test
    void testGetPassword(){
        Utente utente = new Utente();
        utente.setPassword("Sup3rm@n!");

        assertEquals("Sup3rm@n!", utente.getPassword());
    }

    @Test
    void testGetPollaio(){
        Utente utente = new Utente();
        Pollaio pollaio = new Pollaio();

        utente.setPollaio(pollaio);

        assertEquals(pollaio, utente.getPollaio());
    }

    @Test
    void testGetMagazzino(){
        Utente utente = new Utente();
        Magazzino magazzino = new Magazzino();

        utente.setMagazzino(magazzino);

        assertEquals(magazzino, utente.getMagazzino());
    }

    @Test
    void testToString(){
        Utente utente = new Utente();
        utente.setNome("Mario");
        utente.setCognome("Rossi");
        utente.setPhone_number("3485126493");
        utente.setRasvsl(true);
        utente.setPassword("Sup3rm@n!");
        utente.setPIVA("12345678901");
        utente.setEmail("mario.rossi@example.com");

        // Valore atteso per il toString
        String expected = "User{nome='Mario', cognome='Rossi', phone_number='3485126493', rasvsl=true, password='Sup3rm@n!', pIVA='12345678901', email='mario.rossi@example.com'}";

        // Verifica che il toString restituisca la stringa attesa
        assertEquals(expected, utente.toString());
    }
}
