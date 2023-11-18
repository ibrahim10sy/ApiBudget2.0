package com.groupe_8.tp_api.Service;

import com.groupe_8.tp_api.Exception.BadRequestException;
import com.groupe_8.tp_api.Exception.NoContentException;
import com.groupe_8.tp_api.Model.Budget;
import com.groupe_8.tp_api.Model.Categorie;
import com.groupe_8.tp_api.Model.Depenses;
import com.groupe_8.tp_api.Model.Utilisateur;
import com.groupe_8.tp_api.Repository.BudgetRepository;
import com.groupe_8.tp_api.Repository.CategorieRepository;
import com.groupe_8.tp_api.Repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;

@Service
public class BudgetService {

    @Autowired // Injection de dependance
    private BudgetRepository budgetRepository; // Interface BudgetRepository

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    NotificationService notificationService;
    @Autowired
    private TransfertService transfertService;
    //Fonction pour la crÃ©ation d'un budget, elle prend un entrÃ©e un objet budget
    public Budget createBudget(Budget budget){

        Utilisateur utilisateur = utilisateurRepository.findByIdUtilisateur(budget.getUtilisateur().getIdUtilisateur()); // Capture de l'utilisateur du budget
        Categorie categorie = categorieRepository.findByUtilisateurAndIdCategorie(utilisateur,budget.getCategorie().getIdCategorie()); // Capture de catÃ©gorie du budget
        LocalDate toDay = LocalDate.now(); // Obtention de la date du jour en type LocalDate
        LocalDate dateDebut = budget.getDateDebut(); // Capture de la date de dÃ©but du buget Ã  inserer
        //LocalDate dateFin ; // DÃ©claration du variable de type LocalDate qui vas nous servir de personnaliser la date de fin du budget Ã  inserer
        LocalDate jourMaxDuMois = dateDebut.with(TemporalAdjusters.lastDayOfMonth()); // Obtention du dernier date du mois actuel
        budget.setDateFin(jourMaxDuMois);
        budget.setMontantRestant(budget.getMontant());
        //=============================================================================================================

        if (categorie == null)
            throw new BadRequestException("Cet categorie n'existe pas");

        if (dateDebut.getMonthValue() < toDay.getMonthValue() || (dateDebut.getYear() != toDay.getYear()))
            throw new BadRequestException("Veuillez choisie une date dans "+toDay.getMonth()+" "+toDay.getYear());

        Budget verifBudget = budgetRepository.findByUtilisateurAndCategorieAndDateFin(utilisateur,categorie,budget.getDateFin());
        if (verifBudget != null )
            throw new BadRequestException("DÃ©solÃ© vous avez dÃ©jÃ  un budget de catÃ©gorie "+categorie.getTitre()+" pour ce mois");

        if(budget.getMontantAlerte() >= budget.getMontant())
            throw  new BadRequestException(("DesolÃ© votre montant d'alerte ne peut pas depassÃ© le montant de votre budget"));

        Budget lastBudget = budgetRepository.findFirstByUtilisateurAndCategorieOrderByDateFinDesc(utilisateur,categorie);
        if (lastBudget != null){
            if (budget.getDateFin().isBefore(lastBudget.getDateDebut()))
                throw new BadRequestException("DÃ©sole vous avez dÃ©jÃ  programmÃ© un budget supÃ©rieur Ã  ce mois");
            budget.setParent(lastBudget);
            if (lastBudget.getDateFin().isBefore(toDay)) {
                if (lastBudget.getMontantRestant() > 0) {
                    budget.setMontant(budget.getMontant() + lastBudget.getMontantRestant());
                    budget.setMontantRestant(budget.getMontant());
                    transfertService.creer(budget,lastBudget);
                }
            }
        }

        System.out.println("Budget service "+budget);
        return budgetRepository.save(budget); // On sauvegarde cet budget dans notre base de donnÃ©e



    }

