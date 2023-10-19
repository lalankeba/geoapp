package com.laan.geoapp.utils;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.request.SectionUpdateRequest;
import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.repository.JobRepository;
import com.laan.geoapp.service.ExportService;
import com.laan.geoapp.service.ImportService;
import com.laan.geoapp.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class TestUtils {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ImportService importService;

    @Autowired
    private ExportService exportService;

    @Value("${spring.security.user.name}")
    private String basicUsername;

    @Value("${spring.security.user.password}")
    private String basicPassword;

    public SectionResponse createSection(final SectionAddRequest sectionAddRequest) {
        return sectionService.createSection(sectionAddRequest);
    }

    public void deleteAllSectionsAfterRunningJobs() {
        List<JobEntity> runningJobEntities = jobRepository.findByJobResult(JobResult.IN_PROGRESS);
        while (runningJobEntities != null && runningJobEntities.size() > 0) {
            try {
                log.info("Waiting for {} running jobs...", runningJobEntities.size());
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            runningJobEntities = jobRepository.findByJobResult(JobResult.IN_PROGRESS);
        }
        List<SectionResponse> sectionResponses = sectionService.getSections();
        List<String> ids = sectionResponses.stream().map(SectionResponse::getName).toList();
        ids.forEach(sectionService::deleteSection);
    }

    public void deleteAllJobs() {
        jobRepository.deleteAll();
    }

    public JobResponse importFile(MultipartFile file) {
        return importService.processImportFile(file);
    }

    public JobResponse exportFile() {
        return exportService.exportFile();
    }

    public JobResponse exportFileAndWait() {
        JobResponse pendingJobResponse = exportService.exportFile();
        JobResponse jobResponse = exportService.getExportJobStatus(pendingJobResponse.getId());
        while (jobResponse != null && jobResponse.getResult() == JobResult.IN_PROGRESS) {
            try {
                log.info("Waiting for export job to be finished...");
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
            jobResponse = exportService.getExportJobStatus(pendingJobResponse.getId());
        }
        return jobResponse;
    }

    public SectionAddRequest makeSectionAddRequest(final Integer sectionNumber, final Integer numberOfClasses) {
        SectionAddRequest sectionAddRequest = new SectionAddRequest();
        sectionAddRequest.setName("Section " + sectionNumber);

        List<GeologicalClassAddRequest> geologicalClassAddRequests = makeGeologicalClassAddRequests(sectionNumber, numberOfClasses);
        sectionAddRequest.setGeologicalClasses(geologicalClassAddRequests);

        return sectionAddRequest;
    }

    public SectionUpdateRequest makeSectionUpdateRequest(final Integer sectionNumber, final Integer numberOfClasses) {
        SectionUpdateRequest sectionUpdateRequest = new SectionUpdateRequest();
        sectionUpdateRequest.setName("Section " + sectionNumber);

        List<GeologicalClassAddRequest> geologicalClassAddRequests = makeGeologicalClassAddRequests(sectionNumber, numberOfClasses);
        sectionUpdateRequest.setGeologicalClasses(geologicalClassAddRequests);

        return sectionUpdateRequest;
    }

    private List<GeologicalClassAddRequest> makeGeologicalClassAddRequests(final Integer sectionNumber, final Integer numberOfClasses) {
        List<GeologicalClassAddRequest> geologicalClassAddRequests = new ArrayList<>();
        for (int i = 0; i < numberOfClasses; i++) {
            GeologicalClassAddRequest geologicalClassAddRequest = makeGeologicalClassAddRequest(sectionNumber, (i + 1));
            geologicalClassAddRequests.add(geologicalClassAddRequest);
        }
        return geologicalClassAddRequests;
    }

    private GeologicalClassAddRequest makeGeologicalClassAddRequest(final Integer sectionNumber, final Integer geoClassNumber) {
        GeologicalClassAddRequest geologicalClassAddRequest = new GeologicalClassAddRequest();
        geologicalClassAddRequest.setName("Geo Class " + sectionNumber + geoClassNumber);
        geologicalClassAddRequest.setCode("GC" + sectionNumber + geoClassNumber);
        return geologicalClassAddRequest;
    }

    public MockMultipartFile getMultipartFile(final String fileName) throws IOException {
        String absolutePath = new File("src/test/resources/sample-files").getAbsolutePath();
        File file = new File(absolutePath, fileName);
        InputStream inputStream = new FileInputStream(file);

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", fileName, MediaType.valueOf("application/vnd.ms-excel").toString(), inputStream);
        return multipartFile;
    }

    public String getBasicUsername() {
        return basicUsername;
    }

    public String getBasicPassword() {
        return basicPassword;
    }
}
