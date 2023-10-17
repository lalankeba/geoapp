package com.laan.geoapp.service.impl;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.enums.JobType;
import com.laan.geoapp.mapper.JobMapper;
import com.laan.geoapp.repository.JobRepository;
import com.laan.geoapp.repository.SectionRepository;
import com.laan.geoapp.service.ExportService;
import com.laan.geoapp.util.FileJobUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExportServiceImpl implements ExportService {

    @Value("${app.file.path}")
    private String filePath;

    private final JobMapper jobMapper;

    private final JobRepository jobRepository;

    private final FileJobUtil fileJobUtil;

    private final SectionRepository sectionRepository;

    @Override
    public JobResponse exportFile() {
        JobEntity jobEntity = jobMapper.mapItemsToEntity(JobType.EXPORT, filePath, JobResult.IN_PROGRESS, new Date());
        JobEntity savedJobEntity = jobRepository.save(jobEntity);
        log.info("Export job saved");

        List<SectionEntity> sectionEntities = sectionRepository.findAll();
        fileJobUtil.exportFile(savedJobEntity, sectionEntities);

        return jobMapper.mapEntityToResponse(savedJobEntity);
    }

}
