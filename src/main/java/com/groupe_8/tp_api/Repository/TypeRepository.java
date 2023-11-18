package com.groupe_8.tp_api.Repository;

import com.groupe_8.tp_api.Model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository <Type, Long>{
    Type findByIdType(long id);
}
