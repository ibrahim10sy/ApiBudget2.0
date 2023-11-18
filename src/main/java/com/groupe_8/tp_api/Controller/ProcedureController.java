package com.groupe_8.tp_api.Controller;

import com.groupe_8.tp_api.Model.ProcedureStockes;
import com.groupe_8.tp_api.Service.ProcedureService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/procedure")
public class ProcedureController {

    @Autowired
    private ProcedureService procedureService;

    //@CrossOrigin(origins = "http://localhost:8080")
    @GetMapping("/expenses/{userId}")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<Map<String, Object>> getTotalExpensesByCategory(@PathVariable Long userId) {
        return procedureService.getTotalExpensesByCategory(userId);
    }

   /* @GetMapping("/expenses/{userId}")
    @Operation(summary = "Donner la statistique des depenses par categorie")
    public List<ProcedureStockes> getTotalExpensesByCategory(@PathVariable Long userId) {
        return procedureService.getTotalExpensesByCategory(userId);
    }*/
}
