package com.laan.geoapp.repository;

import com.laan.geoapp.entity.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<JobEntity, String> {
}
