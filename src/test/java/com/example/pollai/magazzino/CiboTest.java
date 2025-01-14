package com.example.pollai.magazzino;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CiboTest {
    @Test
    void testGetId() {
        Cibo cibo = new Cibo("Riso", 100, null);
        cibo.setId(1L);

        assertEquals(1L, cibo.getId());
    }

    @Test
    void testGetTipo() {
        Cibo cibo = new Cibo("Riso", 100, null);

        assertEquals("Riso", cibo.getTipo());
    }

    @Test
    void testSetTipo() {
        Cibo cibo = new Cibo();

        cibo.setTipo("Pasta");

        assertEquals("Pasta", cibo.getTipo());
    }

    @Test
    void testGetMagazzino() {
        Magazzino magazzino = new Magazzino();
        Cibo cibo = new Cibo("Riso", 100, magazzino);

        assertEquals(magazzino, cibo.getMagazzino());
    }

}