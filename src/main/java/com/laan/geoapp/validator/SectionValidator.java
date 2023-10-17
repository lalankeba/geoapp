package com.laan.geoapp.validator;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.exception.DuplicateElementException;
import com.laan.geoapp.exception.ElementNotFoundException;
import com.laan.geoapp.exception.InvalidElementException;
import com.laan.geoapp.util.ConstantsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class SectionValidator {

    public void validateDuplicateSectionEntity(Optional<SectionEntity> optionalSectionEntity) {
        if (optionalSectionEntity.isPresent()) {
            throw new DuplicateElementException("Section found for the same name: " + optionalSectionEntity.get().getName());
        }
    }

    public void validateNonExistingSectionEntity(String name, Optional<SectionEntity> optionalSectionEntity) {
        if (optionalSectionEntity.isEmpty()) {
            throw new ElementNotFoundException("Section cannot be found for the name: " + name);
        }
    }

    public void validateNamesOfSectionsAndGeologicalClasses(SectionAddRequest sectionAddRequest) {
        String sectionName = sectionAddRequest.getName();
        String sectionNameNumber = sectionName.substring(ConstantsUtil.SECTION_NAME_PREFIX.length());

        String geoClassNamePrefix = ConstantsUtil.GEO_CLASS_NAME_INIT_PREFIX + sectionNameNumber;
        String geoClassCodePrefix = ConstantsUtil.GEO_CLASS_CODE_INIT_PREFIX + sectionNameNumber;
        StringBuilder sb = new StringBuilder();

        sectionAddRequest.getGeologicalClasses()
                .forEach(geoClass -> {
                    String geoClassName = geoClass.getName();
                    String geoClassCode = geoClass.getCode();

                    if (!geoClassName.startsWith(geoClassNamePrefix)) {
                        sb.append("In the section: '").append(sectionName).append("', ")
                                .append("geological class name: '").append(geoClassName).append("' is not valid. ")
                                .append("It should start with '").append(geoClassNamePrefix).append("...'");
                        throw new InvalidElementException(sb.toString());
                    }

                    if (!geoClassCode.startsWith(geoClassCodePrefix)) {
                        sb.append("In the section: '").append(sectionName).append("', ")
                                .append("geological class code: '").append(geoClassCode).append("' is not valid. ")
                                .append("It should start with '").append(geoClassCodePrefix).append("...'");
                        throw new InvalidElementException(sb.toString());
                    }

                    String geoClassNumber = geoClassName.substring(ConstantsUtil.GEO_CLASS_NAME_INIT_PREFIX.length());
                    if (!geoClassCode.substring(ConstantsUtil.GEO_CLASS_CODE_INIT_PREFIX.length()).equals(geoClassNumber)) {
                        sb.append("In the section: '").append(sectionName).append("' and ")
                                .append("geological class name: '").append(geoClassName).append("', ")
                                .append("geological class code: '").append(geoClassCode).append("' is not valid. ")
                                .append("It should be '").append(ConstantsUtil.GEO_CLASS_CODE_INIT_PREFIX).append(geoClassNumber).append("'");
                        throw new InvalidElementException(sb.toString());
                    }

                });
    }
}
