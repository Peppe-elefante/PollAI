package com.example.pollai.utente;

import com.example.pollai.pollaio.Pollaio;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "privato")
public class Utente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String nome;

    @Column(nullable = false, length = 20)
    private String cognome;

    @Column(nullable = false, length = 20, name = "phone_number")
    private String phone_number;

    @Column
    private Boolean rasvsl;

    @Column(nullable = false, length = 64)
    private String password;

    @Column(unique = true, length = 45, name="p_iva")
    private String pIVA;

    @Column(nullable = false, unique = true, length = 45)
    private String email;

    @OneToOne
    @JoinTable(
            name = "utente_pollaio", // Name of the join table
            joinColumns = @JoinColumn(name = "utente_id"), // Foreign key for User
            inverseJoinColumns = @JoinColumn(name = "pollaio_id") // Foreign key for Address
    )
    private Pollaio pollaio;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public Boolean getRasvsl() {
        return rasvsl;
    }

    public void setRasvsl(Boolean rasvsl) {
        this.rasvsl = rasvsl;
    }

    public String getPIVA() {
        return pIVA;
    }

    public void setPIVA(String pIVA) {
        this.pIVA = pIVA;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Pollaio getPollaio(){
        return pollaio;
    }
    public void setPollaio(Pollaio pollaio){
        this.pollaio = pollaio;
    }

    @Override
    public String toString() {
        return "User{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", rasvsl=" + rasvsl +
                ", password='" + password + '\'' +
                ", pIVA='" + pIVA + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
