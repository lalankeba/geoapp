package com.laan.geoapp.mapper;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.enums.JobType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Date;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface JobMapper {

    JobMapper INSTANCE = Mappers.getMapper( JobMapper.class );

    @Mapping(target = "result", source = "jobResult")
    JobResponse mapEntityToResponse(JobEntity jobEntity);

    default JobEntity mapItemsToEntity(JobType jobType, String filePath, JobResult jobResult, Date startDate) {
        JobEntity jobEntity = new JobEntity();
        String fileName = jobType.toString() + "-" + UUID.randomUUID().toString().replace("-", "") + ".xls";

        jobEntity.setJobType(jobType);
        jobEntity.setFilePath(filePath);
        jobEntity.setFileName(fileName);
        jobEntity.setJobResult(jobResult);
        jobEntity.setStartDateTime(startDate);

        return jobEntity;
    }
}
