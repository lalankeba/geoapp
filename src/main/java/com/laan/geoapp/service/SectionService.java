package com.laan.geoapp.service;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.request.SectionUpdateRequest;
import com.laan.geoapp.dto.response.SectionResponse;

import java.util.List;

public interface SectionService {

    SectionResponse createSection(final SectionAddRequest sectionAddRequest);

    SectionResponse getSection(final String name);

    List<SectionResponse> getSections();

    SectionResponse updateSection(final String name, final SectionUpdateRequest sectionUpdateRequest);

    void deleteSection(final String id);
}
