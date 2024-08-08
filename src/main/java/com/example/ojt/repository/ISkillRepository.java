package com.example.ojt.repository;

import com.example.ojt.model.entity.SkillsCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISkillRepository extends JpaRepository<SkillsCandidate,Integer> {
    List<SkillsCandidate> findAllByCandidateId(Integer candidateId);

}
