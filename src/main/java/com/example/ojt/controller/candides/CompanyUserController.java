package com.example.ojt.controller.candides;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.response.CompanyResponse;
import com.example.ojt.model.dto.response.JobResponse;
import com.example.ojt.repository.IJobRepository;
import com.example.ojt.service.candidate.ICandidateService;
import com.example.ojt.service.company.ICompanyService;
import com.example.ojt.service.job.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/candidate/company")
@RequiredArgsConstructor
public class CompanyUserController {
    private final ICompanyService companyService;
    private final IJobService jobService;
    @GetMapping
    public ResponseEntity<Page<CompanyResponse>> findAllCompanies(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(direction), sort)
        );
        Page<CompanyResponse> companies = companyService.findAllCompanies(sortedPageable, location, name);
        return ResponseEntity.ok().body(companies);
    }


    @GetMapping("/{companyId}")
    public ResponseEntity<?> findCompanyById(@PathVariable Integer companyId) {
        try {
            CompanyResponse companyResponse = companyService.findById(companyId);
            return ResponseEntity.ok().body(companyResponse);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }



    @GetMapping("/{companyId}/related-companies")
    public ResponseEntity<List<CompanyResponse>> getRelatedCompanies(@PathVariable Integer companyId) {
        List<CompanyResponse> relatedCompanies = companyService.findCompaniesByTypeCompany(companyId);
        return ResponseEntity.ok(relatedCompanies);
    }

    @GetMapping("/{companyId}/jobs")
    public ResponseEntity<Page<JobResponse>> getAllJobsByCompany(
            @PathVariable Integer companyId,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            Pageable pageable) {
        try {
            Page<JobResponse> jobs = jobService.findAllByCompanyAndSearch(companyId, title, location, pageable);
            return ResponseEntity.ok(jobs);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(null);
        }
    }


}
