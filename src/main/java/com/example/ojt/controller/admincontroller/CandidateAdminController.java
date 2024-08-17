package com.example.ojt.controller.admincontroller;

import com.example.ojt.model.dto.request.CandidateEmailDTO;
import com.example.ojt.service.candidate.ICandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api.myservice.com/v1/admin")
public class CandidateAdminController {

    @Autowired
    private ICandidateService candidateService;


    /**
     * Lấy tất cả ứng viên và email của account
     */
    @GetMapping("/candidates")
    public ResponseEntity<Page<CandidateEmailDTO>> findAllCandidates(
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "ASC") String direction) {

        // Create a sorted pageable object based on the provided sort and direction parameters
        Pageable sortPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                Sort.by(Sort.Direction.fromString(direction), sort));

        // Retrieve candidates with the specified pagination, search, and sorting criteria
        Page<CandidateEmailDTO> candidates = candidateService.getAllCandidatesWithEmail(sortPageable, search);
        return ResponseEntity.ok(candidates);
    }

    /**
     * thay đổi trang thái
     * @param candidateId
     * @return
     */
    @PutMapping("/candidates/{candidateId}")
    public ResponseEntity<Integer> changeStatus(@PathVariable("candidateId") Integer candidateId) {
        return candidateService.changaStatus(candidateId);
    }

    @PatchMapping("/candidates/{candidateId}")
    public ResponseEntity<Integer> changeOutstandingStatus(@PathVariable Integer candidateId) {
        return candidateService.changeOutstandingStatus(candidateId);
    }
}

