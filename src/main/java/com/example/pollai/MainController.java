package com.example.pollai;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Classe che gestisce l'accesso a tutte le pagine generali
@Controller
public class MainController {

    //Mapping verso la homepage
    @GetMapping("/")
    public String homepage() {
        return "homepage";
    }

    //Mapping verso la pagina di scelta del piano
    @GetMapping("/choose-plan")
    public String choosePlan() {
        return "choose-plan";
    }
}
