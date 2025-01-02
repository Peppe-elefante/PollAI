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
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            log.warn("Nessun utente trovato in sessione.");
            return "redirect:/login"; // Reindirizza alla pagina di login
        }
        // Recupera l'utente gestito dal database
        Utente utente = utenteService.getUtenteById(sessionUser.getId()).get();
        //Crea il magazzino se non esiste
        if(utente.getMagazzino() == null){
            magazzinoService.createMagazzino(utente);
            log.info("Creato Magazzino");
        } else{
            //Se non è il primo accesso l'utente avrà una notifica sulle sue scorte
            Magazzino magazzino = utente.getMagazzino();
            magazzino.setNotifica();
            utente.setMagazzino(magazzino);
            session.setAttribute("user", utente);
        }

        return "magazzino";
    }

    @PostMapping("/inserisci-farmaco")
    public String inserisciFarmaco(HttpSession session,String tipo_f, int quantita_f, HttpServletRequest request){
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
        // Crea e aggiungi il farmaco al magazzino
        Farmaco farmaco = new Farmaco(tipo_f, quantita_f, utente.getMagazzino());
        Magazzino magazzinoAggiornato = magazzinoService.addFarmaco(utente.getMagazzino(), farmaco);
        // Aggiorna l'utente con il magazzino aggiornato
        utente.setMagazzino(magazzinoAggiornato);
        session.setAttribute("user", utente);
        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/inserisci-cibo")
    public String inserisciCibo(HttpSession session,String tipo_c, int quantita_c, HttpServletRequest request){
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
        // Crea e aggiungi il cibo al magazzino
        Cibo cibo = new Cibo(tipo_c, quantita_c, utente.getMagazzino());
        Magazzino magazzinoAggiornato = magazzinoService.addCibo(utente.getMagazzino(), cibo);
        // Aggiorna l'utente con il magazzino aggiornato
        utente.setMagazzino(magazzinoAggiornato);
        session.setAttribute("user", utente);
        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/rimuovi-farmaco")
    public String rimuoviFarmaco(HttpSession session, @RequestParam("farmacoId") Long farmacoId, HttpServletRequest request){
        // Recupera l'utente dalla sessione
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            log.warn("Nessun utente trovato in sessione. Reindirizzamento alla pagina di login.");
            return "redirect:/login";
        }
        // Recupera l'utente dal database
        Utente utente = utenteService.getUtenteById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));

        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");

        log.info("Farmaco with id" + farmacoId);
        Magazzino magazzino = magazzinoService.removeFarmaco(utente.getMagazzino(), farmacoId);
        // Aggiorna l'utente con il magazzino aggiornato
        utente.setMagazzino(magazzino);
        session.setAttribute("user", utente);

        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/rimuovi-cibo")
    public String rimuoviCibo(HttpSession session, @RequestParam("ciboId") Long ciboId, HttpServletRequest request){
        // Recupera l'utente dalla sessione
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            log.warn("Nessun utente trovato in sessione. Reindirizzamento alla pagina di login.");
            return "redirect:/login";
        }
        // Recupera l'utente dal database
        Utente utente = utenteService.getUtenteById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));

        // Ottieni il riferimento della pagina precedente
        String referer = request.getHeader("Referer");

        log.info("Cibo with id" + ciboId);

        Magazzino magazzino = magazzinoService.removeCibo(utente.getMagazzino(), ciboId);
        // Aggiorna l'utente con il magazzino aggiornato
        utente.setMagazzino(magazzino);
        session.setAttribute("user", utente);

        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

}
