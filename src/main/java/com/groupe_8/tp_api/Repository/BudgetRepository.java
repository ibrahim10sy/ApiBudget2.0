package com.groupe_8.tp_api.Repository;

import com.groupe_8.tp_api.Model.Budget;
import com.groupe_8.tp_api.Model.Categorie;
import com.groupe_8.tp_api.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long> {
    Budget findByCategorie(Categorie categorie);
    Budget findByIdBudgetAndUtilisateur(long id,Utilisateur utilisateur);
    Budget findByIdBudget(long id);
    List<Budget> findByUtilisateurIdUtilisateur(long idUser);
    List<Budget> findByUtilisateurIdUtilisateurAndDescriptionContaining(long idUser,String desc);
    Budget findByUtilisateurAndCategorieAndDateFinIsAfter(Utilisateur utilisateur, Categorie categorie, LocalDate date);
    Budget findFirstByUtilisateurAndCategorieAndDateFinIsBeforeOrderByDateFinDesc(Utilisateur utilisateur,Categorie categorie,LocalDate localDate);
    Budget findFirstByUtilisateurAndCategorieAndDateDebutIsAfterOrderByDateFinDesc(Utilisateur utilisateur,Categorie categorie,LocalDate localDate);
    Budget findByUtilisateurAndCategorieAndDateFin(Utilisateur utilisateur,Categorie categorie,LocalDate localDate);
    Budget findFirstByUtilisateurAndCategorieOrderByDateFinDesc(Utilisateur utilisateur,Categorie categorie);
    @Query(value = "SELECT sum(montant), sum(montant_restant) FROM Budget WHERE utilisateur_id_utilisateur = :idUser",nativeQuery = true)
    Integer[][] getSommeOfTotalBudgetNotFinish(@Param("idUser") long idUser);

    @Query(value = "SELECT * FROM Budget WHERE utilisateur_id_utilisateur = :idUser AND date_debut LIKE :date ",nativeQuery = true)
    List<Budget> getBudgetByMonthAndYear(@Param("idUser") long idUser, @Param("date") String date);
}
