package com.groupe_8.tp_api.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Transfert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idTransfert;
    private int montant;

    @OneToOne
    @JoinColumn(name="idBudget")
    private Budget budget;
}
