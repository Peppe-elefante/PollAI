package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PollaioService {
    @Autowired
    private PollaioRepository pollaioRepository;
    @Autowired
    private GallinaRepository gallinaRepository;

    public Pollaio savePollaio(Utente utente){
        Pollaio pollaio = new Pollaio();
        pollaio.setUtente(utente);
        pollaio.setQuantity(0);
        return pollaioRepository.save(pollaio);
    }
}
