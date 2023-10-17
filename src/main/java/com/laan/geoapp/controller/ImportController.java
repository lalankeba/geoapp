package com.laan.geoapp.controller;

import com.laan.geoapp.dto.response.JobResponse;
import com.laan.geoapp.service.ImportService;
import com.laan.geoapp.util.PathUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Import file API request controller
 */

@RestController
@RequestMapping(PathUtil.IMPORT)
@RequiredArgsConstructor
@Slf4j
public class ImportController {

    private final ImportService importService;

    @PostMapping
    public ResponseEntity<Object> importFile(@RequestParam("file") MultipartFile file) {
        log.info("Importing file");
        JobResponse jobResponse = importService.processImportFile(file);
        log.info("Importing file job started");
        return new ResponseEntity<>(jobResponse, HttpStatus.OK);
    }

    @GetMapping(PathUtil.ID_PLACEHOLDER)
    public ResponseEntity<Object> getImportJobStatus(@PathVariable("id") String id) {
        log.info("Getting status of import job for id: {}", id);
        JobResponse jobResponse = importService.getImportJobStatus(id);
        log.info("Get import job status done");
        return new ResponseEntity<>(jobResponse, HttpStatus.OK);
    }
}
