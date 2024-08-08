package com.example.ojt.repository;

import com.example.ojt.model.entity.TypeJob;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ITypeJobRepository extends JpaRepository<TypeJob,Integer> {
    Page<TypeJob> findAllByNameContains(String name , Pageable pageable);
}
