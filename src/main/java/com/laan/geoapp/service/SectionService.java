package com.laan.geoapp.service;

import com.laan.geoapp.dto.request.SectionAddRequest;
import com.laan.geoapp.dto.response.SectionResponse;

public interface SectionService {

    SectionResponse createSection(final SectionAddRequest sectionAddRequest);
}
