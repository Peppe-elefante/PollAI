package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
