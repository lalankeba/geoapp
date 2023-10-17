package com.laan.geoapp.controller;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.service.ExportService;
import com.laan.geoapp.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Export file API request controller
 */

@RestController
@RequestMapping(PathUtil.EXPORT)
@RequiredArgsConstructor
@Slf4j
public class ExportController {

    private final ExportService exportService;

    @GetMapping
    public ResponseEntity<Object> exportFile() {
        log.info("Export file");
        JobResponse jobResponse = exportService.exportFile();
        log.info("Export file done");
        return new ResponseEntity<>(jobResponse, HttpStatus.OK);
    }

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getExportJobStatus(@PathVariable("id") String id) {
        log.info("Getting status of export job for id: {}", id);
        JobResponse jobResponse = exportService.getExportJobStatus(id);
        log.info("Get export job status done");
        return new ResponseEntity<>(jobResponse, HttpStatus.OK);
    }

    @GetMapping(PathUtil.ID_PLACEHOLDER + PathUtil.FILE)
    public ResponseEntity<Object> getExportFile(@PathVariable("id") String id) throws IOException {
        log.info("Getting the export file for the id: {}", id);
        byte[] fileData = exportService.getExportFile(id);
        log.info("Get export file done");
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("application/vnd.ms-excel"))
                .body(fileData);
    }
}
