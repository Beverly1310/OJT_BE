package com.example.ojt.repository;

import com.example.ojt.model.entity.JobCandidates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJobCandidateRepository extends JpaRepository<JobCandidates, Integer> {
    List<JobCandidates> findByJobId(Integer id);
}
