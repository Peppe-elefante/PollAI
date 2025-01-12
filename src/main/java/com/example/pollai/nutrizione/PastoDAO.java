package com.example.pollai.nutrizione;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PastoDAO extends JpaRepository<Pasto, LocalDate>{
    //trova tutti i pasti di un magazzino
    List<Pasto> findByMagazzino_Id(Long magazzinoId);

    //trova tutti i pasti di un cibo
    List<Pasto> findByCibo_Id(Long ciboId);

    //trova il pasto di una data
    @Query("SELECT p FROM Pasto p WHERE p.data = :data")
    Pasto findByDataOne(@Param("data") LocalDate data);

    //trova pasti tra due date
    @Query("SELECT p FROM Pasto p WHERE p.data BETWEEN :startDate AND :endDate")
    List<Pasto> findByData(@Param("startDate") LocalDate start, @Param("endDate") LocalDate end);
}
