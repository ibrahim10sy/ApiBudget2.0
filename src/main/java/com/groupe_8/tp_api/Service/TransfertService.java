package com.groupe_8.tp_api.Service;

import com.groupe_8.tp_api.Model.Budget;
import com.groupe_8.tp_api.Model.Transfert;
import com.groupe_8.tp_api.Repository.TransfertRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class TransfertService {
    @Autowired
    private TransfertRepository transfertRepository;
    @Autowired
    private NotificationService notificationService;
    public void creer (Budget budget,Budget lastbudget){
        Transfert transfert =new Transfert();
        transfert.setMontant(lastbudget.getMontantRestant());
       transfert.setBudget(lastbudget);
       notificationService.sendNotifTransf(budget,lastbudget);
       transfertRepository.save(transfert);
    }

}
