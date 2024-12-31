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

    public void addFarmaco(Magazzino m, Farmaco f){
        m.addFarmaco(f);
        farmacoDAO.save(f);
        magazzinoDAO.save(m);
    }

    public void removeFarmaco(Magazzino m, Farmaco f){
        m.removeFarmaco(f);
        farmacoDAO.delete(f);
        magazzinoDAO.save(m);
    }

    public void addCibo(Magazzino m, Cibo c){
        m.addCibo(c);
        ciboDAO.save(c);
        magazzinoDAO.save(m);
    }

    public void removeCibo(Magazzino m, Cibo c){
        m.removeCibo(c);
        ciboDAO.delete(c);
        magazzinoDAO.save(m);
    }
}
