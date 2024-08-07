package com.example.ojt.repository;

import com.example.ojt.model.entity.AddressCompany;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface IAddressCompanyRepository extends JpaRepository<AddressCompany, Integer> {
    Page<AddressCompany> findAllByAddressContains(String address, Pageable pageable);
}
