package com.example.ojt.service.candidate;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EduCandidateAddReq;
import com.example.ojt.model.dto.request.UpdateEduCandidateReq;
import com.example.ojt.model.dto.response.*;
import com.example.ojt.model.entity.*;
import com.example.ojt.repository.*;
import com.example.ojt.service.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CandidateService implements ICandidateService {
    private final ICandidateRepository candidateRepository;
    private final IEducationCandidateRepository educationCandidateRepository;
    private final ICertificateRepository certificateRepository;
    private final ISkillRepository skillRepository;
    private final IExperienceRepository experienceRepository;
    private final IProjectRepository projectRepository;

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

    @Override
    public CVResponse getCandidateCV(Integer candidateId) throws CustomException {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new CustomException("Candidate not found!", HttpStatus.NOT_FOUND));
        CVResponse response = new CVResponse();
//        Thiết lập các thông tin cơ bản
        response.setAbout(candidate.getAboutme());
        response.setPhone(candidate.getPhone());
        LocalDate birthDate = candidate.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        response.setAge(Period.between(birthDate, LocalDate.now()).getYears());
        response.setAddress(candidate.getAddress());
        response.setAvatar(candidate.getAvatar());
        response.setGender(candidate.getGender());
        response.setLinkLinkedin(candidate.getLinkLinkedin());
        response.setLinkGit(candidate.getLinkGit());
//        Chứng chỉ
        List<CertificateCVResponse> certificateCVResponses = new ArrayList<>();
        List<CertificateCandidate> certificateCandidates = certificateRepository.findAllByCandidateId(candidateId);
        for (CertificateCandidate certificateCandidate : certificateCandidates) {
            certificateCVResponses.add(new CertificateCVResponse(certificateCandidate.getName(), certificateCandidate.getOrganization(), certificateCandidate.getStartAt(), certificateCandidate.getEndAt(), certificateCandidate.getInfo()));
        }
        ;
        response.setCertificates(certificateCVResponses);
//      Kỹ năng
        List<SkillsCandidate> skills = skillRepository.findAllByCandidateId(candidateId);
        List<SkillCVResponse> skillCVResponses = new ArrayList<>();
        for (SkillsCandidate skillsCandidate : skills) {
            skillCVResponses.add(new SkillCVResponse(skillsCandidate.getName(), skillsCandidate.getLevelJob().getName()));
        }
        response.setSkills(skillCVResponses);
//        Kinh nghiệm
        List<ExperienceCVResponse> experienceCVResponses = new ArrayList<>();
        List<ExperienceCandidate> experiences = experienceRepository.findAllByCandidateId(candidateId);
        for (ExperienceCandidate experience : experiences) {
            experienceCVResponses.add(new ExperienceCVResponse(experience.getPosition(), experience.getCompany(), experience.getStartAt(), experience.getEndAt(), experience.getInfo()));
        }
        response.setExperiences(experienceCVResponses);
//        Dự án
        List<ProjectCandidate> projects = projectRepository.findAllByCandidateId(candidateId);
        List<ProjectCVResponse> projectCVResponses = new ArrayList<>();
        for (ProjectCandidate projectCandidate : projects) {
            projectCVResponses.add(new ProjectCVResponse(projectCandidate.getName(), projectCandidate.getLink(), projectCandidate.getStartAt(), projectCandidate.getEndAt(), projectCandidate.getInfo()));
        }
        response.setProjects(projectCVResponses);
        return response;
    }

    @Override
    public CandidateBasicInfoResponse getBasicInfo(Integer candidateId) throws CustomException {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new CustomException("Candidate not found!", HttpStatus.NOT_FOUND));
        CandidateBasicInfoResponse response = new CandidateBasicInfoResponse();
//        Thiết lập các thông tin cơ bản
        response.setAbout(candidate.getAboutme());
        response.setPhone(candidate.getPhone());
        LocalDate birthDate = candidate.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        response.setAge(Period.between(birthDate, LocalDate.now()).getYears());
        response.setAddress(candidate.getAddress());
        response.setAvatar(candidate.getAvatar());
        response.setGender(candidate.getGender());
        response.setLinkLinkedin(candidate.getLinkLinkedin());
        response.setLinkGit(candidate.getLinkGit());
        return response;
    }


}
