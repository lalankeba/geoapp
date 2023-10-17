package com.laan.geoapp.controller;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.service.ExportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Export file API request controller
 */

@RestController
@RequestMapping("/export")
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

}
