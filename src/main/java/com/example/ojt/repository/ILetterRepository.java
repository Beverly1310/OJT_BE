package com.example.ojt.repository;

import com.example.ojt.model.entity.ApplicationLetter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ILetterRepository extends JpaRepository<ApplicationLetter,Integer> {
    Optional<ApplicationLetter> findByCandidateId(Integer id);
}
