package com.laan.geoapp.controller;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.service.SectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Section API request controller
 */

@RestController
@RequestMapping("sections")
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

}
