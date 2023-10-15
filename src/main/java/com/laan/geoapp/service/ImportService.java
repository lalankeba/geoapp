package com.laan.geoapp.service;

import com.laan.geoapp.dto.response.JobResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ImportService {

    JobResponse processImportFile(final MultipartFile file);

    JobResponse getImportJobStatus(final String id);
}
