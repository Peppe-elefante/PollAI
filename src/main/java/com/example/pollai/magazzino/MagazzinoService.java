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

    public Magazzino removeFarmaco(Magazzino m, Farmaco f){
        m.removeFarmaco(f);
        farmacoDAO.delete(f);
        return magazzinoDAO.save(m);
    }

    public Magazzino addCibo(Magazzino m, Cibo c){
        // Aggiungi il cibo al magazzino
        m.addCibo(c);

        // Salva il magazzino aggiornato
        return magazzinoDAO.save(m);
    }

    public Magazzino removeCibo(Magazzino m, Cibo c){
        m.removeCibo(c);
        ciboDAO.delete(c);
        return magazzinoDAO.save(m);
    }
}
