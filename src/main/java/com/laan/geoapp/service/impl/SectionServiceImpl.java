package com.laan.geoapp.service.impl;

import com.laan.geoapp.dto.request.GeologicalClassAddRequest;
import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.response.SectionResponse;
import com.laan.geoapp.entity.GeologicalClassEntity;
import com.laan.geoapp.entity.SectionEntity;
import com.laan.geoapp.mapper.GeologicalClassMapper;
import com.laan.geoapp.mapper.SectionMapper;
import com.laan.geoapp.repository.GeologicalClassRepository;
import com.laan.geoapp.repository.SectionRepository;
import com.laan.geoapp.service.SectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SectionServiceImpl implements SectionService {

    private final SectionMapper sectionMapper;

    private final GeologicalClassMapper geologicalClassMapper;

    private final SectionRepository sectionRepository;

    private final GeologicalClassRepository geologicalClassRepository;

    @Override
    public SectionResponse createSection(SectionAddRequest sectionAddRequest) {
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
}
