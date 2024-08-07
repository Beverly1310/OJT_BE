package com.example.ojt.repository;

import com.example.ojt.model.entity.ExperienceCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExperienceRepository extends JpaRepository<ExperienceCandidate,Integer> {
    List<ExperienceCandidate> findAllByCandidateId(Integer candidateId);
}
