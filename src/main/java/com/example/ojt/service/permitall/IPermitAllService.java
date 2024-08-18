package com.example.ojt.service.permitall;

import com.example.ojt.model.dto.response.OutStandingJobRes;
import com.example.ojt.model.dto.response.StatRes;
import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPermitAllService {
    List<Company> getOutstandingCompanies();
    List<OutStandingJobRes> getOutstandingJobs();
    List<Candidate> getOutstandingCandidates();
    StatRes getStatRes();
    Page<Candidate> getCandidates(Pageable pageable,String search);
}
