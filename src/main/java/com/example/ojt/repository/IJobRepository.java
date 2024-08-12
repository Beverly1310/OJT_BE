package com.example.ojt.repository;

import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IJobRepository extends JpaRepository<Job,Integer> {
Page<Job> findAllByTitleContains(String title , Pageable pageable);
Optional<Job> findByTitle(String title);
Optional<Job> findByIdAndCompany(Integer id, Company company);
}
