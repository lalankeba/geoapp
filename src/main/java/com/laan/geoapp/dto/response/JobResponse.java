package com.laan.geoapp.dto.response;

import com.laan.geoapp.enums.JobResult;
import lombok.Data;

@Data
public class JobResponse {

    private String id;
    private JobResult result;
}
