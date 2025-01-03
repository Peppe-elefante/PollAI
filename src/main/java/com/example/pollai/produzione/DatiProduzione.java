package com.example.pollai.produzione;

import com.example.pollai.pollaio.Pollaio;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "produzione")
public class DatiProduzione {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pollaio_id", nullable = false)
    private Pollaio pollaio;

    @ElementCollection
    @CollectionTable(name = "uova_per_data", joinColumns = @JoinColumn(name = "produzione_id"))
    @MapKeyColumn(name = "data")
    @Column(name = "uova")
    private Map<LocalDate, Integer> uovaPerGiorno = new HashMap<>();

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setPollaio(Pollaio pollaio){
        this.pollaio = pollaio;
    }

    public Pollaio getPollaio(){
        return pollaio;
    }

    public Map<LocalDate, Integer> getUovaPerGiorno() {
        return uovaPerGiorno;
    }

    public void setUovaPerGiorno(Map<LocalDate, Integer> uovaPerGiorno) {
        this.uovaPerGiorno = uovaPerGiorno;
    }

    public void aggiungiUova(LocalDate date, Integer num){
        uovaPerGiorno.put(date, num);
    }

    public Integer cercaPerGiorno(LocalDate date){
        Integer uova = uovaPerGiorno.get(date);  // Get the value, it may be null
        if (uova == null) {
            return -1;  // Return -1 if the date is not found
        } else {
            return uova;  // Return the value if found
        }
    }
}
