package com.laan.geoapp.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SectionResponse {

    private Long id;
    private String name;
    private List<GeologicalClassResponse> geologicalClasses;

}
