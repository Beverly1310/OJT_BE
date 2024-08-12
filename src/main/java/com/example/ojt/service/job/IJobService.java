package com.example.ojt.service.job;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.JobAddRequest;
import com.example.ojt.model.dto.request.JobRequest;
import com.example.ojt.model.dto.request.LevelJobRequest;
import com.example.ojt.model.dto.response.JobResponse;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IJobService {
    Page<JobResponse> findAll(Pageable pageable, String search);
    boolean addJob(JobAddRequest jobRequest) throws CustomException;
    boolean updateJob(JobRequest jobRequest) throws CustomException;
    boolean deleteJob(Integer deleteId) throws CustomException;
    SuccessResponse findById(Integer findId) throws CustomException;
}
