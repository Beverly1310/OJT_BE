package com.example.ojt.service.candidate;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.*;
import com.example.ojt.model.entity.*;
import com.example.ojt.model.dto.request.EduCandidateAddReq;
import com.example.ojt.model.dto.request.UpdateEduCandidateReq;
import com.example.ojt.model.dto.response.CVResponse;
import com.example.ojt.model.dto.response.CandidateBasicInfoResponse;
import com.example.ojt.model.entity.EducationCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    CVResponse getCandidateCV(Integer candidateId) throws CustomException;
    CandidateBasicInfoResponse getBasicInfo(Integer candidateId) throws CustomException;
}
