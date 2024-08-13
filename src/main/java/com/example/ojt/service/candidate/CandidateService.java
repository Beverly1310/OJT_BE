package com.example.ojt.service.candidate;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.*;
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
    private final IExperienceCandidateRepository experienceCandidateRepository;
    private final ICertificateRepository certificateRepository;
    private final IProjectCandidateRepository projectCandidateRepository;
    private final ISkillCandidateRepository skillCandidateRepository;
    private final ILevelJobRepository levelJobRepository;
//    private final ISkillRepository skillRepository;
    private final IExperienceRepository experienceRepository;
    private final IProjectRepository projectRepository;

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
        return educationCandidateRepository.findByIdAndCandidate(id,getCurrentCandidate()).orElseThrow(() -> new CustomException("Education not found", HttpStatus.NOT_FOUND));
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
        return experienceCandidateRepository.findByIdAndCandidate(id,getCurrentCandidate()).orElseThrow(() -> new CustomException("Experience not found", HttpStatus.NOT_FOUND));
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
        if (certificateRepository.findByNameAndCandidate(addCertificateReq.getName(),getCurrentCandidate()).orElse(null) != null) {
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
        return certificateRepository.findByIdAndCandidate(id,getCurrentCandidate()).orElseThrow(() -> new CustomException("Certificate not found", HttpStatus.NOT_FOUND));
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
        if (updateCertificateReq.getStartAt()!=null && updateCertificateReq.getEndAt()!=null){
            if (certificateCandidate.getEndAt().toInstant().isBefore(certificateCandidate.getStartAt().toInstant())) {
                throw new CustomException("End date must be after start date", HttpStatus.BAD_REQUEST);
            }
        }
        certificateRepository.save(certificateCandidate);
        return true;
    }

    @Override
    public boolean addProject(AddProjectCandidateReq addProjectCandidateReq) throws CustomException {
        if (projectCandidateRepository.findByNameAndCandidate(addProjectCandidateReq.getName(),getCurrentCandidate()).orElse(null) != null) {
            throw new CustomException("Project already exist", HttpStatus.BAD_REQUEST);
        }
        if (addProjectCandidateReq.getEndAt()!=null && addProjectCandidateReq.getEndAt()!=null){
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
        if (updateProjectReq.getEndAt()!=null && updateProjectReq.getEndAt()!=null){
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
        return projectCandidateRepository.findByIdAndCandidate(id,getCurrentCandidate()).orElseThrow(() -> new CustomException("Project not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public boolean addSkill(AddSkillCandidateReq addSkillCandidateReq) throws CustomException {
        if (skillCandidateRepository.findByNameAndCandidate(addSkillCandidateReq.getName(),getCurrentCandidate()).orElse(null) != null) {
            throw new CustomException("Skill already exist", HttpStatus.BAD_REQUEST);
        }
        SkillsCandidate skillsCandidate = SkillsCandidate.builder()
                .name(addSkillCandidateReq.getName())
                .candidate(getCurrentCandidate())
                .levelJob(levelJobRepository.findByName(addSkillCandidateReq.getLevelJobName()).orElseThrow(() -> new CustomException("Level Job not found", HttpStatus.NOT_FOUND)))
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
        return skillCandidateRepository.findByIdAndCandidate(id,getCurrentCandidate()).orElseThrow(() -> new CustomException("Skill not found", HttpStatus.NOT_FOUND));
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
    public CVResponse getCandidateCV(Integer candidateId) throws CustomException {
        Candidate candidate = candidateRepository.findById(candidateId).orElseThrow(() -> new CustomException("Candidate not found!", HttpStatus.NOT_FOUND));
        CVResponse response = new CVResponse();
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
        response.setName(candidate.getName()    );
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
