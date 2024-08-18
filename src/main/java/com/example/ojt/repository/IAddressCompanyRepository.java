package com.example.ojt.repository;

import com.example.ojt.model.entity.AddressCompany;
import com.example.ojt.model.entity.Company;
import com.example.ojt.model.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAddressCompanyRepository extends JpaRepository<AddressCompany, Integer> {
    boolean existsByAddress (String address);
    Optional<AddressCompany> findByAddress(String address);
    List<AddressCompany> findAllByCompany(Company company);
    Page<AddressCompany> findAllByAddressContains(String address, Pageable pageable);
    List<AddressCompany> findByLocation(Location location);
    Optional<AddressCompany> findByLocationIdAndCompanyId(Integer locationId, Integer companyId);
    Optional<AddressCompany> findByCompanyId(Integer companyId);
}
