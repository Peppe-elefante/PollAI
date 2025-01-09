package com.example.pollai.pollaio;



import com.example.pollai.magazzino.Magazzino;
import com.example.pollai.magazzino.MagazzinoService;
import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteController;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PollaioController {

    private static final Logger log = LoggerFactory.getLogger(UtenteController.class);

    @Autowired
    private PollaioService pollaioService;
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/accesso-pollaio")
    public String Pollaio(HttpSession session){
        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";
        Pollaio pollaio;

        //Se non esiste il pollaio, reindirizza l'utente alla pagina di configurazione del pollaio
        if((pollaio = utente.getPollaio()) == null){
            return "configura-pollaio";
        }
        //aggiorna il pollaio
        utente.setPollaio(pollaio);
        session.setAttribute("user", utente);

        return "pollaio";
    }

    @PostMapping("/inserisci-gallina")
    public String inserisciGallina(HttpSession session,String razza, int eta, int peso, HttpServletRequest request){
        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";
        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");

        // Crea e aggiungi il gallina al magazzino
        Gallina gallina = new Gallina(razza, eta, peso, utente.getPollaio());
        Pollaio pollaioAggiornato = pollaioService.addGallina(utente.getPollaio(), gallina);
        // Aggiorna l'utente con il magazzino aggiornato
        utente.setPollaio(pollaioAggiornato);
        session.setAttribute("user", utente);
        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    private Utente getUtente(HttpSession session){
        //prende l'Utente dalla sessione e verifica se esiste nel database
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            return null;
        }
        Utente utente = utenteService.getUtenteById(sessionUser.getId()).get();

        return utente;
    }
}
