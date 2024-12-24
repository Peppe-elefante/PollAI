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

    public Pollaio savePollaio(Utente utente){
        Pollaio pollaio = new Pollaio();
        pollaio.setUtente(utente);
        pollaio.setQuantity(0);
        utente.setPollaio(pollaio);
        return pollaioDAO.save(pollaio);
    }

    public Optional findPollaioID(long id){
        return pollaioDAO.findById(id);
    }

    public boolean addGallina(Pollaio p, Gallina g) {
        if (p.getQuantity() < 15) {
            p.addGallina(g);
            return true;
        } else
            return false;
    }

    public boolean removeGallina(Pollaio p, Gallina g) {
        if (p.getQuantity() > 0) {
            p.removeGallina(g);
            return true;
        } else
            return false;

    }

}
