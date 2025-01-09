package com.example.pollai.salute;

import com.example.pollai.pollaio.Pollaio;
import com.example.pollai.pollaio.PollaioService;
import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SaluteController {

    @Autowired
    private PollaioService pollaioService;
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/accesso-salute")
    public String salute(HttpSession session){
        //Recupero dell'Utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null)
            return "login";

        Pollaio pollaio;

        //Controllo del Pollaio, se non è presente ne crea uno
        if((pollaio = utente.getPollaio()) == null){
            pollaio = pollaioService.createPollaio(utente);
        }

        return "gestione-salute";
    }

    private Utente getUtente(HttpSession session){
        //Recupera l'Utente dalla sessione e verifica se esiste nel database
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            return null;
        }
        Utente utente = utenteService.getUtenteById(sessionUser.getId()).get();

        return utente;
    }
}
