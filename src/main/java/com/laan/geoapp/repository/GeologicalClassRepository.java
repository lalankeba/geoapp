package com.laan.geoapp.repository;

import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.GeologicalClassEntityId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GeologicalClassRepository extends JpaRepository<GeologicalClassEntity, GeologicalClassEntityId> {
}
