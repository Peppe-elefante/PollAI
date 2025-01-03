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

    // Inietta il repository per l'interazione con il database
    @Autowired
    private UtenteDAO utenteDAO;

    // Crea un logger per il controllo dei log
    private static final Logger log = LoggerFactory.getLogger(UtenteController.class);

    // Crea o aggiorna un Utente (salva)
    public Utente saveUtente(Utente utente, String password) {
        // Hash della password prima di salvarla
        String hashedPassword = hashPassword(password);
        // Imposta la password criptata sull'utente
        utente.setPassword(hashedPassword);
        // Salva l'utente nel repository e restituisce l'utente salvato
        return utenteDAO.save(utente);
    }

    // Ottiene tutti gli utenti
    public List<Utente> getAllUtenti() {
        // Restituisce una lista di tutti gli utenti
        return utenteDAO.findAll();
    }

    // Ottiene un Utente per ID
    public Optional<Utente> getUtenteById(Long id) {
        // Restituisce un utente opzionale trovato tramite ID
        return utenteDAO.findById(id);
    }

    // Elimina un Utente per ID
    public void deleteUtente(Long id) {
        // Elimina l'utente con il dato ID
        utenteDAO.deleteById(id);
    }

    // Esempio di query personalizzata: Ottieni un Utente tramite email
    public Optional<Utente> autenticazione(String email, String password) {
        // Cerca un utente tramite email
        Optional<Utente> userCheck = utenteDAO.findByEmail(email);
        if(userCheck.isPresent()){
            log.info("L'email è stata trovata");
            Utente user = userCheck.get();
            // Verifica se la password corrisponde
            if(!verifyPassword(password, user.getPassword())){
                // Se la password non è corretta, restituisce un risultato vuoto
                return Optional.empty();
            }
        }
        // Restituisce l'utente trovato tramite email
        return userCheck;
    }

    // Metodo per fare l'hash della password
    public static String hashPassword(String plainPassword) {
        // Restituisce la password criptata usando BCrypt
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    // Metodo per verificare se la password inserita corrisponde alla password criptata
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        // Confronta la password in chiaro con quella criptata
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}