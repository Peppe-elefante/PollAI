package com.example.pollai.salute;

import com.example.pollai.utente.Utente;
import com.example.pollai.utente.UtenteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SaluteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtenteService utenteService;

    @Test
    void salute() throws Exception {
        Utente utente = utenteService.getUtenteById(Long.valueOf(1)).get();
        this.mockMvc.perform(MockMvcRequestBuilders.get("/accesso-salute")
                .sessionAttr("user", utente));

        Utente utenteF = new Utente();
        utenteF.setId(Long.valueOf(4000));
        this.mockMvc.perform(MockMvcRequestBuilders.get("/accesso-salute")
                .sessionAttr("user", utenteF));
    }

    @Test
    void richiediConsiglio() {
    }
}