package com.groupe_8.tp_api.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.groupe_8.tp_api.Model.Utilisateur;
import com.groupe_8.tp_api.Service.UtilisateurService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("utilisateur")
@AllArgsConstructor
public class UtilisateurController {

    private final UtilisateurService utilisateurService;
    // @PostMapping("/create")
    // @Operation(summary = "Création  d'un utilisateur")
    // public ResponseEntity<Utilisateur> createUtilisateur(@Valid @RequestBody Utilisateur utilisateur){
    //      return new ResponseEntity<>(utilisateurService.createUtilisateur(utilisateur), HttpStatus.OK);
    //  }
    // @PostMapping("/create2")
    // @Operation(summary = "Création  d'un utilisateur")
    // public ResponseEntity<Utilisateur> createUtilisateurByImg(@Valid @RequestBody Utilisateur utilisateur, @RequestParam ("photos")MultipartFile file) throws IOException {
    //     System.out.println("Fichier==========="+file.getOriginalFilename());

    //     String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    //     utilisateur.setPhotos(fileName);

    //     Utilisateur savedUser = utilisateurService.createUtilisateur(utilisateur);

    //     String uploadDir = "user-photos/" + savedUser.getIdUtilisateur();

    //     FileUploadUtil.saveFile(uploadDir, fileName, file);

    //     return new ResponseEntity<>((savedUser), HttpStatus.OK);
    // }

    ///////////////////////////////////////////
        @PostMapping("/create")
        @Operation(summary = "Création d'un utilisateur")
        public ResponseEntity<Utilisateur> createUtilisateur(
            @Valid @RequestParam("utilisateur") String utilisateurString,
            @RequestParam(value ="photo", required=false) MultipartFile multipartFile) throws Exception {

            Utilisateur utilisateur = new Utilisateur();
            try{
                utilisateur = new JsonMapper().readValue(utilisateurString, Utilisateur.class);
            }catch(JsonProcessingException e){
                throw new Exception(e.getMessage());
            }
            
            Utilisateur savedUser = utilisateurService.createUtilisateur(utilisateur,multipartFile);

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        }

    //////////////////////////////////////////
    //////////////////////////

@PutMapping("/update2/{id}")
@Operation(summary = "Mise à jour d'un utilisateur par ID")
public ResponseEntity<Utilisateur> updateUtilisateur(
    @PathVariable Long id,
    @Valid @RequestParam("utilisateur") String utilisateurString,
    @RequestParam(value ="photo", required=false) MultipartFile multipartFile) throws Exception {

    Utilisateur utilisateur = new Utilisateur();
    try {
        utilisateur = new JsonMapper().readValue(utilisateurString, Utilisateur.class);
    } catch (JsonProcessingException e) {
        throw new Exception(e.getMessage());
    }

    Utilisateur updatedUser = utilisateurService.updateUtilisateur(id, utilisateur, multipartFile);

    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
}

    ////////////////////////////


     @GetMapping("/read")
     @Operation(summary = "Affichage de la  liste des utilisateurs")
    public ResponseEntity<List<Utilisateur>> getUtilisateur(){
        return new ResponseEntity<>(utilisateurService.getAllUtilisateur(),HttpStatus.OK);}
   @GetMapping("/read/{id}")
   @Operation(summary = "Affichage  d'un utilisateur")
    public ResponseEntity<Utilisateur> getUtilisateurById(@Valid @PathVariable long id){
        return new ResponseEntity<>(utilisateurService.getUtilisateurById(id),HttpStatus.OK) ;}
        
   @PutMapping("/update")
   @Operation(summary = "Modification d'un utilisateur")
    public ResponseEntity<Utilisateur>  editUtilisateur(@Valid @RequestBody Utilisateur utilisateur){
        return new ResponseEntity<>( utilisateurService.editutilisateur(utilisateur),HttpStatus.OK);}

    @DeleteMapping("/delete")
    @Operation(summary = "Suppression d'un utilisateur")
    public ResponseEntity<String> deleteUtilisateurById(@Valid @RequestBody Utilisateur utilisateur){
        return new ResponseEntity<>(utilisateurService.deleteUtilisateurById(utilisateur),HttpStatus.OK);}

    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur")
    public Object connexion(@RequestParam("email") String email,
                            @RequestParam("motDePasse") String motDePasse) {
        return utilisateurService.connectionUtilisateur(email, motDePasse);
    }
}

