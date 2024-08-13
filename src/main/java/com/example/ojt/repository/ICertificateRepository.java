package com.example.ojt.repository;

import com.example.ojt.model.entity.Candidate;
import com.example.ojt.model.entity.CertificateCandidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


import java.util.List;
import java.util.Optional;

public interface ICertificateRepository extends CrudRepository<CertificateCandidate, Integer> {
    Optional<CertificateCandidate> findByName(String name);

    Optional<CertificateCandidate> findByNameAndCandidate(String name, Candidate candidate);

    Page<CertificateCandidate> findByCandidateAndNameContains(Candidate candidate, String search, Pageable pageable);

    Page<CertificateCandidate> findByCandidate(Candidate candidate, Pageable pageable);

    Optional<CertificateCandidate> findByIdAndCandidate(Integer id, Candidate candidate);

    List<CertificateCandidate> findAllByCandidateId(Integer id);


}
