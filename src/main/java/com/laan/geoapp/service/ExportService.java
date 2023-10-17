package com.laan.geoapp.service;

import com.laan.geoapp.dto.response.JobResponse;

import java.io.IOException;

public interface ExportService {

    JobResponse exportFile();

    JobResponse getExportJobStatus(final String id);
    byte[] getExportFile(final String id) throws IOException;

}
