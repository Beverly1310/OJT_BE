package com.example.ojt.controller.permitAll;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.response.JobResponse;
import com.example.ojt.model.dto.response.SuccessResponse;
import com.example.ojt.model.entity.Job;
import com.example.ojt.service.job.JobService;
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
@RequestMapping("/api.myservice.com/v1/job")
@RequiredArgsConstructor
public class JobPermitAllController {
    private final JobService jobService;

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
    @GetMapping("/{id}")
    public ResponseEntity<?> getIdJob(@PathVariable Integer id){
        try {
            SuccessResponse response = jobService.findById(id);
            return  ResponseEntity.ok().body(response);
        }catch (CustomException e){
            return  ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/{id}/same-type-jobs")
    public ResponseEntity<List<Job>> getJobsBySameType(@PathVariable Integer id) {
        List<Job> jobs = jobService.getJobsBySameType(id);
        return ResponseEntity.ok(jobs);
    }
}
