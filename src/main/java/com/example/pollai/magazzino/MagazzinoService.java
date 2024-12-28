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
    @Autowired
    private UtenteDAO utenteDAO;

    public Magazzino createMagazzino(Utente utente){
        Magazzino m = new Magazzino();
        m.setUtente(utente);
        utente.setMagazzino(m);
        utenteDAO.save(utente);
        return magazzinoDAO.save(m);
    }
}
