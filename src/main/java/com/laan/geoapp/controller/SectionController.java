package com.laan.geoapp.controller;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Section API request controller
 */

@RestController
@RequestMapping("/sections")
@RequiredArgsConstructor
@Slf4j
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<Object> createSection(@Valid @RequestBody SectionAddRequest sectionAddRequest) {
        log.info("Creating section");
        SectionResponse sectionResponse = sectionService.createSection(sectionAddRequest);
        log.info("Created new section");
        return new ResponseEntity<>(sectionResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSection(@PathVariable("id") Long id) {
        log.info("Getting section");
        SectionResponse sectionResponse = sectionService.getSection(id);
        log.info("Get section done");
        return new ResponseEntity<>(sectionResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Object> getSections() {
        log.info("Getting sections");
        List<SectionResponse> sectionResponses = sectionService.getSections();
        log.info("Get sections done");
        return new ResponseEntity<>(sectionResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSection(@PathVariable("id") Long id) {
        log.info("Deleting section with id: {}", id);
        sectionService.deleteSection(id);
        log.info("Deleted section with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
