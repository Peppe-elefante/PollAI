package com.example.pollai.utente;

import com.example.pollai.magazzino.Magazzino;
import com.example.pollai.pollaio.Pollaio;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class UtenteController {

    private static final Logger log = LoggerFactory.getLogger(UtenteController.class);

    @Autowired
    private UtenteService utenteService;

    //Dopo che l'utente ha inserito tutti fa l'autenticazione
    @PostMapping("/login")
    public String login(String email, String password, HttpSession session, HttpServletRequest request, Model model){
        Optional<Utente> user = utenteService.autenticazione(email, password);
        String referer = request.getHeader("Referer");
        if (user.isPresent()) {
            log.info("User is present");
            session.setAttribute("user", user.get());

            Utente utente = user.get();
            Pollaio pollaio = utente.getPollaio();
            //Se non ha galline o è nullo reinderizzalo verso configura-pollaio
            if (pollaio == null || pollaio.getQuantity() == 0) {
                return "configura-pollaio";
            }

            return "/areautente";
        } else{
            log.info("User isn't present");
            model.addAttribute("Invalid", true);
            return "redirect:" + (referer != null ? referer : "/");
        }
    }

    //Prende i dati dell'utente e lo fa registrare
    @PostMapping("/register")
    public String register(@ModelAttribute("user") Utente user, @RequestParam String password, HttpSession session){
        log.info("here " + user.getNome());
        Utente savedUser = utenteService.saveUtente(user, password);
        session.setAttribute("user", savedUser);
        return "configura-pollaio";
    }

    //Gestione area utente
    @GetMapping("/area-utente")
    public String areaUtente(HttpSession session, Model model) {
        Utente user = (Utente) session.getAttribute("user");
        if (user == null) {
            return "login";
        }

        // Verifica se il pollaio dell'utente esiste
        Pollaio pollaio = user.getPollaio();
        if (pollaio == null || pollaio.getQuantity() == 0) {
            return "configura-pollaio";
        }

        model.addAttribute("user", user);
        model.addAttribute("pollaio", pollaio);
        return "areautente";
    }

    //Restituisce tutti gli utenti
    @GetMapping("/getAll")
    public String getUsers(Model model){
        List<Utente> users = utenteService.getAllUtenti();
        model.addAttribute("users", users);
        return "allusers";
    }

    @GetMapping("/profilo")
    public String profilo(HttpSession session){
        return "profilo";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "login";
    }
}