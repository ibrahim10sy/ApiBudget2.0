package com.groupe_8.tp_api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Entity
@Data
public class Depenses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idDepenses;

    @Lob
    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Min(value = 5, message = "Le montant de votre depense doit être superieur ou égale à 5")
    private int montant;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    private Utilisateur utilisateur;

    @ManyToOne
    @JsonIgnoreProperties(value = {"depenses","utilisateur"})
    private  Budget budget;

    @ManyToOne
    @JsonIgnoreProperties(value = {"depenses","utilisateur"})
    private  Type type;
}
