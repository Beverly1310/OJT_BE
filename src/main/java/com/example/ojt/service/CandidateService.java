package com.example.ojt.service;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EduCandidateAddReq;
import com.example.ojt.model.dto.request.UpdateEduCandidateReq;
import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.EducationCandidate;
import com.example.ojt.repository.ICandidateRepository;
import com.example.ojt.repository.IEducationCandidateRepository;
import com.example.ojt.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateService implements ICandidateService {
    private final ICandidateRepository candidateRepository;
    private final IEducationCandidateRepository educationCandidateRepository;

    private Candidate getCurrentCandidate() {
        return candidateRepository.findCandidateByAccountId(AccountService.getCurrentUser().getId());
    }

    @Override
    public boolean addEducation(EduCandidateAddReq eduCandidateAddReq) {
        EducationCandidate educationCandidate = EducationCandidate.builder()
                .nameEducation(eduCandidateAddReq.getNameEducation())
                .endAt(eduCandidateAddReq.getEndAt())
                .info(eduCandidateAddReq.getInfo())
                .major(eduCandidateAddReq.getMajor())
                .startAt(eduCandidateAddReq.getStartAt())
                .status(eduCandidateAddReq.getStatus())
                .candidate(candidateRepository.findCandidateByAccountId(AccountService.getCurrentUser().getId()))
                .build();
        educationCandidateRepository.save(educationCandidate);
        return true;
    }

    @Override
    public Page<EducationCandidate> getEducationCandidates(Pageable pageable, String search, String direction) {
        if (direction != null) {
            if (direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().ascending());
            }
        }
        Page<EducationCandidate> educationCandidates;
        if (search != null && !search.isBlank()) {
            educationCandidates = educationCandidateRepository.findEducationCandidatesByCandidateAndNameEducationContains(getCurrentCandidate(), search, pageable);
        } else {
            educationCandidates = educationCandidateRepository.findAllByCandidate(getCurrentCandidate(), pageable);
        }
        return educationCandidates;
    }

    @Override
    public EducationCandidate getEducationCandidate(Integer id) throws CustomException {
        return educationCandidateRepository.findById(id).orElseThrow(() -> new CustomException("Education not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean editEducationCandidate(UpdateEduCandidateReq updateEduCandidateReq) throws CustomException {
        EducationCandidate educationCandidate = educationCandidateRepository.findById(updateEduCandidateReq.getId()).orElseThrow(() -> new CustomException("Education not found", HttpStatus.NOT_FOUND));
        if (updateEduCandidateReq.getEndAt() != null) {
            educationCandidate.setEndAt(updateEduCandidateReq.getEndAt());
        }
        if (updateEduCandidateReq.getInfo() != null && !updateEduCandidateReq.getInfo().isBlank()) {
            educationCandidate.setInfo(updateEduCandidateReq.getInfo());
        }
        if (updateEduCandidateReq.getMajor() != null && !updateEduCandidateReq.getMajor().isBlank()) {
            educationCandidate.setMajor(updateEduCandidateReq.getMajor());
        }
        if (updateEduCandidateReq.getNameEducation() != null && !updateEduCandidateReq.getNameEducation().isBlank()) {
            educationCandidate.setNameEducation(updateEduCandidateReq.getNameEducation());
        }
        if (updateEduCandidateReq.getStartAt() != null) {
            educationCandidate.setStartAt(updateEduCandidateReq.getStartAt());
        }
        if (updateEduCandidateReq.getStatus() != null && (updateEduCandidateReq.getStatus() == 0 || updateEduCandidateReq.getStatus() == 2 || updateEduCandidateReq.getStatus() == 1)) {
            educationCandidate.setStatus(updateEduCandidateReq.getStatus());
        }
        educationCandidateRepository.save(educationCandidate);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteEducationCandidate(Integer id) throws CustomException {
        EducationCandidate educationCandidate = educationCandidateRepository.findById(id).orElseThrow(() -> new CustomException("Education not found", HttpStatus.NOT_FOUND));
        educationCandidateRepository.delete(educationCandidate);
        return true;
    }
}
