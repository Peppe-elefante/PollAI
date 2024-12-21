package com.example.pollai.pollaio;

import com.example.pollai.utente.Utente;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "pollaio")
public class Pollaio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @OneToMany(mappedBy = "pollaio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gallina> galline;

    @OneToOne(mappedBy = "pollaio")
    private Utente utente;

    public Pollaio() {
    }

    public Pollaio(int quantity, List<Gallina> galline, Utente utente) {
        this.quantity = galline.size();
        this.galline = galline;
        this.utente = utente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Gallina> getGalline() {
        return galline;
    }

    public void setGalline(List<Gallina> galline) {
        this.galline = galline;
    }

    public void addGallina(Gallina gallina) {
        galline.add(gallina);
        gallina.setPollaio(this);
    }

    public void removeGallina(Gallina gallina) {
        galline.remove(gallina);
        gallina.setPollaio(null);
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }


}
