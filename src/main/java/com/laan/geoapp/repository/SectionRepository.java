package com.laan.geoapp.repository;

import com.laan.geoapp.entity.SectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionRepository extends JpaRepository<SectionEntity, String> {

    @Query("SELECT s FROM SectionEntity s JOIN s.geologicalClassEntities g WHERE g.code = :geoCode")
    List<SectionEntity> findSectionEntitiesByGeologicalClassEntityCode(String geoCode);
}
