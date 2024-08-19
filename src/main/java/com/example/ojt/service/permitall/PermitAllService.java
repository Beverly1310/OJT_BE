package com.example.ojt.service.permitall;

import com.example.ojt.model.dto.response.OutStandingJobRes;
import com.example.ojt.model.dto.response.StatRes;
import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Job;
import com.example.ojt.model.entity.ProjectCandidate;
import com.example.ojt.repository.ICandidateRepository;
import com.example.ojt.repository.ICompanyRepository;
import com.example.ojt.repository.IJobRepository;
import com.example.ojt.repository.ITypeJobRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PermitAllService implements IPermitAllService {
    private final ICandidateRepository candidateRepository;
    private final ICompanyRepository companyRepository;
    private final IJobRepository jobRepository;
    private final ITypeJobRepository typeJobRepository;
    @Override
    public List<Company> getOutstandingCompanies() {
        return companyRepository.getCompanyByOutstanding(1);
    }

    @Override
    public List<OutStandingJobRes> getOutstandingJobs() {
        List<Job> jobList = jobRepository.getJobByOutstanding(1);
       List<OutStandingJobRes> outStandingJobRes = jobList.stream().map((job -> {
            return OutStandingJobRes.builder()
                    .job(job)
                    .typeJob(typeJobRepository.findByJobId(job.getId()).stream().toList().get(0))
                    .build();
        })).toList();
        return outStandingJobRes;
    }

    @Override
    public List<Candidate> getOutstandingCandidates() {
        return candidateRepository.getCandidateByOutstanding(1);
    }

    @Override
    public StatRes getStatRes() {
        // Tạo một đối tượng Date hiện tại
        Date today = new Date();

        // Tạo đối tượng Calendar và thiết lập ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        // Trừ 10 ngày
        calendar.add(Calendar.DAY_OF_MONTH, -10);

        // Lấy ngày mới sau khi trừ
        Date tenDaysAgo = calendar.getTime();

        StatRes statRes = StatRes.builder()
                .candidates(candidateRepository.findAll().size())
                .companies(companyRepository.findAll().size())
                .liveJob(jobRepository.getCountJob())
                .newJobs(jobRepository.getCountNewJob(tenDaysAgo,today))
                .build();
        return statRes;
    }

    @Override
    public Page<Candidate> getCandidates(Pageable pageable, String search) {
        Page<Candidate> candidates;
        if (search != null && !search.isBlank()) {
            candidates = candidateRepository.findAllByNameContains( pageable,search);
        } else {
            candidates = candidateRepository.findAll( pageable);
        }
        return candidates;
    }
    }

