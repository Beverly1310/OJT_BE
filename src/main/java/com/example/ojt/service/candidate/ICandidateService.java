package com.example.ojt.service.candidate;

import com.example.ojt.exception.CustomException;
import com.example.ojt.model.dto.request.EduCandidateAddReq;
import com.example.ojt.model.dto.request.UpdateEduCandidateReq;
import com.example.ojt.model.dto.response.CVResponse;
import com.example.ojt.model.dto.response.CandidateBasicInfoResponse;
import com.example.ojt.model.entity.EducationCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICandidateService {
    boolean addEducation(EduCandidateAddReq eduCandidateAddReq);

    Page<EducationCandidate> getEducationCandidates(Pageable pageable,String search,String direction);

    EducationCandidate getEducationCandidate(Integer id) throws CustomException;

    boolean editEducationCandidate(UpdateEduCandidateReq updateEduCandidateReq) throws CustomException;

    boolean deleteEducationCandidate(Integer id) throws CustomException;
    CVResponse getCandidateCV(Integer candidateId) throws CustomException;
    CandidateBasicInfoResponse getBasicInfo(Integer candidateId) throws CustomException;
}
