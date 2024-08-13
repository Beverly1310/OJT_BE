package com.example.ojt.repository;

import com.example.ojt.model.entity.LevelJob;
import com.example.ojt.model.entity.TypeCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILevelJobRepository extends JpaRepository<LevelJob, Integer> {
    Page<LevelJob> findAllByNameContains(String name , Pageable pageable);
    Optional<LevelJob> findByName(String name);
}