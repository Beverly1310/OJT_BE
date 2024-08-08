package com.example.ojt.repository;

import com.example.ojt.model.entity.TypesJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITypesJobsRepository extends JpaRepository<TypesJobs,Integer> {

    void deleteAllByJobId(Integer jobId);
}
