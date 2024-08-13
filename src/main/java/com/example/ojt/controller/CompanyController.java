package com.example.ojt.controller;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EditCompanyRequest;
import com.example.ojt.service.candidate.ICandidateService;
import com.example.ojt.service.company.ICompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api.myservice.com/v1/company/")
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
    @GetMapping("/viewCandidateCV/{candidateId}")
    public ResponseEntity<?> viewCandidateCV(@PathVariable Integer candidateId) throws CustomException{
        return ResponseEntity.ok(candidateService.getCandidateCV(candidateId));
    }

    @GetMapping("/viewCandidateInfo/{candidateId}")
    public ResponseEntity<?> viewCandidateBasicInformation(@PathVariable Integer candidateId) throws CustomException{
        return ResponseEntity.ok(candidateService.getBasicInfo(candidateId));
    }
}
