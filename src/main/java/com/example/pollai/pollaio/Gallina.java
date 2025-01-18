package com.example.pollai.pollaio;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "gallina")
public class Gallina implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, length = 20)
    private String razza;

    @Column(nullable = false)
    private int eta;

    @Column(nullable = false)
    private int peso;

    @ManyToOne
    @JoinColumn(name = "pollaio_id", nullable = false)
    @JsonIgnore
    private Pollaio pollaio;

    public Gallina() {
    }

    // Parameterized constructor
    public Gallina(String razza, int eta, int peso, Pollaio pollaio) {
        this.razza = razza;
        this.eta = eta;
        this.peso = peso;
        this.pollaio = pollaio;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazza() {
        return razza;
    }

    public void setRazza(String razza) {
        this.razza = razza;
    }

    public int getEta() {
        return eta;
    }

    public void setEta(int eta) {
        this.eta = eta;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public Pollaio getPollaio() {
        return pollaio;
    }

    public void setPollaio(Pollaio pollaio) {
        this.pollaio = pollaio;
    }


}
