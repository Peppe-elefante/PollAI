package com.example.pollai.magazzino;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FarmacoTest {

    @Test
    void testGetId() {
        Farmaco farmaco = new Farmaco("Aspirina", 100, null);
        farmaco.setId(1L);

        assertEquals(1L, farmaco.getId());
    }

    @Test
    void testGetTipo() {
        Farmaco farmaco = new Farmaco("Aspirina", 100, null);

        assertEquals("Aspirina", farmaco.getTipo());
    }

    @Test
    void testSetTipo() {
        Farmaco farmaco = new Farmaco();

        farmaco.setTipo("Ibuprofene");

        assertEquals("Ibuprofene", farmaco.getTipo());
    }

    @Test
    void testGetMagazzino() {
        Magazzino magazzino = new Magazzino();
        Farmaco farmaco = new Farmaco("Aspirina", 100, magazzino);

        assertEquals(magazzino, farmaco.getMagazzino());
    }
}