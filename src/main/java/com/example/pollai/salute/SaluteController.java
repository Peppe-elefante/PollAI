package com.example.pollai.salute;

import ch.qos.logback.core.model.Model;
import com.example.pollai.pollaio.Gallina;
import com.example.pollai.pollaio.Pollaio;
import com.example.pollai.pollaio.PollaioService;
import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SaluteController {

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/accesso-salute")
    public String salute(HttpSession session){
        //Recupero dell'Utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null)
            return "login";

        Pollaio pollaio = utente.getPollaio();

        //Se non ha galline o è nullo reinderizzalo verso configura-pollaio
        if(pollaio == null || pollaio.getQuantity() == 0 ){
            return "configura-pollaio";
        }

        utente.setPollaio(pollaio);
        session.setAttribute("user", utente);

        List<Gallina> galline = pollaio.getGalline();
        session.setAttribute("galline", galline);

        return "gestione-salute";
    }

    @PostMapping("/consiglio/{idGallina}")
    public ResponseEntity<Map<String, String>> richiediConsiglio(@PathVariable String idGallina, HttpSession session) {
        System.out.println("Richiesto consiglio per la gallina con ID: " + idGallina);
        int id = Integer.valueOf(idGallina);

        Utente utente = (Utente) session.getAttribute("user");
        Pollaio pollaio = utente.getPollaio();

        Optional<Gallina> checkGallina = pollaio.getGallinaByid(id);

        if (checkGallina.isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Hen with ID " + idGallina + " not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }

        Gallina gallina = checkGallina.get();
        Map<String, String> response = new HashMap<>();
        if (gallina.getPeso() < 1500 || gallina.getPeso() > 2500) {
            response.put("message", "The hen is not in good health.");
        } else {
            response.put("message", "The hen is in good health.");
        }

        return ResponseEntity.ok(response);
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
