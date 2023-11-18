package com.groupe_8.tp_api.Controller;

import com.groupe_8.tp_api.Model.Budget;
import com.groupe_8.tp_api.Model.Notification;
import com.groupe_8.tp_api.Service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    /* @Autowired
    private NotificationService notificationService;

   @PostMapping("/send")
    @Operation(summary = "Création d'une notification")
    public void envoieNotification(@Valid @RequestBody Budget budget){
        notificationService.sendNotification(budget);
    }*/
}
