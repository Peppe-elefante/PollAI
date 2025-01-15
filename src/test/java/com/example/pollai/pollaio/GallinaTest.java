package com.example.pollai.pollaio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GallinaTest {

    private Gallina gallina;
    private Pollaio pollaio;

    @BeforeEach
    public void setup() {
        pollaio = new Pollaio();
        pollaio.setId(1L);

        gallina = new Gallina("Livorno", 6, 2000, pollaio);
    }

    @Test
    public void testCreateGallina() {
        assertNotNull(gallina);
        assertEquals("Livorno", gallina.getRazza());
        assertEquals(6, gallina.getEta());
        assertEquals(2000, gallina.getPeso());
        assertEquals(pollaio, gallina.getPollaio());
    }

    @Test
    public void testSetRazza() {
        gallina.setRazza("Brahma");
        assertEquals("Brahma", gallina.getRazza());
    }

    @Test
    public void testSetEta() {
        gallina.setEta(3);
        assertEquals(3, gallina.getEta());
    }

    @Test
    public void testSetPeso() {
        gallina.setPeso(5);
        assertEquals(5, gallina.getPeso());
    }

    @Test
    public void testSetPollaio() {
        Pollaio nuovoPollaio = new Pollaio();
        nuovoPollaio.setId(2L);
        gallina.setPollaio(nuovoPollaio);
        assertEquals(nuovoPollaio, gallina.getPollaio());
    }

}
