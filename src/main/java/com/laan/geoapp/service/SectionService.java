package com.laan.geoapp.service;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.response.SectionResponse;

import java.util.List;

public interface SectionService {

    SectionResponse createSection(final SectionAddRequest sectionAddRequest);

    SectionResponse getSection(final Long id);

    List<SectionResponse> getSections();
}
