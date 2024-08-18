package com.example.ojt.repository;

import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.ProjectCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IProjectCandidateRepository extends CrudRepository<ProjectCandidate, Integer> {
    Optional<ProjectCandidate> findByName(String name);
    Optional<ProjectCandidate> findByNameAndCandidate(String name,Candidate candidate);

    Page<ProjectCandidate> findByCandidateAndNameContains(Candidate candidate, String name, Pageable pageable);

    Page<ProjectCandidate> findByCandidate(Candidate candidate, Pageable pageable);

    Optional<ProjectCandidate> findByCandidateAndNameContains(Candidate candidate, String name);

    Optional<ProjectCandidate> findByIdAndCandidate(Integer id, Candidate candidate);
    List<ProjectCandidate> findAllByCandidateId(Integer candidateId);
}
