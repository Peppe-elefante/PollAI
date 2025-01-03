package com.example.pollai.produzione;

import com.example.pollai.pollaio.GallinaDAO;
import com.example.pollai.pollaio.Pollaio;
import com.example.pollai.pollaio.PollaioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProduzioneService {
    @Autowired
    private ProduzioneDAO produzioneDAO;

    public DatiProduzione createProduzione(Pollaio pollaio){
        DatiProduzione produzione = new DatiProduzione();
        produzione.setPollaio(pollaio);
        pollaio.setProduzione(produzione);
        return produzioneDAO.save(produzione);
    }

    public DatiProduzione aggiornaUovaGiorno(int uova, DatiProduzione produzione, LocalDate date){

        produzione.aggiungiUova(date, uova);
        return produzioneDAO.save(produzione);

    }

    public int getProduzionePeriodo(LocalDate date, DatiProduzione produzione){
        Long id = produzione.getId();
        List<Integer> uova = produzioneDAO.findUovaAfterDate(id, date);
        int totale = 0;
        for(Integer u : uova){
            totale += u;
        }
        return totale;
    }

}
