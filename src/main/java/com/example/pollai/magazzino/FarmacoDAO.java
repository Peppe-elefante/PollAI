package com.example.pollai.magazzino;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmacoDAO extends JpaRepository<Farmaco, Long> {
}
