package com.laan.geoapp.repository;

import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.GeologicalClassEntityId;
import com.laan.geoapp.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeologicalClassRepository extends JpaRepository<GeologicalClassEntity, GeologicalClassEntityId> {

    void deleteAllBySectionEntity(SectionEntity sectionEntity);
}
