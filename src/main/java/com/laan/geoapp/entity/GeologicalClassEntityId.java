package com.laan.geoapp.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GeologicalClassEntityId implements Serializable {

    private String name;

    private SectionEntity sectionEntity;

    public GeologicalClassEntityId() {
    }

    public GeologicalClassEntityId(String name, SectionEntity sectionEntity) {
        this.name = name;
        this.sectionEntity = sectionEntity;
    }
}
