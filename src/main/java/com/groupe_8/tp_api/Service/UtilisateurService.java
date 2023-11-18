package com.groupe_8.tp_api.Service;

import com.groupe_8.tp_api.Exception.NoContentException;
import com.groupe_8.tp_api.Model.Utilisateur;
import com.groupe_8.tp_api.Repository.UtilisateurRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService  {
    @Autowired
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurService(UtilisateurRepository userRepository) {

        this.utilisateurRepository = userRepository;
    }
  /*  @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Objects.requireNonNull(username);
        Utilisateur utilisateur = utilisateurRepository.findUserWithName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return utilisateur;
        Utilisateur utilisateur =utilisateurRepository.findUserWithName(username);
        if(utilisateur ==null) {
            throw new UsernameNotFoundException("L'utilisateur ne correspond pas au username" + utilisateur);
        }
        return new Utilisateur();
        }
*/

        //Le debut du methode ajouter
    public Utilisateur createUtilisateur(Utilisateur utilisateur, MultipartFile multipartFile) throws Exception{
        if (utilisateurRepository.findByEmail(utilisateur.getEmail()) == null) {

            if(multipartFile != null){
                    String location = "C:\\xampp\\htdocs\\musaka";
                    try{
                        Path rootlocation = Paths.get(location);
                        if(!Files.exists(rootlocation)){
                            Files.createDirectories(rootlocation);
                            Files.copy(multipartFile.getInputStream(),
                            rootlocation.resolve(multipartFile.getOriginalFilename()));
                            utilisateur.setPhotos("http://10.0.2.2/musaka/"
                            +multipartFile.getOriginalFilename());
                        }else{
                            try{
                                String nom = location+"\\"+multipartFile.getOriginalFilename();
                                Path name = Paths.get(nom);
                                if(!Files.exists(name)){
                                    Files.copy(multipartFile.getInputStream(),
                                    rootlocation.resolve(multipartFile.getOriginalFilename()));

                                    utilisateur.setPhotos("http://10.0.2.2/musaka/"
                                    +multipartFile.getOriginalFilename());
                                }else{
                                    Files.delete(name);
                                    Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                                    utilisateur.setPhotos("http://10.0.2.2/musaka/"+multipartFile.getOriginalFilename());
                                }
                            }catch(Exception e){
                                throw new Exception("some error");
                            }
                        }
                    }catch(Exception e){
                        throw new Exception(e.getMessage());
                    }
                }

            return utilisateurRepository.save(utilisateur);
        } else {
            throw new EntityExistsException("Cet email existe déjà");
        }
    }
//La fin du methode ajouter


    public List<Utilisateur> getAllUtilisateur(){

        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        if (utilisateurs.isEmpty())
            throw new NoContentException("Aucun utilisateur trouvé");
        return utilisateurs;
    }
    public Utilisateur getUtilisateurById(long idUtilisateur){

        Utilisateur utilisateur= utilisateurRepository.findByIdUtilisateur(idUtilisateur);
        if(utilisateur ==null)
            throw new EntityNotFoundException("cet utilisateur n'existe pas");
        return utilisateur;
    }

    public Utilisateur editutilisateur(Utilisateur utilisateur){
        Utilisateur user= utilisateurRepository.findByIdUtilisateur(utilisateur.getIdUtilisateur());
         if (user == null)
             throw new EntityNotFoundException("cet utilisateur n'existe pas");
         return utilisateurRepository.save(utilisateur);

    }

    public Utilisateur updateUtilisateur(Long id, Utilisateur utilisateur, MultipartFile multipartFile) throws Exception {
        Utilisateur existingUtilisateur = utilisateurRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Utilisateur introuvable avec l'ID : " + id));
    
        // Appliquez les modifications souhaitées à l'utilisateur existant.
        existingUtilisateur.setNom(utilisateur.getNom());
        existingUtilisateur.setPrenom(utilisateur.getPrenom());
        existingUtilisateur.setEmail(utilisateur.getEmail());
    
        // Gérez également la mise à jour de la photo de profil si une nouvelle image est fournie.
        // if (multipartFile != null) {
        //     String location = "C:\\xampp\\htdocs\\musaka";
        //     try {
        //         Path rootlocation = Paths.get(location);
        //         if (!Files.exists(rootlocation)) {
        //             Files.createDirectories(rootlocation);
        //         }
    
        //         // Gérez l'ajout ou la mise à jour de la photo de profil ici.
        //         // Vous pouvez réutiliser une partie de la logique que vous avez dans la méthode d'ajout.
        //     } catch (Exception e) {
        //         throw new Exception(e.getMessage());
        //     }
        // }
        if(multipartFile != null){
                    String location = "C:\\xampp\\htdocs\\musaka";
                    try{
                        Path rootlocation = Paths.get(location);
                        if(!Files.exists(rootlocation)){
                            Files.createDirectories(rootlocation);
                            Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                            utilisateur.setPhotos("http://10.0.2.2/musaka/"+multipartFile.getOriginalFilename());
                        }else{
                            try{
                                String nom = location+"\\"+multipartFile.getOriginalFilename();
                                Path name = Paths.get(nom);
                                if(!Files.exists(name)){
                                    Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                                    utilisateur.setPhotos("http://10.0.2.2/musaka/"+multipartFile.getOriginalFilename());
                                }else{
                                    Files.delete(name);
                                    Files.copy(multipartFile.getInputStream(),rootlocation.resolve(multipartFile.getOriginalFilename()));
                                    utilisateur.setPhotos("http://10.0.2.2/musaka/"+multipartFile.getOriginalFilename());
                                }
                            }catch(Exception e){
                                throw new Exception("some error");
                            }
                        }
                    }catch(Exception e){
                        throw new Exception(e.getMessage());
                    }
                }
    
        return utilisateurRepository.save(existingUtilisateur);
    }
    


    public String deleteUtilisateurById(Utilisateur utilisateur){
        Utilisateur user= utilisateurRepository.findByIdUtilisateur(utilisateur.getIdUtilisateur());
        if (user == null)
            throw new EntityNotFoundException("Désolé  l'utilisateur à supprimé n'existe pas");
        utilisateurRepository.delete(utilisateur);
        return "Utilisateur Supprimé";
    }
    public Utilisateur connectionUtilisateur(String email, String motDePasse) {
        Utilisateur user = utilisateurRepository.findByEmailAndMotDePasse(email, motDePasse);
        if (user == null) {
            throw new EntityNotFoundException("Cet utilisateur n'existe pas");
        }

        return user;
    }
}
