package com.example.ojt.repository;

import com.example.ojt.model.entity.AddressCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAddressCompanyRepository extends JpaRepository<AddressCompany, Integer> {
    boolean existsByAddress (String address);
    Optional<AddressCompany> findByAddress(String address);
    Page<AddressCompany> findAllByAddressContains(String address, Pageable pageable);
}
