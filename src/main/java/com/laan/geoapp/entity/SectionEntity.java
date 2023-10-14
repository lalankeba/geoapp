package com.laan.geoapp.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "section")
public class SectionEntity {

    @Id
    private String name;

    @OneToMany(mappedBy = "sectionEntity", cascade = CascadeType.ALL)
    private List<GeologicalClassEntity> geologicalClassEntities;
}
