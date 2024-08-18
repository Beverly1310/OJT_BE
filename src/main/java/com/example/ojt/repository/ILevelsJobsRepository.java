package com.example.ojt.repository;

import com.example.ojt.model.entity.LevelsJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ILevelsJobsRepository extends JpaRepository<LevelsJobs,Integer> {

    void deleteAllByJobId(Integer jobId);
    Set<LevelsJobs> findByJobId(Integer jobId);
}
