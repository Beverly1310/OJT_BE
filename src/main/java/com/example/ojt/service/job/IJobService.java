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
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IJobService {
    Page<JobResponse> findAll(Pageable pageable, String search, String location);
    boolean addJob(JobAddRequest jobRequest) throws CustomException;
    boolean updateJob(JobRequest jobRequest) throws CustomException;
    boolean deleteJob(Integer deleteId) throws CustomException;
    SuccessResponse findById(Integer findId) throws CustomException;


    ResponseEntity<?> getAllJobs(Pageable pageable);

    ResponseEntity<Integer> changeOutstandingStatus(Integer jobId);

    List<Job> getJobsBySameType(Integer jobId);
    Page<JobResponse> findAllByCurrentCompany(String title, String location, Pageable pageable) throws CustomException;

    Page<JobResponse> findAllByCompanyAndSearch(Integer companyId, String title, String location, Pageable pageable) throws CustomException;


}
