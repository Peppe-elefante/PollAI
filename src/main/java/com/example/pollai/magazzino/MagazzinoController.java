package com.example.pollai.magazzino;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class MagazzinoController {

    @Autowired
    private MagazzinoService magazzinoService;
    @Autowired
    private UtenteService utenteService;

    private static final Logger log = LoggerFactory.getLogger(UtenteController.class);


    @GetMapping("/accesso-magazzino")
    public String Magazzino(HttpSession session, Model model){
        Utente utente = getUtente(session);
        if(utente == null) return "login";

        Magazzino magazzino;
        if(utente.getMagazzino() == null){
            magazzino = magazzinoService.createMagazzino(utente);
            log.info("Creato Magazzino");
        } else{
            //Se non è il primo accesso l'utente avrà una notifica sulle sue scorte
            magazzino = utente.getMagazzino();
            magazzino.setNotifica();
        }
        //aggiorna il magazzino
        utente.setMagazzino(magazzino);
        session.setAttribute("user", utente);

        return "magazzino";
    }

    @PostMapping("/inserisci-farmaco")
    public String inserisciFarmaco(HttpSession session,String tipo_f, int quantita_f, HttpServletRequest request){
        Utente utente = getUtente(session);
        if(utente == null) return "login";
        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");
        if(quantita_f > 0) {
            // Crea e aggiungi il farmaco al magazzino
            Farmaco farmaco = new Farmaco(tipo_f, quantita_f, utente.getMagazzino());
            Magazzino magazzinoAggiornato = magazzinoService.addFarmaco(utente.getMagazzino(), farmaco);
            // Aggiorna l'utente con il magazzino aggiornato
            utente.setMagazzino(magazzinoAggiornato);
            session.setAttribute("user", utente);
        }
        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/inserisci-cibo")
    public String inserisciCibo(HttpSession session,String tipo_c, int quantita_c, HttpServletRequest request){
        Utente utente = getUtente(session);
        if(utente == null) return "login";
        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");
        // Crea e aggiungi il cibo al magazzino
        if(quantita_c > 0) {
            Cibo cibo = new Cibo(tipo_c, quantita_c, utente.getMagazzino());
            Magazzino magazzinoAggiornato = magazzinoService.addCibo(utente.getMagazzino(), cibo);
            // Aggiorna l'utente con il magazzino aggiornato
            utente.setMagazzino(magazzinoAggiornato);
            session.setAttribute("user", utente);
        }
        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping({"/rimuovi-farmaco", "/rimuovi-cibo"})
    public String rimuoviElemento(HttpSession session,
                                  @RequestParam("id") Long itemId,
                                  @RequestParam("tipo") String itemType,
                                  HttpServletRequest request) {

        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";

        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");

        // Log per tipo di elemento
        log.info(itemType + " with id " + itemId);

        // Esegui l'operazione di rimozione in base al tipo (farmaco o cibo)
        Magazzino magazzino = null;
        if ("farmaco".equals(itemType)) {
            magazzino = magazzinoService.removeFarmaco(utente.getMagazzino(), itemId);
        } else if ("cibo".equals(itemType)) {
            magazzino = magazzinoService.removeCibo(utente.getMagazzino(), itemId);
        }

        // Aggiorna l'utente con il magazzino aggiornato
        utente.setMagazzino(magazzino);
        session.setAttribute("user", utente);

        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }
    @PostMapping({"/modifica-farmaco", "/modifica-cibo"})
    public String modificaFarmaco(@RequestParam("cambio") int cambio,
                                  @RequestParam("azione") String azione,
                                  @RequestParam("id") Long itemId,
                                  @RequestParam("tipo") String itemType,
                                  HttpSession session, HttpServletRequest request){
        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";

        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");

        //aggiungere o rimuovere cambio in base all'input
        cambio = Math.abs(cambio) * (azione.equals("aggiungi") ? 1 : -1);

        // Esegui l'operazione di modifica in base al tipo (farmaco o cibo)
        Magazzino magazzino = null;
        if ("farmaco".equals(itemType)) {
            magazzino = magazzinoService.modificaFarmaco(utente.getMagazzino(), itemId, cambio);
        } else if ("cibo".equals(itemType)) {
            magazzino = magazzinoService.modificaCibo(utente.getMagazzino(), itemId, cambio);
        }

        // Aggiorna l'utente con il magazzino aggiornato
        utente.setMagazzino(magazzino);
        session.setAttribute("user", utente);

        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    private Utente getUtente(HttpSession session){
        //prende l'Utente dalla sessione e verifica se esiste nel database
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            log.warn("Nessun utente trovato in sessione.");
            return null;
        }
        Utente utente = utenteService.getUtenteById(sessionUser.getId()).get();

        return utente;
    }

}
