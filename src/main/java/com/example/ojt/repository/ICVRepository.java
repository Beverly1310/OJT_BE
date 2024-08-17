package com.example.ojt.repository;

import com.example.ojt.model.entity.CV;
import com.example.ojt.model.entity.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICVRepository extends JpaRepository<CV,Integer> {
    List<CV> findAllByCandidate(Candidate candidate);
    CV findByCandidateIdAndStatusTrue(Integer candidateId);

}
