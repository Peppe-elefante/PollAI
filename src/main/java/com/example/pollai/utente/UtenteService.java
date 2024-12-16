package com.example.pollai.utente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    private static final Logger log = LoggerFactory.getLogger(UtenteController.class);

    // Create or update an Utente (save)
    public Utente saveUtente(Utente utente, String password) {
        String hashedPassword = hashPassword(password);
        utente.setPassword(hashedPassword);
        return utenteRepository.save(utente);
    }

    // Get all users
    public List<Utente> getAllUtenti() {
        return utenteRepository.findAll();
    }

    // Get an Utente by ID
    public Optional<Utente> getUtenteById(Long id) {
        return utenteRepository.findById(id);
    }

    // Delete an Utente by ID
    public void deleteUtente(Long id) {
        utenteRepository.deleteById(id);
    }

    // Custom query example: Get Utente by email
    public Optional<Utente> getUtenteByEmail(String email, String password) {
        Optional<Utente> userCheck = utenteRepository.findByEmail(email);
        if(userCheck.isPresent()){
            log.info("the email is found");
            Utente user = userCheck.get();
            if(!verifyPassword(password, user.getPassword())){
                return Optional.empty();
            }
        }
        return userCheck;
    }

    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}