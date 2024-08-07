package com.example.ojt.repository;

import com.example.ojt.model.entity.CertificateCandidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ICertificateRepository extends JpaRepository<CertificateCandidate,Integer> {
    List<CertificateCandidate> findAllByCandidateId(Integer id);
}
