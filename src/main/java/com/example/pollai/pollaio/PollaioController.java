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
    public String Pollaio(HttpSession session, Model model){
        Utente sessionUser = (Utente) session.getAttribute("user");
        Pollaio pollaio;
        if (sessionUser == null) {
            log.warn("Nessun utente trovato in sessione.");
            return "redirect:/login"; // Reindirizza alla pagina di login
        }
        // Recupera l'utente gestito dal database
        Utente utente = utenteService.getUtenteById(sessionUser.getId()).get();
        //Crea il pollaio se non esiste
        if((pollaio = utente.getPollaio()) == null){
            pollaio = pollaioService.createPollaio(utente);
            log.info("Creato pollaio");
        }
        //aggiorna il pollaio
        utente.setPollaio(pollaio);
        session.setAttribute("user", utente);

        return "pollaio";
    }

    @PostMapping("/inserisci-gallina")
    public String inserisciGallina(HttpSession session,String razza, int eta, int peso, HttpServletRequest request){
        // Recupera l'utente dalla sessione
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            log.warn("Nessun utente trovato in sessione. Reindirizzamento alla pagina di login.");
            return "redirect:/login";
        }
        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");
        // Recupera l'utente dal database
        Utente utente = utenteService.getUtenteById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));
        // Crea e aggiungi il gallina al magazzino
        Gallina gallina = new Gallina(razza, eta, peso, utente.getPollaio());
        Pollaio pollaioAggiornato = pollaioService.addGallina(utente.getPollaio(), gallina);
        // Aggiorna l'utente con il magazzino aggiornato
        utente.setPollaio(pollaioAggiornato);
        session.setAttribute("user", utente);
        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }
}
