package com.laan.geoapp.service.impl;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.request.SectionUpdateRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.mapper.GeologicalClassMapper;
import com.laan.geoapp.mapper.SectionMapper;
import com.laan.geoapp.repository.GeologicalClassRepository;
import com.laan.geoapp.repository.SectionRepository;
import com.laan.geoapp.service.SectionService;
import com.laan.geoapp.validator.GeologicalClassValidator;
import com.laan.geoapp.validator.SectionValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionServiceImpl implements SectionService {

    private final SectionMapper sectionMapper;

    private final GeologicalClassMapper geologicalClassMapper;

    private final SectionRepository sectionRepository;

    private final GeologicalClassRepository geologicalClassRepository;

    private final SectionValidator sectionValidator;

    private final GeologicalClassValidator geologicalClassValidator;

    @Override
    @Transactional
    public SectionResponse createSection(final SectionAddRequest sectionAddRequest) {
        sectionValidator.validateNamesOfSectionsAndGeologicalClasses(sectionAddRequest);
        geologicalClassValidator.validateDuplicateGeologicalClasses(sectionAddRequest.getGeologicalClasses());
        log.info("Validated section name, geological class names and codes");

        Optional<SectionEntity> optionalSectionEntity  = sectionRepository.findById(sectionAddRequest.getName());
        sectionValidator.validateDuplicateSectionEntity(optionalSectionEntity);

        SectionEntity sectionEntity = sectionMapper.mapAddRequestToEntity(sectionAddRequest);
        SectionEntity savedSectionEntity = sectionRepository.save(sectionEntity);
        log.info("Saved section");

        List<GeologicalClassAddRequest> geologicalClassAddRequests = sectionAddRequest.getGeologicalClasses();
        List<GeologicalClassEntity> geologicalClassEntities = geologicalClassMapper.mapAddRequestsToEntities(geologicalClassAddRequests, savedSectionEntity);
        List<GeologicalClassEntity> savedGeologicalClassEntities = geologicalClassRepository.saveAll(geologicalClassEntities);
        log.info("Saved geological classes");

        savedSectionEntity.setGeologicalClassEntities(savedGeologicalClassEntities);

        return sectionMapper.mapEntityToResponse(savedSectionEntity);
    }

    @Override
    public SectionResponse getSection(final String name) {
        Optional<SectionEntity> optionalSectionEntity = sectionRepository.findById(name);
        sectionValidator.validateNonExistingSectionEntity(name, optionalSectionEntity);

        SectionResponse sectionResponse = null;
        if (optionalSectionEntity.isPresent()) {
            sectionResponse = sectionMapper.mapEntityToResponse(optionalSectionEntity.get());
        }
        return sectionResponse;
    }

    @Override
    public List<SectionResponse> getSections() {
        List<SectionEntity> sectionEntities = sectionRepository.findAll();
        return sectionMapper.mapEntitiesToResponses(sectionEntities);
    }

    @Override
    @Transactional
    public SectionResponse updateSection(final String name, final SectionUpdateRequest sectionUpdateRequest) {
        Optional<SectionEntity> optionalSectionEntity = sectionRepository.findById(name);
        sectionValidator.validateNonExistingSectionEntity(name, optionalSectionEntity);

        sectionValidator.validateNamesOfSectionsAndGeologicalClasses(sectionUpdateRequest);
        geologicalClassValidator.validateDuplicateGeologicalClasses(sectionUpdateRequest.getGeologicalClasses());
        log.info("Validated section name, geological class names and codes");

        geologicalClassRepository.deleteAllBySectionEntity(optionalSectionEntity.get());

        SectionEntity sectionEntity = sectionMapper.mapUpdateRequestToEntity(sectionUpdateRequest);
        SectionEntity updatedSectionEntity = sectionRepository.save(sectionEntity);
        log.info("Updated section");

        List<GeologicalClassAddRequest> geologicalClassUpdateRequests = sectionUpdateRequest.getGeologicalClasses();
        List<GeologicalClassEntity> geologicalClassEntities = geologicalClassMapper.mapAddRequestsToEntities(geologicalClassUpdateRequests, updatedSectionEntity);
        List<GeologicalClassEntity> savedGeologicalClassEntities = geologicalClassRepository.saveAll(geologicalClassEntities);
        log.info("Updated geological classes");

        updatedSectionEntity.setGeologicalClassEntities(savedGeologicalClassEntities);

        return sectionMapper.mapEntityToResponse(updatedSectionEntity);
    }

    @Override
    @Transactional
    public void deleteSection(final String name) {
        Optional<SectionEntity> optionalSectionEntity = sectionRepository.findById(name);
        sectionValidator.validateNonExistingSectionEntity(name, optionalSectionEntity);

        sectionRepository.deleteById(name);
    }

    @Override
    public List<SectionResponse> getSectionsByGeoCode(final String geoCode) {
        List<SectionEntity> sectionEntities = sectionRepository.findSectionEntitiesByGeologicalClassEntityCode(geoCode);
        return sectionMapper.mapEntitiesToResponses(sectionEntities);
    }


}
