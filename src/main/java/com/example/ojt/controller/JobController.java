package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.JobAddRequest;
import com.example.ojt.model.dto.request.JobRequest;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.response.JobResponse;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.Job;
import com.example.ojt.service.job.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api.myservice.com/v1/company/job")
public class JobController {

    private final JobService jobService;
    @GetMapping("/company")
    public ResponseEntity<Page<JobResponse>> getAllJobsByCompany(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            Pageable pageable) {
        try {
            Page<JobResponse> jobResponses = jobService.findAllByCurrentCompany(title, location, pageable);
            return new ResponseEntity<>(jobResponses, HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<Page<JobResponse>> getJobs(
            @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String location,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction) {
        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(direction), sort));
        return ResponseEntity.ok().body(jobService.findAll(sortPageable, search, location));
    }

    @PostMapping
    public ResponseEntity<?> addJob(@Valid @RequestBody JobAddRequest jobRequest) {
        try {
            boolean isAdded = jobService.addJob(jobRequest);
            if (isAdded) {
                return ResponseEntity.status(HttpStatus.CREATED).body("Tạo Job thành công");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tạo được Job");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Integer id, @RequestBody JobRequest jobRequest) {
        try {
            jobRequest.setId(id);
            boolean isUpdated = jobService.updateJob(jobRequest);
            if (isUpdated) {
                return ResponseEntity.ok().body("Job updated successfully");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Job");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getIdJob(@PathVariable Integer id){
        try {
            SuccessResponse response = jobService.findById(id);
            return  ResponseEntity.ok().body(response);
        }catch (CustomException e){
            return  ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping ("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Integer id) throws CustomException {
        boolean check  = jobService.deleteJob(id);
        if (check) {
            APIResponse apiResponse = new APIResponse(200, "Delete job success");
            return  new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }else {
            throw new CustomException("Loi", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{id}/same-type-jobs")
    public ResponseEntity<List<Job>> getJobsBySameType(@PathVariable Integer id) {
        List<Job> jobs = jobService.getJobsBySameType(id);
        return ResponseEntity.ok(jobs);
    }
}
