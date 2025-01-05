package com.example.pollai.produzione;

import com.example.pollai.pollaio.Pollaio;
import com.example.pollai.pollaio.PollaioService;
import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class ProduzioneController {

    @Autowired
    private ProduzioneService produzioneService;
    @Autowired
    private PollaioService pollaioService;
    @Autowired
    private UtenteService utenteService;

    @GetMapping("/accesso-produzione")
    public String produzione(HttpSession session){
        Utente sessionUser = (Utente) session.getAttribute("user");
        Pollaio pollaio;
        DatiProduzione produzione;
        if (sessionUser == null) {
            return "redirect:/login"; // Reindirizza alla pagina di login
        }
        // Recupera l'utente gestito dal database
        Utente utente = utenteService.getUtenteById(sessionUser.getId()).get();
        //Crea il pollaio se non esiste
        if((pollaio = utente.getPollaio()) == null){
            pollaio = pollaioService.createPollaio(utente);
        }


        if((produzione = pollaio.getProduzione()) == null){
            produzione = produzioneService.createProduzione(pollaio);
        }
        //aggiorna il pollaio
        pollaio.setProduzione(produzione);
        utente.setPollaio(pollaio);
        session.setAttribute("user", utente);

        return "gestione-produzione";
    }

    @PostMapping("/inserisci-uova")
    public String inserisciUova(String date, int quantita, HttpServletRequest request, HttpSession session){
        LocalDate data = LocalDate.parse(date);
        String referer = request.getHeader("Referer");

        Utente sessionUser = (Utente) session.getAttribute("user");
        Utente utente = utenteService.getUtenteById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));

        Pollaio pollaio = utente.getPollaio();

        DatiProduzione produzione = produzioneService.aggiornaUovaGiorno(quantita, pollaio.getProduzione(), data);
        pollaio.setProduzione(produzione);
        utente.setPollaio(pollaio);
        session.setAttribute("user", utente);
        // Reindirizza alla pagina precedente o alla home
        return "redirect:" + (referer != null ? referer : "/");
    }

    @GetMapping("/archivio-produzione")
    public String archivioProduzione(@RequestParam String category, HttpSession session, HttpServletRequest request, RedirectAttributes redirectAttributes){
        LocalDate data = LocalDate.now();
        String referer = request.getHeader("Referer");

        Utente sessionUser = (Utente) session.getAttribute("user");
        Utente utente = utenteService.getUtenteById(sessionUser.getId())
                .orElseThrow(() -> new RuntimeException("Utente non trovato nel database"));

        Pollaio pollaio = utente.getPollaio();
        DatiProduzione produzione = pollaio.getProduzione();
        
        switch (category) {

            case "3 months":
                data = data.minusMonths(3);
                break;
            case "1 month":
                data = data.minusMonths(1);
                break;

            default:
                data = data.minusYears(1);
        }

        String archivio = "The eggs produced in the last " + category + " are ";
        int eggs = produzioneService.getProduzionePeriodo(data, produzione);
        archivio += eggs;

        redirectAttributes.addFlashAttribute("archivio", archivio);

        return "redirect:" + (referer != null ? referer : "/");
    }

}
