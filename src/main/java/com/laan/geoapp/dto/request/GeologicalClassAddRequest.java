package com.laan.geoapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class GeologicalClassAddRequest {

    @NotBlank(message = "Geological Class name is mandatory")
    @Size(min = 1, max = 200, message = "Geological Class name '${validatedValue}' must be valid value between {min} and {max} characters long")
    private String name;

    @NotBlank(message = "Geological Class code is mandatory")
    @Size(min = 1, max = 20, message = "Geological Class code '${validatedValue}' must be valid value between {min} and {max} characters long")
    private String code;
}
