package com.groupe_8.tp_api.Controller;


import com.groupe_8.tp_api.Model.Notification;
import com.groupe_8.tp_api.Service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("notification")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @GetMapping("/list/{idUser}")
    public ResponseEntity<List<Notification>> listNotif(@PathVariable long idUser){
        return new ResponseEntity<>(notificationService.getAllNotif(idUser), HttpStatus.OK);
    }
    /* @Autowired
    private NotificationService notificationService;

   @PostMapping("/send")
    @Operation(summary = "Création d'une notification")
    public void envoieNotification(@Valid @RequestBody Budget budget){
        notificationService.sendNotification(budget);
    }*/
}
