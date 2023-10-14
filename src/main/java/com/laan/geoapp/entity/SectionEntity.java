package com.laan.geoapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "section")
public class SectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "sectionEntity", cascade = CascadeType.ALL)
    private List<GeologicalClassEntity> geologicalClassEntities;
}
