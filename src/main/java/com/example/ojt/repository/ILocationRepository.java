package com.example.ojt.repository;

import com.example.ojt.model.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILocationRepository extends JpaRepository<Location,Integer> {
    Page<Location> findAllByNameCityContains(String name, Pageable pageable);
}
