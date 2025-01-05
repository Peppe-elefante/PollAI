package com.example.pollai.produzione;

import com.example.pollai.pollaio.Pollaio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProduzioneDAO extends JpaRepository<DatiProduzione, Long> {
    @Query(value = "SELECT uova FROM uova_per_data WHERE produzione_id = :produzioneId AND data > :date", nativeQuery = true)
    List<Integer> findUovaAfterDate(@Param("produzioneId") Long produzioneId, @Param("date") LocalDate date);
}
