package com.example.pollai.magazzino;

import com.example.pollai.pollaio.Gallina;
import com.example.pollai.utente.Utente;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "magazzino")
public class Magazzino implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "magazzino", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Farmaco> farmaci = new ArrayList<>();

    @OneToMany(mappedBy = "magazzino", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Cibo> cibo = new ArrayList<>();

    @OneToOne(mappedBy = "magazzino", cascade = CascadeType.ALL)
    private Utente utente;

    public Magazzino() {
    }

    // Constructor with All Fields (except ID, which is auto-generated)
    public Magazzino(List<Farmaco> farmaci, List<Cibo> cibo, Utente utente) {
        this.farmaci = farmaci;
        this.cibo = cibo;
        this.utente = utente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Farmaco> getFarmaci() {
        return farmaci;
    }

    public void setFarmaci(List<Farmaco> farmaci) {
        this.farmaci = farmaci;
    }

    public void addFarmaco(Farmaco farmaco) {
        farmaci.add(farmaco);
        farmaco.setMagazzino(this);
    }

    public void removeFarmaco(Farmaco farmaco) {
        farmaci.remove(farmaco);
        farmaco.setMagazzino(null);
    }

    public List<Cibo> getCibo() {
        return cibo;
    }

    public void setCibo(List<Cibo> cibo) {
        this.cibo = cibo;
    }

    public void addCibo(Cibo cibo1) {
        cibo.add(cibo1);
        cibo1.setMagazzino(this);
    }

    public void removeCibo(Cibo cibo1) {
        cibo.remove(cibo1);
        cibo1.setMagazzino(null);
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