    // Fonction qui permet de modifier un budget, elle prend un entrÃ©e un objet budget
    public Budget updateBudget(Budget budget){

        // VÃ©rification de l'existance du budget Ã  modifier dans la base de donnÃ©e Ã  travers son id
        Budget budgetVerif = budgetRepository.findByIdBudget(budget.getIdBudget());

        // Si budgetVerif est null, alors le budget n'a pas Ã©tÃ©t trouvÃ© et le systÃ¨me lÃ¨vera une exception
        if (budgetVerif == null)
            throw new EntityNotFoundException("DÃ©solÃ© cet budget n'existe pas");

        LocalDate toDay = LocalDate.now(); // Obtention de la date du jour en type LocalDate
        LocalDate dateDebut = budget.getDateDebut(); // Capture de la date de dÃ©but du buget Ã  inserer
        LocalDate dateFin = dateDebut.with(TemporalAdjusters.lastDayOfMonth()) ; // DÃ©claration du variable de type LocalDate qui vas nous servir de personnaliser la date de fin du budget Ã  inserer
        budget.setDateFin(dateFin);

        /* Verification si la date de debut du budgetVerif est different de la date du budget Ã  modifier, alors le systÃ¨me lÃ¨vera une exception */
        if(!budgetVerif.getDateDebut().equals(budget.getDateDebut())){
            /* Verification si le budget Ã  modifier possÃ¨de dÃ©jÃ  une depanse */
            if (!budgetVerif.getDepenses().isEmpty())
                throw new BadRequestException("DÃ©solÃ© vous ne pouvez plus modifier la date de cet budget car possÃ¨de dÃ©jÃ  au moins une depense");

            if (budgetVerif.getDateFin().isBefore(toDay))
                throw new BadRequestException("DÃ©solÃ© vous ne pouvez pas modifier un budget dÃ©jÃ  expirer");

            if (dateDebut.getMonthValue() < budgetVerif.getDateDebut().getMonthValue() || (dateDebut.getYear() != toDay.getYear()))
                throw new BadRequestException("Veuillez choisie une date dans "+toDay.getMonth()+" "+toDay.getYear());

            //Budget budgetPrecedant = budgetRepository.findFirstByUtilisateurAndCategorieAndDateFinIsBeforeOrderByDateFinDesc(budgetVerif.getUtilisateur(),
                    //budgetVerif.getCategorie(), budgetVerif.getDateDebut());
            Budget budgetPrecedant = budgetVerif.getParent();

            if (budgetPrecedant != null){
                if(budget.getDateDebut().isBefore(budgetPrecedant.getDateFin()) || budget.getDateDebut().equals(budgetPrecedant.getDateFin()))
                    throw new BadRequestException("DÃ©solÃ© votre ne doit pas commencÃ© Ã  une date infÃ©ieure Ã  la da te de fin du budget prÃ©cÃ©dant qui est "+budgetPrecedant.getDateFin());
            }

            //Budget budgetSuivant = budgetRepository.findFirstByUtilisateurAndCategorieAndDateDebutIsAfterOrderByDateFinDesc(budgetVerif.getUtilisateur(),
                    //budgetVerif.getCategorie(),budgetVerif.getDateFin());
            Budget budgetSuivant = budgetVerif.getEnfant();

            if (budgetSuivant != null){
                if(budget.getDateDebut().isAfter(budgetSuivant.getDateDebut()) || budget.getDateDebut().equals(budgetSuivant.getDateDebut()))
                    throw new BadRequestException("DÃ©solÃ© votre ne doit pas commencÃ© Ã  une date superieure Ã  la da te de debut du budget suivant qui est "+budgetSuivant.getDateDebut());
            }

            //dateFin = dateDebut.plusDays(30); // On ajoute 30 jours Ã  la date de debut du budget Ã  inserer, et on l'affecte Ã  la variable dateFin
            //budget.setDateFin(dateFin); // On donne cette date Ã  la date de fin du budget Ã  inserer
        }

        if(budget.getMontantRestant() != budgetVerif.getMontantRestant())
            throw new BadRequestException("DesolÃ© vous ne pouvez pas modifier le montant restant de votre budget ");

        if (budget.getMontant() < (budgetVerif.getMontant() - budgetVerif.getMontantRestant()))
            throw new BadRequestException("DesolÃ© vous ne pouvez pas modifier le montant de votre budget ent deÃ§ue de "+(budgetVerif.getMontant() - budgetVerif.getMontantRestant()));

        if (budget.getMontant() > (budgetVerif.getMontant() - budgetVerif.getMontantRestant())){
            budget.setMontantRestant(budget.getMontant() - (budgetVerif.getMontant() - budgetVerif.getMontantRestant()));
        }

        if(budget.getMontantAlerte() >= budget.getMontant())
            throw  new BadRequestException(("DesolÃ© votre montant d'alerte ne peut pas depassÃ© le montant de votre budget"));

        //dateFin = dateDebut.plusDays(30); // On ajoute 30 jours Ã  la date de debut du budget Ã  inserer, et on l'affecte Ã  la variable dateFin
        //budget.setDateFin(dateFin);

        if (budget.getMontantRestant() <= budget.getMontantAlerte())
            notificationService.sendNotification(budget);

        /* Dans le cas contraire on sauvegarde la modification du budget dans la base de donnÃ©e
        et retourne le budget modifiÃ©
         */
        return budgetRepository.save(budget);
    }

    //Fonction qui retourne la somme de l'ensemble des budget non despasser
    public HashMap<String,Object> sommeOfAllBudegtNotFinich(long idUser){
        HashMap<String,Object> hashMap = new HashMap<>();
        Integer[][] result = budgetRepository.getSommeOfTotalBudgetNotFinish(idUser);
        if(result[0][0] == null || result[0][1] == null){
            hashMap.put("Total",0);
            hashMap.put("Restant",0);
        }else{
            hashMap.put("Total",result[0][0]);
            hashMap.put("Restant",result[0][1]);
        }
        return hashMap;
    }

