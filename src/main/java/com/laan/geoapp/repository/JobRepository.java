package com.laan.geoapp.repository;

import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.enums.JobType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<JobEntity, String> {

    Optional<JobEntity> findByIdAndJobType(String id, JobType jobType);

    List<JobEntity> findByJobResult(JobResult jobResult);
}
