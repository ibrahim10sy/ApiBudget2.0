package com.groupe_8.tp_api.Controller;

import com.groupe_8.tp_api.Model.Categorie;
import com.groupe_8.tp_api.Service.CategorieService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/Categorie")
public class CategorieController {
    private final CategorieService categorieService;
    @PostMapping("/creer")
    @Operation(summary = "création d'une categorie")
      public String creer(@RequestBody Categorie categorie){
         categorieService.creer(categorie);
         return "La categorie "+ categorie.getTitre() +" a été créée";
    }
    @GetMapping("/lire")
    @Operation(summary = "affichage des categories")
    public List<Categorie> lire(){
        return categorieService.lire();
    }
   /* @GetMapping("/list/{idUser}")
    @Operation(summary = "affichage des categories à travers l'id de utilisateur")
    public ResponseEntity<List<Categorie>> listeCategorieByUseur(@PathVariable long id){
        return  new ResponseEntity<>(categorieService.getAllCatUser(id), HttpStatus.OK);
    }*/
    @GetMapping("/list/{idUser}")
    @Operation(summary = "affichage des categories à travers l'id de utilisateur")
    public ResponseEntity<List<Categorie>> listeCategorieByUseur(@PathVariable long idUser){
        return  new ResponseEntity<>(categorieService.getAllCatUser(idUser), HttpStatus.OK);
    }

    @PutMapping("/modifier")
    @Operation(summary = "Modification d'un categorie")
    public ResponseEntity<Object> modifier(@RequestBody Categorie categorie)

    {
        Categorie categorieModifiee = categorieService.modifier(categorie);
        if (categorieModifiee != null) {
            return ResponseEntity.ok(categorieModifiee);
        } else {
            return ResponseEntity.notFound().build();
        }
       // return categorieModifiee+" a été remplacée par l'ancienne";
    }
    @DeleteMapping("/Supprimer/{idCategorie}")
    @Operation(summary = "suppression  d'un categorie")
      public String supprimer(@PathVariable long idCategorie){
       return categorieService.supprimer(idCategorie);
      }
}
