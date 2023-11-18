package com.groupe_8.tp_api.Repository;

import com.groupe_8.tp_api.Model.Budget;
import com.groupe_8.tp_api.Model.Transfert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransfertRepository extends JpaRepository<Transfert , Long> {
    Transfert findByBudget(Budget budget);
}
