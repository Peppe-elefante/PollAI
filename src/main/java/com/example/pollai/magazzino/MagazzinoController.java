package com.example.pollai.magazzino;

import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteController;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
        }
        return "magazzino";
    }
}
