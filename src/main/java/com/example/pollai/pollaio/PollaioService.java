package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PollaioService {
    @Autowired
    private PollaioDAO pollaioDAO;
    @Autowired
    private GallinaDAO gallinaDAO;

    public PollaioService(PollaioDAO pollaioDAOMock, GallinaDAO gallinaDAOMock) {
        this.pollaioDAO = pollaioDAOMock;
        this.gallinaDAO = gallinaDAOMock;
    }


    public Pollaio createPollaio(Utente utente){
        Pollaio pollaio = new Pollaio();
        pollaio.setUtente(utente);
        pollaio.setQuantity(0);
        utente.setPollaio(pollaio);
        return pollaioDAO.save(pollaio);
    }

    public Optional findPollaioID(long id){
        return pollaioDAO.findById(id);
    }

    public Pollaio addGallina(Pollaio p, Gallina g) {
        if (p.getQuantity() < 15) {
            p.addGallina(g);
            gallinaDAO.save(g);
        } else {
            throw new IllegalStateException("Il pollaio è pieno!");
        }

        return pollaioDAO.save(p);
    }


    public Pollaio removeGallina(Pollaio p, Gallina g) {
        if (p.getQuantity() > 0) {
            p.removeGallina(g);
            gallinaDAO.delete(g);
            p.setQuantity(p.getQuantity()-1);
            return pollaioDAO.save(p);
        }
        return p;
    }

}
