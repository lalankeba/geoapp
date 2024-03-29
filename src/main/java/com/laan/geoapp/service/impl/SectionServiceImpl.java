package com.laan.geoapp.service.impl;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.request.SectionUpdateRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.mapper.GeologicalClassMapper;
import com.laan.geoapp.mapper.SectionMapper;
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
        List<GeologicalClassEntity> geologicalClassEntities = geologicalClassMapper.mapAddRequestsToEntities(sectionAddRequest.getGeologicalClasses(), sectionEntity);
        sectionEntity.setGeologicalClassEntities(geologicalClassEntities);

        SectionEntity savedSectionEntity = sectionRepository.save(sectionEntity);
        log.info("Saved section with geological class data");

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

        sectionRepository.deleteById(name);

        SectionEntity sectionEntity = sectionMapper.mapUpdateRequestToEntity(sectionUpdateRequest);
        List<GeologicalClassEntity> geologicalClassEntities = geologicalClassMapper.mapAddRequestsToEntities(sectionUpdateRequest.getGeologicalClasses(), sectionEntity);
        sectionEntity.setGeologicalClassEntities(geologicalClassEntities);

        SectionEntity updatedSectionEntity = sectionRepository.save(sectionEntity);
        log.info("Updated section with geological class data");

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

    @Override
    @Transactional
    public List<SectionResponse> createSections(final List<SectionAddRequest> sectionAddRequests) {
        return sectionAddRequests.stream().map(this::createSection).toList();
    }

}
