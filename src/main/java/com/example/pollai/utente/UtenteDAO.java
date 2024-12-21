package com.example.pollai.utente;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtenteDAO extends JpaRepository<Utente, Long> {
    Optional<Utente> findByEmail(String email);
}