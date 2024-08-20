package com.example.ojt.service.candidate;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.*;
import com.example.ojt.model.dto.response.CVResponse;
import com.example.ojt.model.dto.response.UserInfo;
import com.example.ojt.model.entity.*;
import com.example.ojt.model.dto.request.EduCandidateAddReq;
import com.example.ojt.model.dto.request.UpdateEduCandidateReq;
import com.example.ojt.model.dto.response.CVPage;
import com.example.ojt.model.dto.response.CandidateBasicInfoResponse;
import com.example.ojt.model.entity.EducationCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Date;

public interface ICandidateService {
    boolean addEducation(EduCandidateAddReq eduCandidateAddReq) throws CustomException;

    Page<EducationCandidate> getEducationCandidates(Pageable pageable, String search, String direction);

    EducationCandidate getEducationCandidate(Integer id) throws CustomException;

    boolean editEducationCandidate(UpdateEduCandidateReq updateEduCandidateReq) throws CustomException;

    boolean deleteEducationCandidate(Integer id) throws CustomException;

    boolean addExp(AddExpCandidateReq addExpCandidateReq) throws CustomException;

    Page<ExperienceCandidate> getExperienceCandidates(Pageable pageable, String search, String direction);

    ExperienceCandidate getExperienceCandidate(Integer id) throws CustomException;

    boolean editExperienceCandidate(UpdateExpCandidateReq updateExpCandidateReq) throws CustomException;

    boolean deleteExperienceCandidate(Integer id) throws CustomException;

    boolean addCertificate(AddCertificateReq addCertificateReq) throws CustomException;

    Page<CertificateCandidate> getCertificateCandidates(Pageable pageable, String search, String direction);

    CertificateCandidate getCertificateCandidate(Integer id) throws CustomException;

    boolean deleteCertificate(Integer id) throws CustomException;

    boolean editCertificate(UpdateCertificateReq updateCertificateReq) throws CustomException;

    boolean addProject(AddProjectCandidateReq addProjectCandidateReq) throws CustomException;

    Page<ProjectCandidate> getProjects(Pageable pageable, String search, String direction);

    ProjectCandidate getProject(Integer id) throws CustomException;

    ProjectCandidate findProjectByName(String projectName) throws CustomException;

    boolean editProject(UpdateProjectReq updateProjectReq) throws CustomException;

    boolean deleteProject(Integer id) throws CustomException;

    boolean addSkill(AddSkillCandidateReq addSkillCandidateReq) throws CustomException;

    Page<SkillsCandidate> getSkills(Pageable pageable, String search, String direction);

    SkillsCandidate getSkill(Integer id) throws CustomException;

    boolean updateSkill(UpdateSkillReq updateSkillReq) throws CustomException;

    boolean deleteSkill(Integer id) throws CustomException;

    Object getCandidateCV(Integer candidateId) throws CustomException;

    CandidateBasicInfoResponse getBasicInfo(Integer candidateId) throws CustomException;


    Page<CandidateEmailDTO> getAllCandidatesWithEmail(Pageable pageable, String search);

    ResponseEntity<Integer> changaStatus(Integer candidateId);

    UserInfo getInfoByUser();


    List<Candidate> getCandidatesByJobId(Integer jobId);


    ResponseEntity<Integer> changeOutstandingStatus(Integer candidateId);

    List<LevelJob> getLevelJobs();

    ResponseEntity<?> findOutstandingCandidates();

    Page<CandidatePerMonth> findCandidatesByDateRange(Date startDate, Date endDate, Pageable pageable);

    List<CandidatePerMonth> findCandidatesByMonth(int year, Pageable pageable);

    long countCandidates();

    void uploadCV(MultipartFile file) throws CustomException;
    List<CVResponse> findAllByCurrentCandidate();
    void toggleCVPriority(Integer id) throws CustomException;
    void deleteCV(Integer id) throws CustomException;
    CV getTopCV(Integer userId);
    void editCVName(Integer id,String name) throws CustomException;
    CVResponse getCVById(Integer id) throws CustomException;
    CVPage getDefaultCVByCandidate(Integer candidateId) throws CustomException;
    CVPage getDefaultCV() throws CustomException;
    String getCurrentCandidateLetter();
    void addLetter(String content);
    void editLetter(String content);
    boolean followCompany( Integer companyId);
    boolean unfollowCompany( Integer companyId);

    static void doSomeThing() {}
}
