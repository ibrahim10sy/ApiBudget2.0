package com.groupe_8.tp_api.Repository;

import com.groupe_8.tp_api.Model.Notification;
import com.groupe_8.tp_api.Model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

    Notification findByIdNotification(long id);
    List<Notification> findByUtilisateur(Utilisateur utilisateur);
    List<Notification> findByUtilisateurIdUtilisateur(long idUser);
}