    //Fonction qui permet retourner la liste de tous les budget, elle ne prend rien en paramÃ¨tre
    public List<Budget> allBudget(){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Budget> budgetList = budgetRepository.findAll();

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new NoContentException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

    public List<Budget> allBudgetByUser(long idUser){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Budget> budgetList = budgetRepository.findByUtilisateurIdUtilisateur(idUser);

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new EntityNotFoundException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

    public List<Budget> searchBudget(long idUser,String desc){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Budget> budgetList = budgetRepository.findByUtilisateurIdUtilisateurAndDescriptionContaining(idUser,desc);

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new EntityNotFoundException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

    public List<Budget> sortBudgetByMonthAndYear(long idUser, String date){

        // Obtention de tous les budget dans la base de donnÃ©es
        List<Budget> budgetList = budgetRepository.getBudgetByMonthAndYear(idUser, "%"+date+"%");

        // Si la liste est vide, le systÃ¨me lÃ¨vera une exception
        if (budgetList.isEmpty())
            throw new EntityNotFoundException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me retourne la liste
        return budgetList;
    }

    // Obtention d'un budget Ã  travers son id
    public Budget getBudgetById(long id){

        // Obtention d'un budget dans la base de donnÃ©e Ã  travers son id
        Budget budget = budgetRepository.findByIdBudget(id);

        // Si budget est null, alors le budget n'a pas Ã©tÃ©t trouvÃ© et le systÃ¨me lÃ¨vera une exception
        if (budget == null)
            throw  new EntityNotFoundException("Aucun budget trouvÃ©");

        // Dans le cas contraire le systÃ¨me enregistre et retourne le budget enregistrÃ©
        return budget;
    }

    // Fonction qui permet de supprimer un budget
    public String deleteBudget(long idBudget){

        // VÃ©rification de l'existance du budget Ã  modifier dans la base de donnÃ©e Ã  travers son id
        Budget budgetVerif = budgetRepository.findByIdBudget(idBudget);

        // Si budgetVerif est null, alors le budget n'a pas Ã©tÃ©t trouvÃ© et le systÃ¨me lÃ¨vera une exception
        if (budgetVerif == null)
            throw  new EntityNotFoundException("Aucun budget trouvÃ©");

        if (budgetVerif.getDateFin().isBefore(LocalDate.now()))
            throw new BadRequestException("DÃ©solÃ© vous ne pouvez pas modifier un budget dÃ©jÃ  expirer");

        if (!budgetVerif.getDepenses().isEmpty())
            throw new BadRequestException("DÃ©solÃ© vous ne pouvez plus supprimer cet budget car possÃ¨de dÃ©jÃ  au moins une depense");

        if(budgetVerif.getEnfant() != null)
            throw new BadRequestException("DÃ©solÃ© vous ne pouvez pas supprimer ce budget car il y'a un budget aprÃ¨s luis");

        // Dans le cas contraire le systÃ¨me supprime le budget puis retourne un message succes
        budgetRepository.deleteById(idBudget);
        return "succes";
    }

    public void updateMontantRestant(Depenses depenses, Object... depensesMotif){
        //int defaultMontant = depenses.getMontant();
        int montantDepense = 0;

        // Recuperer le budget en cours de la mÃªme catÃ©gorie de depense
        Budget budget = budgetRepository.findByIdBudget(depenses.getBudget().getIdBudget());
        if (budget == null)
            throw  new EntityNotFoundException("Vous n'avez aucun budget pour ce categorie de depenses");
        if (budget.getDateFin().isBefore(LocalDate.now()))
            throw new BadRequestException("ce budjet n'est plus valide");
        if (budget.getDateDebut().isAfter(LocalDate.now()))
            throw new BadRequestException("ce budjet n'est pas encore commencÃ©");

        if(depensesMotif.length != 0 ){
            if (depensesMotif[0] instanceof Depenses){
                Depenses depensesVerif = (Depenses) depensesMotif[0];
                if (depenses.getMontant() != depensesVerif.getMontant()){
                    montantDepense = depenses.getMontant()-depensesVerif.getMontant();
                }
            } else {
                String motif = (String) depensesMotif[0];
                if (!motif.equals("sup"))
                    throw  new BadRequestException("Motif incorrect");
                montantDepense = -depenses.getMontant();
            }
        } else
            montantDepense = depenses.getMontant(); // Recupere montant de la depense

        int montantRestantBudget = budget.getMontantRestant();

        if (montantDepense > montantRestantBudget)
            throw new BadRequestException("DesolÃ© le montant de votre depense depasse le montant restant de votre budget");

        int montantRestantBudgetActuel = montantRestantBudget - montantDepense;
        budget.setMontantRestant(montantRestantBudgetActuel);
        if (montantRestantBudgetActuel <= budget.getMontantAlerte())


        budgetRepository.save(budget);


    }

    public void transfertBudget(Budget budget, Budget lastBudget){
        budget.setMontant(budget.getMontant() + lastBudget.getMontantRestant());
        budget.setMontantRestant(budget.getMontantRestant() + lastBudget.getMontantRestant());
        transfertService.creer(budget,lastBudget);
        budgetRepository.save(budget);
    }

}
