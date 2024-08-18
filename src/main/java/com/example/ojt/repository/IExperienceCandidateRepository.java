package com.example.ojt.repository;

import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.ExperienceCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IExperienceCandidateRepository extends JpaRepository<ExperienceCandidate, Integer> {
    Optional<ExperienceCandidate> findByPositionAndCompany(String position, String company);

    Optional<ExperienceCandidate> findByPositionAndCompanyAndCandidate(String position, String company, Candidate candidate);

    Page<ExperienceCandidate> findByCandidateAndCompanyContains(Candidate candidate, String company, Pageable pageable);

    Page<ExperienceCandidate> findByCandidate(Candidate candidate, Pageable pageable);

    Optional<ExperienceCandidate> findByIdAndCandidate(Integer id, Candidate candidate);

    List<ExperienceCandidate> findAllByCandidate(Candidate candidate);
    List<ExperienceCandidate> findAllByCandidateId(Integer candidateId);
}
