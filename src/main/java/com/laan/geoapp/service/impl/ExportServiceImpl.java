package com.laan.geoapp.service.impl;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.enums.JobType;
import com.laan.geoapp.exception.ElementNotFoundException;
import com.laan.geoapp.exception.InvalidElementException;
import com.laan.geoapp.mapper.JobMapper;
import com.laan.geoapp.repository.JobRepository;
import com.laan.geoapp.repository.SectionRepository;
import com.laan.geoapp.service.ExportService;
import com.laan.geoapp.util.FileJobUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        JobEntity jobEntity = jobMapper.mapItemsToEntity(JobType.EXPORT, getAbsoluteFilePath(), JobResult.IN_PROGRESS, new Date());
        JobEntity savedJobEntity = jobRepository.save(jobEntity);
        log.info("Export job saved");

        List<SectionEntity> sectionEntities = sectionRepository.findAll();
        fileJobUtil.exportFile(savedJobEntity, sectionEntities);

        return jobMapper.mapEntityToResponse(savedJobEntity);
    }

    @Override
    public JobResponse getExportJobStatus(final String id) {
        Optional<JobEntity> optionalJobEntity = jobRepository.findById(id);

        if (optionalJobEntity.isEmpty()) {
            throw new ElementNotFoundException("Export job cannot be found the given id: " + id);
        }
        return jobMapper.mapEntityToResponse(optionalJobEntity.get());
    }

    @Override
    public byte[] getExportFile(final String id) throws IOException {
        Optional<JobEntity> optionalJobEntity = jobRepository.findById(id);

        if (optionalJobEntity.isEmpty()) {
            throw new ElementNotFoundException("Export file cannot be found the given id: " + id);
        }

        JobEntity jobEntity = optionalJobEntity.get();
        if (jobEntity.getJobResult() == JobResult.IN_PROGRESS) {
            throw new InvalidElementException("Export file is still in progress");
        } else if (jobEntity.getJobResult() == JobResult.ERROR) {
            throw new InvalidElementException("Export file has an error");
        } else if (jobEntity.getJobType() == JobType.IMPORT) {
            throw new InvalidElementException("Export file cannot be found for the id");
        }

        String fileNameWithAbsPath = jobEntity.getFilePath() + File.separator + jobEntity.getFileName();
        File file = new File(fileNameWithAbsPath);
        if (!file.exists()) {
            throw new ElementNotFoundException("Export file: " + jobEntity.getFileName() + " cannot be found in the file system");
        }

        return Files.readAllBytes(new File(fileNameWithAbsPath).toPath());
    }

    private String getAbsoluteFilePath() {
        return new File(filePath).getAbsolutePath();
    }

}
