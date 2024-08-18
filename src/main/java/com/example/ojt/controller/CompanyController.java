package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import com.example.ojt.model.dto.response.APIResponse;
import com.example.ojt.model.dto.response.CandidateInfoRes;
import com.example.ojt.model.dto.response.CompanyResponse;
import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.dto.responsewapper.DataResponse;
import com.example.ojt.model.entity.Company;
import com.example.ojt.service.candidate.ICandidateService;
import com.example.ojt.service.company.ICompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api.myservice.com/v1/company")
@RequiredArgsConstructor
public class CompanyController {
    private final ICompanyService companyService;
    private final ICandidateService candidateService;


    @PutMapping("/update")
    public ResponseEntity<?> updateCompany(
            @ModelAttribute @Valid EditCompanyRequest companyRequest) {
        try {
            boolean isUpdated = companyService.update(companyRequest);
            if (isUpdated) {
                return ResponseEntity.ok("Company updated successfully");
            } else {
                return ResponseEntity.status(404).body("Company not found");
            }
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Lỗi ko mong muốn");
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> findCurrentCompany() {
        try {
            CompanyResponse companyResponse = companyService.findCurrentCompany();
            return ResponseEntity.ok().body(companyResponse);
        } catch (CustomException e) {
            return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
        }
    }
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

    @GetMapping("/{companyId}/related-companies")
    public ResponseEntity<List<CompanyResponse>> getRelatedCompanies(@PathVariable Integer companyId) {
        List<CompanyResponse> relatedCompanies = companyService.findCompaniesByTypeCompany(companyId);
        return ResponseEntity.ok(relatedCompanies);
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

    @GetMapping("outstanding")
    public ResponseEntity<?> OutstandingCandidate() {
        return candidateService.findOutstandingCandidates();
    }




    @GetMapping("viewCandidateInfo/{candidateId}")
    public ResponseEntity<?> viewCandidateBasicInformation(@PathVariable Integer candidateId) throws CustomException{
        return ResponseEntity.ok(candidateService.getBasicInfo(candidateId));
    }


    @GetMapping("/job/{jobId}/candidates")
    public List<Candidate> getCandidatesByJob(@PathVariable Integer jobId) {
        return candidateService.getCandidatesByJobId(jobId);
    }

    @GetMapping("/candidate/info/{id}")
    public ResponseEntity<?> findCandidateById(@PathVariable Integer id) throws CustomException {
        CandidateInfoRes candidateInfoRes = companyService.getCandidateInfoById(id);
        APIResponse response = new APIResponse(200, "Get info candidate success");
        return new ResponseEntity<>(new DataResponse<>(response, candidateInfoRes), HttpStatus.OK);
    }
}
