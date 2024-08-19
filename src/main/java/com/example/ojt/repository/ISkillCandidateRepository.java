package com.example.ojt.repository;

import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.SkillsCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ISkillCandidateRepository extends JpaRepository<SkillsCandidate,Integer> {
    Optional<SkillsCandidate> findByName(String name);
    Optional<SkillsCandidate> findByNameAndCandidate(String name,Candidate candidate);
    Page<SkillsCandidate> findByCandidateAndNameContains(Candidate candidate, String name, Pageable pageable);
    Page<SkillsCandidate> findByCandidate(Candidate candidate, Pageable pageable);
    Optional<SkillsCandidate> findByIdAndCandidate(Integer id, Candidate candidate);
    List<SkillsCandidate> findAllByCandidateId(Integer candidateId);
}
