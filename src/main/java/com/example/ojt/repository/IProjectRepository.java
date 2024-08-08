package com.example.ojt.repository;

import com.example.ojt.model.entity.ProjectCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProjectRepository extends JpaRepository<ProjectCandidate,Integer> {
    List<ProjectCandidate> findAllByCandidateId(Integer candidateId);
}
