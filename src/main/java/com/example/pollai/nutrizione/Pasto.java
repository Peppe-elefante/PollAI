package com.example.pollai.nutrizione;

import com.example.pollai.magazzino.*;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "pasto")
public class Pasto implements Serializable {

    @Id
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "cibo_id")
    private Cibo cibo;

    @ManyToOne
    @JoinColumn(name = "magazzino_id")
    private Magazzino magazzino;

    private int quantità;

    public Pasto(){}

    public Pasto(LocalDate data, Cibo cibo, Magazzino magazzino, int quantità){
        this.data = data;
        this.cibo = cibo;
        this.magazzino = magazzino;
        this.quantità = quantità;
    }

    // Getters and Setters
    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Cibo getCibo() {
        return cibo;
    }

    public void setCibo(Cibo cibo) {
        this.cibo = cibo;
    }

    public Magazzino getMagazzino() {
        return magazzino;
    }

    public void setMagazzino(Magazzino magazzino) {
        this.magazzino = magazzino;
    }

    public int getQuantita() {
        return quantità;
    }

    public void setQuantita(int quantita) {
        this.quantità = quantita;
    }
}
