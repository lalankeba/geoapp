package com.laan.geoapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "geological_class")
@IdClass(GeologicalClassEntityId.class)
public class GeologicalClassEntity {

    @Id
    private String name;

    private String code;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_name")
    private SectionEntity sectionEntity;
}
