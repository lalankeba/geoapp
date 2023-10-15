package com.laan.geoapp.controller;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.request.SectionUpdateRequest;
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

    @GetMapping("/{name}")
    public ResponseEntity<Object> getSection(@PathVariable("name") String name) {
        log.info("Getting section");
        SectionResponse sectionResponse = sectionService.getSection(name);
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

    @PutMapping("/{name}")
    public ResponseEntity<Object> createSection(@PathVariable("name") String name, @Valid @RequestBody SectionUpdateRequest sectionUpdateRequest) {
        log.info("Updating section");
        SectionResponse sectionResponse = sectionService.updateSection(name, sectionUpdateRequest);
        log.info("Updated new section");
        return new ResponseEntity<>(sectionResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Object> deleteSection(@PathVariable("name") String name) {
        log.info("Deleting section with name: {}", name);
        sectionService.deleteSection(name);
        log.info("Deleted section with name: {}", name);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/by-code")
    public ResponseEntity<Object> getSectionsByGeoCode(@RequestParam("code") String code) {
        log.info("Getting sections by geological class code");
        List<SectionResponse> sectionResponses = sectionService.getSectionsByGeoCode(code);
        log.info("Get sections by geological class code, done");
        return new ResponseEntity<>(sectionResponses, HttpStatus.OK);
    }

}
