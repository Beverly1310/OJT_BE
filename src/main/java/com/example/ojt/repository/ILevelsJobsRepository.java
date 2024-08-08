package com.example.ojt.repository;

import com.example.ojt.model.entity.LevelsJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILevelsJobsRepository extends JpaRepository<LevelsJobs,Integer> {

    void deleteAllByJobId(Integer jobId);
}
