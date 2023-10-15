package com.laan.geoapp.util;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.repository.JobRepository;
import com.laan.geoapp.service.SectionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileJobUtil {

    @Value("${app.file.path}")
    private String filePath;

    private final JobRepository jobRepository;

    private final SectionService sectionService;

    @Async
    public void importFile(final MultipartFile multipartFile, JobEntity jobEntity) {
        log.info("Start importing file...");
        JobResult jobResult;
        String detail;

        try {
            List<SectionAddRequest> sectionAddRequests = readAndCreateAddRequests(multipartFile, jobEntity.getFileName());
            List<SectionResponse> sectionResponses = sectionService.createSections(sectionAddRequests);
            jobResult = JobResult.DONE;
            detail = "Successfully imported " + sectionResponses.size() + " section details";
        } catch (Exception e) {
            jobResult = JobResult.ERROR;
            detail = e.getMessage();
            log.error("Exception occurred: {}", e.getMessage());
        }

        jobEntity.setJobResult(jobResult);
        jobEntity.setDetail(detail);
        jobRepository.save(jobEntity);

        log.info("Finished importing file");
    }

    private List<SectionAddRequest> readAndCreateAddRequests(final MultipartFile multipartFile, final String fileName) throws IOException {
        List<SectionAddRequest> sectionAddRequests = new ArrayList<>();
        File tempFile = new File(filePath + fileName);
        multipartFile.transferTo(tempFile);

        FileInputStream fileInputStream = new FileInputStream(tempFile);
        Workbook workbook = new HSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        int rowIndex = 0;
        first:
        for (Row row: sheet) {
            SectionAddRequest sectionAddRequest = new SectionAddRequest();
            GeologicalClassAddRequest geologicalClassAddRequest = null;
            List<GeologicalClassAddRequest> geologicalClassAddRequests = new ArrayList<>();
            int cellIndex = 0;
            for (Cell cell: row) {
                if (rowIndex == 0) { // header row
                    rowIndex++;
                    continue first;
                } else { // data rows
                    if (cellIndex == 0) { // section name column
                        sectionAddRequest.setName(cell.getRichStringCellValue().getString().trim());
                    } else {
                        if (cellIndex % 2 == 1) { // geological class name column
                            geologicalClassAddRequest = new GeologicalClassAddRequest();
                            geologicalClassAddRequest.setName(cell.getRichStringCellValue().getString().trim());
                        } else { // geological class code
                            geologicalClassAddRequest.setCode(cell.getRichStringCellValue().getString().trim());
                            geologicalClassAddRequests.add(geologicalClassAddRequest);
                        }
                    }
                }
                cellIndex++;
            }
            sectionAddRequest.setGeologicalClasses(geologicalClassAddRequests);
            sectionAddRequests.add(sectionAddRequest);
            rowIndex++;
        }

        return sectionAddRequests;
    }

    @PostConstruct
    private void init() {
        File sectionFolder = new File(filePath);
        if (!sectionFolder.exists()){
            boolean folderCreated = sectionFolder.mkdir();
            log.info("Folder {} created: {}", sectionFolder.getName(), folderCreated);
        }
    }
}
