package com.example.pollai.produzione;

import com.example.pollai.pollaio.Gallina;
import com.example.pollai.pollaio.Pollaio;
import com.example.pollai.pollaio.PollaioService;
import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


import java.time.LocalDate;
import java.util.List;

@Controller
public class ProduzioneController {

    @Autowired
    private ProduzioneService produzioneService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private RestTemplate restTemplate;




    @GetMapping("/accesso-produzione")
    public String produzione(HttpSession session){
        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";

        Pollaio pollaio = utente.getPollaio();
        DatiProduzione produzione;

        //Se non ha galline o è nullo reinderizzalo verso configura-pollaio
        if (pollaio == null || pollaio.getQuantity() == 0) {
            return "configura-pollaio";
        }

        //Crea i dati sulla produzione se non esistono
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

        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";

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

        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";

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

    /**@GetMapping("/predizione-produzione")
    public String predizioneProduzione(@RequestParam String category,
                                       HttpSession session, HttpServletRequest request,
                                       RedirectAttributes redirectAttributes){
        int eggs;
        String referer = request.getHeader("Referer");


        switch (category) {

            case "3 months":
                eggs = 30;
                break;
            case "1 month":
                eggs = 10;
                break;

            default:
                eggs = 100;
        }

        String predizione = "The eggs that will be produces in the next " + category + " are " + eggs;

        redirectAttributes.addFlashAttribute("predizione", predizione);

        return "redirect:" + (referer != null ? referer : "/");
    }*/

    @GetMapping("/predizione-produzione")
    public String predizioneProduzione(@RequestParam String category,
                                       @RequestParam String feed,
                                       @RequestParam int temperature,
                                       HttpSession session, HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) throws JsonProcessingException {
        // Recupera l'utente dalla sessione
        Utente utente = getUtente(session);
        if(utente == null) return "login";

        Pollaio pollaio = utente.getPollaio();
        String referer = request.getHeader("Referer");
        //WebClient client = WebClient.create("http://192.168.1.13:8050/predizione");
        //converte le galline in json per mandarle al servizio di predizione
        ObjectMapper objectMapper = new ObjectMapper();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        List<Gallina> galline = pollaio.getGalline();

        for(Gallina g : galline){
            g.setMangime(feed);
            g.setTemperatura(temperature);
        }

        HttpEntity<List<Gallina>> requestJson = new HttpEntity<>(galline, headers);
        String response = restTemplate.postForObject("http://uova-service:8050/predizione", requestJson, String.class);


        /**String response = client.post()
                .retrieve()
                .bodyToMono(String.class)
                .block();  */

        Prediction eggs = objectMapper.readValue(response, Prediction.class);

        switch (category){
            case "3 months": eggs.setPrediction(eggs.getPrediction() * 3); break;
            case "1 months": break;
            case "1 year": eggs.setPrediction(eggs.getPrediction() * 12); break;
        }

        String predizione = "The eggs that will be produces in the next " + category + " are " + eggs.getPrediction();

        redirectAttributes.addFlashAttribute("predizione", predizione);

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
