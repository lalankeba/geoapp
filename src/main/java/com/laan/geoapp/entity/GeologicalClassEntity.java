package com.laan.geoapp.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "geological_class")
public class GeologicalClassEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private SectionEntity sectionEntity;
}
