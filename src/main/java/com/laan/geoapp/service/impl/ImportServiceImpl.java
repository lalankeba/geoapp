package com.laan.geoapp.service.impl;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.enums.JobType;
import com.laan.geoapp.exception.ElementNotFoundException;
import com.laan.geoapp.mapper.JobMapper;
import com.laan.geoapp.repository.JobRepository;
import com.laan.geoapp.service.ImportService;
import com.laan.geoapp.util.FileJobUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImportServiceImpl implements ImportService {

    @Value("${app.file.path}")
    private String filePath;

    private final JobRepository jobRepository;

    private final JobMapper jobMapper;

    private final FileJobUtil fileJobUtil;

    @Override
    public JobResponse processImportFile(final MultipartFile file) {
        JobEntity jobEntity = jobMapper.mapItemsToEntity(JobType.IMPORT, filePath, JobResult.IN_PROGRESS, new Date());
        JobEntity savedJobEntity = jobRepository.save(jobEntity);
        log.info("Import job saved");

        fileJobUtil.importFile(file, savedJobEntity);

        return jobMapper.mapEntityToResponse(savedJobEntity);
    }

    @Override
    public JobResponse getImportJobStatus(String id) {
        Optional<JobEntity> optionalJobEntity = jobRepository.findById(id);

        if (optionalJobEntity.isEmpty()) {
            throw new ElementNotFoundException("Import job cannot be found the given id: " + id);
        }
        return jobMapper.mapEntityToResponse(optionalJobEntity.get());
    }

}
