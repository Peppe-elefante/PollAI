package com.example.pollai;

import com.example.pollai.utente.Utente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    //Mapping verso la pagina per il login
    @GetMapping("/login-page")
    public String loginpage(){
        return "login";
    }

    //Mapping verso la pagina di registrazione per l'Utente
    @GetMapping("/register-page")
    public String registerPage(Model model){
        model.addAttribute("user", new Utente());
        return "registeruser";
    }

    // Mapping verso la pagina Our Story
    @GetMapping("/our-story")
    public String ourStory() {
        return "our-story";
    }

    // Mapping verso la pagina Our Solutions
    @GetMapping("/our-solutions")
    public String ourSolutions() {
        return "our-solutions";
    }

    // Mapping verso la pagina Privacy Policy
    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

}
