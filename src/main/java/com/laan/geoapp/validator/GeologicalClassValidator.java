package com.laan.geoapp.validator;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.exception.DuplicateElementException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GeologicalClassValidator {

    public void validateDuplicateGeologicalClasses(List<GeologicalClassAddRequest> geologicalClassAddRequests) {
        Set<String> duplicateNames = new HashSet<>();
        List<GeologicalClassAddRequest> duplicateRequestsWithName = geologicalClassAddRequests.stream()
                .filter(request -> !duplicateNames.add(request.getName()))
                .toList();

        if (duplicateRequestsWithName.isEmpty()) { // no name duplicates
            Set<String> duplicateCodes = new HashSet<>();
            List<GeologicalClassAddRequest> duplicateRequestsWithCode = geologicalClassAddRequests.stream()
                    .filter(request -> !duplicateCodes.add(request.getCode()))
                    .toList();
            if (!duplicateRequestsWithCode.isEmpty()) {
                throw new DuplicateElementException("Codes of the geological classes cannot be duplicated: " + duplicateRequestsWithCode);
            }
        } else {
            throw new DuplicateElementException("Names of the geological classes cannot be duplicated: " + duplicateRequestsWithName);
        }
    }
}
