package com.example.pollai.magazzino;

import com.example.pollai.utente.Utente;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MagazzinoTest {

    @Test
    void testMagazzinoConstructor() {
        Cibo cibo1 = new Cibo("Riso", 100, null);
        Farmaco farmaco1 = new Farmaco("Aspirina", 10, null);
        Magazzino magazzino = new Magazzino(Arrays.asList(farmaco1), Arrays.asList(cibo1), new Utente());

        assertNotNull(magazzino);
        assertEquals(1, magazzino.getFarmaci().size());
        assertEquals(1, magazzino.getCibo().size());
    }

    @Test
    void testGetId() {
        Magazzino magazzino = new Magazzino();
        magazzino.setId(1L);

        assertEquals(1L, magazzino.getId());
    }

    @Test
    void testSetFarmaci() {
        Magazzino magazzino = new Magazzino();
        Farmaco farmaco1 = new Farmaco("Aspirina", 10, magazzino);
        Farmaco farmaco2 = new Farmaco("Paracetamolo", 20, magazzino);

        magazzino.setFarmaci(Arrays.asList(farmaco1, farmaco2));

        assertEquals(2, magazzino.getFarmaci().size());
        assertTrue(magazzino.getFarmaci().contains(farmaco1));
        assertTrue(magazzino.getFarmaci().contains(farmaco2));
    }

    @Test
    void testSetCibo() {
        Magazzino magazzino = new Magazzino();
        Cibo cibo1 = new Cibo("Riso", 100, magazzino);
        Cibo cibo2 = new Cibo("Pasta", 150, magazzino);

        magazzino.setCibo(Arrays.asList(cibo1, cibo2));

        assertEquals(2, magazzino.getCibo().size());
        assertTrue(magazzino.getCibo().contains(cibo1));
        assertTrue(magazzino.getCibo().contains(cibo2));
    }

    @Test
    void testSetNotificaWithQuantityBetween200And400() {
        Magazzino magazzino = new Magazzino();
        Cibo cibo1 = new Cibo("Riso", 150, magazzino);
        Cibo cibo2 = new Cibo("Pasta", 100, magazzino);
        magazzino.addCibo(cibo1);
        magazzino.addCibo(cibo2);

        magazzino.setNotifica();

        assertEquals("We will soon run out of food supplies", magazzino.getNotifica().getAvvertimento());
    }

    @Test
    void testSetNotificaWithQuantityGreaterThan400() {
        Magazzino magazzino = new Magazzino();
        Cibo cibo1 = new Cibo("Riso", 300, magazzino);
        Cibo cibo2 = new Cibo("Pasta", 200, magazzino);
        magazzino.addCibo(cibo1);
        magazzino.addCibo(cibo2);

        magazzino.setNotifica();

        assertEquals("Our food supplies are enough", magazzino.getNotifica().getAvvertimento());
    }

    @Test
    void testSetNotificaWithQuantityLessThan200() {
        Magazzino magazzino = new Magazzino();
        Cibo cibo1 = new Cibo("Riso", 100, magazzino);
        magazzino.addCibo(cibo1);

        magazzino.setNotifica();

        assertEquals("We are running out of food supplies", magazzino.getNotifica().getAvvertimento());
    }
}