package com.laan.geoapp.service;

import com.laan.geoapp.dto.response.JobResponse;

public interface ExportService {

    JobResponse exportFile();

    JobResponse getExportJobStatus(final String id);
}
