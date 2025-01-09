package com.example.pollai.salute;


import com.example.pollai.pollaio.PollaioService;
import com.example.pollai.utente.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaluteService {

    @Autowired
    public PollaioService pollaioService;
    @Autowired
    public UtenteService utenteService;



}
