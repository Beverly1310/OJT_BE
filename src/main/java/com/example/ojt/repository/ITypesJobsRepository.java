package com.example.ojt.repository;

import com.example.ojt.model.entity.Job;
import com.example.ojt.model.entity.TypesJobs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ITypesJobsRepository extends JpaRepository<TypesJobs,Integer> {


    void deleteAllByJobId(Integer jobId);
}
