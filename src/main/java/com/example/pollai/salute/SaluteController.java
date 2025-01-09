package com.example.pollai.salute;

import ch.qos.logback.core.model.Model;
import com.example.pollai.pollaio.Gallina;
import com.example.pollai.pollaio.Pollaio;
import com.example.pollai.pollaio.PollaioService;
import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SaluteController {

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private PollaioService pollaioService;

    @GetMapping("/accesso-salute")
    public String salute(HttpSession session){
        //Recupero dell'Utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null)
            return "login";

        Pollaio pollaio = utente.getPollaio();

        //Se non ha galline o è nullo reinderizzalo verso configura-pollaio
        if(pollaio.getQuantity() == 0 || pollaio == null){
            return "configura-pollaio";
        }

        List<Gallina> galline = pollaio.getGalline();

        session.setAttribute("galline", galline);

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
