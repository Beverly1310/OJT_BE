package com.example.ojt.repository;

import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Job;
import com.example.ojt.model.entity.JobCandidates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJobCandidateRepository extends JpaRepository<JobCandidates, Integer> {
    List<JobCandidates> findByJobId(Integer id);
    JobCandidates findByCandidateAndJob(Candidate candidate, Job job);
    // Tìm JobCandidates dựa trên ứng viên và công ty
    JobCandidates findByCandidateAndJob_Company(Candidate candidate, Company company);
}
