package com.groupe_8.tp_api.Service;

import com.groupe_8.tp_api.Exception.BadRequestException;
import com.groupe_8.tp_api.Exception.NoContentException;
import com.groupe_8.tp_api.Model.*;
import com.groupe_8.tp_api.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class DepensesService {

    @Autowired
    private  DepensesRepository depensesRepository;
    @Autowired
    private  BudgetRepository budgetRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private  BudgetService budgetService;
    @Autowired
    private TransfertRepository transfertRepository;
    @Autowired
    NotificationService notificationService;


    public Depenses creer(Depenses depenses) {
        LocalDate dateDepenses = depenses.getDate();
        Budget budget = budgetRepository.findByIdBudgetAndUtilisateur(depenses.getBudget().getIdBudget(), depenses.getUtilisateur());
        Utilisateur user = utilisateurRepository.findByIdUtilisateur(depenses.getUtilisateur().getIdUtilisateur());

        if(user == null)
            throw new NoContentException("non trouvé");
        // Vérifier si le montant de la dépense ne dépasse pas le montant restant du budget
        if (budget.getMontantRestant() < depenses.getMontant()) {
            throw new BadRequestException("Le montant de la dépense ne peut pas être supérieur à celui du budget restant");
        }else if(budget.getMontantRestant() == 0){
            throw new BadRequestException("Impossible d'effectue cette depense car le montant budget est 0");
        }
        System.out.println("verif condition "+depenses.toString());
        if (depenses.getBudget().getMontantRestant() <= depenses.getBudget().getMontantAlerte()) {
            System.out.println("Envoie mail DE");
            notificationService.sendNotificationDepenses(depenses);
            System.out.println("Apres envoie mail DE ");
            System.out.println("Apres envoie mail DE");
        }
        // Mettre à jour le montant restant du budget
        budget.setMontantRestant(budget.getMontantRestant() - depenses.getMontant());
        budgetRepository.save(budget);



        // Vérifier et gérer les transferts si nécessaire
        if (budget.getDepenses().isEmpty()) {
            Budget lastBudget = budget.getParent();
            if (lastBudget != null) {
                Transfert transfert = transfertRepository.findByBudget(lastBudget);
                if (transfert == null) {
                    if (lastBudget.getMontantRestant() > 0) {
                        budgetService.transfertBudget(budget, lastBudget);
                    }
                }
            }
        }

        // Enregistrer la dépense mise à jour
        return depensesRepository.save(depenses);
    }

   /* public Depenses creer(Depenses depenses){
        LocalDate dateDepenses = depenses.getDate();
        Budget budget = budgetRepository.findByIdBudgetAndUtilisateur(depenses.getBudget().getIdBudget(), depenses.getUtilisateur());
        if (budget == null)
            throw  new EntityNotFoundException("Vous n'avez aucun budget pour ce categorie de depenses");
        if (budget.getDateFin().isBefore(LocalDate.now()))
            throw new BadRequestException("ce budget n'est plus valide");
        if (budget.getDateDebut().isAfter(LocalDate.now()))
            throw new BadRequestException("ce budget n'est pas encore commencer");

        Categorie categorie = budget.getCategorie();
        Utilisateur user = utilisateurRepository.findByIdUtilisateur(depenses.getUtilisateur().getIdUtilisateur());
        Type type = typeRepository.findByIdType(depenses.getType().getIdType());
        Depenses depensesVerif = null;
        if(categorie == null)
            throw new BadRequestException("Desolé cette catégorie n'existe pas");

        if (user == null)
            throw new BadRequestException("User invalid");

        if(dateDepenses.isBefore(budget.getDateDebut()) || dateDepenses.isAfter(LocalDate.now()))
            throw new BadRequestException("Entrez une date correcte");

        if(depenses.getBudget().getMontant() < depenses.getMontant()){
            throw new BadRequestException("Le montant du dépense ne peut pas être supérieur à celle du budget");
        }else if(depenses.getBudget().getMontant() > depenses.getMontant()) {
            budgetService.updateMontantRestant(depenses);
        } else if (depenses.getBudget().getMontant() == 0) {
            throw new BadRequestException("Impossible d'effectue une dépense");
        } else {
            throw new BadRequestException("Vous ne pouvez plus effectue une dépense");
        }

       switch (type.getTitre()){
            case "quotidienne" :
                depensesVerif = depensesRepository.findByUtilisateurAndBudgetAndTypeAndDescriptionAndDate(user,budget,type,depenses.getDescription(),dateDepenses);
                if (depensesVerif != null)
                    throw  new BadRequestException("Desole vous avez deja effectué votre depenses journalière de " +depenses.getDescription());

                budgetService.updateMontantRestant(depenses);
                break;
            case  "hebdomendaire" :
                depensesVerif = depensesRepository.findFirstByUtilisateurAndBudgetAndTypeAndDescriptionOrderByDateDesc(user,
                        budget,type,depenses.getDescription());
                if (depensesVerif != null){
                    long diff = ChronoUnit.DAYS.between(depensesVerif.getDate(),dateDepenses) < 0 ? -ChronoUnit.DAYS.between(depensesVerif.getDate(),dateDepenses) : ChronoUnit.DAYS.between(depensesVerif.getDate(),dateDepenses);
                    if (diff <= 7)
                        throw  new BadRequestException("Desole vous avez deja effectué votre depenses hebdomadaire de " +depenses.getDescription());

                }
                budgetService.updateMontantRestant(depenses);
                break;
            case "mensuelle" :
                depensesVerif = depensesRepository.findFirstByUtilisateurAndBudgetAndTypeAndDescriptionOrderByDateDesc(user,
                        budget,type,depenses.getDescription());
                if (depensesVerif != null){
                    long diff = ChronoUnit.DAYS.between(depensesVerif.getDate(),dateDepenses) < 0 ? -ChronoUnit.DAYS.between(depensesVerif.getDate(),dateDepenses) : ChronoUnit.DAYS.between(depensesVerif.getDate(),dateDepenses);
                    if (diff <= 30)
                        throw  new BadRequestException("Desole vous avez deja effectué votre depenses mensuelle de " +categorie.getTitre());
                }
                budgetService.updateMontantRestant(depenses);
                break;
            default:
                throw  new BadRequestException("Ce type de depense n'existe pas");

        }

            if (budget.getDepenses().isEmpty()){
                Budget lastBudget = budget.getParent();
                if (lastBudget != null){
                    Transfert transfert = transfertRepository.findByBudget(lastBudget);
                    if (transfert == null){
                        if (lastBudget.getMontantRestant() > 0)
                            budgetService.transfertBudget(budget,lastBudget);
                    }
                }
            }


        return depensesRepository.save(depenses);
    }*/
    public List<Depenses> lire(){
        List<Depenses> depensesList = depensesRepository.findAll();
        if (depensesList.isEmpty())
            throw new NoContentException("Aucune depenses trouvée");
        return depensesList;

    }
    public List<Depenses> lireParUser(long idUtilisateur){
        List<Depenses> depensesList = depensesRepository.findByUtilisateurIdUtilisateur(idUtilisateur);
        if (depensesList.isEmpty())
            throw new EntityNotFoundException("Aucune depenses trouvée");
        return depensesList;

    }

    public List<Depenses> lireParBudget(long idBudget){
        List<Depenses> depensesList = depensesRepository.findByBudgetIdBudget(idBudget);
        if (depensesList.isEmpty())
            throw new EntityNotFoundException("Aucune depenses trouvée");
        return depensesList;

    }

    public Depenses getDepenseById(long idDepenses){
        Depenses depenses=depensesRepository.findByIdDepenses(idDepenses);
        if (depenses == null)
            throw  new EntityNotFoundException("Cette depense n'existe pas");
        return depenses;
    }
    public Depenses modifier(Depenses depenses){
        Depenses depensesVerif = depensesRepository.findByIdDepenses(depenses.getIdDepenses());
        if (depensesVerif == null)
            throw  new EntityNotFoundException("cette depenses n'existe pas");
        if(depensesVerif.getBudget().getDateFin().isBefore(LocalDate.now()))
            throw new BadRequestException("Vous ne pouvez pas modifier ce depense car son budget est expiré");
        if (!depensesVerif.getDate().equals(depenses.getDate()))
            throw new BadRequestException("Vous ne pouvez pas changer la date lors de la modification");
        if (depenses.getMontant() != depensesVerif.getMontant())
            budgetService.updateMontantRestant(depenses,depensesVerif);

        return depensesRepository.save(depenses);
    }
    public String Supprimer(long id){
        Depenses depensesVerif = depensesRepository.findByIdDepenses(id);
        if (depensesVerif == null)
            throw  new EntityNotFoundException("cette depenses n'existe pas");

        Budget budget = depensesVerif.getBudget();
        if (budget.getDateFin().isBefore(LocalDate.now()))
            throw new BadRequestException("Vous ne pouvez pas supprimer ce depense car son budget est exispiré");

        budgetService.updateMontantRestant(depensesVerif,"sup");

        depensesRepository.delete(depensesVerif);

        return "Depenses Supprimer";
    }
}
