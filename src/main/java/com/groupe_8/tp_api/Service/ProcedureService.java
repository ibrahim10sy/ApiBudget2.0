/*package com.groupe_8.tp_api.Service;

import com.groupe_8.tp_api.Model.ProcedureStockes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ProcedureService {


    private JdbcTemplate jdbcTemplate;
    public List<ProcedureStockes> getTotalExpensesByCategory(Long userId) {
        String query = "CALL GetTotalExpensesByCategory(?)";
        return jdbcTemplate.query(query, new Object[]{userId}, new BeanPropertyRowMapper<>(ProcedureStockes.class));
    }
}*/


package com.groupe_8.tp_api.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProcedureService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> getTotalExpensesByCategory(Long userId) {
        String query = "CALL GetTotalExpensesByCategory(?)";
        return jdbcTemplate.queryForList(query, userId);
    }
}