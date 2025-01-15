package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class PollaioTest {

    private Pollaio pollaio;

    @BeforeEach
    public void setUp() {
        pollaio = new Pollaio();
    }

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
        List<Gallina> galline = new ArrayList<>();

        Pollaio pollaio = new Pollaio(0, galline, utente);

        assertEquals(0, pollaio.getQuantity(), "La quantità di galline dovrebbe essere 0");
        assertTrue(pollaio.getGalline().isEmpty(), "La lista di galline dovrebbe essere vuota");
        assertEquals(utente, pollaio.getUtente(), "L'utente dovrebbe essere correttamente associato al pollaio");
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
    void testAddNullGallina() {
        Pollaio pollaio = new Pollaio();

        assertThrows(IllegalArgumentException.class, () -> pollaio.addGallina(null),
                "Dovrebbe lanciare un'eccezione quando si tenta di aggiungere una gallina null");
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
    void testGallinaSetPollaio() {
        Pollaio pollaio1 = new Pollaio();
        Pollaio pollaio2 = new Pollaio();
        Gallina gallina = new Gallina("Razza1", 2, 3, pollaio1);

        assertEquals(pollaio1, gallina.getPollaio(), "La gallina dovrebbe essere associata al primo pollaio");
        gallina.setPollaio(pollaio2);
        assertEquals(pollaio2, gallina.getPollaio(), "La gallina dovrebbe essere ora associata al secondo pollaio");
    }


    @Test
    public void testSetGalline_Success() {
        Pollaio pollaio = new Pollaio();
        List<Gallina> galline = new ArrayList<>();
        galline.add(new Gallina("Razza1", 2, 3, pollaio));
        galline.add(new Gallina("Razza2", 3, 4, pollaio));

        pollaio.setGalline(galline);

        assertEquals(galline, pollaio.getGalline(), "Il getter dovrebbe restituire la lista di galline impostata.");
        assertEquals(2, pollaio.getGalline().size(), "La lista di galline dovrebbe contenere 2 elementi.");
    }


    @Test
    public void testSetGalline_EmptyList() {
        Pollaio pollaio = new Pollaio();
        List<Gallina> gallineVuote = new ArrayList<>();

        pollaio.setGalline(gallineVuote);

        assertEquals(0, pollaio.getGalline().size(), "La lista di galline dovrebbe essere vuota.");
    }


    @Test
    public void testSetGetId() {
        Pollaio pollaio = new Pollaio();

        Long expectedId = 123L;
        pollaio.setId(expectedId);

        assertEquals(expectedId, pollaio.getId(), "L'ID restituito dal getter non corrisponde a quello impostato.");
    }


    @Test
    public void testGetGallinaById_Found() {
        Pollaio pollaio = new Pollaio();

        Gallina gallina1 = new Gallina("Razza1", 2, 2000, pollaio);
        Gallina gallina2 = new Gallina("Razza2", 3, 2000, pollaio);

        gallina1.setId(1L);
        gallina2.setId(2L);

        pollaio.setGalline(List.of(gallina1, gallina2));

        Long gallina1Id = gallina1.getId();

        Optional<Gallina> result = pollaio.getGallinaByid(gallina1Id.intValue());

        assertTrue(result.isPresent(), "La gallina con ID " + gallina1Id + " dovrebbe essere trovata.");
        assertEquals(gallina1, result.get(), "La gallina trovata non è quella attesa.");
    }


    @Test
    public void testGetGallinaById_NotFound() {
        Gallina gallina1 = new Gallina("Razza1", 2, 2000, pollaio);
        Gallina gallina2 = new Gallina("Razza2", 3, 2000, pollaio);

        pollaio.setGalline(List.of(gallina1, gallina2));

        Optional<Gallina> result = pollaio.getGallinaByid(999);

        assertFalse(result.isPresent(), "Non dovrebbe esserci una gallina con ID 999.");
    }


}
