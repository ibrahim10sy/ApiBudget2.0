package com.groupe_8.tp_api.Repository;

import com.groupe_8.tp_api.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    public Utilisateur findByEmail(String email);

    public Utilisateur findByEmailAndMotDePasse(String email, String mot_de_passe);
    Utilisateur findByIdUtilisateur(long id);


}
