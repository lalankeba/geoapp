package com.laan.geoapp.util;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.JobEntity;
import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.repository.JobRepository;
import com.laan.geoapp.service.SectionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

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

        File currDir = new File(filePath);
        String path = currDir.getAbsolutePath();
        String fileLocation = path + File.separator + fileName;

        File tempFile = new File(fileLocation);
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

    @Async
    public void exportFile(JobEntity jobEntity, final List<SectionEntity> sectionEntities) {
        log.info("Start exporting file...");
        JobResult jobResult;
        String detail;

        try {
            writeExportFile(sectionEntities, jobEntity.getFileName());

            jobResult = JobResult.DONE;
            detail = "Successfully exported " + sectionEntities.size() + " section details";
        } catch (Exception e) {
            jobResult = JobResult.ERROR;
            detail = e.getMessage();
            log.error("Exception occurred: {}", e.getMessage());
        }

        jobEntity.setJobResult(jobResult);
        jobEntity.setDetail(detail);
        jobRepository.save(jobEntity);

        log.info("Finished exporting file");
    }

    private void writeExportFile(final List<SectionEntity> sectionEntities, final String fileName) throws IOException {
        log.info("Writing export file...");
        Integer maxGeoClassNumber = getMaxGeoClassNumber(sectionEntities);

        HSSFWorkbook workbook = new HSSFWorkbook();

        // creating sheet
        Sheet sheet = workbook.createSheet("data");

        // create header
        makeHeaderRow(workbook, sheet, maxGeoClassNumber);

        // create body
        makeBodyRows(workbook, sheet, sectionEntities);

        File currDir = new File(filePath);
        String path = currDir.getAbsolutePath();
        String fileLocation = path + File.separator + fileName;

        FileOutputStream outputStream = new FileOutputStream(fileLocation);
        workbook.write(outputStream);
        workbook.close();
    }

    private void makeBodyRows(final HSSFWorkbook workbook, final Sheet sheet, final List<SectionEntity> sectionEntities) {
        int maxRows = sectionEntities.size();
        CellStyle bodyStyle = workbook.createCellStyle();
        bodyStyle.setWrapText(true);

        for (int rowIndex = 1; rowIndex <= maxRows; rowIndex++) {
            SectionEntity sectionEntity = sectionEntities.get(rowIndex - 1);
            String sectionName = sectionEntity.getName();
            Row row = sheet.createRow(rowIndex);

            Cell cell = row.createCell(0);
            cell.setCellValue(sectionName);
            cell.setCellStyle(bodyStyle);

            List<GeologicalClassEntity> geologicalClassEntities = sectionEntity.getGeologicalClassEntities();
            Integer sectionNumber = getSectionNumber(sectionName);

            geologicalClassEntities.forEach(geologicalClassEntity -> {
                String geoClassName = geologicalClassEntity.getName();
                int geoClassNumber = getGeoClassNumber(sectionNumber, geoClassName);

                Cell geoCell = row.createCell((geoClassNumber * 2) - 1);
                geoCell.setCellValue(geoClassName);
                geoCell.setCellStyle(bodyStyle);

                geoCell = row.createCell((geoClassNumber * 2));
                geoCell.setCellValue(geologicalClassEntity.getCode());
                geoCell.setCellStyle(bodyStyle);
            });
        }
    }

    private void makeHeaderRow(final HSSFWorkbook workbook, final Sheet sheet, final Integer maxGeoClassNumber) {
        Row headerRow = sheet.createRow(0);

        CellStyle headerStyle = workbook.createCellStyle();

        HSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        headerStyle.setFont(font);

        sheet.setColumnWidth(0, 4000);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("Section name");
        headerCell.setCellStyle(headerStyle);

        for (int i = 0; i < maxGeoClassNumber; i++) {
            sheet.setColumnWidth((2 * i) + 1, 4000);
            headerCell = headerRow.createCell((2 * i) + 1);
            headerCell.setCellValue("Class " + (i + 1) + " name");
            headerCell.setCellStyle(headerStyle);

            sheet.setColumnWidth((2 * i) + 2, 4000);
            headerCell = headerRow.createCell((2 * i) + 2);
            headerCell.setCellValue("Class " + (i + 1) + " code");
            headerCell.setCellStyle(headerStyle);
        }
    }

    private Integer getMaxGeoClassNumber(final List<SectionEntity> sectionEntities) {
        Set<Integer> geoClassNumberSet = new HashSet<>();
        sectionEntities.forEach(sectionEntity -> {
            String sectionName = sectionEntity.getName();
            Integer sectionNumber = getSectionNumber(sectionName);

            List<GeologicalClassEntity> geologicalClassEntities = sectionEntity.getGeologicalClassEntities();
            Integer maxGeoClassNumberFromGeoClasses = getMaxGeoClassNumber(sectionNumber, geologicalClassEntities);
            geoClassNumberSet.add(maxGeoClassNumberFromGeoClasses);
        });
        return Collections.max(geoClassNumberSet);
    }

    private Integer getMaxGeoClassNumber(final Integer sectionNumber, final List<GeologicalClassEntity> geologicalClassEntities) {
        final Set<Integer> geoClassNumberSet = new HashSet<>();
        geologicalClassEntities.forEach(geologicalClassEntity -> {
            String geoClassName = geologicalClassEntity.getName();
            Integer geoClassNumber = getGeoClassNumber(sectionNumber, geoClassName);
            geoClassNumberSet.add(geoClassNumber);
        });
        return Collections.max(geoClassNumberSet);
    }

    private Integer getSectionNumber (final String sectionName) {
        String secNumber = sectionName.substring(ConstantsUtil.SECTION_NAME_PREFIX.length()).trim();
        return Integer.parseInt(secNumber);
    }

    private Integer getGeoClassNumber (final Integer sectionNumber, final String geoClassName) {
        String geoClassNamePrefix = ConstantsUtil.GEO_CLASS_NAME_INIT_PREFIX + sectionNumber;
        String geoNumber = geoClassName.substring(geoClassNamePrefix.length()).trim();
        return Integer.parseInt(geoNumber);
    }

    @PostConstruct
    private void init() {
        File sectionFolder = new File(filePath);
        if (!sectionFolder.exists()){
            boolean folderCreated = sectionFolder.mkdirs();
            log.info("Folder {} created: {}", sectionFolder.getName(), folderCreated);
        }
    }
}
