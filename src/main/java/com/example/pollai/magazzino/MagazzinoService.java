package com.example.pollai.magazzino;

import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MagazzinoService {
    @Autowired
    private MagazzinoDAO magazzinoDAO;
    @Autowired
    private FarmacoDAO farmacoDAO;
    @Autowired
    private CiboDAO ciboDAO;


    public Magazzino createMagazzino(Utente utente){
        Magazzino m = new Magazzino();
        m.setUtente(utente);
        utente.setMagazzino(m);
        return magazzinoDAO.save(m);
    }

    public Magazzino addFarmaco(Magazzino m, Farmaco f){

        // Salva il farmaco per generare l'ID
        m.addFarmaco(f);

        // Salva il magazzino aggiornato
        return magazzinoDAO.save(m);
    }

    public Magazzino removeFarmaco(Magazzino m, Long farmacoId){
        Farmaco f = farmacoDAO.findById(farmacoId).get();
        try {
            // Rimuove il farmaco dal magazzino
            m.removeFarmaco(f);

            // Elimina il farmaco dal database
            farmacoDAO.delete(f);

            // Salva il magazzino aggiornato e lo restituisce
            return magazzinoDAO.save(m);
        } catch (Exception e) {
            // Gestisce eventuali errori di rimozione o salvataggio
            throw new RuntimeException("Errore durante la rimozione del farmaco", e);
        }
    }

    public Magazzino addCibo(Magazzino m, Cibo c){
        // Aggiungi il cibo al magazzino
        m.addCibo(c);

        // Salva il magazzino aggiornato
        return magazzinoDAO.save(m);
    }

    public Magazzino removeCibo(Magazzino m, Long ciboId) {
        Cibo c = ciboDAO.findById(ciboId).get();
        try {
            // Rimuove il farmaco dal magazzino
            m.removeCibo(c);

            // Elimina il farmaco dal database
            ciboDAO.delete(c);

            // Salva il magazzino aggiornato e lo restituisce
            return magazzinoDAO.save(m);
        } catch (Exception e) {
            // Gestisce eventuali errori di rimozione o salvataggio
            throw new RuntimeException("Errore durante la rimozione del cibo", e);
        }
    }

    public Magazzino modificaFarmaco(Magazzino m, Long itemId, int cambio) {
        // Recupera l'oggetto Farmaco dal database utilizzando l'ID dell'item
        Farmaco f = farmacoDAO.findById(itemId).get();

        try {
            // Calcola la nuova quantità di farmaco dopo l'operazione di aggiunta o rimozione
            int quantita = f.getQuantita() + cambio;

            // Verifica se la quantità risulta negativa o zero
            if (quantita <= 0) {
                // Se la quantità è 0 o negativa, non è possibile modificare il farmaco (lo lascia invariato)
                return m;
            } else {
                // Altrimenti, aggiorna la quantità del farmaco con il nuovo valore calcolato
                f.setQuantita(quantita);
                // Salva l'oggetto Farmaco aggiornato nel database
                farmacoDAO.save(f);
            }

            // Salva il Magazzino aggiornato nel database e lo restituisce
            return magazzinoDAO.save(m);
        } catch (Exception e) {
            // Gestisce eventuali errori che potrebbero verificarsi durante la modifica del farmaco
            // (ad esempio problemi con il salvataggio o la rimozione)
            throw new RuntimeException("Errore durante la modifica del farmaco", e);
        }
    }

    public Magazzino modificaCibo(Magazzino m, Long itemId, int cambio) {
        // Recupera l'oggetto Cibo dal database utilizzando l'ID dell'item
        Cibo c = ciboDAO.findById(itemId).get();

        try {
            // Calcola la nuova quantità di cibo dopo l'operazione di aggiunta o rimozione
            int quantita = c.getQuantita() + cambio;

            // Verifica se la quantità risulta negativa o zero
            if (quantita <= 0) {
                // Se la quantità è 0 o negativa, non è possibile modificare il cibo (lo lascia invariato)
                return m;
            } else {
                // Altrimenti, aggiorna la quantità del cibo con il nuovo valore calcolato
                c.setQuantita(quantita);
                // Salva l'oggetto Cibo aggiornato nel database
                ciboDAO.save(c);
            }

            // Salva il Magazzino aggiornato nel database e lo restituisce
            return magazzinoDAO.save(m);
        } catch (Exception e) {
            // Gestisce eventuali errori che potrebbero verificarsi durante la modifica del cibo
            // (ad esempio problemi con il salvataggio o la rimozione)
            throw new RuntimeException("Errore durante la modifica del cibo", e);
        }
    }
}
