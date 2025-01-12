package com.example.pollai.nutrizione;

import com.example.pollai.magazzino.Cibo;
import com.example.pollai.magazzino.Magazzino;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NutrizioneService {

    @Autowired
    private PastoDAO pastoDAO;

    public Pasto aggiungiPasto(LocalDate data, Cibo cibo, int quantità, Magazzino magazzino){
        Pasto pasto = new Pasto(data, cibo, magazzino, quantità);
        return pastoDAO.save(pasto);
    }

    // Trova tutti i pasti di un magazzino
    public List<Pasto> trovaPastiPerMagazzino(Long magazzinoId) {
        return pastoDAO.findByMagazzino_Id(magazzinoId);
    }

    // Trova tutti i pasti di un cibo specifico
    public List<Pasto> trovaPastiPerCibo(Long ciboId) {
        return pastoDAO.findByCibo_Id(ciboId);
    }

    // Trova pasti in un intervallo di tempo
    public List<Pasto> trovaPastiPerPeriodo(LocalDate start, LocalDate end, Magazzino magazzino) {

        return pastoDAO.findByData(start, end, magazzino);
    }

    // Trova pasto in una data
    public Pasto trovaPastoData(LocalDate data) {
        return pastoDAO.findByDataOne(data);
    }

    // Modifica un pasto
    public Pasto modificaPasto(LocalDate data, Cibo cibo, int nuovaQuantita) {
        Pasto pasto = trovaPastoData(data);
        if (pasto == null) {
            throw new RuntimeException("Pasto non trovato per la data: " + data);
        }
        pasto.setCibo(cibo);
        pasto.setQuantita(nuovaQuantita);
        return pastoDAO.save(pasto);
    }

    // Rimuovi un pasto
    public void rimuoviPasto(LocalDate data) {
        Pasto pasto = pastoDAO.findByDataOne(data);
        if (pasto != null) {
            pastoDAO.delete(pasto);
        }
    }
}
