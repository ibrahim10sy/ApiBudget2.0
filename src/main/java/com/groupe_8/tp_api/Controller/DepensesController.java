package com.groupe_8.tp_api.Controller;

import com.groupe_8.tp_api.Model.Depenses;
import com.groupe_8.tp_api.Service.DepensesService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("Depenses")
@AllArgsConstructor
public class DepensesController {
    @Autowired
    private DepensesService depensesService;
    @PostMapping("/create")
    @Operation(summary = "création de dépense")
    public Depenses create(@Valid @RequestBody Depenses depenses){
        System.out.println("Depense creer "+depenses);
        return depensesService.creer(depenses);
    }
    @PutMapping("/update")
    @Operation(summary = "Modification d'un dépense")
    public  Depenses update(@Valid @RequestBody Depenses depenses){
        return depensesService.modifier(depenses);
    }
    @GetMapping("/read")
    @Operation(summary = "Affichage des dépenses")
    public List<Depenses> read(){
        return depensesService.lire();
    }
    @GetMapping("{idUtilisateur}/read")
    @Operation(summary = "Affichage des dépenses")
    public List<Depenses> readByUser(@PathVariable long idUtilisateur){
        return depensesService.lireParUser(idUtilisateur);
    }
    @GetMapping("/{idBudget}")
    @Operation(summary = "Affichage des dépenses")
    public List<Depenses> readByBudget(@PathVariable long idBudget){
        return depensesService.lireParBudget(idBudget);
    }
    @GetMapping("/read/{id}")
    @Operation(summary = "Affichage d'un dépense")
    public Depenses readById(@Valid @PathVariable long id){
        return depensesService.getDepenseById(id);
    }
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Suppression d'un dépense")
    public  String delete(@PathVariable long id){
        return depensesService.Supprimer(id);
    }
}
