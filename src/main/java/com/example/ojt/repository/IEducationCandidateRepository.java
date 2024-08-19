package com.example.ojt.repository;

import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.EducationCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IEducationCandidateRepository extends JpaRepository<EducationCandidate, Integer> {
    Page<EducationCandidate> findEducationCandidatesByCandidateAndNameEducationContains(Candidate candidate, String name, Pageable pageable);

    Page<EducationCandidate> findAllByCandidate(Candidate candidate, Pageable pageable);
    List<EducationCandidate> findAllByCandidateId(Integer id);

    Optional<EducationCandidate> findByNameEducation(String name);

    Optional<EducationCandidate> findByNameEducationAndCandidate(String name, Candidate candidate);

    Optional<EducationCandidate> findByIdAndCandidate(Integer id, Candidate candidate);

    List<EducationCandidate> findAllByCandidate(Candidate candidate);


}
