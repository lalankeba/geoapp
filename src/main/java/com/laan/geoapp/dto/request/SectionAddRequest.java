package com.laan.geoapp.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SectionAddRequest {

    @NotBlank(message = "Section name is mandatory")
    @Size(min = 1, max = 200, message = "Section name '${validatedValue}' must be valid value between {min} and {max} characters long")
    private String name;

    @Valid
    private List<GeologicalClassAddRequest> geologicalClasses;

}
