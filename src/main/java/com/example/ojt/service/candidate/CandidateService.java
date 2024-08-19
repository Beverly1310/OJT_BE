package com.example.ojt.service.candidate;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.*;
import com.example.ojt.model.dto.response.*;
import com.example.ojt.model.entity.*;
import com.example.ojt.repository.*;
import com.example.ojt.service.UploadService;
import com.example.ojt.service.account.AccountService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class CandidateService implements ICandidateService {
    private final ICandidateRepository candidateRepository;
    private final IEducationCandidateRepository educationCandidateRepository;
    private final IExperienceCandidateRepository experienceCandidateRepository;
    private final ICertificateRepository certificateRepository;
    private final IProjectCandidateRepository projectCandidateRepository;
    private final ISkillCandidateRepository skillCandidateRepository;
    private final ILevelJobRepository levelJobRepository;

    //    private final ISkillRepository skillRepository;

    private final IExperienceRepository experienceRepository;
    private final ICVRepository cvRepository;
    private final IProjectRepository projectRepository;
    private final IJobRepository jobRepository;
    private final ICompanyRepository companyRepository;
    private final IJobCandidateRepository jobCandidateRepository;

    private final UploadService uploadService;
    private final ILetterRepository letterRepository;

    private Candidate getCurrentCandidate() {
        return candidateRepository.findCandidateByAccountId(AccountService.getCurrentUser().getId());
    }

    @Override
    @Transactional
    public boolean addEducation(EduCandidateAddReq eduCandidateAddReq) throws CustomException {
        EducationCandidate educationCandidateCheck = educationCandidateRepository.findByNameEducationAndCandidate(eduCandidateAddReq.getNameEducation(), getCurrentCandidate()).orElse(null);
        if (educationCandidateCheck != null) {
            throw new CustomException("Education already exist", HttpStatus.BAD_REQUEST);
        }
        if (eduCandidateAddReq.getStartAt() != null && eduCandidateAddReq.getEndAt() != null) {
            if (eduCandidateAddReq.getEndAt().toInstant().isBefore(eduCandidateAddReq.getStartAt().toInstant())) {
                throw new CustomException("End date must after start date", HttpStatus.BAD_REQUEST);
            }
        }

        EducationCandidate educationCandidate = EducationCandidate.builder()
                .nameEducation(eduCandidateAddReq.getNameEducation())
                .endAt(eduCandidateAddReq.getEndAt())
                .info(eduCandidateAddReq.getInfo())
                .major(eduCandidateAddReq.getMajor())
                .startAt(eduCandidateAddReq.getStartAt())
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
        return educationCandidateRepository.findByIdAndCandidate(id, getCurrentCandidate()).orElseThrow(() -> new CustomException("Education not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public boolean editEducationCandidate(UpdateEduCandidateReq updateEduCandidateReq) throws CustomException {
        EducationCandidate educationCandidate = educationCandidateRepository.findById(updateEduCandidateReq.getId()).orElseThrow(() -> new CustomException("Education not found", HttpStatus.NOT_FOUND));
        EducationCandidate educationCandidateCheck = educationCandidateRepository.findByNameEducation(updateEduCandidateReq.getNameEducation()).orElse(null);
        if (educationCandidateCheck != null && educationCandidateCheck.getNameEducation().equals(updateEduCandidateReq.getNameEducation())) {
            throw new CustomException("Education already exist", HttpStatus.BAD_REQUEST);
        }
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
        if (updateEduCandidateReq.getEndAt() != null && updateEduCandidateReq.getEndAt() != null) {
            if (educationCandidate.getEndAt().toInstant().isBefore(educationCandidate.getStartAt().toInstant())) {
                throw new CustomException("End date must after start date", HttpStatus.BAD_REQUEST);
            }
        }

        educationCandidateRepository.save(educationCandidate);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteEducationCandidate(Integer id) throws CustomException {
        EducationCandidate educationCandidate = educationCandidateRepository.findById(id).orElseThrow(() -> new CustomException("Education not found", HttpStatus.NOT_FOUND));
        if (educationCandidate.getCandidate() != getCurrentCandidate()) {
            throw new CustomException("This education is not belong to you", HttpStatus.BAD_REQUEST);
        }
        educationCandidateRepository.delete(educationCandidate);
        return true;
    }

    @Override
    @Transactional
    public boolean addExp(AddExpCandidateReq addExpCandidateReq) throws CustomException {
        ExperienceCandidate experienceCandidateCheck = experienceCandidateRepository.findByPositionAndCompanyAndCandidate(addExpCandidateReq.getPosition(), addExpCandidateReq.getCompany(), getCurrentCandidate()).orElse(null);
        if (experienceCandidateCheck != null) {
            throw new CustomException("Experience already exist", HttpStatus.BAD_REQUEST);
        }
        if (addExpCandidateReq.getStartAt() != null && addExpCandidateReq.getEndAt() != null) {
            if (addExpCandidateReq.getEndAt().toInstant().isBefore(addExpCandidateReq.getStartAt().toInstant())) {
                throw new CustomException("End date must after start date", HttpStatus.BAD_REQUEST);
            }
        }
        ExperienceCandidate experienceCandidate = ExperienceCandidate.builder()
                .company(addExpCandidateReq.getCompany())
                .endAt(addExpCandidateReq.getEndAt())
                .info(addExpCandidateReq.getInfo())
                .position(addExpCandidateReq.getPosition())
                .startAt(addExpCandidateReq.getStartAt())
                .candidate(getCurrentCandidate())
                .build();
        experienceCandidateRepository.save(experienceCandidate);
        return true;
    }

    @Override
    public Page<ExperienceCandidate> getExperienceCandidates(Pageable pageable, String search, String direction) {
        if (direction != null) {
            if (direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().ascending());
            }
        }
        Page<ExperienceCandidate> experienceCandidates;
        if (search != null && !search.isBlank()) {
            experienceCandidates = experienceCandidateRepository.findByCandidateAndCompanyContains(getCurrentCandidate(), search, pageable);
        } else {
            experienceCandidates = experienceCandidateRepository.findByCandidate(getCurrentCandidate(), pageable);
        }
        return experienceCandidates;
    }

    @Override
    public ExperienceCandidate getExperienceCandidate(Integer id) throws CustomException {
        return experienceCandidateRepository.findByIdAndCandidate(id, getCurrentCandidate()).orElseThrow(() -> new CustomException("Experience not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public boolean editExperienceCandidate(UpdateExpCandidateReq updateExpCandidateReq) throws CustomException {
        ExperienceCandidate experienceCandidate = experienceCandidateRepository.findById(updateExpCandidateReq.getId()).orElseThrow(() -> new CustomException("Experience not found", HttpStatus.NOT_FOUND));
        ExperienceCandidate experienceCandidateCheck = experienceCandidateRepository.findByPositionAndCompany(updateExpCandidateReq.getPosition(), updateExpCandidateReq.getCompany()).orElse(null);
        if (experienceCandidateCheck != null && !experienceCandidateCheck.getId().equals(experienceCandidate.getId())) {
            throw new CustomException("Experience already exist", HttpStatus.BAD_REQUEST);
        }

        if (updateExpCandidateReq.getCompany() != null && !updateExpCandidateReq.getCompany().isBlank()) {
            experienceCandidate.setCompany(updateExpCandidateReq.getCompany());
        }
        if (updateExpCandidateReq.getEndAt() != null) {
            experienceCandidate.setEndAt(updateExpCandidateReq.getEndAt());
        }
        if (updateExpCandidateReq.getStartAt() != null) {
            experienceCandidate.setStartAt(updateExpCandidateReq.getStartAt());
        }
        if (updateExpCandidateReq.getStartAt() != null && updateExpCandidateReq.getEndAt() != null) {
            if (experienceCandidate.getEndAt().toInstant().isBefore(experienceCandidate.getStartAt().toInstant())) {
                throw new CustomException("End date must after start date", HttpStatus.BAD_REQUEST);
            }
        }
        if (updateExpCandidateReq.getInfo() != null && !updateExpCandidateReq.getInfo().isBlank()) {
            experienceCandidate.setInfo(updateExpCandidateReq.getInfo());
        }
        if (updateExpCandidateReq.getPosition() != null && !updateExpCandidateReq.getPosition().isBlank()) {
            experienceCandidate.setPosition(updateExpCandidateReq.getPosition());
        }
        if (updateExpCandidateReq.getStartAt() != null && updateExpCandidateReq.getEndAt() != null) {
            if (experienceCandidate.getEndAt().toInstant().isBefore(experienceCandidate.getStartAt().toInstant())) {
                throw new CustomException("End date must after start date", HttpStatus.BAD_REQUEST);
            }
        }
        experienceCandidateRepository.save(experienceCandidate);
        return true;
    }

    @Override
    @Transactional
    public boolean deleteExperienceCandidate(Integer id) throws CustomException {
        ExperienceCandidate experienceCandidate = experienceCandidateRepository.findById(id).orElseThrow(() -> new CustomException("Experience not found", HttpStatus.NOT_FOUND));
        if (experienceCandidate.getCandidate() != getCurrentCandidate()) {
            throw new CustomException("This experience is not belong to you", HttpStatus.BAD_REQUEST);
        }
        experienceCandidateRepository.delete(experienceCandidate);
        return true;
    }

    @Override
    @Transactional
    public boolean addCertificate(AddCertificateReq addCertificateReq) throws CustomException {
        if (certificateRepository.findByNameAndCandidate(addCertificateReq.getName(), getCurrentCandidate()).orElse(null) != null) {
            throw new CustomException("Certificate already exist", HttpStatus.BAD_REQUEST);
        }
        CertificateCandidate certificateCandidate = CertificateCandidate.builder()
                .endAt(addCertificateReq.getEndAt())
                .info(addCertificateReq.getInfo())
                .name(addCertificateReq.getName())
                .organization(addCertificateReq.getOrganization())
                .startAt(addCertificateReq.getStartAt())
                .candidate(getCurrentCandidate())
                .build();
        certificateRepository.save(certificateCandidate);
        return true;
    }

    @Override
    public Page<CertificateCandidate> getCertificateCandidates(Pageable pageable, String search, String direction) {
        if (direction != null) {
            if (direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().ascending());
            }
        }
        Page<CertificateCandidate> certificateCandidates;
        if (search != null && !search.isBlank()) {
            certificateCandidates = certificateRepository.findByCandidateAndNameContains(getCurrentCandidate(), search, pageable);
        } else {
            certificateCandidates = certificateRepository.findByCandidate(getCurrentCandidate(), pageable);
        }
        return certificateCandidates;
    }

    @Override
    public CertificateCandidate getCertificateCandidate(Integer id) throws CustomException {
        return certificateRepository.findByIdAndCandidate(id, getCurrentCandidate()).orElseThrow(() -> new CustomException("Certificate not found", HttpStatus.NOT_FOUND));
    }

    @Override
    @Transactional
    public boolean deleteCertificate(Integer id) throws CustomException {
        CertificateCandidate certificateCandidate = certificateRepository.findById(id).orElseThrow(() -> new CustomException("Certificate not found", HttpStatus.NOT_FOUND));
        if (certificateCandidate.getCandidate() != getCurrentCandidate()) {
            throw new CustomException("This certificate is not belong to you", HttpStatus.BAD_REQUEST);
        }
        certificateRepository.delete(certificateCandidate);
        return true;
    }

    @Override
    @Transactional
    public boolean editCertificate(UpdateCertificateReq updateCertificateReq) throws CustomException {
        CertificateCandidate certificateCandidate = certificateRepository.findById(updateCertificateReq.getId()).orElseThrow(() -> new CustomException("Certificate not found", HttpStatus.NOT_FOUND));
        CertificateCandidate certificateCandidateCheck = certificateRepository.findByName(updateCertificateReq.getName()).orElse(null);
        if (certificateCandidateCheck != null && !certificateCandidateCheck.getId().equals(certificateCandidate.getId())) {
            throw new CustomException("Certificate already exist", HttpStatus.BAD_REQUEST);
        }
        if (updateCertificateReq.getEndAt() != null) {
            certificateCandidate.setEndAt(updateCertificateReq.getEndAt());
        }
        if (updateCertificateReq.getInfo() != null && !updateCertificateReq.getInfo().isBlank()) {
            certificateCandidate.setInfo(updateCertificateReq.getInfo());
        }
        if (updateCertificateReq.getName() != null && !updateCertificateReq.getName().isBlank()) {
            certificateCandidate.setName(updateCertificateReq.getName());
        }
        if (updateCertificateReq.getOrganization() != null && !updateCertificateReq.getOrganization().isBlank()) {
            certificateCandidate.setOrganization(updateCertificateReq.getOrganization());
        }
        if (updateCertificateReq.getStartAt() != null) {
            certificateCandidate.setStartAt(updateCertificateReq.getStartAt());
        }
        if (updateCertificateReq.getStartAt() != null && updateCertificateReq.getEndAt() != null) {
            if (certificateCandidate.getEndAt().toInstant().isBefore(certificateCandidate.getStartAt().toInstant())) {
                throw new CustomException("End date must be after start date", HttpStatus.BAD_REQUEST);
            }
        }
        certificateRepository.save(certificateCandidate);
        return true;
    }

    @Override
    public boolean addProject(AddProjectCandidateReq addProjectCandidateReq) throws CustomException {
        if (projectCandidateRepository.findByNameAndCandidate(addProjectCandidateReq.getName(), getCurrentCandidate()).orElse(null) != null) {
            throw new CustomException("Project already exist", HttpStatus.BAD_REQUEST);
        }
        if (addProjectCandidateReq.getEndAt() != null && addProjectCandidateReq.getEndAt() != null) {
            if (addProjectCandidateReq.getEndAt().toInstant().isBefore(addProjectCandidateReq.getStartAt().toInstant())) {
                throw new CustomException("End date must be after start date", HttpStatus.BAD_REQUEST);
            }
        }
        ProjectCandidate projectCandidate = ProjectCandidate.builder()
                .endAt(addProjectCandidateReq.getEndAt())
                .info(addProjectCandidateReq.getInfo())
                .link(addProjectCandidateReq.getLink())
                .name(addProjectCandidateReq.getName())
                .startAt(addProjectCandidateReq.getStartAt())
                .candidate(getCurrentCandidate())
                .build();
        projectCandidateRepository.save(projectCandidate);
        return true;
    }

    @Override
    public Page<ProjectCandidate> getProjects(Pageable pageable, String search, String direction) {
        if (direction != null) {
            if (direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().ascending());
            }
        }
        Page<ProjectCandidate> projectCandidates;
        if (search != null && !search.isBlank()) {
            projectCandidates = projectCandidateRepository.findByCandidateAndNameContains(getCurrentCandidate(), search, pageable);
        } else {
            projectCandidates = projectCandidateRepository.findByCandidate(getCurrentCandidate(), pageable);
        }
        return projectCandidates;
    }

    @Override
    public ProjectCandidate findProjectByName(String projectName) throws CustomException {
        return projectCandidateRepository.findByCandidateAndNameContains(getCurrentCandidate(), projectName).orElseThrow(() -> new CustomException("Project not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean editProject(UpdateProjectReq updateProjectReq) throws CustomException {
        ProjectCandidate projectCandidate = projectCandidateRepository.findById(updateProjectReq.getId()).orElseThrow(() -> new CustomException("Project not found", HttpStatus.NOT_FOUND));
        ProjectCandidate projectCandidateCheck = projectCandidateRepository.findByName(updateProjectReq.getName()).orElse(null);
        if (projectCandidateCheck != null && !projectCandidateCheck.getId().equals(projectCandidate.getId())) {
            throw new CustomException("Project already exist", HttpStatus.BAD_REQUEST);
        }
        if (updateProjectReq.getInfo() != null && !updateProjectReq.getInfo().isBlank()) {
            projectCandidate.setInfo(updateProjectReq.getInfo());
        }
        if (updateProjectReq.getName() != null && !updateProjectReq.getName().isBlank()) {
            projectCandidate.setName(updateProjectReq.getName());
        }
        if (updateProjectReq.getEndAt() != null) {
            projectCandidate.setEndAt(updateProjectReq.getEndAt());
        }
        if (updateProjectReq.getStartAt() != null) {
            projectCandidate.setStartAt(updateProjectReq.getStartAt());
        }
        if (updateProjectReq.getEndAt() != null && updateProjectReq.getEndAt() != null) {
            if (projectCandidate.getEndAt().toInstant().isBefore(projectCandidate.getStartAt().toInstant())) {
                throw new CustomException("End at must be after start at", HttpStatus.BAD_REQUEST);
            }
        }
        if (updateProjectReq.getLink() != null && !updateProjectReq.getLink().isBlank()) {
            projectCandidate.setLink(updateProjectReq.getLink());
        }
        projectCandidateRepository.save(projectCandidate);
        return true;
    }

    @Override
    public boolean deleteProject(Integer id) throws CustomException {
        ProjectCandidate projectCandidate = projectCandidateRepository.findById(id).orElseThrow(() -> new CustomException("Project not found", HttpStatus.NOT_FOUND));
        if (projectCandidate.getCandidate() != getCurrentCandidate()) {
            throw new CustomException("This project is not belong to you", HttpStatus.BAD_REQUEST);
        }
        projectCandidateRepository.delete(projectCandidate);
        return true;
    }

    @Override
    public ProjectCandidate getProject(Integer id) throws CustomException {
        return projectCandidateRepository.findByIdAndCandidate(id, getCurrentCandidate()).orElseThrow(() -> new CustomException("Project not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean addSkill(AddSkillCandidateReq addSkillCandidateReq) throws CustomException {
        if (skillCandidateRepository.findByNameAndCandidate(addSkillCandidateReq.getName(), getCurrentCandidate()).orElse(null) != null) {
            throw new CustomException("Skill already exist", HttpStatus.BAD_REQUEST);
        }
        SkillsCandidate skillsCandidate = SkillsCandidate.builder()
                .name(addSkillCandidateReq.getName())
                .candidate(getCurrentCandidate())
                .levelJob(levelJobRepository.findById(addSkillCandidateReq.getLevelJobId()).orElseThrow(() -> new CustomException("Level Job not found", HttpStatus.NOT_FOUND)))
                .build();
        skillCandidateRepository.save(skillsCandidate);
        return true;
    }

    @Override
    public Page<SkillsCandidate> getSkills(Pageable pageable, String search, String direction) {
        if (direction != null) {
            if (direction.equalsIgnoreCase("desc")) {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().descending());
            } else {
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().ascending());
            }
        }
        Page<SkillsCandidate> skillsCandidates;
        if (search != null && !search.isBlank()) {
            skillsCandidates = skillCandidateRepository.findByCandidateAndNameContains(getCurrentCandidate(), search, pageable);
        } else {
            skillsCandidates = skillCandidateRepository.findByCandidate(getCurrentCandidate(), pageable);
        }
        return skillsCandidates;
    }

    @Override
    public SkillsCandidate getSkill(Integer id) throws CustomException {
        return skillCandidateRepository.findByIdAndCandidate(id, getCurrentCandidate()).orElseThrow(() -> new CustomException("Skill not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean updateSkill(UpdateSkillReq updateSkillReq) throws CustomException {
        SkillsCandidate skillsCandidate = skillCandidateRepository.findById(updateSkillReq.getId()).orElseThrow(() -> new CustomException("Skill not found", HttpStatus.NOT_FOUND));
        SkillsCandidate skillsCandidateCheck = skillCandidateRepository.findByName(updateSkillReq.getName()).orElse(null);
        if (skillsCandidateCheck != null && !skillsCandidateCheck.getId().equals(skillsCandidate.getId())) {
            throw new CustomException("Skill already exist", HttpStatus.BAD_REQUEST);
        }
        if (updateSkillReq.getName() != null && !updateSkillReq.getName().isBlank()) {
            skillsCandidate.setName(updateSkillReq.getName());
        }
        if (updateSkillReq.getLevelJobName() != null && !updateSkillReq.getLevelJobName().isBlank()) {
            skillsCandidate.setLevelJob(levelJobRepository.findByName(updateSkillReq.getLevelJobName()).orElseThrow(() -> new CustomException("Level Job not found", HttpStatus.NOT_FOUND)));
        }
        skillCandidateRepository.save(skillsCandidate);
        return true;
    }

    @Override
    public boolean deleteSkill(Integer id) throws CustomException {
        SkillsCandidate skillsCandidate = skillCandidateRepository.findById(id).orElseThrow(() -> new CustomException("Skill not found", HttpStatus.NOT_FOUND));
        if (skillsCandidate.getCandidate() != getCurrentCandidate()) {
            throw new CustomException("This skill is not belong to you", HttpStatus.BAD_REQUEST);
        }
        skillCandidateRepository.delete(skillsCandidate);
        return true;
    }

    @Override
    public Object getCandidateCV(Integer candidateId) throws CustomException {
        CV topCV = cvRepository.findByCandidateIdAndStatusTrue(candidateId);
        if (topCV != null) {
            return new CVResponse(topCV.getId(), topCV.getFileName(), topCV.getUrl(), topCV.isStatus(), topCV.getCreatedAt());
        } else {
            return getDefaultCVByCandidate(candidateId);
        }
    }

    @Override
    public CandidateBasicInfoResponse getBasicInfo(Integer candidateId) throws CustomException {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new CustomException("Candidate not found!", HttpStatus.NOT_FOUND));
        CandidateBasicInfoResponse response = new CandidateBasicInfoResponse();
//        Thiết lập các thông tin cơ bản
        response.setName(candidate.getName());
        response.setAbout(candidate.getAboutme());
        response.setPhone(candidate.getPhone());
        LocalDate birthDate = candidate.getBirthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        response.setAge(Period.between(birthDate, LocalDate.now()).getYears());
        response.setAddress(candidate.getAddress());
        response.setAvatar(candidate.getAvatar());
        response.setGender(candidate.getGender());
        response.setLinkLinkedin(candidate.getLinkLinkedin());
        response.setLinkGit(candidate.getLinkGit());
        response.setPosition(candidate.getPosition());
        List<ExperienceCVResponse> experienceCVResponses = new ArrayList<>();
        List<ExperienceCandidate> experiences = experienceRepository.findAllByCandidateId(candidateId);
        for (ExperienceCandidate experience : experiences) {
            experienceCVResponses.add(new ExperienceCVResponse(experience.getPosition(), experience.getCompany(), experience.getStartAt(), experience.getEndAt(), experience.getInfo()));
        }
        response.setExperience(experienceCVResponses);
        List<SkillsCandidate> skills = skillCandidateRepository.findAllByCandidateId(candidateId);
        List<String> skillList = new ArrayList<>();
        for (SkillsCandidate skill : skills) {
            skillList.add(skill.getName());
        }
        response.setSkills(skillList);
        Optional<ApplicationLetter> letter = letterRepository.findByCandidateId(candidateId);
        letter.ifPresent(applicationLetter -> response.setLetter(applicationLetter.getContent()));
        return response;
    }


    @Override
    public Page<CandidateEmailDTO> getAllCandidatesWithEmail(Pageable pageable, String search) {
        // Search for candidates by name or account email
        return candidateRepository.findByNameContainingOrAccountEmailContaining(search, search, pageable)
                .map(candidate -> new CandidateEmailDTO(
                        candidate.getId(),
                        candidate.getName(),
                        candidate.getAccount() != null ? candidate.getAccount().getEmail() : null,
                        candidate.getBirthday(),
                        candidate.getAddress(),
                        candidate.getPhone(),
                        candidate.getStatus(),
                        candidate.getGender(),
                        candidate.getLinkLinkedin(),
                        candidate.getLinkGit(),
                        candidate.getPosition(),
                        candidate.getOutstanding()
                ));
    }


    @Override
    public ResponseEntity<Integer> changaStatus(Integer candidateId) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);
        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setStatus(candidate.getStatus() == 1 ? 0 : 1);
            candidateRepository.save(candidate);
            return ResponseEntity.ok(candidate.getStatus());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




    @Override
    public UserInfo getInfoByUser() {
        return UserInfo.builder()
                .candidate(getCurrentCandidate())
                .certificate(certificateRepository.findAllByCandidateId(getCurrentCandidate().getId()))
                .education(educationCandidateRepository.findAllByCandidate(getCurrentCandidate()))
                .experience(experienceCandidateRepository.findAllByCandidate(getCurrentCandidate()))
                .project(projectRepository.findAllByCandidateId(getCurrentCandidate().getId()))
                .skillsCandidates(skillCandidateRepository.findAllByCandidateId(getCurrentCandidate().getId()))
                .build();
    }

    @Override
    public List<Candidate> getCandidatesByJobId(Integer jobId) {
        List<JobCandidates> jobCandidates = jobCandidateRepository.findByJobId(jobId);
        return jobCandidates.stream()
                .map(JobCandidates::getCandidate)
                .toList();

    }

    @Override
    public ResponseEntity<Integer> changeOutstandingStatus(Integer candidateId) {
        Optional<Candidate> candidateOptional = candidateRepository.findById(candidateId);

        if (candidateOptional.isPresent()) {
            Candidate candidate = candidateOptional.get();
            candidate.setOutstanding(candidate.getOutstanding() == 1 ? 0 : 1);
            candidateRepository.save(candidate);
            return ResponseEntity.status(HttpStatus.OK).body(candidate.getOutstanding());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Override

    public List<LevelJob> getLevelJobs() {
        return levelJobRepository.findAll();
    }

    @Override

    public ResponseEntity<List<Candidate>> findOutstandingCandidates() {
        List<Candidate> outstandingCandidates = candidateRepository.findOutstandingCandidates();
        return ResponseEntity.status(HttpStatus.OK).body(outstandingCandidates);
    }

    @Override
    public Page<CandidatePerMonth> findCandidatesByDateRange(Date startDate, Date endDate, Pageable pageable) {
        // Fetch candidates within the date range from the repository
        Page<Candidate> candidates = candidateRepository.findByCreatedAtBetween(startDate, endDate, pageable);

        // Map candidates to CandidateEmailDTO and return the paginated result
        return null;
    }

    @Override
    public List<CandidatePerMonth> findCandidatesByMonth(int year, Pageable pageable) {
        List<CandidatePerMonth> candidatesByMonth = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            List<Candidate> candidates = candidateRepository.findCandidatesByMonth(year, month);
            CandidatePerMonth candidatePerMonth = CandidatePerMonth.builder()
                    .month("Tháng "+month)
                    .number(candidates.size())
                    .build();
            candidatesByMonth.add(candidatePerMonth);
        }

        return candidatesByMonth;
    }

    @Override
    public long countCandidates() {
        return candidateRepository.count();
    }

    public void uploadCV(MultipartFile file) throws CustomException {
        // Validate dạng file
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("application/pdf") &&
                        !contentType.equals("application/msword") &&
                        !contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))) {
            throw new CustomException("Invalid file type. Only PDF and DOC/DOCX files are allowed.", HttpStatus.BAD_REQUEST);
        }
        //Upload file
        String url = uploadService.uploadFileToServer(file);
        CV cv = CV.builder()
                .url(url)
                .candidate(getCurrentCandidate())
                .fileName(getCurrentCandidate().getName() + LocalDate.now())
                .status(false)
                .createdAt(new Date())
                .build();
        cvRepository.save(cv);
    }


    @Override
    public List<CVResponse> findAllByCurrentCandidate() {
        List<CV> cvList = cvRepository.findAllByCandidate(getCurrentCandidate());
        List<CVResponse> responses = new ArrayList<>();
        for (CV cv : cvList) {
            responses.add(new CVResponse(cv.getId(), cv.getFileName(), cv.getUrl(), cv.isStatus(), cv.getCreatedAt()));
        }
        return responses;
    }

    @Override
    @Transactional
    public void toggleCVPriority(Integer id) throws CustomException {
        List<CV> userCVs = cvRepository.findAllByCandidate(getCurrentCandidate());
        for (CV userCV : userCVs) {
            if (!Objects.equals(userCV.getId(), id)) {
                userCV.setStatus(false);
            } else {
                userCV.setStatus(!userCV.isStatus());
            }
        }

    }

    @Override
    public void deleteCV(Integer id) throws CustomException {
        CV cv = cvRepository.findById(id).orElseThrow(() -> new CustomException("CV not found!", HttpStatus.NOT_FOUND));
        cvRepository.delete(cv);
    }

    @Override
    public CV getTopCV(Integer userId) {
        return cvRepository.findByCandidateIdAndStatusTrue(userId);
    }

    @Override
    public void editCVName(Integer id, String name) throws CustomException {
        CV cv = cvRepository.findById(id).orElseThrow(() -> new CustomException("CV not found!", HttpStatus.NOT_FOUND));
        cv.setFileName(name);
        cvRepository.save(cv);
    }

    @Override
    public CVResponse getCVById(Integer id) throws CustomException {
        CV cv = cvRepository.findById(id).orElseThrow(() -> new CustomException("CV not found!", HttpStatus.NOT_FOUND));
        if (!Objects.equals(getCurrentCandidate().getId(), cv.getCandidate().getId())) {
            throw new CustomException("You cannot view someone else's CV!", HttpStatus.UNAUTHORIZED);
        }
        return new CVResponse(cv.getId(), cv.getFileName(), cv.getUrl(), cv.isStatus(), cv.getCreatedAt());
    }

    @Override
    public CVPage getDefaultCVByCandidate(Integer candidateId) throws CustomException {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new CustomException("Candidate not found!", HttpStatus.NOT_FOUND));
        CVPage response = new CVPage();
//        Thiết lập các thông tin cơ bản
        response.setName(candidate.getName());
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
        List<SkillsCandidate> skills = skillCandidateRepository.findAllByCandidateId(candidateId);
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
        response.setExperience(experienceCVResponses);
//        Dự án
        List<ProjectCandidate> projects = projectRepository.findAllByCandidateId(candidateId);
        List<ProjectCVResponse> projectCVResponses = new ArrayList<>();
        for (ProjectCandidate projectCandidate : projects) {
            projectCVResponses.add(new ProjectCVResponse(projectCandidate.getName(), projectCandidate.getLink(), projectCandidate.getStartAt(), projectCandidate.getEndAt(), projectCandidate.getInfo()));
        }
        response.setProjects(projectCVResponses);
//        Học vấn
        List<EducationCandidate> educations = educationCandidateRepository.findAllByCandidateId(candidateId);
        List<EducationCVResponse> educationCVResponses = new ArrayList<>();
        for (EducationCandidate education : educations) {
            educationCVResponses.add(new EducationCVResponse(education.getNameEducation(), education.getMajor(), education.getStartAt(), education.getEndAt(), education.getInfo()));
        }
        response.setEducations(educationCVResponses);
        return response;
    }

    @Override
    public CVPage getDefaultCV() throws CustomException {
        Integer id = getCurrentCandidate().getId();
        return getDefaultCVByCandidate(id);
    }

    @Override
    public String getCurrentCandidateLetter() {
        Optional<ApplicationLetter> letter=letterRepository.findByCandidateId(getCurrentCandidate().getId());
        return letter.map(ApplicationLetter::getContent).orElse(null);
    }

    @Override
    public void addLetter(String content) {
        ApplicationLetter letter = ApplicationLetter.builder()
                .candidate(getCurrentCandidate())
                .content(content).build();
        letterRepository.save(letter);
    }

    @Override
    public void editLetter(String content) {
        ApplicationLetter letter = letterRepository.findByCandidateId(getCurrentCandidate().getId()).get();
        letter.setContent(content);
        letterRepository.save(letter);

    }

    @Override
    public boolean followCompany(Integer companyId) {
        // Lấy ứng viên hiện tại từ phiên đăng nhập
        Candidate candidate = getCurrentCandidate();

        // Tìm công ty
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        // Kiểm tra xem ứng viên đã theo dõi công ty này chưa
        JobCandidates existingJobCandidate = jobCandidateRepository.findByCandidateAndJob_Company(candidate, company);

        if (existingJobCandidate != null) {
            // Nếu đã theo dõi thì hủy theo dõi
            jobCandidateRepository.delete(existingJobCandidate);

            // Giảm số lượng người theo dõi
            company.setFollowers(company.getFollowers() - 1);
            companyRepository.save(company);
        } else {
            // Nếu chưa theo dõi thì theo dõi
            Job job = findJobByCompany(company);
            JobCandidates jobCandidates = new JobCandidates();
            jobCandidates.setCandidate(candidate);
            jobCandidates.setJob(job);
            jobCandidateRepository.save(jobCandidates);

            // Tăng số lượng người theo dõi
            company.setFollowers(company.getFollowers() + 1);
            companyRepository.save(company);
        }

        return true;
    }

    @Override
    public boolean unfollowCompany(Integer companyId) {
        // Lấy ứng viên hiện tại từ phiên đăng nhập
        Candidate candidate = getCurrentCandidate();

        // Tìm công ty
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        // Tìm jobCandidate hiện tại
        JobCandidates existingJobCandidate = jobCandidateRepository.findByCandidateAndJob_Company(candidate, company);

        if (existingJobCandidate != null) {
            // Xóa jobCandidate
            jobCandidateRepository.delete(existingJobCandidate);

            // Giảm số lượng người theo dõi
            company.setFollowers(company.getFollowers() - 1);
            companyRepository.save(company);

            return true;
        } else {
            return false;
        }
    }

    public Job findJobByCompany(Company company) {
        // Tìm một công việc liên quan đến công ty
        List<Job> jobs = jobRepository.findByCompany(company);
        if (jobs.isEmpty()) {
            throw new RuntimeException("No job found for company");
        }
        // Giả sử bạn chọn công việc đầu tiên trong danh sách
        return jobs.get(0);
    }

    public void applyForJob(Integer jobId, String cvUrl, String content) {
        // Lấy thông tin ứng viên hiện tại
        Candidate currentCandidate = getCurrentCandidate();


        // Kiểm tra xem ứng viên có tồn tại không
        if (currentCandidate == null) {
            throw new RuntimeException("Candidate not found");
        }

        // Lấy thông tin công việc
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new RuntimeException("Job not found"));

        // Tạo đối tượng ứng tuyển
        JobCandidates jobCandidates = JobCandidates.builder()
                .candidate(currentCandidate)
                .job(job)
                .cvUrl(cvUrl)
                .content(content)
                .status(0) // Hoặc giá trị phù hợp với trạng thái mặc định
                .build();

        // Lưu thông tin ứng tuyển vào cơ sở dữ liệu
        jobCandidateRepository.save(jobCandidates);
    }

}
//  private Integer id;
//    private String name;
//    private String accountEmail;
//    private Date birthday;
//    private String address;
//    private String phone;
//    private Integer status;
//    private Boolean gender;
//    private String linkLinkedin;
//    private String linkGit;
//    private String position;
//    private Integer outstanding;


