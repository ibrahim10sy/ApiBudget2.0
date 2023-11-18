package com.groupe_8.tp_api.Service;

import com.groupe_8.tp_api.Exception.BadRequestException;
import com.groupe_8.tp_api.Model.Budget;
import com.groupe_8.tp_api.Model.Categorie;
import com.groupe_8.tp_api.Model.Utilisateur;
import com.groupe_8.tp_api.Repository.BudgetRepository;
import com.groupe_8.tp_api.Repository.CategorieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategorieService {
    @Autowired
    private CategorieRepository categorieRepository;

    private BudgetRepository budgetRepository;

    public Categorie creer(Categorie categorie){
        Categorie categorieVerif = categorieRepository.findByUtilisateurAndTitre(categorie.getUtilisateur(), categorie.getTitre());
        if (categorieVerif != null )
            throw new BadRequestException("Désolé cet type de catégorie existe déjà");
        return categorieRepository.save(categorie);
    }
    public List<Categorie> lire(){

        return categorieRepository.findAll();
    }

    public List<Categorie> getAllCatUser(long idUser){
        List<Categorie>  categoriesListe = categorieRepository.findByUtilisateurIdUtilisateur(idUser);

        if(categoriesListe.isEmpty()){
            throw new EntityNotFoundException("Aucun categorie trouvé");
        }

        return categoriesListe;
    }
    public Categorie modifier(Categorie categorie) {
        Categorie categorieVerif = categorieRepository.findById(categorie.getIdCategorie()).orElse(null);
        if (categorieVerif != null) {
            return categorieRepository.save(categorie);
        }
        return null;
    }
    public String supprimer(Long idCategorie) {
        Categorie categorie = categorieRepository.findByIdCategorie(idCategorie);
        if (categorie == null)
            throw new BadRequestException("Cette categorie n'existe pas");

        Budget budget = budgetRepository.findByCategorie(categorie);
        if (budget != null)
            throw new BadRequestException("Vous ne pouvez pas supprimer une categorie qui est déjà lier à un budget");

        categorieRepository.deleteById(idCategorie);
        return "succes";
    }
}
