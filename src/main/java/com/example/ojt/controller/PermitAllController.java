package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.response.*;
import com.example.ojt.model.dto.responsewapper.DataResponse;
import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.EducationCandidate;
import com.example.ojt.service.candidate.ICandidateService;
import com.example.ojt.service.company.ICompanyService;
import com.example.ojt.service.job.IJobService;
import com.example.ojt.service.permitall.IPermitAllService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api.myservice.com/v1/permitall")
@RequiredArgsConstructor
public class PermitAllController {
   private final IPermitAllService permitAllService;
   private final IJobService jobService;
   private final ICompanyService companyService;
   private final ICandidateService candidateService;

   @GetMapping("/outstandingjob")
    public ResponseEntity<?> getOutStandingJobs() {
       List<OutStandingJobRes> outStandingJobs = permitAllService.getOutstandingJobs();
       APIResponse response = new APIResponse(200, "Get projects success");
       return new ResponseEntity<>(new DataResponse<>(response, outStandingJobs), HttpStatus.OK);
   }
    @GetMapping("/outstandingcompany")
    public ResponseEntity<?> getOutStandingJCompany() {
        List<Company> outStandingCompany = permitAllService.getOutstandingCompanies();
        APIResponse response = new APIResponse(200, "Get projects success");
        return new ResponseEntity<>(new DataResponse<>(response, outStandingCompany), HttpStatus.OK);
    }
    @GetMapping("/outstandingcandidate")
    public ResponseEntity<?> getOutStandingCandidate() {
        List<Candidate> outStandingCandidate = permitAllService.getOutstandingCandidates();
        APIResponse response = new APIResponse(200, "Get projects success");
        return new ResponseEntity<>(new DataResponse<>(response, outStandingCandidate), HttpStatus.OK);
    }

    @GetMapping("/stat")
    public ResponseEntity<?> getStat() {
        StatRes statRes = permitAllService.getStatRes();
       APIResponse response = new APIResponse(200, "Get projects success");
       return new ResponseEntity<>(new DataResponse<>(response, statRes), HttpStatus.OK);
    }


    @GetMapping("/candidate")
    public ResponseEntity<?> getAllCandidate(@PageableDefault(page = 0, size = 9, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                                              @RequestParam(defaultValue = "") String search)  {
        Page<Candidate> candidates = permitAllService.getCandidates(pageable,search);
        APIResponse response = new APIResponse(200, "Get candidate success");
        return new ResponseEntity<>(new DataResponse<>(response, candidates), HttpStatus.OK);
    }
}
