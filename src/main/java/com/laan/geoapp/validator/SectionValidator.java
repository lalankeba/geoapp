package com.laan.geoapp.validator;

import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.exception.ElementNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SectionValidator {

    public void validateNonExistingSectionEntity(Long id, Optional<SectionEntity> optionalSectionEntity) {
        if (optionalSectionEntity.isEmpty()) {
            throw new ElementNotFoundException("Section cannot be found for the id: " + id);
        }
    }
}
