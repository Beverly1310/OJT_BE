package com.example.ojt.repository;

import com.example.ojt.model.entity.TypeJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface ITypeJobRepository extends JpaRepository<TypeJob, Integer> {
    Page<TypeJob> findAllByNameContains(String name, Pageable pageable);

    Optional<TypeJob> findByName(String name);

    @Query("select tj.name from TypesJobs tjs join tjs.typeJob tj where tjs.job.id = :id")
    Set<String> findByJobId(Integer id);
}

