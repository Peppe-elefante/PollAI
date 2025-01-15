package com.example.pollai.pollaio;

import com.example.pollai.produzione.DatiProduzione;
import com.example.pollai.utente.Utente;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "pollaio")
public class Pollaio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int quantity;
    @OneToMany(mappedBy = "pollaio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Gallina> galline;

    @OneToOne(mappedBy = "pollaio", cascade = CascadeType.ALL)
    private Utente utente;

    @OneToOne(mappedBy = "pollaio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DatiProduzione produzione;

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

    public Optional<Gallina> getGallinaByid(int id){
        Long Gid = (long) id;
        return galline.stream().filter(gallina -> gallina.getId() == Gid).findFirst();
    }

    public void setGalline(List<Gallina> galline) {
        this.galline = galline;
    }

    public void addGallina(Gallina gallina) {
        if (galline == null) {
            galline = new ArrayList<>();
        }

        if (gallina != null) {
            galline.add(gallina);  // Aggiungiamo la gallina alla lista
            gallina.setPollaio(this);  // Impostiamo il pollaio nella gallina
            this.quantity++;  // Incrementiamo la quantità di galline
        } else {
            throw new IllegalArgumentException("La gallina non può essere null");  // Lancia un'eccezione se gallina è null
        }
    }




    public void removeGallina(Gallina gallina) {
        if (galline != null && galline.contains(gallina)) {
            galline.remove(gallina); // Rimuoviamo la gallina dalla lista
            gallina.setPollaio(null); // Rimuoviamo il riferimento al pollaio nella gallina
        } else {
            System.out.println("Gallina non trovata nel pollaio");
        }
    }


    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public DatiProduzione getProduzione() {
        return produzione;
    }

    public void setProduzione(DatiProduzione produzione) {
        this.produzione = produzione;
    }


}
