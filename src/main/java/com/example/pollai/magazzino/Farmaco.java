package com.example.pollai.magazzino;

import com.example.pollai.pollaio.Pollaio;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "farmaco")
public class Farmaco implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo;
    private int quantita;

    @ManyToOne
    @JoinColumn(name = "magazzino_id")
    private Magazzino magazzino;

    // Default Constructor
    public Farmaco() {
    }

    // Constructor with All Fields
    public Farmaco( String tipo, int quantita, Magazzino magazzino) {
        this.tipo = tipo;
        this.quantita = quantita;
        this.magazzino = magazzino;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantita() {
        return quantita;
    }

    public void setQuantita(int quantita) {
        this.quantita = quantita;
    }

    public Magazzino getMagazzino(){
        return magazzino;
    }

    public void setMagazzino(Magazzino m){
        this.magazzino = m;
    }
}
