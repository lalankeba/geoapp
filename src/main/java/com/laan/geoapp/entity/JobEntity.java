package com.laan.geoapp.entity;

import com.laan.geoapp.enums.JobResult;
import com.laan.geoapp.enums.JobType;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "job")
public class JobEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "job_type")
    @Enumerated(EnumType.STRING)
    private JobType jobType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "job_result")
    @Enumerated(EnumType.STRING)
    private JobResult jobResult;

    private String detail;

    @Column(name = "start_date_time")
    private Date startDateTime;

}
