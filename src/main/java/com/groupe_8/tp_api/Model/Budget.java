package com.groupe_8.tp_api.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idBudget;
    @NotBlank(message = "La description de la depense ne doit pas être null ou vide")
    @Column(nullable = false)
    private String description;

    @NotNull
    @Min(value = 1000, message = "Désole le montant doit etre superieure ou égale a 1000")
    @Column(nullable = false)
    private int montant;



    @Column(nullable = false)
    private int montantRestant;

    @NotNull
    @Min(value = 1000, message = "Désole le montant doit etre superieure ou égale a 0")
    @Column(nullable = false)
    private int montantAlerte;

    @NotNull(message = "Désole la date ne doit pas etre null")
    @Column(nullable = false)
    private LocalDate dateDebut;


    @Column(nullable = false)
    private LocalDate dateFin;

    @NotNull
    @ManyToOne
    private Utilisateur utilisateur;

    @NotNull
    @ManyToOne
    @JsonIgnoreProperties(value = {"budgets","utilisateur"})
    private Categorie categorie;

    @OneToOne
    @JoinColumn(name = "idParent")
    @JsonIgnoreProperties(value = {"utilisateur","categorie","parent"})
    private Budget parent;

    @OneToOne(mappedBy = "parent")
    @JsonIgnoreProperties(value = {"utilisateur","categorie","parent"})
    private Budget enfant;

    @OneToMany(mappedBy="budget", orphanRemoval = true)
    @JsonIgnore
    private List<Depenses> depenses = new ArrayList<>();

    @OneToMany(mappedBy = "budget", orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    @OneToOne(mappedBy = "budget")
    @JsonIgnoreProperties(value = {"budget"})
    private Transfert transfert;

}
