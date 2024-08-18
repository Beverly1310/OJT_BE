package com.example.ojt.controller.admincontroller;

import com.example.ojt.model.dto.request.CandidatePerMonth;
import com.example.ojt.model.dto.request.CandidateEmailDTO;
import com.example.ojt.service.candidate.ICandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


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
     *
     * @param candidateId
     * @return
     */
    @PutMapping("/candidates/{candidateId}")
    public ResponseEntity<Integer> changeStatus(@PathVariable("candidateId") Integer candidateId) {
        return candidateService.changaStatus(candidateId);
    }


    /**
     * ứng viên nổi bật
     *
     * @param candidateId
     * @return
     */
    @PatchMapping("/candidates/{candidateId}")
    public ResponseEntity<Integer> changeOutstandingStatus(@PathVariable Integer candidateId) {
        return candidateService.changeOutstandingStatus(candidateId);
    }

    /**
     * Lấy ứng viên theo khoảng thời gian
     *
     * @param startDate
     * @param endDate
     * @param pageable
     * @return
     */
    @GetMapping("/candidates/date-range")
    public ResponseEntity<Page<CandidatePerMonth>> findCandidatesByDateRange(
            @RequestParam("startDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "dd/MM/yyyy") Date endDate,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {

        // Retrieve candidates based on date range and pagination
        Page<CandidatePerMonth> candidates = candidateService.findCandidatesByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(candidates);
    }


    /**
     * lấy danh sách ứng viên theo tháng
     *
     * @param year
     * @param pageable
     * @return
     */

    @GetMapping("/candidates/by-month")
    public ResponseEntity<List<CandidatePerMonth>> findCandidatesByMonth(
            @RequestParam("year") int year,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        List<CandidatePerMonth> candidatesByMonth = candidateService.findCandidatesByMonth(year, pageable);
        return ResponseEntity.ok(candidatesByMonth);
    }

    /**
     * lấy s lượng ứng viên
     * @return
     */

    @GetMapping("/candidates/count")
    public ResponseEntity<Long> countCandidates() {
        long count = candidateService.countCandidates();
        return ResponseEntity.ok(count);
    }
}


