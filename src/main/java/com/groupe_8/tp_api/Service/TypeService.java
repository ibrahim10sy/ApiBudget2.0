package com.groupe_8.tp_api.Service;

import com.groupe_8.tp_api.Model.Type;
import com.groupe_8.tp_api.Repository.TypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TypeService {
    @Autowired
    private TypeRepository typeRepository;

    public Type creer(Type type) {
        return typeRepository.save(type);
    }

    public List<Type> lire() {
        return typeRepository.findAll();
    }

    public Type modifier( Type type) {
        Type typeAModifier = typeRepository.findById(type.getIdType()).orElse(null);
        if (typeAModifier != null) {
            typeAModifier.setTitre(type.getTitre());
        }
        return typeRepository.save(typeAModifier);
    }

    public void supprimer(Long idType) {
        Optional<Type> type = typeRepository.findById(idType);
        if (type != null) {
            typeRepository.deleteById(idType);
        }
    }

}

