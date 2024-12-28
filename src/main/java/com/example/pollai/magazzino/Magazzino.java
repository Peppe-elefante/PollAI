package com.example.pollai.magazzino;

import com.example.pollai.pollaio.Gallina;
import com.example.pollai.utente.Utente;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "magazzino")
public class Magazzino implements Serializable {
    @Id
    private Long id;

    @OneToMany(mappedBy = "magazzino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Farmaco> farmaci;

    @OneToMany(mappedBy = "magazzino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cibo> cibo;

    @OneToOne(mappedBy = "magazzino")
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

    public List<Cibo> getCibo() {
        return cibo;
    }

    public void setCibo(List<Cibo> cibo) {
        this.cibo = cibo;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}