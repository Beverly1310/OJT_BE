package com.example.ojt.controller.admincontroller;


import com.example.ojt.service.job.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/admin/jobs") // Adjusted to avoid conflicts with path syntax
public class JobsAdminController {

    @Autowired
    private IJobService jobService;

    /**
     * Get list of jobs
     * @param pageable Pagination information
     * @return Response with job list
     */
    @GetMapping
    public ResponseEntity<?> findAllJobs(@PageableDefault Pageable pageable) {
        return jobService.getAllJobs(pageable);
    }

    /**
     * Change the outstanding status of a job
     * @param jobId ID of the job to update
     * @return Response with updated status
     */
    @PatchMapping("/{jobId}") // Path variable should be included in the URL
    public ResponseEntity<Integer> changeOutstandingStatus(@PathVariable Integer jobId) {
        return jobService.changeOutstandingStatus(jobId);
    }
}

