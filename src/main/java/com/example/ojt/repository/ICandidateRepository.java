package com.example.ojt.repository;

import com.example.ojt.model.entity.Candidate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICandidateRepository extends JpaRepository<Candidate, Integer> {

    // Method to find a Candidate by Account ID
    Candidate findCandidateByAccountId(int accountId);

    // Method to search for candidates by name or account email with pagination support
    Page<Candidate> findByNameContainingOrAccountEmailContaining(String name, String email, Pageable pageable);

    @Query("select cd from Candidate cd where cd.Outstanding=:outstanding and cd.status=1 and cd.account.status=1")
    List<Candidate> getCandidateByOutstanding(Integer outstanding);

    Page<Candidate> findAllByNameContains(Pageable pageable, String name);
}
