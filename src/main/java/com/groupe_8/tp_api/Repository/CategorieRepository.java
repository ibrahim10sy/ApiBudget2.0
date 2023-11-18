package com.groupe_8.tp_api.Repository;

import com.groupe_8.tp_api.Model.Categorie;
import com.groupe_8.tp_api.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie,Long> {
    Categorie findByIdCategorie(long id);
    Categorie findByUtilisateurAndTitre(Utilisateur utilisateur, String titre);
    Categorie findByUtilisateurAndIdCategorie(Utilisateur utilisateur, long id);
    List<Categorie> findByUtilisateurIdUtilisateur(long idUser);
}
