package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PollaioTest {

    @Test
    void testPollaioConstructor_Success() {
        Utente utente = new Utente();
        List<Gallina> galline = new ArrayList<>();
        galline.add(new Gallina("Razza1", 2, 3, null)); // Aggiungi una gallina
        galline.add(new Gallina("Razza2", 3, 4, null)); // Aggiungi un'altra gallina

        Pollaio pollaio = new Pollaio(galline.size(), galline, utente);

        assertEquals(2, pollaio.getQuantity(), "La quantità di galline dovrebbe essere 2");
        assertEquals(galline, pollaio.getGalline(), "Le galline dovrebbero essere correttamente assegnate al pollaio");
        assertEquals(utente, pollaio.getUtente(), "L'utente dovrebbe essere correttamente associato al pollaio");
    }

    @Test
    void testPollaioConstructor_EmptyGalline() {
        Utente utente = new Utente();
        List<Gallina> galline = new ArrayList<>(); // Lista vuota

        Pollaio pollaio = new Pollaio(galline.size(), galline, utente);

        assertEquals(0, pollaio.getQuantity(), "La quantità di galline dovrebbe essere 0");
        assertTrue(pollaio.getGalline().isEmpty(), "La lista di galline dovrebbe essere vuota");
        assertEquals(utente, pollaio.getUtente(), "L'utente dovrebbe essere correttamente associato al pollaio");
    }

    @Test
    void testPollaioConstructor_NullGalline() {
        Utente utente = new Utente();
        List<Gallina> galline = null;

        Pollaio pollaio = new Pollaio(0, galline != null ? galline : new ArrayList<>(), utente);

        assertEquals(0, pollaio.getQuantity(), "La quantità di galline dovrebbe essere 0");
        assertTrue(pollaio.getGalline().isEmpty(), "La lista di galline dovrebbe essere vuota");
        assertEquals(utente, pollaio.getUtente(), "L'utente dovrebbe essere correttamente associato al pollaio");
    }


    @Test
    void testPollaioConstructor_QuantityCalculatedFromGalline() {
        Utente utente = new Utente();
        List<Gallina> galline = new ArrayList<>();
        galline.add(new Gallina("Razza1", 2, 3, null));
        galline.add(new Gallina("Razza2", 3, 4, null));

        Pollaio pollaio = new Pollaio(galline.size(), galline, utente);

        assertEquals(2, pollaio.getQuantity(), "La quantità di galline dovrebbe essere 2, calcolata dalla lista di galline");
    }

    @Test
    void testAddGallina() {
        Pollaio pollaio = new Pollaio(0, new ArrayList<>(), new Utente());
        Gallina gallina = new Gallina("Razza1", 2, 3, pollaio);

        pollaio.addGallina(gallina);
        assertEquals(1, pollaio.getQuantity(), "La quantità di galline dovrebbe essere 1 dopo l'aggiunta");
        assertTrue(pollaio.getGalline().contains(gallina), "La gallina dovrebbe essere aggiunta alla lista di galline");
        assertEquals(pollaio, gallina.getPollaio(), "Il pollaio della gallina dovrebbe essere correttamente associato");
    }


    @Test
    public void testRemoveGallina() {
        Pollaio pollaio = new Pollaio();
        Gallina gallina = new Gallina();

        pollaio.addGallina(gallina);
        assertEquals(1, pollaio.getGalline().size());
        pollaio.removeGallina(gallina);
        assertEquals(0, pollaio.getGalline().size());
    }

    @Test
    void testAddNullGallina() {
        Pollaio pollaio = new Pollaio();

        assertThrows(IllegalArgumentException.class, () -> pollaio.addGallina(null),
                "Dovrebbe lanciare un'eccezione quando si tenta di aggiungere una gallina null");
    }


    @Test
    void testGallinaSetPollaio() {
        Pollaio pollaio1 = new Pollaio();
        Pollaio pollaio2 = new Pollaio();
        Gallina gallina = new Gallina("Razza1", 2, 3, pollaio1);

        assertEquals(pollaio1, gallina.getPollaio(), "La gallina dovrebbe essere associata al primo pollaio");
        gallina.setPollaio(pollaio2);
        assertEquals(pollaio2, gallina.getPollaio(), "La gallina dovrebbe essere ora associata al secondo pollaio");
    }

}
