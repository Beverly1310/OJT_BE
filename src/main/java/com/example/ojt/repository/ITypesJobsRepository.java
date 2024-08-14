package com.example.ojt.repository;

import com.example.ojt.model.entity.Job;
import com.example.ojt.model.entity.TypesJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ITypesJobsRepository extends JpaRepository<TypesJobs,Integer> {

    @Query("SELECT tj.typeJob.name FROM TypesJobs tj WHERE tj.job.id = :jobId")
    Set<String> findTypeNamesByJobId(@Param("jobId") Integer jobId);
    void deleteAllByJobId(Integer jobId);
}
