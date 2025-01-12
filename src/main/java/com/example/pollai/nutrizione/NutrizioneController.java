package com.example.pollai.nutrizione;


import com.example.pollai.magazzino.Cibo;
import com.example.pollai.magazzino.Magazzino;
import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteController;
import com.example.pollai.utente.UtenteService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;
import java.util.List;

@Controller
public class NutrizioneController {

    private static final Logger log = LoggerFactory.getLogger(NutrizioneController.class);


    @Autowired
    private NutrizioneService nutrizioneService;

    @Autowired
    private UtenteService utenteService;

    private static final Logger logger = LoggerFactory.getLogger(NutrizioneController.class);

    @GetMapping("/nutrizione")
    public String mostraSezioneNutrizione(HttpSession session, Model model) {
        Utente utente = getUtente(session);
        if (utente == null) return "login";

        Magazzino magazzino = utente.getMagazzino();

        model.addAttribute("cibi", magazzino.getCibo());
        model.addAttribute("magazzino", magazzino);
        model.addAttribute("pasti", nutrizioneService.trovaPastiPerMagazzino(magazzino.getId()));

        return "nutrizione";
    }

    @GetMapping("/aggiungi-pasto")
    public String mostraFormAggiungiPasto(HttpSession session, Model model) {
        Utente utente = getUtente(session);
        if (utente == null) return "login";

        // Aggiungi alla vista le informazioni necessarie (es. lista cibi e magazzini)
        Magazzino magazzino = utente.getMagazzino();
        model.addAttribute("cibi", magazzino.getCibo());

        return "nutrizione";
    }

    @PostMapping("/aggiungi-pasto")
    public String aggiungiPasto(HttpSession session,
                                @RequestParam(name = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                @RequestParam("ciboId") Long ciboId, @RequestParam("quantità") int quantità){
        Utente utente = getUtente(session);
        if(utente == null) return "login";

        Cibo cibo = null;
        for (Cibo item : utente.getMagazzino().getCibo()) {
            if (item.getId().equals(ciboId)) {
                cibo = item;
                break;
            }
        }

        if (cibo == null) {
            return "redirect:/nutrizione";
        }
        Pasto pastoEsistente = nutrizioneService.trovaPastoData(data);
        if (pastoEsistente != null) {
            // Se il pasto esiste già, aggiorna la quantità
            pastoEsistente.setQuantita(pastoEsistente.getQuantita() + quantità);
            nutrizioneService.modificaPasto(pastoEsistente.getData(), pastoEsistente.getCibo(), pastoEsistente.getQuantita() + quantità);
        } else {
            // Se il pasto non esiste, crea un nuovo pasto
            Pasto nuovoPasto = new Pasto();
            nuovoPasto.setData(data);
            nuovoPasto.setCibo(cibo);
            nuovoPasto.setQuantita(quantità);
            nuovoPasto.setMagazzino(utente.getMagazzino());
            nutrizioneService.aggiungiPasto(data, cibo, quantità, utente.getMagazzino());
        }
        return "nutrizione";
    }

    @PostMapping("/modifica-pasto")
    public String modificaPasto(@RequestParam("data") LocalDate data,
                                @RequestParam("quantita") int quantita) {
        Pasto pasto = nutrizioneService.trovaPastoData(data);
        Cibo cibo = pasto.getCibo();

        // Modifica il pasto
        pasto.setCibo(cibo);
        pasto.setQuantita(quantita);
        nutrizioneService.modificaPasto(data, cibo, quantita);

        return "redirect:/nutrizione";
    }

    @GetMapping("/visualizza-pasti-periodo")
    public String visualizzaPastiPeriodo(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                         @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                         HttpSession session, Model model) {
        Utente utente = getUtente(session);
        if(utente == null) return "login";

        Magazzino magazzino = utente.getMagazzino();
        // Imposta le date di default se non sono fornite dall'utente
        if (startDate == null) {
            startDate = LocalDate.now().minusWeeks(1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        // Recupera i pasti filtrati dall'intervallo di date
        List<Pasto> pastiFiltrati = nutrizioneService.trovaPastiPerPeriodo(startDate, endDate, magazzino);
        model.addAttribute("pastiFiltrati", pastiFiltrati);

        return "nutrizione";
    }

    @PostMapping("/visualizza-pasti-periodo")
    public String visualizzaPastiPeriodoPost(@RequestParam("startDate") String startStr,
                                             @RequestParam("endDate") String endStr,
                                             Model model,
                                             HttpSession session) {
        LocalDate start = LocalDate.parse(startStr);
        LocalDate end = LocalDate.parse(endStr);

        Utente utente = getUtente(session);
        if(utente == null) return "login";
        //Prende l'id del magazzino
        Magazzino magazzino = utente.getMagazzino();
        model.addAttribute("pasti", nutrizioneService.trovaPastiPerMagazzino(magazzino.getId()));

        List<Pasto> pastiFiltrati = nutrizioneService.trovaPastiPerPeriodo(start, end, magazzino);

        model.addAttribute("pastiFiltrati", pastiFiltrati);

        List<Cibo> cibi = magazzino.getCibo();
        model.addAttribute("cibi", cibi);

        return "nutrizione";
    }

    private Utente getUtente(HttpSession session) {
        // Recupera l'utente dalla sessione
        Utente sessionUser = (Utente) session.getAttribute("user");
        if (sessionUser == null) {
            log.warn("Nessun utente trovato in sessione.");
            return null;
        }
        return utenteService.getUtenteById(sessionUser.getId()).orElse(null);
    }
}
